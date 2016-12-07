package com.example.helloworld.fragments.pages;

import com.example.helloworld.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FeedsListFragment extends Fragment {

	View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view == null){
			view = inflater.inflate(R.layout.fragment_page_feeds_list, null);
		}
		
		//-----------
		//listView-R.id.list
		
		return view;
	}
	
	
	//-------
	//创建适配器 BaseAdapter listAdapter
	//getView(重要),getItemID,getItem,getCount
	//Suppresslint("InflateParams")
	//在getView里面,取出视图 layoutInflater *= *.from(parent.getContext())
	//view = in...in(and.r.lay.simple_List_items,null)
	
	
}
