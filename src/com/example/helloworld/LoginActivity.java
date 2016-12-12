package com.example.helloworld;

import java.io.IOException;

import com.example.helloworld.fragments.inputcells.SimpleTextInputCellFragment;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import api.entity.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.example.helloworld.HelloWorldActivity;
import api.Server;

public class LoginActivity extends Activity {

	SimpleTextInputCellFragment fragInputCellUserName ;
	SimpleTextInputCellFragment fragInputCellUserPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);


		fragInputCellUserName = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_user_name);
		fragInputCellUserPassword = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_user_password);


		findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goRegister();
			}
		});

		findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goLogin();

			}
		});

		findViewById(R.id.btn_forget_password).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goRecoverPassword();

			}
		});



	}

	@Override
	protected void onResume() {
		super.onResume();
		fragInputCellUserName.setLabelText("用户名");{
			fragInputCellUserName.setHintText("请输入用户名");
		}
		fragInputCellUserPassword.setLabelText("密码");{
			fragInputCellUserPassword.setHintText("请输入密码");
			fragInputCellUserPassword.setIsPassword(true);
		}
	}

	void goRegister(){
		Intent itnt = new Intent(this,RegisterActivity.class);
		startActivity(itnt);
	}

	//登陆按钮
	void goLogin(){
		//---判断用户密码是否正确--
		String account = fragInputCellUserName.getText(); //提取用户账号
		String password = fragInputCellUserPassword.getText(); //提取密码

		if(account.length() == 0 || password.length() == 0){
			new AlertDialog.Builder(LoginActivity.this)
			.setMessage("用户名和密码都要写哦")
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setNegativeButton("好", null)
			.show();
			return ;
		}else {
			//-----------------------------
			/*
			//创建OKHttp客户端
			OkHttpClient client = new OkHttpClient();

			MultipartBody newBody = new MultipartBody.Builder()
					.setType(MultipartBody.FORM)
					.addFormDataPart("account", account)
					.addFormDataPart("passwordHash", password)
					.build();

			//创建请求，包含地址，方法("GET","POST","PUT","DELETE")
			Request request = new Request.Builder()
					.url("http://172.27.0.10:8080/membercenter/api/login")
					.method("post", null)
					.post(newBody)
					.build();
*/
			//---------------
			//上面代码已经被弄到Server上
			//-----------------
			OkHttpClient client = Server.getSharedClient();
			//加密MD5
			password = MD5.getMD5(password);
			MultipartBody newBody = new MultipartBody.Builder()
					.setType(MultipartBody.FORM)
					.addFormDataPart("account", account)
					.addFormDataPart("passwordHash", password)
					.build();
			
			Request request = Server.requestBuilderWithApi("login")
					.method("post", null)
					.post(newBody)
					.build();
			//-----------------
			//ProgressDialog
			final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
			progressDialog.setMessage("等待中，请稍后...");
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();

			client.newCall(request).enqueue(new Callback() {

				@Override
				public void onResponse(final Call arg0, final Response arg1) throws IOException {
					//把消息处理按user类格式化

					try{						
						ObjectMapper obMapper = new ObjectMapper();
						final User user = obMapper.readValue(arg1.body().string(), User.class);
						final String hello = "Hello "+user.getName();

						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								progressDialog.dismiss();
								LoginActivity.this.onResponse(arg0, hello);
							}
						});
					}catch(final Exception e){
						runOnUiThread(new Runnable() {							
							@Override
							public void run() {
								progressDialog.dismiss();
								LoginActivity.this.onFailure(arg0, e);
							}
						});						
					}
				}

				@Override
				public void onFailure(final Call arg0, final IOException arg1) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							progressDialog.dismiss();
							LoginActivity.this.onFailure(arg0, arg1);

						}
					});

				}
			});
		}
	}

	void goRecoverPassword(){
		Intent itnt = new Intent(this, PasswordRecoverActivity.class);
		startActivity(itnt);
	}


	void onResponse(Call arg0, String arg1){
		new AlertDialog.Builder(this)
		.setTitle("登录成功")
		.setMessage(arg1)
		.setNegativeButton("好",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent itnt = new Intent(LoginActivity.this,HelloWorldActivity.class);
				startActivity(itnt);
//				finish();
			}
		})
		.show();

		//		Intent itnt = new Intent(this,HelloWorldActivity.class);
		//		
		//		startActivity(itnt);
}

void onFailure(Call arg0, Exception e){
	new AlertDialog.Builder(this)
	.setTitle("登录失败")
	.setMessage(e.getLocalizedMessage())
	.setNegativeButton("好", null)
	.show();
}

}
