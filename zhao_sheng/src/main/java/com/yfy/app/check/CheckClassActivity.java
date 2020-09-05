package com.yfy.app.check;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.bean.DateBean;
import com.yfy.app.check.adapter.CheckClassAdapter;
import com.yfy.app.check.bean.CheckRes;
import com.yfy.app.check.bean.IllType;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.check.CheckGetClassReq;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.dialog.ConfirmDateWindow;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.DateUtils;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.DefaultItemAnimator;
import com.yfy.final_tag.recycerview.RecycleViewDivider;
import com.yfy.view.SQToolBar;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckClassActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = CheckClassActivity.class.getSimpleName();


    private DateBean dateBean;
    private CheckClassAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_main);
        getData();
        dateBean=new DateBean();
        dateBean.setName(DateUtils.getCurrentDateName());
        dateBean.setValue(DateUtils.getCurrentDateValue());

        initDateDialog();
        initRecycler();
        initSQtoolbar();
        getCheckClass(true);

    }
    private String admin_right="",admin_add=TagFinal.FALSE,admin_manage=TagFinal.FALSE;
    public void getData(){
        getIntent().getParcelableExtra(TagFinal.OBJECT_TAG);
        Intent intent=getIntent();
        admin_right=intent.getStringExtra(Base.content);
        //权限
        List<String> adminList=StringUtils.listToStringSplitCharacters(admin_right,",");
        for (String s:adminList){
            switch (s){
                case "view":
                    break;
                case "add":
                    admin_add=TagFinal.TRUE;
                    break;
                case "admin":
                    admin_manage=TagFinal.TRUE;
                    break;
            }
        }
    }




    private IllType bean;

    private TextView one_menu;
    private void initSQtoolbar() {
        assert toolbar!=null;
        toolbar.setTitle("群组列表");
        one_menu=toolbar.addMenuText(TagFinal.ONE_INT,dateBean.getName());
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                date_dialog.showAtBottom();
            }
        });
    }



    private ConfirmDateWindow date_dialog;
    private void initDateDialog(){
        date_dialog = new ConfirmDateWindow(mActivity);
        date_dialog.setOnPopClickListenner(new ConfirmDateWindow.OnPopClickListenner() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.set:

                        dateBean.setName(date_dialog.getTimeName());
                        dateBean.setValue(date_dialog.getTimeValue());

                        one_menu.setText(dateBean.getName());
                        getCheckClass(false);

                        date_dialog.dismiss();
                        break;
                    case R.id.cancel:
                        date_dialog.dismiss();
                        break;
                }

            }
        });
    }


    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView = (RecyclerView) findViewById(R.id.public_recycler);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.public_swip);
        //AppBarLayout 展开执行刷新
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(ColorRgbUtil.getBaseColor());
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                getCheckClass(false);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        xlist.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mActivity,
                LinearLayoutManager.HORIZONTAL,
                1,
                getResources().getColor(R.color.gray)));
        adapter=new CheckClassAdapter(mActivity);
        recyclerView.setAdapter(adapter);
        adapter.setDateBean(dateBean);


    }

    public void closeSwipeRefresh(){
        if (swipeRefreshLayout!=null){
            swipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            }, 200);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_REFRESH:
                    getCheckClass(false);
                    break;
            }
        }
    }

    /**
     * ----------------------------retrofit-----------------------
     */



    public void getCheckClass(boolean is) {

        ReqEnv evn = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        CheckGetClassReq request = new CheckGetClassReq();
        //获取参数

        request.setDate(dateBean.getValue());
        reqBody.checkGetClassReq= request;
        evn.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().check_get_class(evn);
        call.enqueue(this);
        if (is)showProgressDialog("正在加载");
//        Logger.e(evn.toString());

    }


    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();
        closeSwipeRefresh();
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.checkGetClassRes!=null) {
                String result = b.checkGetClassRes.result;
                Logger.e(call.request().headers().toString() + result);
                CheckRes res=gson.fromJson(result,CheckRes.class);
                if (res.getResult().equals(TagFinal.TRUE)){
                    adapter.setDataList(res.getClasslist());
                    adapter.setLoadState(TagFinal.LOADING_COMPLETE);
                }else{
                    toastShow(res.getError_code());
                }
            }
        }else{
            try {
                String s=response.errorBody().string();
                Logger.e(StringUtils.getTextJoint("%1$s:%2$d:%3$s",name,response.code(),s));
            } catch (IOException e) {
                Logger.e(TagFinal.ZXX, "onResponse: IOException");
                e.printStackTrace();
            }
            toastShow(StringUtils.getTextJoint("数据错误:%1$d",response.code()));
        }
    }

    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        if (!isActivity())return;
        Logger.e("onFailure  "+call.request().headers().toString() );
        dismissProgressDialog();
        closeSwipeRefresh();
        toastShow(R.string.fail_do_not);
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
