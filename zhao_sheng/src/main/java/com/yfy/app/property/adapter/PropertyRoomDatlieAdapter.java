/**
 * 
 */
package com.yfy.app.property.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.property.PropertyBadActivity;
import com.yfy.app.property.PropertyRoomActivity;
import com.yfy.app.property.bean.ArticleRoom;
import com.yfy.app.property.bean.BadObj;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.TagFinal;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @Desprition
 */
public class PropertyRoomDatlieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



	private List<ArticleRoom> articles;
	private PropertyRoomActivity mActivity;
	private ConvertObjtect obj;
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

	public void setRooms(List<ArticleRoom> articles) {
		this.articles = articles;
	}

	public PropertyRoomDatlieAdapter(PropertyRoomActivity mActivity, List<ArticleRoom> articles) {
		this.articles=articles;
		this.mActivity=mActivity;
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
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.property_room_datlie_item, parent, false);
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
			reHolder.bean=articles.get(position);
			reHolder.name.setText(Html.fromHtml(html(reHolder.bean.getName(),reHolder.bean.getAll_num())));
			reHolder.deficiency_num.setText("缺失："+reHolder.bean.getDefi_num());


			reHolder.bad_num.setText("损坏："+getBadNum(reHolder.bean));
			reHolder.all_good.setText("完好："+reHolder.bean.getGood_num());
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
		return articles.size();
	}

	private class RecyclerViewHolder extends RecyclerView.ViewHolder {

		TextView name;
		TextView all_good;
		TextView deficiency_num;
		TextView bad_num;
		ArticleRoom bean;
		private DialogInterface.OnClickListener ok= new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which){
					case -1:
						changeBean(bean,true );
						notifyDataSetChanged();
						break;
					case -2:
						changeBean(bean,false);
						notifyDataSetChanged();
						break;
				}
				dialog.dismiss();
			}
		};
		RecyclerViewHolder(View itemView) {
			super(itemView);
			name= (TextView) itemView.findViewById(R.id.property_article_name);
			all_good= (TextView) itemView.findViewById(R.id.property_article_all_good);
			deficiency_num= (TextView) itemView.findViewById(R.id.property_article_deficiency);
			bad_num= (TextView) itemView.findViewById(R.id.property_article_bad);


			all_good.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					if (all_good.getText().equals("KO")){
						deficiency_num.setVisibility(View.VISIBLE);
						bad_num.setVisibility(View.VISIBLE);
						bad_num.setText("损坏：0");
						deficiency_num.setText("缺失：0");
						all_good.setTextColor(Color.DKGRAY);
						all_good.setText("完好："+bean.getGood_num());
					}else{
						all_good.setText("KO");
						bean.setDefi_num("0");
						bean.setGood_num(bean.getAll_num());
						all_good.setTextColor(Color.GREEN);
						deficiency_num.setVisibility(View.GONE);
						bad_num.setVisibility(View.GONE);
					}
				}
			});
			bad_num.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(mActivity, PropertyBadActivity.class);
					intent.putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) articles);
					intent.putExtra("index_id",bean.getId());
					mActivity.startActivityForResult(intent, TagFinal.UI_REFRESH);
				}
			});
			deficiency_num.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
					builder.setMessage("物品是否缺失");
					builder.setTitle("");
					builder.setPositiveButton("+", ok);
					builder.setNegativeButton("- ", ok);
					builder.create().show();
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

	private String html(String name,String num){

		String gray="#3c3f41", green="#942328";
		String font="<font color=";
		String font_end=" >";
		String date_end="</font>";
		StringBuilder sb=new StringBuilder();
		if (StringJudge.isEmpty(num)){
			sb.append(font).append(gray).append(font_end).append(name).append(date_end)
					.append(font).append(green).append(font_end).append(num).append(date_end);
		}else{
			sb.append(font).append(gray).append(font_end).append(name).append(date_end)
					.append(font).append(green).append(font_end).append("  (")
					.append(num).append(")").append(date_end);
		}

		return  sb.toString();
	}

	//缺失操作逻辑

	/**
	 * @param bean
	 * @param is true添加缺失
     */
	public void changeBean(ArticleRoom bean,boolean is){


		int all=obj.getInt(bean.getAll_num());
		int good=obj.getInt(bean.getGood_num());
		int deficiency=obj.getInt(bean.getDefi_num());

		if (is){
			if (deficiency<all&&good>0){
				good--;
				deficiency++;
				bean.setGood_num(good+"");
				bean.setDefi_num(deficiency+"");
			}
		}else{
			if (good!=all&&deficiency>=0){
				good++;
				deficiency--;
				bean.setGood_num(good+"");
				bean.setDefi_num(deficiency+"");
			}
		}
	}

	public String getBadNum(ArticleRoom bean){
		List<BadObj> badObjs=bean.getBad_num();
		int all=obj.getInt(bean.getAll_num());
		int deficiency=obj.getInt(bean.getDefi_num());
		if (StringJudge.isEmpty(badObjs)){
			return "0";
		}else{
			int num=0;
			for (BadObj bad:badObjs ){
				num+=obj.getInt(bad.getNum());
			}
			bean.setGood_num((all-num-deficiency)+"");
			return ""+num;
		}
	}

}
