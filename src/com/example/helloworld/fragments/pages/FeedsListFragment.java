package com.example.helloworld.fragments.pages;

import java.util.Random;
import java.util.zip.Inflater;

import com.example.helloworld.FeedsContentActivity;
import com.example.helloworld.R;

import android.R.string;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FeedsListFragment extends Fragment {

	View view;
	ListView listView;

	String data[];

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view == null){
			view = inflater.inflate(R.layout.fragment_page_feeds_list, null);

			Random rand = new Random();
			data = new String[10 + Math.abs(rand.nextInt()%20)];

			for(int i = 0;i< data.length;i++){
				data[i] = "This is row "+ rand.nextInt();
			}
		}

		//-----------
		//listView-R.id.list
		listView = (ListView) view.findViewById(R.id.list);

		listView.setAdapter(listAdapter);



		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				onItemClickLis(position);
			}


		});

		return view;
	}




	//-------
	//创建适配器 BaseAdapter listAdapter
	//getView(重要),getItemID,getItem,getCount
	//Suppresslint("InflateParams")
	//在getView里面,取出视图 layoutInflater *= *.from(parent.getContext())
	//view = in...in(and.r.lay.simple_List_items,null)
	BaseAdapter listAdapter = new BaseAdapter() {


		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;

			if(convertView == null){
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(android.R.layout.simple_list_item_1, null);

			}else{
				view = convertView;
			}

			TextView text1 = (TextView) view.findViewById(android.R.id.text1);
			text1.setText(data[position]);

			return view;
		}


		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return data[position];
		}

		@Override
		public int getCount() {
			return data==null ? 0 : data.length;
		}
	};

	public void onItemClickLis(int position){
		String text = new String(data[position]);
		Intent itnt = new Intent(getActivity(),FeedsContentActivity.class);
	
		
		startActivity(itnt);
	}

}
