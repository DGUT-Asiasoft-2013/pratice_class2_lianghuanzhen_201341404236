package com.example.helloworld.fragments.pages;

import java.io.IOException;
import java.util.List;

import com.example.helloworld.CommentsContentActivity;
import com.example.helloworld.FeedsContentActivity;
import com.example.helloworld.R;
import com.example.helloworld.fragments.widgets.AvatarView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import api.Server;
import api.entity.Article;
import api.entity.Comment;
import api.entity.Page;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class CommentListFragment extends Fragment {
	View view;
	ListView listView;

	List<Comment> data;
	int page = 0;
	View btnLoadMore;//加载更多
	TextView textLoadMore;
	String articleId;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view == null){
			view = inflater.inflate(R.layout.fragment_page_comment_list, null);
			btnLoadMore = inflater.inflate(R.layout.fragment_btn_load_more, null);
			textLoadMore = (TextView) btnLoadMore.findViewById(R.id.text_loadmore);

			listView = (ListView) view.findViewById(R.id.comment_list);
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
				view = inflater.inflate(R.layout.fragment_cell_comment_list, null);

			}else{
				view = convertView;
			}

			//设置数据，并获取

			AvatarView avatar = (AvatarView)view.findViewById(R.id.user_avatar); //头像
			TextView textComment = (TextView) view.findViewById(R.id.text);  //评论内容
			TextView textAuthorName = (TextView)view.findViewById(R.id.user_name); //作者
			TextView commentDate = (TextView)view.findViewById(R.id.comment_date);  //评论日期

			Comment comment = data.get(position);


			textComment.setText(comment.getText());
			textAuthorName.setText(comment.getAuthor().getName());
			avatar.load(Server.serverAddress + comment.getAuthor().getAvatar());
			//
			String list_createDate = DateFormat
					.format("yyyy-MM-dd hh:mm",
							data.get(position).getCreateDate()).toString();
			commentDate.setText(list_createDate);

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
		Comment text = data.get(position);

		Intent itnt = new Intent(getActivity(), FeedsContentActivity.class);
		itnt.putExtra("text", text);

		startActivity(itnt);
	}

	@Override
	public void onResume() {
		super.onResume();
		reload();
	}

	void reload(){
		Request request = Server.requestBuilderWithApi("article/"+getArticleId()+"/comments")
				.get()
				.build();

		Server.getSharedClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {
					final Page<Comment> data = new ObjectMapper()
							.readValue(arg1.body().string(),
									new TypeReference<Page<Article>>() {});

					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							CommentListFragment.this.page = data.getNumber();//放进主线程进行，确保数据部位空
							CommentListFragment.this.data = data.getContent();

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

		Request request = Server.requestBuilderWithApi("article"+getArticleId()+"/comments")
				.build();
		
		Server.getSharedClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						btnLoadMore.setEnabled(true);
						textLoadMore.setText("加载更多");

					}
				});
				try {
					final Page<Comment> comments = new ObjectMapper()
							.readValue(arg1.body().string(), 
									new TypeReference<Page<Article>>(){});
					if(comments.getNumber()>page){


						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								if(data==null){
									data = comments.getContent();
								}else{
									data.addAll(comments.getContent());
								}
								page = comments.getNumber();

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
	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

}
