/**
 * 
 */
package com.yfy.app.pro.adapter;

import java.util.List;

import android.content.Context;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.adapter.base.AbstractAdapter3;
import com.yfy.beans.Supply;

/**
 * @version 1.0
 * @Desprition
 */
public class MySupplyAdapter extends AbstractAdapter3<Supply> {

	public MySupplyAdapter(Context context, List<Supply> list) {
		super(context, list);
	}

	@Override
	public void renderData(int position,
			DataViewHolder holder,
			List<Supply> list) {
		Supply supply = list.get(position);
		holder.getView(TextView.class, R.id.supply_name).setText(
				supply.getSupplies());
		holder.getView(TextView.class, R.id.supply_remark).setText(
				supply.getBz());
	}

	@Override
	public ResInfo getResInfo() {
		ResInfo resInfo = new ResInfo();
		resInfo.layout = R.layout.item_supply_type_lv;
		resInfo.initIds = new int[] { R.id.supply_name, R.id.supply_remark };
		return resInfo;
	}
}
