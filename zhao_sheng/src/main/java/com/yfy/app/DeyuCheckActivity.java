package com.yfy.app;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.DateUtils;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.dialog.DialogTools;
import com.yfy.final_tag.Logger;
import com.yfy.view.LoadingView;

import com.yfy.view.MyWebViewClient;

import java.util.Calendar;

public class DeyuCheckActivity extends BaseActivity implements OnClickListener {

	private String session_key = Base.user.getSession_key();
	private String date = "";

	private WebView webView;
	private LoadingView loadingView;
	private TextView headTitle, date_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deyu_check);
		initAll();
	}

	@Override
	public void finish() {
		super.finish();
	}

	private void initAll() {
		date = DateUtils.getCurrentTime();
		initViews();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initViews() {
		headTitle = (TextView) findViewById(R.id.head_title);
		date_tv = (TextView) findViewById(R.id.right_tv);

		headTitle.setVisibility(View.VISIBLE);
		headTitle.setText(getDate() + " 班级评比");
		date_tv.setText("选择日期");

		setOnClickListener(mActivity, R.id.left_rela);
		setOnClickListener(mActivity, date_tv);
		iniWebView();
	}





	private String getDate() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR) + "年"
				+ (calendar.get(Calendar.MONTH) + 1) + "月"
				+ calendar.get(Calendar.DAY_OF_MONTH) + "日";
	}

	private void iniWebView() {
		webView = (WebView) findViewById(R.id.webView);
		loadingView = (LoadingView) findViewById(R.id.loadingView);

		webView.setHorizontalScrollBarEnabled(false);
		webView.setWebChromeClient(new WebChromeClient());

		WebSettings setttings  = webView.getSettings();
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



		MyWebViewClient webViewClient = new MyWebViewClient(loadingView);
		webView.loadUrl(getUrl(session_key, date));
		webView.setWebViewClient(webViewClient);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (webView != null) {
			webView.resumeTimers();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (webView != null) {
			webView.destroy();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (webView != null) {
			webView.pauseTimers();
		}
	}

	private String getUrl(String sessionkey, String date) {
		String url =StringUtils.stringToGetTextJoint(Base.DEYU_KEY,Base.user.getIdU(),Base.user.getUsertype(),date);
		return url;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.left_rela:
				finish();
				break;
			case R.id.right_tv:

				DialogTools
						.getInstance()
						.showDatePickerDialog(mActivity, new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
								String date2 = DateUtils.getDate2(year, month, dayOfMonth);
								headTitle.setText(date2 + " 班级评比");

								String date = DateUtils.getDate(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

								webView.loadUrl(getUrl(session_key, date));

							}
						});
				break;
		}
	}
}
