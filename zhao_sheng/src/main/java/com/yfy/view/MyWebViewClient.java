package com.yfy.view;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yfy.net.loading.interf.Dialog;

public class MyWebViewClient extends WebViewClient {

	private Dialog dialog;

	public MyWebViewClient(Dialog dialog) {
		this.dialog = dialog;
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		view.loadUrl(url);
		return true;
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		super.onPageStarted(view, url, favicon);
		if (dialog != null) {
			dialog.show();
		}
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
		if (dialog != null) {
			dialog.dismiss();
		}
	}
}
