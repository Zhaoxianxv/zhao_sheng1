/**
 * 
 */
package com.yfy.app.notice.adapter;

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
import com.yfy.app.notice.adapter.NoticeAdapter;
import com.yfy.app.notice.bean.SendNotice;
import com.yfy.app.notice.cyc.OutBoxDetailActivity;
import com.yfy.final_tag.TagFinal;

import java.util.ArrayList;
import java.util.List;


/**
 * @author yfy1

 * @version 1.0
 * @Desprition
 */
public class SendBoxAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{



	private List<SendNotice> dataList;
	private Activity mContext;
	private String base_bg="#b3b3b3", text_color="#444444";

	public void setDataList(List<SendNotice> dataList) {
		this.dataList = dataList;
	}
	// 普通布局
	private final int TYPE_ITEM = 1;
	private final int TYPE_TOP = 3;
	// 脚布局
	private final int TYPE_FOOTER = 2;
	// 当前加载状态，默认为加载完成
	private int loadState = 2;
	// 正在加载
	public final int LOADING = 1;
	// 加载完成
	public final int LOADING_COMPLETE = 2;
	// 加载到底
	public final int LOADING_END = 3;




	public SendBoxAdapter(Activity mContext) {
		this.mContext=mContext;
		this.dataList = new ArrayList<>();

	}

	@Override
	public int getItemCount() {
		return dataList.size()+1;
	}


	@Override
	public int getItemViewType(int position) {
		// 最后一个item设置为FooterView
		if (position + 1 == getItemCount()) {
			return TYPE_FOOTER;
		}else{
			return TYPE_ITEM;
		}

	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		//进行判断显示类型，来创建返回不同的View
		if (viewType == TYPE_ITEM) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_outbox_adp_item, parent, false);
			return new RecyclerViewHolder(view);
		} else if (viewType == TYPE_FOOTER) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_refresh_footer, parent, false);
			return new FootViewHolder(view);
		}
		return null;
	}


	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if (holder instanceof RecyclerViewHolder) {
			RecyclerViewHolder reHolder = (RecyclerViewHolder) holder;
			reHolder.bean=dataList.get(position);
			reHolder.name.setText(reHolder.bean.getTitle());
			reHolder.time.setText(reHolder.bean.getMailSendDate());


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



	private class RecyclerViewHolder extends RecyclerView.ViewHolder {
		TextView time;
		TextView name;
		SendNotice bean;
		LinearLayout layout;
		RecyclerViewHolder(View itemView) {
			super(itemView);
			name=  itemView.findViewById(R.id.title);
			time=  itemView.findViewById(R.id.date);
			layout=  itemView.findViewById(R.id.notice_outbox_layout);

			layout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {

					Intent intent = new Intent(mContext, OutBoxDetailActivity.class);
					Bundle b = new Bundle();
					b.putString(TagFinal.ID_TAG, bean.getId());
					b.putString(TagFinal.TITLE_TAG, "通知详情");
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
			pbLoading =  itemView.findViewById(R.id.pb_loading);
			tvLoading =  itemView.findViewById(R.id.tv_loading);
			llEnd =  itemView.findViewById(R.id.ll_end);
			allEnd =  itemView.findViewById(R.id.recycler_bottom);

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
