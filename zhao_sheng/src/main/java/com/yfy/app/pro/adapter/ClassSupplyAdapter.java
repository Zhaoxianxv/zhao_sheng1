/**
 * 
 */
package com.yfy.app.pro.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.roamer.slidelistview.SlideBaseAdapter;
import com.yfy.beans.Property;

/**
 *
 * @version 1.0
 * @Desprition
 */
public class ClassSupplyAdapter extends SlideBaseAdapter {

	private List<Property> list;

	public ClassSupplyAdapter(Context context, List<Property> list) {
		super(context);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = createConvertView(position);
			holder = new ViewHolder();
			holder.supply_name = (TextView) convertView
					.findViewById(R.id.supply_name);
			holder.supply_date = (TextView) convertView
					.findViewById(R.id.supply_date);
			holder.supply_remark = (TextView) convertView
					.findViewById(R.id.supply_remark);
			holder.delete_supply = (TextView) convertView
					.findViewById(R.id.delete_supply);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Property property = list.get(position);
		holder.supply_name.setText(property.getSupplies());
		holder.supply_date.setText(property.getAddtime());
		holder.supply_remark.setText(property.getBz());
		holder.delete_supply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (listenner != null) {
					listenner.onAdapterClick(holder.delete_supply, position,
							list);
				}
			}
		});
		return convertView;
	}

	@Override
	public int getFrontViewId(int position) {
		return R.layout.item_oneclass_prop_lv;
	}

	@Override
	public int getLeftBackViewId(int position) {
		return 0;
	}

	@Override
	public int getRightBackViewId(int position) {
		return R.layout.item_slide_behind;
	}

	public void notifyDataSetChanged(List<Property> list) {
		this.list = list;
		super.notifyDataSetChanged();
	}

	private class ViewHolder {
		public TextView supply_name;
		public TextView supply_date;
		public TextView supply_remark;
		public TextView delete_supply;
	}

	private OnAdapterListenner listenner = null;

	public void setOnAdapterListenner(OnAdapterListenner listenner) {
		this.listenner = listenner;
	}

	public static interface OnAdapterListenner {
		public void onAdapterClick(View v, int position, List<Property> list);
	}
}
