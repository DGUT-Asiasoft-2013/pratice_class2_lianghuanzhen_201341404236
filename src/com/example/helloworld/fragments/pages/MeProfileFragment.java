package com.example.helloworld.fragments.pages;

import java.io.IOException;

import com.example.helloworld.R;
import com.example.helloworld.fragments.inputcells.SimpleTextInputCellFragment;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import api.Server;
import api.entity.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MeProfileFragment extends Fragment {
	 
	private TextView text;
	View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view == null){
			view = inflater.inflate(R.layout.fragment_page_me_profile, null);
			text = (TextView) view.findViewById(R.id.frag_me_name);
			
		}
		
		showView();
		
		return view;
	}
	
	void showView(){
		
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
				// TODO Auto-generated method stub
				
			}
		});
		
		
	}
	void onResponse(Call arg0, User user) {
		text.setText("Hello "+ user.getAccount());
	}

	void onFailure(Call arg0, Exception e){
		new AlertDialog.Builder(getActivity())
		.setMessage("请求失败")
		.setNegativeButton("好", null)
		.show();
	}

	

	
}
