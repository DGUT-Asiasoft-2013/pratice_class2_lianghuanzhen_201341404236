package com.example.helloworld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


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
		
	}

	void goHelloWorld(){


		Intent itnt = new Intent(this,HelloWorldActivity.class);
		startActivity(itnt);

		overridePendingTransition(R.anim.add_in_button, R.anim.add_out_button);
		finish();

	}


}
