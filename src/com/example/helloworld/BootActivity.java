package com.example.helloworld;

import java.io.IOException;

import javax.security.auth.callback.Callback;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BootActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_boot);


	}

	@Override
	protected void onResume() {
		super.onResume();

		//等待一秒，装装
		//---------
		//		Handler handler = new Handler();
		//		handler.postDelayed(new Runnable() {
		//			private int abcd = 0;
		//
		//			public void run() {
		//				startLoginActivity();
		//			}
		//		}, 1000);
		//-------------
		//---创建客户端，访问服务器的hellloword
		OkHttpClient client = new OkHttpClient();

		//创建请求，包含地址，方法("GET","POST","PUT","DELETE")
		Request request = new Request.Builder()
				.url("http://172.27.0.10:8080/membercenter/api/hello")
				.method("GET", null)
				.build();
		
		//enqueue是异步的，先运行UI发送请求到后台的队列中，后台运行完返回数据给UI，运行UI
		client.newCall(request).enqueue(new okhttp3.Callback() {

			//如果连接成功
			@Override
			public void onResponse(Call arg0, final Response arg1) throws IOException {
				//创建 runOnUiThread，使它成为主线程，Toast 只能在主线程中调用
				BootActivity.this.runOnUiThread(new  Runnable() {
					public void run() {
						try {

							Toast toast = Toast.makeText(getApplicationContext(),
									arg1.body().string(), Toast.LENGTH_LONG);
							toast.setGravity(Gravity.CENTER, 0, 0);//位置
							LinearLayout toastView = (LinearLayout) toast.getView();
							ImageView imageCodeProject = new ImageView(getApplicationContext());
							imageCodeProject.setImageResource(R.drawable.dgut_pp);
							toastView.addView(imageCodeProject, 0);
							toast.show();

							//							Toast.makeText(getApplicationContext(), arg1.body().string(),Toast.LENGTH_SHORT).show();
						} catch (IOException e) {
							e.printStackTrace();
						}
						startLoginActivity();
					}
				});


			}

			//连接失败
			@Override
			public void onFailure(Call arg0, final IOException arg1) {
				BootActivity.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText(getApplicationContext(), arg1.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
						startLoginActivity();
					}
				});
				

			}
		});
	}

	void startLoginActivity(){
		Intent itnt = new Intent(this, LoginActivity.class);
		startActivity(itnt);
		finish();
	}
}
