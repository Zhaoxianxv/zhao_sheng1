package com.yfy.app.info_submit.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import java.util.LinkedHashMap;

public class HeaderMenuIndicator extends MenuIndicator {

	private TextView[] tvs = new TextView[2];
	private String[] name = { "学生资料", "分班查询" };
	private int[] backgroud = { R.drawable.radius_left_top6_bottom6,
			R.drawable.radius_rigth_top6_bottom6};

	public HeaderMenuIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public LinkedHashMap<Integer, View> getViewMap(LayoutInflater inflater) {
		LinkedHashMap<Integer, View> viewMap = new LinkedHashMap<Integer, View>();
		View view;
		for (int i = 0; i < 2; i++) {
			view = inflater.inflate(R.layout.item_menu, null);
			tvs[i] = (TextView) view.findViewById(R.id.test_tv);
			tvs[i].setText(name[i]);
			if (i == 0) {
				tvs[i].setTextColor(Color.WHITE);
				tvs[i].setBackgroundResource(backgroud[i]);
			}
			viewMap.put(i, view);
		}
		return viewMap;
	}

	public void selectItem(int position) {

		for (int i = 0; i < tvs.length; i++) {
			if (i == position) {
				tvs[i].setTextColor(Color.WHITE);
				tvs[i].setBackgroundResource(backgroud[position]);
			} else {
				tvs[i].setTextColor(Color.BLACK);
				tvs[i].setBackgroundColor(Color.WHITE);
			}
		}
	}

	@Override
	public void onSelect(View v) {

		int i = v.getId();
		tvs[i].setTextColor(Color.WHITE);
		tvs[i].setBackgroundResource(backgroud[i]);

	}

	@Override
	public void offSelect(View v) {
		int i = v.getId();
		tvs[i].setTextColor(Color.BLACK);
		tvs[i].setBackgroundColor(Color.WHITE);
	}
}
