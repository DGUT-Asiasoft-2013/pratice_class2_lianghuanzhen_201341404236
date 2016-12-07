package com.example.helloworld;

import com.example.helloworld.fragments.widgets.PasswordRecoverStep1Fragment;
import com.example.helloworld.fragments.widgets.PasswordRecoverStep2Fragment;
import com.example.helloworld.fragments.widgets.PasswordRecoverStep1Fragment.OnGoNextListener;

import android.accounts.Account;
import android.accounts.OnAccountsUpdateListener;
import android.app.Activity;
import android.os.Bundle;

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
	
}
