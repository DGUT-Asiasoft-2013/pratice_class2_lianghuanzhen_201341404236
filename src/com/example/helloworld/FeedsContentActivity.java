package com.example.helloworld;

import java.io.IOException;
import java.util.List;

import com.example.helloworld.fragments.pages.CommentListFragment;
import com.example.helloworld.fragments.widgets.AvatarView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import api.Server;
import api.entity.Article;
import api.entity.Comment;
import api.entity.Page;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

public class FeedsContentActivity extends Activity {


	List<Comment> comments;
	int page = 0;
	CommentListFragment fragComment;
	//	Article commentMess;
	private Article article;
	ListView commentListView;
	Button btnLikes;
	private boolean isLiked;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);



		setContentView(R.layout.activity_feeds_list_view);

		fragComment = (CommentListFragment) getFragmentManager().findFragmentById(R.id.show_comment_list);

		//		commentListView = (ListView) findViewById(R.id.list_comment);

		article = (Article) getIntent().getSerializableExtra("data");//获取Article
		TextView textView = (TextView) findViewById(R.id.text);
		AvatarView avatar = (AvatarView) findViewById(R.id.user_avatar);
		TextView textAuthorName = (TextView) findViewById(R.id.user_name);
		TextView textTitle = (TextView) findViewById(R.id.text_title);
		TextView textDate = (TextView) findViewById(R.id.text_date);


		textView.setText(article.getText());
		avatar.load(Server.serverAddress + article.getAuthor().getAvatar());
		textAuthorName.setText(article.getAuthor().getName());
		textTitle.setText(article.getTitle());
		textDate.setText(article.getCreateDate().toString());
		fragComment.setArticleId(article.getId().toString());

		//评论
		findViewById(R.id.btn_comment).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goCommentActivity();

			}
		});

		//点赞
		btnLikes = (Button) findViewById(R.id.btn_like);

		findViewById(R.id.btn_like).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				toggleLikes();
			}
		});


	}

	void checkLiked(){
		Request request = Server.requestBuilderWithApi("article/"+article.getId()+"/isliked").get().build();
		Server.getSharedClient().newCall(request).enqueue(new Callback() {
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try{
					final String responseString = arg1.body().string();
					final Boolean result = new ObjectMapper().readValue(responseString, Boolean.class);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							onCheckLikedResult(result);
						}
					});
				}catch(final Exception e){
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							onCheckLikedResult(false);
						}
					});
				}
			}

			@Override
			public void onFailure(Call arg0, IOException e) {
				e.printStackTrace();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						onCheckLikedResult(false);
					}
				});				
			}
		});
	}

	void onCheckLikedResult(boolean result){
		isLiked = result;
		btnLikes.setTextColor(result ? Color.BLUE : Color.BLACK);
	}

	void reloadLikes(){
		Request request = Server.requestBuilderWithApi("/article/"+article.getId()+"/likes")
				.get().build();

		Server.getSharedClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try{
					String responseString = arg1.body().string();
					final Integer count = new ObjectMapper().readValue(responseString, Integer.class);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							onReloadLikesResult(count);
						}
					});
				}catch (Exception e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							onReloadLikesResult(0);
						}
					});
				}
			}

			@Override
			public void onFailure(Call arg0, IOException e) {
				e.printStackTrace();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						onReloadLikesResult(0);
					}
				});
			}
		});
	}

	void onReloadLikesResult(int count){
		if(count>0){
			btnLikes.setText("赞("+count+")");
		}else{
			btnLikes.setText("赞");
		}
	}

	void toggleLikes(){
		MultipartBody body = new MultipartBody.Builder()
				.addFormDataPart("likes", String.valueOf(!isLiked))
				.build(); 

		Request request = Server.requestBuilderWithApi("article/"+article.getId()+"/likes")
				.post(body).build();

		Server.getSharedClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				runOnUiThread(new Runnable() {
					public void run() {
						reload();
					}
				});
			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				runOnUiThread(new Runnable() {
					public void run() {
						reload();
					}
				});
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		//		Article article = (Article) getIntent().getSerializableExtra("data");
		reloadLikes();
		checkLiked();

	}

	void goCommentActivity(){
		//		Article commentMess = text;
		Article article = (Article) getIntent().getSerializableExtra("data");

		Intent itnt = new Intent(this,CommentActivity.class);
		itnt.putExtra("data", article);
		//
		startActivity(itnt);


		overridePendingTransition(R.anim.add_in_button, R.anim.add_out_button);
		finish();
	}

	void reload(){
		reloadLikes();
		checkLiked();

		Request request = Server.requestBuilderWithApi("/article/"+article.getId()+"/comments")
				.get().build();

		Server.getSharedClient().newCall(request).enqueue(new Callback() {
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try{
					final Page<Comment> data = new ObjectMapper().readValue(arg1.body().string(), new TypeReference<Page<Comment>>() {
					});

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							FeedsContentActivity.this.reloadData(data);
						}
					});
				}catch(final Exception e){
					runOnUiThread(new Runnable() {
						public void run() {
							FeedsContentActivity.this.onFailure(e);
						}
					});
				}
			}

			@Override
			public void onFailure(Call arg0, final IOException e) {
				runOnUiThread(new Runnable() {
					public void run() {
						FeedsContentActivity.this.onFailure(e);
					}
				});
			}
		});
	}
	protected void reloadData(Page<Comment> data) {
		page = data.getNumber();
		comments = data.getContent();
		fragComment.getAdapter().notifyDataSetInvalidated();
	}
	
	protected void appendData(Page<Comment> data) {
		if(data.getNumber() > page){
			page = data.getNumber();
			
			if(comments==null){
				comments = data.getContent();		
			}else{
				comments.addAll(data.getContent());
			}
		}
		
		fragComment.getAdapter().notifyDataSetChanged();
	}

	void onFailure(Exception e){
		new AlertDialog.Builder(this).setMessage(e.getMessage()).show();
}
	
}
