package com.example.helloworld;

import java.io.IOException;

import com.example.helloworld.fragments.widgets.PasswordRecoverStep1Fragment;
import com.example.helloworld.fragments.widgets.PasswordRecoverStep2Fragment;
import com.example.helloworld.fragments.widgets.PasswordRecoverStep2Fragment.OnGoSubmitListener;
import com.example.helloworld.fragments.widgets.PasswordRecoverStep1Fragment.OnGoNextListener;

import android.accounts.Account;
import android.accounts.OnAccountsUpdateListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import api.Server;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PasswordRecoverActivity extends Activity{

	PasswordRecoverStep1Fragment step1 = new PasswordRecoverStep1Fragment();
	PasswordRecoverStep2Fragment step2 = new PasswordRecoverStep2Fragment();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_password_recover);

		step1.setOnGoNextListener(new OnGoNextListener() {

			@Override
			public void onGoNext() {
				goStep2();

			}
		}); 

		step2.setOnSubmitListener(new OnGoSubmitListener() {

			@Override
			public void goSubmit() {
				passwordRecover();

			}
		});


		getFragmentManager().beginTransaction().replace(R.id.container, step1).commit();

	}

	void goStep2(){

		getFragmentManager()
		.beginTransaction()
		.setCustomAnimations(
				R.animator.slide_in_right,
				R.animator.slide_out_left, 
				R.animator.slide_in_left, 
				R.animator.slide_out_right)
		.replace(R.id.container, step2)
		.addToBackStack(null)
		.commit();
	}

	void passwordRecover(){
		//得到step1的email
		//		String email = PasswordRecoverStep1Fragment.getEmail();
		//		String password = PasswordRecoverStep2Fragment.getPassword();

		OkHttpClient client = Server.getSharedClient();
		//加密MD5

		String passwordHash = MD5.getMD5(PasswordRecoverStep2Fragment.getPassword());
		MultipartBody newBody = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("email", PasswordRecoverStep1Fragment.getEmail())
				.addFormDataPart("passwordHash",passwordHash )
				.build();

		Request request = Server.requestBuilderWithApi("passwordrecover")
				.method("post", null)
				.post(newBody)
				.build();


		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						try {

							PasswordRecoverActivity.this.onResponseYes();
						} catch (Exception e) {
							e.printStackTrace();
							PasswordRecoverActivity.this.onFailureYes();
						}
					}
				});


			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						PasswordRecoverActivity.this.onFailureYes();

					}
				});


			}
		});
	}

	/*
	 * 
	 * 
	 * 			
	 * 

	 */
	void onResponseYes(){
		new AlertDialog.Builder(PasswordRecoverActivity.this)
		.setMessage("请求成功")//arg1.body().string()放后台
		.setNegativeButton("好", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent itnt = new Intent(PasswordRecoverActivity.this, LoginActivity.class);
				startActivity(itnt);
				finish();
				
			}
		})
		.show();

				


	}

	void onFailureYes(){
		new AlertDialog.Builder(PasswordRecoverActivity.this)
		.setMessage("请求失败")
		.setNegativeButton("好", null)
		.show();
	}


}
