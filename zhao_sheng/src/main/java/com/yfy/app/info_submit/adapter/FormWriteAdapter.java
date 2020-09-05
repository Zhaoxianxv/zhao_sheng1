package com.yfy.app.info_submit.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yfy.app.info_submit.infos.WriteItem;
import com.yfy.final_tag.StringJudge;
import com.example.zhao_sheng.R;

import java.util.List;

public class FormWriteAdapter extends AbstractAdapter<WriteItem> {

	private TextView item_name,item_value;
	
	public FormWriteAdapter(Context context, List<WriteItem> list) {
		super(context, list);
	}

	@Override
	public int[] getFindViewByIDs() {
		return new int[] { R.id.item_name, R.id.item_value};
	}

	@Override
	public int getLayoutId() {
		return R.layout.info_item_formwrite_lv;
	}

	@Override
	public void renderData(int position,
                           AbstractAdapter<WriteItem>.DataViewHolder holder, List<WriteItem> list) {
		WriteItem item=list.get(position);
		item_name=holder.getView(TextView.class, R.id.item_name);
		item_value=holder.getView(TextView.class, R.id.item_value);
		item_name.setText(item.getItemName());
		if (!StringJudge.isEmpty(item.getItemValue())) {
			item_value.setText(item.getItemValue());
		}else {
			item_value.setText(R.string.not_fill_w);
		}
	}

	@Override
	public void onViewclick(View v, int position, List<WriteItem> list) {
		
	}

	@Override
	public int[] setListennerId() {
		return null;
	}
}
