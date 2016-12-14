package com.example.helloworld;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import api.Server;
import api.entity.Article;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

public class CommentActivity extends Activity{

	Article text;
	EditText commentText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_comment);

		commentText = (EditText) findViewById(R.id.input_comment_text);
		text = (Article) getIntent().getSerializableExtra("text");

		findViewById(R.id.btn_comment_send).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				sendComment();

			}
		});

	}

	void sendComment(){
		String comment = commentText.getText().toString();

		MultipartBody body = new MultipartBody.Builder()
				.addFormDataPart("text", comment)
				.build();

		Request request = Server.requestBuilderWithApi("article/"+text.getId()+"/comments")
				.post(body)
				.build();

		Server.getSharedClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				final String responseBody = arg1.body().string();

				runOnUiThread(new Runnable() {
					public void run() {
						CommentActivity.this.onSucceed(responseBody);
					}
				});

			}

			@Override
			public void onFailure(Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {
					public void run() {
						CommentActivity.this.onFailure(arg1);
					}
				});
			}

		});



	}
	void onSucceed(String text1){
		new AlertDialog.Builder(this).setMessage(text1)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				//					finish();
				//					overridePendingTransition(R.anim.add_in_button, R.anim.add_out_button);
				finish();
				overridePendingTransition(R.anim.add_in_button, R.anim.add_out_button);
				
			}
		}).show();
	}

	void onFailure(Exception e){
		new AlertDialog.Builder(this).setMessage(e.getMessage()).show();
	}


}
