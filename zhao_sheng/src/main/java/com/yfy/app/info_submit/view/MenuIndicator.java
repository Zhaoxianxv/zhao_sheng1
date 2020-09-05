package com.yfy.app.info_submit.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public abstract class MenuIndicator extends LinearLayout implements
		OnClickListener {

	@SuppressLint("UseSparseArrays")
	private LinkedHashMap<Integer, View> viewMap = new LinkedHashMap<>();

	private OnItemclickListener onItemclickListener = null;

	private LayoutInflater inflater;

	public MenuIndicator(Context context) {
		super(context);
		inflater = LayoutInflater.from(context);
	}

	public MenuIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflater = LayoutInflater.from(context);
	}

	public void initIndicatorView() {
		removeAllViews();

		viewMap = getViewMap(inflater);
		setWeightSum(viewMap.size());
		for (Entry<Integer, View> entry : viewMap.entrySet()) {
			View view = entry.getValue();
			view.setId(entry.getKey());
			view.setOnClickListener(this);

			LayoutParams params = new LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 1.0f);
			view.setLayoutParams(params);
			addView(view);
		}

		invalidate();
	}

	public abstract LinkedHashMap<Integer, View> getViewMap(LayoutInflater inflater);

	public void setOnItemclickListener(OnItemclickListener onItemclickListener) {
		this.onItemclickListener = onItemclickListener;
	}

	public abstract void onSelect(View v);

	public abstract void offSelect(View v);

	@Override
	public void onClick(View v) {
		for (Entry<Integer, View> entry : viewMap.entrySet()) {
			if (v.getId() == entry.getKey()) {
				onSelect(v);
			} else {
				offSelect(entry.getValue());
			}
		}

		if (onItemclickListener != null) {
			onItemclickListener.onClick(v);
		}
	}

	public static abstract class OnItemclickListener {
		public abstract void onClick(View v);
	}

}
