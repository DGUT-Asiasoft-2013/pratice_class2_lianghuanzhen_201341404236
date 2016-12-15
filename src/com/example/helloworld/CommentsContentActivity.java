package com.example.helloworld;

import com.example.helloworld.fragments.widgets.AvatarView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import api.Server;
import api.entity.Article;
import api.entity.Comment;

public class CommentsContentActivity extends Activity {
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_comment_view);
		
		Comment text = (Comment) getIntent().getSerializableExtra("text");
		
		AvatarView avatar = (AvatarView) findViewById(R.id.user_avatar); //头像
		TextView textComment = (TextView) findViewById(R.id.text);  //评论内容
		TextView textAuthorName = (TextView) findViewById(R.id.user_name); //作者
		TextView commentDate = (TextView) findViewById(R.id.comment_date);  //评论日期

		textComment.setText(text.getText());
		textAuthorName.setText(text.getAuthor().getName());
		avatar.load(Server.serverAddress + text.getAuthor().getAvatar());
		commentDate.setText(text.getCreateDate().toString());
		
		
	}
	
}
