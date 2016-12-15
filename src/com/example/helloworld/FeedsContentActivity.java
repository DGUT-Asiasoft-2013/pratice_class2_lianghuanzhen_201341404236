package com.example.helloworld;

import com.example.helloworld.fragments.pages.CommentListFragment;
import com.example.helloworld.fragments.widgets.AvatarView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import api.Server;
import api.entity.Article;

public class FeedsContentActivity extends Activity {
	
	CommentListFragment fragComment;
	//	Article commentMess;
	ListView commentListView;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_feeds_list_view);
		
		fragComment = (CommentListFragment) getFragmentManager().findFragmentById(R.id.show_comment_list);
		
//		commentListView = (ListView) findViewById(R.id.list_comment);

		Article article = (Article) getIntent().getSerializableExtra("data");//获取Article
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
		

	}
	@Override
	protected void onResume() {
		super.onResume();
//		Article article = (Article) getIntent().getSerializableExtra("data");
		
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
}
