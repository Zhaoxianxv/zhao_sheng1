package com.yfy.view;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

public class MyDialog extends Dialog {

	private int layoutId;
	private int[] initId;
	private int[] listennerId;

	private OnCustomDialogListener listener = null;

	@SuppressLint("UseSparseArrays")
	private HashMap<Integer, View> map = new HashMap<Integer, View>();
	private View view;

	public MyDialog(Context context, int layoutId, int[] initId,
			int[] listennerId) {
		super(context);
		this.layoutId = layoutId;
		this.initId = initId;
		this.listennerId = listennerId;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(layoutId);
		init();
	}

	private void init() {
		if (initId == null)
			initId = new int[0];
		for (int i = 0; i < initId.length; i++) {
			view = findViewById(initId[i]);
			map.put(initId[i], view);
		}
		if (listennerId == null)
			listennerId = new int[0];
		for (int i = 0; i < listennerId.length; i++) {
			map.get(listennerId[i]).setOnClickListener(onClickListener);
		}

		setCancelable(true);
		setCanceledOnTouchOutside(true);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);

		if (listener != null) {
			listener.init();
		}
	}

	public void showAtBottom() {
		Window dialogWindow = getWindow();
		dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
		show();
	}

	public void showAtCenter() {
		Window dialogWindow = getWindow();
		dialogWindow.setGravity(Gravity.CENTER);
		show();
	}

	public View getView(int key) {
		return map.get(key);
	}

	@SuppressWarnings("unchecked")
	public <T> T getView(Class<T> clazz, int key) {
		return (T) map.get(key);
	}

	public void setOnCustomDialogListener(OnCustomDialogListener listener) {
		this.listener = listener;
	}

	public static abstract class OnCustomDialogListener {
		public abstract void onClick(View v);

		public void init() {
			
		}
	}

	private android.view.View.OnClickListener onClickListener = new android.view.View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (listener != null) {
				listener.onClick(v);
			}
		}
	};

}
