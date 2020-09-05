package com.yfy.app.notice.adapter;

import android.content.Context;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.notice.bean.ReadState;

import java.util.List;


public class ReadStateAdapter extends AbstractAdapter<ReadState> {

	private int readColor, unreadColor;

	public ReadStateAdapter(Context context, List<ReadState> list) {
		super(context, list);
		readColor = context.getResources().getColor(R.color.notice_receipt_yes);
		unreadColor = context.getResources().getColor(R.color.notice_receipt_no);
	}

	@Override
	public void renderData(int position,
			AbstractAdapter<ReadState>.DataViewHolder holder,
			List<ReadState> list) {
		ReadState readState = list.get(position);
		TextView readName = holder.getView(TextView.class, R.id.read_name);
		readName.setText(readState.getName());
		if (readState.getStatus().equals("1")) {
			readName.setTextColor(readColor);
		} else {
			readName.setTextColor(unreadColor);
		}
	}

	@Override
	public com.yfy.app.notice.adapter.AbstractAdapter.ResInfo getResInfo() {
		ResInfo resInfo = new ResInfo();
		resInfo.layout = R.layout.notice_readstate_adp_item;
		resInfo.initIds = new int[] { R.id.read_name };
		return resInfo;
	}

}
