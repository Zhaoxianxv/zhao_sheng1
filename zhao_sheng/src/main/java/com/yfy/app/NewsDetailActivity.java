/**
 *
 */
package com.yfy.app;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.dialog.AbstractDialog;
import com.yfy.final_tag.dialog.AbstractDialog.OnCustomDialogListener;
import com.yfy.final_tag.dialog.MyDialog;
import com.yfy.final_tag.Logger;
import com.yfy.json.JsonParser;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.final_tag.MyWebViewClient;
import com.yfy.view.LoadingView;

import butterknife.OnClick;


/**
 * @author yfy1
 * @version 1.0
 * @Desprition
 */
public class NewsDetailActivity extends WcfActivity {

    private final static String TAG = NewsDetailActivity.class.getSimpleName();

    private ViewGroup container_view;
    private WebView webView;
    private LoadingView loadingView;

    private ImageView write_comment_iv;
    private FrameLayout collect_frala;
    private ImageView collect_sel_iv;
    private MyDialog dialog;
    private EditText popu_edit;
    private TextView popu_ok;
    private WebSettings setttings;

    private String newsId;
    private String newsUrl;

    private String session_key = "";
    private String userid = "";
    private String userType = "";
    private String userName = "";
    private String content;
    private final String sendrep = "sendrep";
    private final String addfav = "addfav";
    private final String delfav = "delfav";
    private final String isfav = "isfav";
    private ParamsTask commentTask, addFavTask, deleteFavTask, isFavTask;

    private boolean isCollect;
    private String favId;

    private int b3_gray;
    private ColorStateList colorStateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitya_newsdetail);
        initSQToolbar();
        intData();
        initViews();
        initWeb();
        initDialog();
    }
    private String getData() {
        Bundle b = getIntent().getExtras();
        if (b != null) {

            if (b.containsKey("newsId")){
                newsId=b.getString("newsId");
            }
            if (b.containsKey("newsUrl")){

                newsUrl= b.getString("newsUrl");
            }
            if (b.containsKey("title")) {
                return b.getString("title");
            }
        }
        return "";
    }

    private void initSQToolbar() {
        assert toolbar!=null;
        toolbar.setTitle("新闻详情");

//        toolbar.addMenuText(2,"查看评论");
//        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                Intent intent = new Intent(mActivity, CommentsActivity.class);
//                Bundle b = new Bundle();
//                b.putString("newsId", newsId);
//                intent.putExtras(b);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initTask();
        if (isFavTask != null) {
            execute(isFavTask);
        }
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
            container_view.removeView(webView);
            webView.removeAllViews();
            webView.destroy();
        }
    }

    private void initTask() {
        if (Base.user != null) {
            session_key = Base.user.getSession_key();
            userid = Base.user.getIdU();
            userType = Base.user.getHeadPic();
            userName = Base.user.getName();

            Object[] params = new Object[]{session_key, newsId};
            isFavTask = new ParamsTask(params, isfav, "isfav");

            params = new Object[]{newsId, userid, userType};
            addFavTask = new ParamsTask(params, addfav, "addfav");
        }
    }

    private void intData() {
        Bundle b = getIntent().getExtras();
        newsId = b.getString("newsId");
        newsUrl = b.getString("newsUrl");
    }

    private void initViews() {
        container_view = (ViewGroup) findViewById(R.id.container_view);


        webView = (WebView) findViewById(R.id.webView);
        write_comment_iv = (ImageView) findViewById(R.id.write_comment_iv);
        collect_frala = (FrameLayout) findViewById(R.id.collect_frala);
        collect_sel_iv = (ImageView) findViewById(R.id.collect_sel_iv);
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
    private void initDialog() {
        dialog = new MyDialog(mActivity, R.layout.dialog_edittext_popu,
                new int[]{R.id.popu_cancel, R.id.popu_title, R.id.popu_ok, R.id.popu_edit},
                new int[]{R.id.popu_cancel, R.id.popu_ok});
        dialog.setOnCustomDialogListener(onCustomDialogListener);
        dialog.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                popu_edit.getText().clear();
            }
        });
    }

    private OnCustomDialogListener onCustomDialogListener = new OnCustomDialogListener() {

        @Override
        public void onClick(View v, AbstractDialog dialog) {
            switch (v.getId()) {
                case R.id.popu_cancel:
                    dialog.dismiss();
                    break;
                case R.id.popu_ok:
                    sendComment();
                    break;
            }
        }

        @SuppressWarnings("ResourceType")
        @Override
        public void init(AbstractDialog dialog) {
            b3_gray = getResources().getColor(R.color.b3_gray);
            try {
				XmlResourceParser xrp = getResources().getXml(R.color.selector_text_click4);
				colorStateList = ColorStateList.createFromXml(getResources(), xrp);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            popu_edit = dialog.getView(EditText.class, R.id.popu_edit);
            popu_ok = dialog.getView(TextView.class, R.id.popu_ok);
            popu_edit.addTextChangedListener(watcher);
        }
    };

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(s)) {
                popu_ok.setTextColor(b3_gray);
                popu_ok.setClickable(false);
            } else {
                popu_ok.setTextColor(colorStateList);
                popu_ok.setClickable(true);
            }
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @OnClick(R.id.collect_frala)
    void setCollect_frala() {
        if (isCollect) {
            delFav();
        } else {
            addFav();
        }
    }

    @OnClick(R.id.write_comment_iv)
    void setWrite_comment_iv() {
        if (Base.user != null) {
            dialog.showAtBottom();
        } else {
            toastShow("登录后才能发表评论");
        }
    }

    /**
     * @description
     */
    private void sendComment() {
        content = popu_edit.getText().toString().trim();
        if (!TextUtils.isEmpty(content)) {
            Object[] params = new Object[]{newsId, content, userid, userType,
                    userName};
            commentTask = new ParamsTask(params, sendrep, "sendrep");
            execute(commentTask);
        } else {
            toastShow("请输入评论内容");
        }
    }

    /**
     * @description
     */
    private void addFav() {
        if (addFavTask != null) {
            execute(addFavTask);
        } else {
            toastShow("登录后才能收藏");
        }
    }

    /**
     * @description
     */
    private void delFav() {
        if (!TextUtils.isEmpty(favId)) {
            Object[] params = new Object[]{session_key, favId};
            deleteFavTask = new ParamsTask(params, delfav, "delfav");
            execute(deleteFavTask);
        }
    }

    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        String taskName = wcfTask.getName();

        Logger.e( result);
        if (JsonParser.isSuccess(result)) {
            if (taskName.equals("sendrep")) {
                toastShow("评论成功");
                dialog.dismiss();
            } else if (taskName.equals("addfav")) {
                toastShow("收藏成功");
                isCollect = true;
                collect_sel_iv.setVisibility(View.VISIBLE);
                execute(isFavTask);
            } else if (taskName.equals("delfav")) {
                toastShow("取消收藏成功");
                isCollect = false;
                collect_sel_iv.setVisibility(View.GONE);
            } else if (taskName.equals("isfav")) {
                isCollect = true;
                favId = JsonParser.getFavId(result);
                collect_sel_iv.setVisibility(View.VISIBLE);
            }
        }
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        String taskName = wcfTask.getName();
        if (taskName.equals("sendrep")) {
            toastShow("网络异常,发表评论失败");
        } else if (taskName.equals("addfav")) {
            toastShow("网络异常,添加收藏失败");
        } else if (taskName.equals("delfav")) {
            toastShow("网络异常,取消收藏失败");
        }
    }
}
