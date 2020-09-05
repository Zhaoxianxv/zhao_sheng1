/**
 * 
 */
package com.yfy.app.property.adapter;

import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.property.PropertyBadActivity;
import com.yfy.app.property.bean.ArticleRoom;
import com.yfy.app.property.bean.BadObj;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.dialog.DialogTools;
import com.yfy.final_tag.StringJudge;

import java.util.List;

/**
 * @version 1.0
 * @Desprition
 */
public class PropertyBadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


	private List<ArticleRoom> articleRoomList;
	private List<BadObj> badObjs;
	private PropertyBadActivity activity;
	private ConvertObjtect obj;
	private int max_num=0;

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

	public void setBadObj(List<BadObj> badObjs) {
		this.badObjs = badObjs;

	}
	public List<BadObj> getBadObj() {
		return badObjs;

	}

	public PropertyBadAdapter(PropertyBadActivity activity, List<BadObj> badObjs,int max_num) {
		this.badObjs=badObjs;
		this.activity = activity;
		this.max_num = max_num;
		obj=ConvertObjtect.getInstance();


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
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.property_bad_item, parent, false);
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

			reHolder.bean=badObjs.get(position);
			reHolder.name.setText(reHolder.bean.getContent()+"       "+reHolder.bean.getNum());
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
		return badObjs.size();
	}

	private class RecyclerViewHolder extends RecyclerView.ViewHolder {


		BadObj bean;
		TextView name;
		TextView clear;
		TextView add;
		ImageView delete_all;
		RecyclerViewHolder(View itemView) {
			super(itemView);
			name= (TextView) itemView.findViewById(R.id.bad_name_num);
			add= (TextView) itemView.findViewById(R.id.bad_add_num);
			clear= (TextView) itemView.findViewById(R.id.bad_clear_num);
			delete_all= (ImageView) itemView.findViewById(R.id.bad_delete_all);
			add.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int num=obj.getInt(bean.getNum());
					if (getNum()==0){
						DialogTools.getInstance().showDialog(activity,"","（损坏+缺失）已达到最大值！");
					}else if (getNum()>0){
						num++;
						bean.setNum(num+"");
						notifyDataSetChanged();
					}
				}
			});
			clear.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int num=obj.getInt(bean.getNum());
					switch (num){
						case 0:
							break;
						case 1:
							badObjs.remove(bean);
							getNum();
							notifyDataSetChanged();
							break;
						default:
							num--;
							bean.setNum(num+"");
							getNum();
							notifyDataSetChanged();
							break;
					}
						return;
				}
			});
			delete_all.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					DialogTools.getInstance().showDialog(activity, "", "是否删除此条数据", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							badObjs.remove(bean);
							getNum();
							notifyDataSetChanged();
						}
					});
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

	public int getNum(){
		List<BadObj> bads=getBadObj();
		if (StringJudge.isEmpty(bads))
			return max_num;
		int num=0;
		for (BadObj bad:bads){
			num+=obj.getInt(bad.getNum());
		}
		return max_num-num;
	}
}
