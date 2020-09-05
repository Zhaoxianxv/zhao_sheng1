package com.yfy.app.info_submit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yfy.app.info_submit.constants.InfosConstant;
import com.yfy.app.info_submit.infos.WriteItem;
import com.yfy.app.info_submit.view.WriteInfoView;
import com.example.zhao_sheng.R;

import java.util.ArrayList;
import java.util.List;

public class FormShowAdapter extends BaseAdapter {

	private Context context = null;
	private List<ArrayList<WriteItem>> totalList;

	public FormShowAdapter(Context context, List<ArrayList<WriteItem>> totalList) {
		this.context = context;
		this.totalList = totalList;
	}

	@Override
	public int getCount() {
		return totalList.size();
	}

	@Override
	public Object getItem(int position) {
		return totalList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.info_item_formshow_lv, null);
			holder.itemParentTv = (TextView) convertView.findViewById(R.id.form_item_title);

			holder.complete_degree = (TextView) convertView.findViewById(R.id.complete_degree);

			holder.writeInfo = (WriteInfoView) convertView.findViewById(R.id.write_info_view);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.itemParentTv.setText(InfosConstant.SUBMIT_ITEM_PARENT[position]);
		holder.complete_degree.setText(InfosConstant.COMPLETE_DEGRESS[position]);
		ArrayList<WriteItem> list = totalList.get(position);

		holder.writeInfo.setPadding(10, 10, 10, 30, 10, 10);
		holder.writeInfo.setMarginTotal(26);
		holder.writeInfo.setTextSize(16);
		holder.writeInfo.setData(list);

		return convertView;
	}

	private class ViewHolder {
		public TextView itemParentTv;
		public TextView complete_degree;
		public WriteInfoView writeInfo;
	}
}
