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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;


//分享文章
public class ShareActivity extends Activity{

	EditText editTitle,editText;


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_share);


		editText = (EditText) findViewById(R.id.input_share_thing);
		editTitle = (EditText) findViewById(R.id.input_share_title);

		findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				sendContent();
				//				goHelloWorld();//返回helloword


			}
		});
	}

	void sendContent(){
		String text = editText.getText().toString();
		String title = editTitle.getText().toString();

		MultipartBody body = new MultipartBody.Builder()
				.addFormDataPart("title", title)
				.addFormDataPart("text", text)
				.build();

		Request request = Server.requestBuilderWithApi("article")
				.post(body)
				.build();

		Server.getSharedClient().newCall(request).enqueue(new Callback() {
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				final String responseBody = arg1.body().string();

				runOnUiThread(new Runnable() {
					public void run() {
						ShareActivity.this.onSucceed(responseBody);
					}
				});
			}

			@Override
			public void onFailure(Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {
					public void run() {
						ShareActivity.this.onFailure(arg1);
					}
				});
			}
		});
	}


	void onSucceed(String text){
		new AlertDialog.Builder(this).setMessage(text)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
//				finish();
//				overridePendingTransition(R.anim.add_in_button, R.anim.add_out_button);
				goHelloWorld();
			}
		}).show();
	}

	void onFailure(Exception e){
		new AlertDialog.Builder(this).setMessage(e.getMessage()).show();
	}
	
	void goHelloWorld(){


		Intent itnt = new Intent(this,HelloWorldActivity.class);
		startActivity(itnt);

		overridePendingTransition(R.anim.add_in_button, R.anim.add_out_button);
		finish();

	}


}
