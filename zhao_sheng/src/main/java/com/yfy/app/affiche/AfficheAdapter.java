package com.yfy.app.affiche;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.school.bean.SchoolNews;
import com.yfy.final_tag.TagFinal;

import java.util.ArrayList;
import java.util.List;

public class AfficheAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private List<SchoolNews> dataList;

	private Activity mContext;

	public void setDataList(List<SchoolNews> dataList) {
		this.dataList = dataList;
	}

	// 当前加载状态，默认为加载完成
	private int loadState = 2;
	public AfficheAdapter(Activity mContext) {
		this.mContext = mContext;
		this.dataList = new ArrayList<>();

	}
	@Override
	public int getItemViewType(int position) {
		// 最后一个item设置为FooterView
		if (position + 1 == getItemCount()) {
			return TagFinal.TYPE_FOOTER;
		} else {
			return TagFinal.TYPE_ITEM;
		}
	}
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		//进行判断显示类型，来创建返回不同的View
		if (viewType == TagFinal.TYPE_ITEM) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.affiche_item_layout, parent, false);
			return new RecyclerViewHolder(view);

		} else if (viewType == TagFinal.TYPE_FOOTER) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_refresh_footer, parent, false);
			return new FootViewHolder(view);
		}
		return null;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if (holder instanceof RecyclerViewHolder) {
			RecyclerViewHolder reHolder = (RecyclerViewHolder) holder;
			reHolder.bean = dataList.get(position);
			reHolder.name.setText(reHolder.bean.getNewslist_head());
			reHolder.time.setText(reHolder.bean.getNewslist_time().split(" ")[0]);
		} else if (holder instanceof FootViewHolder) {
			FootViewHolder footViewHolder = (FootViewHolder) holder;
			switch (loadState) {
				case TagFinal.LOADING: // 正在加载
					footViewHolder.pbLoading.setVisibility(View.VISIBLE);
					footViewHolder.tvLoading.setVisibility(View.VISIBLE);
					footViewHolder.llEnd.setVisibility(View.GONE);
					break;
				case TagFinal.LOADING_COMPLETE: // 加载完成
					footViewHolder.pbLoading.setVisibility(View.INVISIBLE);
					footViewHolder.tvLoading.setVisibility(View.INVISIBLE);
					footViewHolder.llEnd.setVisibility(View.GONE);
					footViewHolder.allEnd.setVisibility(View.GONE);
					break;
				case TagFinal.LOADING_END: // 加载到底
					footViewHolder.pbLoading.setVisibility(View.GONE);
					footViewHolder.tvLoading.setVisibility(View.GONE);
					footViewHolder.llEnd.setVisibility(View.GONE);
					break;
				default:
					break;
			}
		}
	}

	@Override
	public int getItemCount() {
		return dataList.size() + 1;
	}
	private class RecyclerViewHolder extends RecyclerView.ViewHolder {
		TextView name;
		TextView time;
		RelativeLayout layout;
		SchoolNews bean;
		RecyclerViewHolder(View itemView) {
			super(itemView);
			name = (TextView) itemView.findViewById(R.id.affiche_item_title);
			layout=itemView.findViewById(R.id.affiche_item_lilayout);
			time = (TextView) itemView.findViewById(R.id.affiche_item_time);
			layout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {

					Intent intent = new Intent(mContext, AffWebActivity.class);
					Bundle b = new Bundle();
					b.putString(TagFinal.URI_TAG, bean.getNews_info_detailed());
					b.putString(TagFinal.TITLE_TAG, "公告详情");
					intent.putExtras(b);
					mContext.startActivity(intent);

				}
			});


		}
	}

	private class FootViewHolder extends RecyclerView.ViewHolder {
		ProgressBar pbLoading;
		TextView tvLoading;
		LinearLayout llEnd;
		RelativeLayout allEnd;
		FootViewHolder(View itemView) {
			super(itemView);
			pbLoading = (ProgressBar) itemView.findViewById(R.id.pb_loading);
			tvLoading = (TextView) itemView.findViewById(R.id.tv_loading);
			llEnd = (LinearLayout) itemView.findViewById(R.id.ll_end);
			allEnd = (RelativeLayout) itemView.findViewById(R.id.recycler_bottom);

		}
	}

	/**
	 * 设置上拉加载状态
	 *
	 * @param loadState 1.正在加载 2.加载完成 3.加载到底
	 */
	public void setLoadState(int loadState) {
		this.loadState = loadState;
		notifyDataSetChanged();
	}
}