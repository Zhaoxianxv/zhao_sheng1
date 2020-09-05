package com.yfy.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.example.zhao_sheng.R;


public class LoadingDialog implements com.yfy.net.loading.interf.Dialog{

	private Context context;
	private Dialog dialog;

	public LoadingDialog() {

	}

	public LoadingDialog(Context context) {
		this.context = context;
		init();
	}

	private void init() {

		dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.layout_loading_dialog);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);

	}

	@Override
	public void show() {
		if (dialog != null) {
			dialog.show();
		}
	}

	@Override
	public void dismiss() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}

}
