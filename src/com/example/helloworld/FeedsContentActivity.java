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

		Article text = (Article) getIntent().getSerializableExtra("text");//获取Article
		TextView textView = (TextView) findViewById(R.id.text);
		AvatarView avatar = (AvatarView) findViewById(R.id.user_avatar);
		TextView textAuthorName = (TextView) findViewById(R.id.user_name);
		TextView textTitle = (TextView) findViewById(R.id.text_title);
		TextView textDate = (TextView) findViewById(R.id.text_date);


		textView.setText(text.getText());
		avatar.load(Server.serverAddress + text.getAuthorAvatar());
		textAuthorName.setText(text.getAuthorName());
		textTitle.setText(text.getTitle());
		textDate.setText(text.getCreateDate().toString());

		//评论
		findViewById(R.id.btn_comment).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goCommentActivity();

			}
		});

//		//评论列表点击
//		commentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent,View view,int position,long id){
//				onItemClicked(position);
//			}
//
//		});

	}
//	void onItemClicked(int position){
//		Article text = (Article) getIntent().getSerializableExtra("text");
//
//		Intent itnt = new Intent(this, FeedsContentActivity.class);
//		itnt.putExtra("text", text);
//
//		startActivity(itnt);
//	}

	void goCommentActivity(){
		//		Article commentMess = text;
//		Article text = (Article) getIntent().getSerializableExtra("text");

		Intent itnt = new Intent(this,CommentActivity.class);
//		itnt.putExtra("text", text);
//
		startActivity(itnt);


		overridePendingTransition(R.anim.add_in_button, R.anim.add_out_button);
		finish();
	}
}
