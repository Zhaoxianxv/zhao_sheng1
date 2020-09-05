package com.yfy.app.delay_service;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.zhao_sheng.R;
import com.yfy.app.bean.DateBean;
import com.yfy.app.delay_service.adapter.AlterListAdapter;
import com.yfy.app.delay_service.bean.AbsentInfo;
import com.yfy.app.delay_service.bean.DelayServiceRes;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.delay_service.DelayCopySetReq;
import com.yfy.app.net.delay_service.DelayGetCopyListReq;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.RecycleViewDivider;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EventAlterActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = EventAlterActivity.class.getSimpleName();



    private AlterListAdapter adapter;

    private String elective_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_main);
        getData();
        initRecycler();
        initSQtoolbar();
    }
    private DateBean dateBean;
    private void getData(){
        Intent intent=getIntent();
        dateBean=intent.getParcelableExtra(Base.date);
        elective_id=intent.getStringExtra(TagFinal.ID_TAG);
        getCopyList();
    }

    private void initSQtoolbar() {
        assert toolbar!=null;
        toolbar.setTitle("考勤");

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
                getCopyList();
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
        adapter=new AlterListAdapter(this);
        adapter.setTell(new AlterListAdapter.Tell() {
            @Override
            public void tell(AbsentInfo bean) {
                setSubmit(bean.getElectiveid());
            }
        });
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
     * --------------------------retrofit--------------------------
     */




    private void getCopyList(){


        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        DelayGetCopyListReq req = new DelayGetCopyListReq();
        //获取参数
        req.setDate(dateBean.getValue());
        req.setElectiveid(ConvertObjtect.getInstance().getInt(elective_id));

        reqBody.delayGetCopyListReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().delay_get_copy_list(reqEnv);
        call.enqueue(this);
    }

    private void setSubmit(String e_id){



        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        DelayCopySetReq req = new DelayCopySetReq();
        //获取参数
        req.setDate(dateBean.getValue());
        req.setElectiveid(ConvertObjtect.getInstance().getInt(elective_id));
        req.setElectiveid1(ConvertObjtect.getInstance().getInt(e_id));
        reqBody.delayCopySetReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().delay_copy_set(reqEnv);
        call.enqueue(this);
        showProgressDialog("");

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
            if (b.delayGetCopyListRes !=null){
                String result=b.delayGetCopyListRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                DelayServiceRes res=gson.fromJson(result,DelayServiceRes.class );
                if (StringJudge.isEmpty(res.getElective_classdetail())){
                    toastShow("当前无可复制考勤信息");
                }else{
                    adapter.setDataList(res.getElective_classdetail());
                    adapter.setLoadState(TagFinal.LOADING_COMPLETE);
                }
            }
            if (b.delayCopySetRes !=null){
                String result=b.delayCopySetRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                DelayServiceRes res=gson.fromJson(result,DelayServiceRes.class );
                if (res.getResult().equals(TagFinal.TRUE)){
                    toastShow(R.string.success_do);
                    setResult(RESULT_OK);
                    finish();
                }else{
                    toastShow(res.getError_code());
                }
            }

        }else{
            Logger.e(StringUtils.getTextJoint("%1$s:%2$d",name,response.code()));
            toastShow(StringUtils.getTextJoint("数据错误:%1$d",response.code()));
        }
    }

    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        if (!isActivity())return;
        Logger.e("onFailure  :"+call.request().headers().toString());
        dismissProgressDialog();
        closeSwipeRefresh();
        toastShow(R.string.fail_do_not);
    }


    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }

}
