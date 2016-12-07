package com.example.helloworld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ShareActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_share);
		
		findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				goHelloWorld();
				
				
			}
		});
	}
	
	void goHelloWorld(){
		Intent itnt = new Intent(this,HelloWorldActivity.class);	
		startActivity(itnt);
		
		overridePendingTransition(R.anim.add_out_button, R.anim.add_in_button);
	}
	
	
}
