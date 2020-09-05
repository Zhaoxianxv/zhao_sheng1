package com.yfy.app.check;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.zhao_sheng.R;
import com.yfy.app.check.adapter.CheckChildAdapter;
import com.yfy.app.check.bean.ChecKParent;
import com.yfy.app.check.bean.CheckChild;
import com.yfy.app.check.bean.CheckRes;
import com.yfy.app.check.bean.CheckStu;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.check.CheckStuChildDetailReq;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.DefaultItemAnimator;
import com.yfy.final_tag.recycerview.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckStuListActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = CheckStuListActivity.class.getSimpleName();


    private CheckChildAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_main);

        getData();
        initRecycler();
        initSQtoolbar();
        getStuChildDetail();

    }
    private CheckStu checkStu;
    private boolean isdel=false;
    private void getData(){
        checkStu=getIntent().getParcelableExtra(TagFinal.OBJECT_TAG);
        isdel=getIntent().getBooleanExtra(TagFinal.TYPE_TAG,false);
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }

    private void initSQtoolbar() {
        assert toolbar!=null;
        toolbar.setTitle(checkStu.getUsername());
        toolbar.setOnNaviClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                getStuChildDetail();
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
                ColorRgbUtil.getGainsboro()));
        adapter=new CheckChildAdapter(mActivity);
        recyclerView.setAdapter(adapter);
        adapter.setCheckStu(checkStu);
        adapter.setIsdel(isdel);

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
        if (RESULT_OK==resultCode){
            switch (requestCode){
                case TagFinal.UI_REFRESH:
                    getStuChildDetail();
                    break;
            }
        }
    }

    /**
     * ----------------------------retrofit-----------------------
     */



    public void getStuChildDetail() {

        ReqEnv evn = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        CheckStuChildDetailReq req = new CheckStuChildDetailReq();
        //获取参数

        req.setUserid(ConvertObjtect.getInstance().getInt(checkStu.getUserid()));
        reqBody.checkStuChildDetailReq= req;
        evn.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().check_get_stu_child_detail(evn);
        call.enqueue(this);
//        showProgressDialog("正在加载");
//        Logger.e(evn.toString());

    }



    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();
        closeSwipeRefresh();
        if (response.code()==500){
//            toastShow("数据出差了");
        }
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.checkStuChildDetailRes!=null) {
                String result = b.checkStuChildDetailRes.result;
                Logger.e(call.request().headers().toString() + result);
                CheckRes res=gson.fromJson(result,CheckRes.class);
                if (res.getResult().equals(TagFinal.TRUE)){
                    if (StringJudge.isEmpty(res.getIllhistory())){
                        toastShow("暂无历史纪录");
                    }
                    adapter.setDataList(res.getIllhistory());
                    adapter.setLoadState(TagFinal.LOADING_COMPLETE);
                }else{
                    toastShow(res.getError_code());
                }
            }


        }else{
            Logger.e("evn: null" +call.request().headers().toString());
        }
    }

    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        if (!isActivity())return;
        Logger.e("onFailure  "+call.request().headers().toString() );
        dismissProgressDialog();
        closeSwipeRefresh();
//        toast(R.string.fail_do_not);
    }


    private void initCreat(CheckRes res){

        List<CheckChild> showData=new ArrayList<>();
        for (ChecKParent checKParent:res.getUserstate()){
            CheckChild checkChild=new CheckChild();
            checkChild.setView_type(TagFinal.TYPE_PARENT);
            showData.add(checkChild);
            for (CheckChild child:checKParent.getIllstate()){
                child.setView_type(TagFinal.TYPE_CHILD);
                showData.add(child);
            }
        }

    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
