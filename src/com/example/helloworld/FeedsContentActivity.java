package com.example.helloworld;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class FeedsContentActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		//		setContentView(R.layout.activity_feeds_list_view);

		setContentView(R.layout.activity_feeds_list_view);

		String text = getIntent().getStringExtra("text");

		TextView textView = (TextView) findViewById(R.id.text);
		textView.setText(text);

	}

}
