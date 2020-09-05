package com.yfy.app.footbook;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.final_tag.glide.GlideTools;

public class MyListAdapter extends BaseExpandableListAdapter {
	private static final String TAG = "zxx";
	private LayoutInflater mInflater;
	private Context mContext;
	private List<WeekFoot> group;

	public void setGroup(List<WeekFoot> group) {
		this.group = group;
		notifyDataSetChanged();
	}

	public MyListAdapter(Context context, List<WeekFoot> group) {
		this.mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.group = group;
	}



	public Object getChild(int groupPosition, int childPosition) {
		return group.get(groupPosition).getFoods().get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public int getChildrenCount(int groupPosition) {
		return group.get(groupPosition).getFoods().size();
	}

	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		Foot foot=group.get(groupPosition).getFoods().get(childPosition);
		ViewHolder vHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.foot_book_item, null);
			vHolder = new ViewHolder();
			vHolder.title = (TextView) convertView.findViewById(R.id.foot_title);
			vHolder.content = (TextView) convertView.findViewById(R.id.foot_content);
			vHolder.num = (TextView) convertView.findViewById(R.id.foot_reting);
			vHolder.imge = (ImageView) convertView.findViewById(R.id.foot_image);
			vHolder.priase = (ImageView) convertView.findViewById(R.id.foot_ispriase);
			convertView.setTag(vHolder);
		}else {
			vHolder = (ViewHolder) convertView.getTag();
		}
		vHolder.title.setText(foot.getName());
		vHolder.content.setText(foot.getContent());
		vHolder.num.setText(foot.getPraise());
		if (foot.getIspraise().equals("true")){
			GlideTools.loadImage(mContext,R.drawable.graded_dynamic_praise_selected,vHolder.priase);
		}else{
			GlideTools.loadImage(mContext,R.drawable.graded_dynamic_praise_unselected,vHolder.priase);
		}

		final String id=foot.getId(),isP=foot.getIspraise();
		vHolder.priase.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (isPriase!=null){
					isPriase.isPriase(id,isP);
				}
			}
		});
		GlideTools.chanMult(mContext, foot.getImage(), vHolder.imge);
		return convertView;
	}
	
	class ViewHolder{

		public TextView title;
		public TextView content;
		public TextView num;
		public ImageView imge;
		public ImageView priase;
	}
	class ViewHolderGroup{

		public TextView top_group;
	}

	public Object getGroup(int groupPosition) {
		return group.get(groupPosition);
	}

	public int getGroupCount() {
		return group.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	/**
	 * create group view and bind data to view
	 */
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		ViewHolderGroup vHolder=null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.property_item_group, null);
			vHolder = new ViewHolderGroup();
			vHolder.top_group= (TextView) convertView.findViewById(R.id.grade_name);
			convertView.setTag(vHolder);
		}else{
			vHolder = (ViewHolderGroup) convertView.getTag();
		}
		vHolder.top_group.setBackgroundColor(Color.rgb(238,238,238));
		vHolder.top_group.setTextColor(Color.rgb(97,97,97));
		vHolder.top_group.setText(group.get(groupPosition).toString());
		return convertView;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public boolean hasStableIds() {
		return true;
	}



	public IsPriaseTab isPriase;

	public void setIsPriase(IsPriaseTab isPriase) {
		this.isPriase = isPriase;
	}

	public interface IsPriaseTab{
		void isPriase(String id,String state);
	}

}
