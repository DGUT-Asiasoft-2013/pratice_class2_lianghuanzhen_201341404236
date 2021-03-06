package com.example.helloworld.fragments.pages;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.zip.Inflater;

import com.example.helloworld.FeedsContentActivity;
import com.example.helloworld.R;
import com.example.helloworld.fragments.widgets.AvatarView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.R.string;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import api.Server;
import api.entity.Page;
import api.entity.Article;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class FeedsListFragment extends Fragment {

	View view;
	ListView listView;

	List<Article> data;
	int page = 0;
	View btnLoadMore;//加载更多
	TextView textLoadMore;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view == null){
			view = inflater.inflate(R.layout.fragment_page_feeds_list, null);
			btnLoadMore = inflater.inflate(R.layout.fragment_btn_load_more, null);
			textLoadMore = (TextView) btnLoadMore.findViewById(R.id.text_loadmore);

			listView = (ListView) view.findViewById(R.id.list);
			listView.addFooterView(btnLoadMore);
			listView.setAdapter(listAdapter);

			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent,View view,int position,long id){
					onItemClicked(position);
				}

			});

			btnLoadMore.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					loadmore();
				}
			});

		}
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
				view = inflater.inflate(R.layout.fragment_cell_feeds_listcell, null);

			}else{
				view = convertView;
			}

			//设置数据，并获取
			TextView Title = (TextView) view.findViewById(R.id.text_title);  //标题
			AvatarView avatar = (AvatarView)view.findViewById(R.id.user_avatar); //头像
			TextView textContent = (TextView) view.findViewById(R.id.text);  //文章
			TextView textAuthorName = (TextView)view.findViewById(R.id.user_name); //作者
			TextView textDate = (TextView)view.findViewById(R.id.text_date);  //编写日期

			Article article = data.get(position);

			Title.setText(article.getTitle());
			textContent.setText(article.getText());
			textAuthorName.setText(article.getAuthor().getName());
			avatar.load(Server.serverAddress + article.getAuthor().getAvatar());

			String list_createDate = DateFormat
					.format("yyyy-MM-dd hh:mm",
							data.get(position).getCreateDate()).toString();
			textDate.setText(list_createDate);

			return view;
		}


		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public int getCount() {
			return data==null ? 0 : data.size();
		}
	};


	void onItemClicked(int position){
		Article article = data.get(position);

		Intent itnt = new Intent(getActivity(), FeedsContentActivity.class);
		itnt.putExtra("data", article);

		startActivity(itnt);
	}

	@Override
	public void onResume() {
		super.onResume();
		reload();
	}
	
	void reload(){
		Request request = Server.requestBuilderWithApi("feeds")
				.get()
				.build();

		Server.getSharedClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {
					String value = arg1.body().string();
					final Page<Article> data = new ObjectMapper()
							.readValue(value,
									new TypeReference<Page<Article>>() {});
				
					getActivity().runOnUiThread(new Runnable() {
					
						@Override
						public void run() {
							FeedsListFragment.this.page = data.getNumber();//放进主线程进行，确保数据部位空
							FeedsListFragment.this.data = data.getContent();
							
							listAdapter.notifyDataSetInvalidated();

						}
					});
				} catch (final Exception e) {
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							new AlertDialog.Builder(getActivity())
							.setMessage(e.getMessage())
							.show();
						}
					});
				}


			}

			@Override
			public void onFailure(Call arg0, final IOException e) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						new AlertDialog.Builder(getActivity())
						.setMessage(e.getMessage())
						.show();
					}
				});

			}
		});
	}


	void loadmore(){
		btnLoadMore.setEnabled(false);
		textLoadMore.setText("载入中...");

		Request request = Server.requestBuilderWithApi("feeds"+(page+1)).get().build();
		Server.getSharedClient().newCall(request).enqueue(new Callback() {
			
			public void onResponse(Call arg0, Response arg1) throws IOException {
				getActivity().runOnUiThread(new Runnable() {

					public void run() {
						btnLoadMore.setEnabled(true);
						textLoadMore.setText("加载更多");

					}
				});
				try {
					final Page<Article> feeds = new ObjectMapper()
							.readValue(arg1.body().string(), 
									new TypeReference<Page<Article>>(){});
					if(feeds.getNumber()>page){
						

						getActivity().runOnUiThread(new Runnable() {

							public void run() {
								if(data==null){
									data = feeds.getContent();
								}else{
									data.addAll(feeds.getContent());
								}
								page = feeds.getNumber();
								
								listAdapter.notifyDataSetInvalidated();

							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
				}


			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						btnLoadMore.setEnabled(true);
						textLoadMore.setText("加载更多");
					}
				});

			}
		});

	}
}
