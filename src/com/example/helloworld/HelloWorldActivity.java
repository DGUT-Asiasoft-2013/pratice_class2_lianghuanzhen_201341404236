package com.example.helloworld;

import java.util.zip.Inflater;

import com.example.helloworld.fragments.pages.FeedsListFragment;
import com.example.helloworld.fragments.pages.MeProfileFragment;
import com.example.helloworld.fragments.pages.NoteListFragment;
import com.example.helloworld.fragments.pages.SearchPageFragment;
import com.example.helloworld.fragments.widgets.MainTabbarFragment;
import com.example.helloworld.fragments.widgets.MainTabbarFragment.OnTabSelectedListener;

import android.R.integer;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HelloWorldActivity extends Activity {

	//	TextView forgetPView;

	//在此创建 fragment 4个 page，少了创建new函数，程序崩溃
	FeedsListFragment fragFeedsList = new FeedsListFragment();
	NoteListFragment fragNoteList = new NoteListFragment();
	SearchPageFragment fragSearchPage = new SearchPageFragment();
	MeProfileFragment fragMeProfile = new MeProfileFragment();
	

	MainTabbarFragment tabbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_welcome);

		tabbar = (MainTabbarFragment) getFragmentManager().findFragmentById(R.id.frag_tabbar);
		tabbar.setOnTabSelectedListener(new OnTabSelectedListener() {

			@Override
			public void onTabSelected(int index) {
				changeContentFragment(index);

			}
		});
		
		findViewById(R.id.btn_new).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onShareClicked();
				
			}
		});


	}

	@Override
	protected void onResume() {
		super.onResume();
		tabbar.setSelectedItem(0);
	}
	
	//------------------------
    //跟PasswordRecover类里面更换Fragstep1和step2类似
	void changeContentFragment(int index){
		Fragment newFrag = null;

		switch (index) {
		case 0: newFrag = fragFeedsList;
		    break;
		case 1: newFrag = fragNoteList;
		    break;
		case 2: newFrag = fragSearchPage;
		    break;
		case 3: newFrag = fragMeProfile; 
		    break;
		default:
			break;
		}
		
		if(newFrag == null) return;
		
		getFragmentManager()
		.beginTransaction()
		.replace(R.id.content, newFrag) //把newfrag传进content
		.commit();
		
	}

	void onShareClicked(){
		Intent itnt = new Intent(this,ShareActivity.class);	
		startActivity(itnt);
		
		
		overridePendingTransition(R.anim.add_in_button,R.anim.add_out_button);
	}


}
