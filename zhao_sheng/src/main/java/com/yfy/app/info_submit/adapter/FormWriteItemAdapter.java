package com.yfy.app.info_submit.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import java.util.Iterator;
import java.util.List;

public class FormWriteItemAdapter extends AbstractAdapter<String> {

	private TextView item_ability;
	public boolean[] isselected;
	private StringBuilder sb;
	public int noChange;

	public FormWriteItemAdapter(Context context, List<String> list) {
		super(context, list);
		isselected = new boolean[100];
		noChange = list.size() - 1;
	}

	@Override
	public int[] getFindViewByIDs() {
		return new int[] { R.id.item_ability, R.id.item_ability_rela };
	}

	@Override
	public int getLayoutId() {
		return R.layout.info_item_formwriteitem_gv;
	}

	@Override
	public void renderData(int position, DataViewHolder holder,
			List<String> list) {
		item_ability = holder.getView(TextView.class, R.id.item_ability);
		
		if (!isselected[position]) {
			item_ability.setBackgroundResource(R.color.Gainsboro);
		} else {
			item_ability.setBackgroundResource(R.color.app_base_color);
		}
		item_ability.setText(list.get(position));
	}

	public void addItem(String s) {
		list.add(s);
		notifyDataSetChanged();
	}

	public void removeItem(int position) {
		list.remove(position);
		for (int i = position; i < list.size()+1 ; i++) {
			isselected[i] = isselected[i + 1];
		}
		notifyDataSetChanged();
	}

	public String allSelectedItem() {
		sb = new StringBuilder();
		int i = 0;
		for (Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
			String item = iterator.next();
			if (isselected[i]) {
				sb.append(item).append(";");
			}
			i++;
		}
		if (sb.length()>0) {
			sb.delete(sb.length()-1, sb.length());
		}
		return sb.toString();
	}

	@Override
	public void onViewclick(View v, int position, List<String> list) {

	}

	@Override
	public int[] setListennerId() {
		return null;
	}
}
