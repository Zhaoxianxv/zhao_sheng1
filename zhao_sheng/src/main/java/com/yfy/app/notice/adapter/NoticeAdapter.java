package com.yfy.app.notice.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.maintainnew.adapter.MaintainAdapter;
import com.yfy.app.notice.bean.NoticeBean;
import com.yfy.app.notice.cyc.NoticeDetailsActivity;
import com.yfy.app.notice.cyc.SendBoxActivity;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.DateUtils;
import com.yfy.final_tag.TagFinal;

import java.util.ArrayList;
import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



	private String read_no="未读",read_yes="已读";


	private List<NoticeBean> dataList;
	private Activity mContext;

	public void setDataList(List<NoticeBean> dataList) {
		this.dataList = dataList;
	}


	// 当前加载状态，默认为加载完成
	private int loadState = 2;

	public NoticeAdapter(Activity mContext) {
		this.mContext=mContext;
		this.dataList = new ArrayList<>();

	}

	@Override
	public int getItemViewType(int position) {
		// 最后一个item设置为FooterView
		if (position + 1 == getItemCount()) {
			return TagFinal.TYPE_FOOTER;
		} else {
			return dataList.get(position).getView_type();
		}
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		//进行判断显示类型，来创建返回不同的View
		if (viewType == TagFinal.TYPE_ITEM) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_main_item, parent, false);
			return new RecyclerViewHolder(view);

		}
		if (viewType == TagFinal.TYPE_TOP) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_main_top_item, parent, false);
			return new TopHolder(view);

		}
		if (viewType == TagFinal.TYPE_FOOTER) {
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
			reHolder.index=position;
			reHolder.time.setText(DateUtils.dateChange(reHolder.bean.getDate())+" : "+reHolder.bean.getSender());
			reHolder.title.setText(reHolder.bean.getTitle());
			reHolder.context.setText(reHolder.bean.getContent());
			GradientDrawable myGrad = (GradientDrawable)reHolder.read_tag.getBackground();
			if (reHolder.bean.getState().equals("0")) {
				reHolder.title.setTextColor(ColorRgbUtil.getBaseText());
				reHolder.context.setTextColor(ColorRgbUtil.getBaseText());
				reHolder.time.setTextColor(ColorRgbUtil.getBaseText());
				reHolder.read_tag.setText(read_no);
				myGrad.setColor(mContext.getResources().getColor(R.color.LightSalmon));
			} else {
				reHolder.title.setTextColor(ColorRgbUtil.getGrayText());
				reHolder.context.setTextColor(ColorRgbUtil.getGrayText());
				reHolder.time.setTextColor(ColorRgbUtil.getGrayText());
				reHolder.read_tag.setText(read_yes);
				myGrad.setColor(ColorRgbUtil.getGreen());
			}

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

	@Override
	public int getItemCount() {
		return dataList.size() + 1;
	}

	private class RecyclerViewHolder extends RecyclerView.ViewHolder {

		RelativeLayout layout;
		TextView title;
		TextView time;
		TextView context;
		TextView read_tag;
		int index;
		NoticeBean bean;
		RecyclerViewHolder(View itemView) {
			super(itemView);
			layout=  itemView.findViewById(R.id.notice_main_item_layout);
			time=  itemView.findViewById(R.id.notice_main_item_time);
			title=  itemView.findViewById(R.id.notice_main_item_title);
			context=  itemView.findViewById(R.id.notice_main_item_context);
			read_tag=  itemView.findViewById(R.id.notice_main_item_read_tag);
			layout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(mContext, NoticeDetailsActivity.class);
					intent.putExtra(TagFinal.OBJECT_TAG, bean);
					intent.putExtra(TagFinal.ALBUM_LIST_INDEX,index);
					mContext.startActivityForResult(intent,TagFinal.UI_TAG);
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

	private class TopHolder extends RecyclerView.ViewHolder {


		RelativeLayout layout;

		TopHolder(View itemView) {
			super(itemView);
			layout =  itemView.findViewById(R.id.notice_top_layout);
			layout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mContext.startActivity(new Intent(mContext,SendBoxActivity.class));
				}
			});

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
