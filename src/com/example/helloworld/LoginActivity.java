package com.example.helloworld;

import com.example.helloworld.fragments.inputcells.SimpleTextInputCellFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends Activity {
	
	SimpleTextInputCellFragment fragInputCellUserName ;
	SimpleTextInputCellFragment fragInputCellUserPassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		fragInputCellUserName = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_user_name);
		fragInputCellUserPassword = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_user_password);
		
		findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goLogin();
				
			}
		});
		
		findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goRegister();
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		fragInputCellUserName.setLabelText("�û���");
		fragInputCellUserName.setHintText("�������û���");
		fragInputCellUserPassword.setLabelText("����");
		fragInputCellUserPassword.setHintText("����������");
		
	}
	
	void goRegister(){
		Intent itnt = new Intent(this,RegisterActivity.class);
		startActivity(itnt);
	}
	
	void goLogin(){
		setContentView(R.layout.activity_welcome);
		
	}
}
