package com.example.helloworld;

import java.util.zip.Inflater;

import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HelloWorldActivity extends Activity {

//	TextView forgetPView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_welcome);
		/*	
		forgetPView = (TextView) findViewById(R.id.btn_forget_password);
		forgetPView.setClickable(true);
		forgetPView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//调到拨号界面


			}
		});
		 */
		/*
		 * textView.setOnClickListener(new OnClickListener() {

@Override

public void onClick(View arg0) {

//调到拨号界面

Uri uri = Uri.parse("tel:18764563501");   

Intent intent = new Intent(Intent.ACTION_DIAL, uri);     

startActivity(intent);

}

});*/
	}



}
