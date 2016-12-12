package com.example.helloworld.fragments.widgets;

import java.io.IOException;

import com.example.helloworld.LoginActivity;
import com.example.helloworld.MD5;
import com.example.helloworld.PasswordRecoverActivity;
import com.example.helloworld.R;
import com.example.helloworld.RegisterActivity;
import com.example.helloworld.fragments.inputcells.SimpleTextInputCellFragment;
import com.example.helloworld.fragments.widgets.PasswordRecoverStep1Fragment.OnGoNextListener;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import api.Server;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PasswordRecoverStep2Fragment extends Fragment{
	SimpleTextInputCellFragment fragPassword;
	SimpleTextInputCellFragment fragPasswordRepeat;
	SimpleTextInputCellFragment fragVerify;

	static String password;
	static String passwordRepeat;

	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if(view == null){
			view = inflater.inflate(R.layout.fragment_password_recover_step2, null);

			fragPassword = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password);
			fragPasswordRepeat = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password_repeat);
			fragVerify = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_verify);


			//view.findViewById(R.id.btn_next).setOnClickListener(
			view.findViewById(R.id.btn_submit_recover).setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					submit();

				}
			});
		}


		return view;
	}
	
	//----------创建监听器
	public static interface OnGoSubmitListener{	
		void goSubmit();
	}

	OnGoSubmitListener onSubmitListener;

	public void setOnSubmitListener(OnGoSubmitListener onSubmitListener){
		this.onSubmitListener = onSubmitListener;
	}

	void submit(){
		password = fragPassword.getText();
		passwordRepeat = fragPasswordRepeat.getText();
		if(onSubmitListener != null){

			//--------------
			//验证密码与重复密码是否相同


			if(!password.equals(passwordRepeat)){
				new AlertDialog.Builder(getActivity())
				.setMessage("输入密码不一致哦")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setNegativeButton("好", null)
				.show();
				return ;
			}else if(password.length() == 0 || passwordRepeat.length() == 0){ 
				new AlertDialog.Builder(getActivity())
				.setMessage("密码和重复密码不能为空哦")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setNegativeButton("好", null)
				.show();
				return;
			}
			onSubmitListener.goSubmit();
			//跳转到Activity
		}
			
	}


	public static String getPassword(){
		return password;
	}



	@Override
	public void onResume() {
		super.onResume();
		fragVerify.setLabelText("验证码");{
			fragVerify.setHintText("请输入验证码");
		}
		fragPassword.setLabelText("新密码");{
			fragPassword.setHintText("请输入新密码");
		}
		fragPasswordRepeat.setLabelText("重复新密码");{
			fragPasswordRepeat.setHintText("请重复输入新密码");
		}

	}



}
