package com.yfy.app.login;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.zhao_sheng.R;
import com.yfy.app.attennew.bean.Subject;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.satisfaction.SatisfactionTeaGetStuReq;
import com.yfy.base.activity.BaseActivity;
import com.yfy.db.UserPreferences;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeaResetStuPassActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = TeaResetStuPassActivity.class.getSimpleName();


    private TeaResetStuPassAdapter adapter;
    private List<Subject> users=new ArrayList<>();

    private String term_id,class_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_main);
        term_id=UserPreferences.getInstance().getTermId();
        initSQToolbar();
        initRecycler();
        getStu(true);

    }





    private void initSQToolbar() {
        assert toolbar!=null;
        toolbar.setTitle("");
    }


    public SwipeRefreshLayout swipeRefreshLayout;
    public RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView =  findViewById(R.id.public_recycler);
        swipeRefreshLayout =  findViewById(R.id.public_swip);
        //AppBarLayout 展开执行刷新
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(ColorRgbUtil.getBaseColor());
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                getStu(false);
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
        adapter=new TeaResetStuPassAdapter(mActivity);
        recyclerView.setAdapter(adapter);

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


    /**
     * ----------------------------retrofit-----------------------
     */


    public void getStu(boolean is){
        if (StringJudge.isEmpty(term_id)){
            toast("没有获取到当前学期");
            return;
        }
        if (StringJudge.isEmpty(class_id)){
            toast("没有获取到老师班级信息");
            return;
        }
        ReqEnv evn = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        SatisfactionTeaGetStuReq request = new SatisfactionTeaGetStuReq();
        //获取参数
        request.setTermid(ConvertObjtect.getInstance().getInt(term_id));
        request.setClassid(ConvertObjtect.getInstance().getInt(class_id));
        reqBody.satisfactionTeaGetStuReq = request;
        evn.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().satisfaction_tea_get_stu(evn);
        call.enqueue(this);
        Logger.e(evn.toString());
        if (is)showProgressDialog("");
    }
    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();
        closeSwipeRefresh();
        if (response.code()==500){
            toastShow("数据出差了");
        }
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.satisfactionTeaGetStuRes !=null) {
                String result = b.satisfactionTeaGetStuRes.result;
                Logger.e(call.request().headers().toString() + result);
            }

        }else{
            Logger.e("evn: null" );
        }
    }

    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        if (!isActivity())return;
        Logger.e("onFailure  "+call.request().headers().toString() );
        dismissProgressDialog();
        closeSwipeRefresh();
        toast(R.string.fail_do_not);
    }


    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
