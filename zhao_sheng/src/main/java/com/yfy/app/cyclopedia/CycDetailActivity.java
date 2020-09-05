/**
 *
 */
package com.yfy.app.cyclopedia;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.example.zhao_sheng.R;
import com.yfy.app.cyclopedia.beans.AncyclopediaList;
import com.yfy.app.login.LoginActivity;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.MyWebViewClient;
import com.yfy.view.LoadingView;

import butterknife.OnClick;


/**
 * @author yfy1
 * @version 1.0
 * @Desprition
 */
public class CycDetailActivity extends BaseActivity {

    private final static String TAG = "zxx";


    private WebView webView;
    private LoadingView loadingView;
    private FrameLayout collect_frala;
    private WebSettings setttings;

    private   AncyclopediaList bean;

    private String newsUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cyc_newsdetail);
        initSQToolbar();
        intData();
        initViews();
        initWeb();

    }

    private void initSQToolbar() {
        assert toolbar!=null;
        toolbar.setTitle(R.string.cyclopedia_school);

    }

    @Override
    public void onResume() {
        super.onResume();

        webView.resumeTimers();
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.pauseTimers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {

            webView.removeAllViews();
            webView.destroy();
        }
    }



    private void intData() {
        Bundle b = getIntent().getExtras();

        bean= (AncyclopediaList) b.getSerializable("data");

        newsUrl =bean.getUrl();
    }

    private void initViews() {
        webView = (WebView) findViewById(R.id.webView);
        collect_frala = (FrameLayout) findViewById(R.id.collect_frala);
        loadingView = (LoadingView) findViewById(R.id.loadingView);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("SetJavaScriptEnabled")
    private void initWeb() {
        MyWebViewClient webViewClient = new MyWebViewClient(loadingView);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebChromeClient(new WebChromeClient());

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

        setttings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.loadUrl(newsUrl);
        webView.setWebViewClient(webViewClient);
    }

    /**
     * @description
     */


    @OnClick(R.id.include_bottom)
    void setBotton() {
        if (Base.user != null) {
            Intent i=new Intent(mActivity,CycEditorDetailsActivity.class);
            i.putExtra("pid",bean.getId());
            i.putExtra("title",bean.getTitle());
            startActivity(i);
        }else{
            startActivity(new Intent(mActivity, LoginActivity.class));
        }
    }



}
