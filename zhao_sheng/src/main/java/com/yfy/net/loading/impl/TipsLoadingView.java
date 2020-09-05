/**
 * 
 */
package com.yfy.net.loading.impl;

import android.view.View;
import android.widget.TextView;

import com.yfy.net.loading.interf.Dialog;
import com.yfy.net.loading.interf.Indicator;


/**
 * @author yfy1
 * @version 1.0
 * @Desprition
 */
public class TipsLoadingView extends Indicator {
//	private final static String TAG = TipsLoadingView.class.getSimpleName();

	private Dialog dialog;
	private TextView tip_tv;
	private View[] views;
	private TipText tipText = new TipText();

	public TipsLoadingView(Dialog dialog, TextView tip_tv, View... views) {
		this.dialog = dialog;
		this.tip_tv = tip_tv;
		if (views == null) {
			this.views = new View[] {};
		} else {
			this.views = views;
		}
	}

	@Override
	public void dismiss() {
		dialog.dismiss();
	}

	@Override
	public void show() {
		dialog.show();
	}

	@Override
	public void loading() {
		if (tipText.getLoadingText() != null) {
			tip_tv.setText(tipText.getLoadingText());
		}
		for (int i = 0; i < views.length; i++) {
			views[i].setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void fail() {
		dialog.dismiss();
		if (tipText.getFalseText() != null) {
			tip_tv.setText(tipText.getFalseText());
		}
	}

	@Override
	public void emptyResult() {
		dialog.dismiss();
		if (tipText.getEmptyText() != null) {
			tip_tv.setText(tipText.getEmptyText());
		}
	}

	@Override
	public void valueResult() {
		for (int i = 0; i < views.length; i++) {
			views[i].setVisibility(View.GONE);
		}
	}

	public TipText buildTipText() {
		return tipText;
	}

	public class TipText {
		private String loadingText;
		private String falseText;
		private String emptyText;

		public TipText loadingText(String text) {
			this.loadingText = text;
			return this;
		}

		public TipText falseText(String text) {
			this.falseText = text;
			return this;
		}

		public TipText emptyText(String text) {
			this.emptyText = text;
			return this;
		}

		public String getLoadingText() {
			return loadingText;
		}

		public String getFalseText() {
			return falseText;
		}

		public String getEmptyText() {
			return emptyText;
		}
	}

}
