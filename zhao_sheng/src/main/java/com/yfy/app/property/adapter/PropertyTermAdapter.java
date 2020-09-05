/**
 * 
 */
package com.yfy.app.property.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.property.PropertyMainActivity;
import com.yfy.app.property.PropertyRoomActivity;
import com.yfy.app.property.bean.PropretyRoom;
import com.yfy.final_tag.TagFinal;

import java.util.List;

/**
 * @version 1.0
 * @Desprition
 */
public class PropertyTermAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


	private List<PropretyRoom> rooms;
	private Context activity;

	// 普通布局
	private final int TYPE_ITEM = 1;
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

	public void setRooms(List<PropretyRoom> rooms) {
		this.rooms = rooms;

	}

	public PropertyTermAdapter(Context activity, List<PropretyRoom> rooms) {
		this.rooms=rooms;
		this.activity = activity;
	}


	@Override
	public int getItemViewType(int position) {
		if (position==getItemCount()){
			return TYPE_FOOTER;
		} else {
			return TYPE_ITEM;
		}
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == TYPE_ITEM) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.property_room_term_item, parent, false);
			return new RecyclerViewHolder(view);

		} else if (viewType == TYPE_FOOTER) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_refresh_footer, parent, false);
			return new FootViewHolder(view);
		}
		return null;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if (holder instanceof RecyclerViewHolder){
			RecyclerViewHolder reHolder = (RecyclerViewHolder) holder;

			reHolder.bean=rooms.get(position);
			reHolder.name.setText(reHolder.bean.getName()+"(负责人)");
			reHolder.term.setText(reHolder.bean.getId());
		}else if (holder instanceof FootViewHolder) {
			FootViewHolder footViewHolder = (FootViewHolder) holder;
			switch (loadState) {
				case LOADING: // 正在加载
					footViewHolder.pbLoading.setVisibility(View.VISIBLE);
					footViewHolder.tvLoading.setVisibility(View.VISIBLE);
					footViewHolder.llEnd.setVisibility(View.GONE);
					break;
				case LOADING_COMPLETE: // 加载完成
					footViewHolder.pbLoading.setVisibility(View.INVISIBLE);
					footViewHolder.tvLoading.setVisibility(View.INVISIBLE);
					footViewHolder.llEnd.setVisibility(View.GONE);
					footViewHolder.allEnd.setVisibility(View.GONE);
					break;
				case LOADING_END: // 加载到底
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
		return rooms.size();
	}

	private class RecyclerViewHolder extends RecyclerView.ViewHolder {


		PropretyRoom bean;
		RelativeLayout layout;
		TextView name;
		TextView term;
		RecyclerViewHolder(View itemView) {
			super(itemView);
			name= (TextView) itemView.findViewById(R.id.property_room_name);
			term= (TextView) itemView.findViewById(R.id.property_room_term);
			layout= (RelativeLayout) itemView.findViewById(R.id.property_room_term_layout);


			layout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(activity, PropertyRoomActivity .class);

					activity.startActivity(intent);
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
