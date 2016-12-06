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
		fragInputCellUserName.setLabelText("用户名");
		fragInputCellUserName.setHintText("请输入用户名");
		fragInputCellUserPassword.setLabelText("密码");
		fragInputCellUserPassword.setHintText("请输入密码");
		fragInputCellUserPassword.setIsPassword(true);
		
	}
	
	void goRegister(){
		Intent itnt = new Intent(this,RegisterActivity.class);
		startActivity(itnt);
	}
	
	void goLogin(){
		Intent itnt = new Intent(this,HelloWorldActivity.class);
		startActivity(itnt);
		
	}
	
	void goRecoverPassword(){
		Intent itnt = new Intent(this, PasswordRecoverActivity.class);
		startActivity(itnt);
	}
	
}
