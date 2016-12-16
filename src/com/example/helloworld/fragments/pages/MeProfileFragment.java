package com.example.helloworld.fragments.pages;

import java.io.IOException;

import com.example.helloworld.PasswordRecoverActivity;
import com.example.helloworld.R;
import com.example.helloworld.fragments.inputcells.SimpleTextInputCellFragment;
import com.example.helloworld.fragments.widgets.AvatarView;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.R.color;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import api.Server;
import api.entity.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MeProfileFragment extends Fragment {

	TextView text;
	View view;
	AvatarView avatar;
	//	ProgressBar progress;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view == null){
			view = inflater.inflate(R.layout.fragment_page_me_profile, null);
			text = (TextView) view.findViewById(R.id.frag_me_name);
			//			progress = (ProgressBar) view.findViewById(R.id.progress);
			avatar = (AvatarView) view.findViewById(R.id.user_avatar);

			view.findViewById(R.id.btn_password_recover).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					goRecoverPassword();
					
				}
			});
			
			
		}
		return view;
	}
	void goRecoverPassword(){
		Intent itnt = new Intent(this.getActivity(), PasswordRecoverActivity.class);
		startActivity(itnt);
	}

	@Override
	public void onResume() {
		super.onResume();

//		view.setVisibility(View.GONE);
		text.setVisibility(View.GONE);
		//		progress.setVisibility(View.VISIBLE);

		OkHttpClient client = Server.getSharedClient();
		Request request = Server.requestBuilderWithApi("me")
				.method("get", null)
				.build();

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(final Call arg0, Response arg1) throws IOException {
				try {
					final User user = new ObjectMapper().readValue(arg1.body().bytes(),	 User.class);
					getActivity().runOnUiThread(new Runnable() {

						public void run() {
							MeProfileFragment.this.onResponse(arg0, user);


						}
					});

				} catch (final Exception e) {
					getActivity().runOnUiThread(new Runnable() {

						public void run() {
							MeProfileFragment.this.onFailure(arg0, e);

						}
					});
				}

			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {

			}
		});


	}

	void showView(){



	}
	void onResponse(Call arg0, User user) {
		text.setText("Hello "+ user.getAccount());
		avatar.load(user);
		text.setVisibility(view.VISIBLE);
		text.setTextColor(Color.BLACK);

	}

	void onFailure(Call arg0, Exception e){
		text.setVisibility(View.VISIBLE);
		text.setTextColor(Color.RED);
		text.setText(e.getMessage());
	}




}
