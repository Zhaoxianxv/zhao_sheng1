package com.yfy.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.zhao_sheng.R;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.TagFinal;

public class WebActivity extends BaseActivity {

	private final static String TAG = WebActivity.class.getSimpleName();

	private ViewGroup container_view;
	private WebView webView;
	private WebSettings setttings;


	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
//		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//			getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//		}
		initAll();
	}

	private void initSQtoolbar(String title){
		assert toolbar!=null;
		toolbar.setTitle(title);
//		toolbar.setNavi(R.drawable.ic_back);
	}



	@Override
	public void finish() {
		if (index==-1){

		}else{
			Intent intent=new Intent();
			intent.putExtra(TagFinal.ALBUM_LIST_INDEX, index);
			setResult(RESULT_OK,intent);
		}

		super.finish();
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (webView != null) {
			container_view.removeView(webView);
			webView.removeAllViews();
			webView.destroy();
		}
	}

	private void initAll() {
		getData();

		container_view = (ViewGroup) findViewById(R.id.container_view);
		webView = (WebView) findViewById(R.id.webView);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		initWeb();
	}

	/**
	 * @description
	 */
	private int index;
	private String url;
	private String title;
	private void getData() {
		Bundle b = getIntent().getExtras();
		if (b != null) {
			if (b.containsKey(TagFinal.URI_TAG)) {
				url = b.getString(TagFinal.URI_TAG);
			}
			if (b.containsKey(TagFinal.ALBUM_LIST_INDEX)) {
				index = b.getInt(TagFinal.ALBUM_LIST_INDEX);
			}else{
				index=-1;
			}
			if (b.containsKey(TagFinal.TITLE_TAG)) {
				title = b.getString(TagFinal.TITLE_TAG);
				if (title.length()>10){
					title="新闻详情";
				}
			}
			if (b.containsKey("session_key")) {
				String session_key = b.getString("session_key");
				url = url.replace("%@", session_key);
			}
			initSQtoolbar(title);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		webView.pauseTimers();
	}

	@Override
	public void onResume() {
		super.onResume();
		webView.resumeTimers();
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	private void initWeb() {
		webView.setHorizontalScrollBarEnabled(false);
		webView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				Log.e(TAG, "progress=" + progress);
				if (progress == 100) {
					progressBar.setProgress(0);
					progressBar.setVisibility(View.GONE);
				} else {
					progressBar.setProgress(progress);
				}
			}
		});

		setttings = webView.getSettings();
		setttings.setJavaScriptEnabled(false);
		setttings.setPluginState(PluginState.ON);
		setttings.setUseWideViewPort(true);
		setttings.setLoadWithOverviewMode(true);
		setttings.setJavaScriptCanOpenWindowsAutomatically(true);



		setttings.setAllowFileAccess(false);
		setttings.setAllowFileAccessFromFileURLs(false);


		webView.removeJavascriptInterface("searchBoxJavaBridge_");
		webView.removeJavascriptInterface("accessibility");
		webView.removeJavascriptInterface("accessibilityTraversal");

		webView.loadUrl(url);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
	}


}
