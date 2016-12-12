package com.example.helloworld.fragments.widgets;

import com.example.helloworld.LoginActivity;
import com.example.helloworld.PasswordRecoverActivity;
import com.example.helloworld.R;
import com.example.helloworld.fragments.inputcells.SimpleTextInputCellFragment;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PasswordRecoverStep1Fragment extends Fragment {
	SimpleTextInputCellFragment fragEmail;
	View view;

	static String email;
	

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view == null){
			view = inflater.inflate(R.layout.fragment_password_recover_step1, null);

			fragEmail = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_email);

			view.findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					goNext();

				}
			});
		}
		return view;
	}
	//让step2得到email
	public static String getEmail(){
		return email;
	}

	@Override
	public void onResume() {
		super.onResume();

		fragEmail.setLabelText("注册邮箱");
		fragEmail.setHintText("请输入注册邮箱");
	}

	public static interface OnGoNextListener{	
		void onGoNext();
	}

	OnGoNextListener onGoNextListener;

	public void setOnGoNextListener(OnGoNextListener onGoNextListener){
		this.onGoNextListener = onGoNextListener;
	}

	void goNext(){
		if(onGoNextListener != null){

			//判断邮箱是否为空
			email = fragEmail.getText();
			if(email.length() == 0){
				new AlertDialog.Builder(getActivity())
				.setMessage("邮箱地址要写哦")
				.setNegativeButton("好", null)
				.show();
				return ;
			}
			
			onGoNextListener.onGoNext();
		}
	}


}
