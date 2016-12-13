package com.example.helloworld.fragments.widgets;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.os.Handler;
import android.text.BidiFormatter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import api.Server;
import api.entity.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AvatarView extends View {

	public AvatarView(Context context) {
		super(context);
	}

	public AvatarView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AvatarView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	Paint paint;
	float radius;

	Handler mainThreadHandler = new Handler();;

	public void setBitmap(Bitmap bmp){
//		if(bmp == null) return;

		if(bmp!=null){
			
			paint = new Paint();
			paint.setShader(new BitmapShader(bmp, TileMode.REPEAT, TileMode.REPEAT));
			radius = (Math.min(bmp.getWidth(), bmp.getHeight()))/4;
			Log.d("radius", radius + "");
			invalidate();	
			
		}
	}

	public void load(User user){
		String imageUrl = Server.serverAddress + user.getAvatar();
		Log.d("yy", imageUrl); // 获取返回数据显示到log cat上
		OkHttpClient client = Server.getSharedClient();
		Request request = new Request.Builder()
				.url(imageUrl)
				.method("get",null)
				.build();

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {


				byte[] bytes = arg1.body().bytes();
				final Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
				mainThreadHandler.post(new Runnable() {

					@Override
					public void run() {
						setBitmap(bmp);

					}
				});




			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if(paint != null){
			canvas.drawCircle(getWidth()/2, getHeight()/2, radius, paint);
		}
	}


}
