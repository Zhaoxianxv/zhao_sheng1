package com.yfy.app.tea_evaluate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.zhao_sheng.R;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.judge.TeaJudgeGetInfoYearListReq;
import com.yfy.app.tea_evaluate.adpter.EvaluateOneAdapter;
import com.yfy.app.tea_evaluate.bean.ItemBean;
import com.yfy.app.tea_evaluate.bean.ResultInfo;
import com.yfy.base.Variables;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.EndlessRecyclerOnScrollListener;
import com.yfy.final_tag.recycerview.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChartEvaluateActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = ChartEvaluateActivity.class.getSimpleName();
    private int  page=0;
    private EvaluateOneAdapter adapter;
    private List<ItemBean> dataList=new ArrayList<>();
    private int year=0;
    private int id_=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_main);
        initSQToolbar();


        initRecycler();
        getData();
    }

    private void getData(){
        Intent intent=getIntent();
        String id=intent.getStringExtra(TagFinal.ID_TAG);
        String y=intent.getStringExtra("year");
        id_= ConvertObjtect.getInstance().getInt(id);
        year= ConvertObjtect.getInstance().getInt(y);
        assert toolbar!=null;
        toolbar.setTitle(y);
        Variables.year=y;
        refresh(true, TagFinal.REFRESH);
    }
    private  void initSQToolbar(){
        assert toolbar!=null;
//        toolbar.setTitle("教师考评");

    }


    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView = findViewById(R.id.public_recycler);
        swipeRefreshLayout = findViewById(R.id.public_swip);
        //AppBarLayout 展开执行刷新
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(ColorRgbUtil.getBaseColor());
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                refresh(false, TagFinal.REFRESH);
            }
        });
        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {

                refresh(false,TagFinal.REFRESH_MORE);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mActivity,
                LinearLayoutManager.HORIZONTAL,
                1,
                getResources().getColor(R.color.gray)));
        adapter=new EvaluateOneAdapter(this,dataList);
        recyclerView.setAdapter(adapter);

    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_CONTENT:
                    refresh(true,TagFinal.REFRESH);
                    break;
            }
        }
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
     * --------------------retrofit-------------
     */


    public void refresh(boolean is,String loadType){

        if (loadType.equals(TagFinal.REFRESH)){
            page=0;
        }else{
            adapter.setLoadState(TagFinal.LOADING);
            page++;
        }


        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        TeaJudgeGetInfoYearListReq req = new TeaJudgeGetInfoYearListReq();
        //获取参数
        req.setId(id_);
        req.setYear(year);
        req.setPage(page);
        req.setSize(TagFinal.TEN_INT);

        reqBody.teaJudgeGetInfoYearListReq = req;
        reqEnv.body= reqBody;

        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().judge_get_info_year_list(reqEnv);
        call.enqueue(this);
        if (is) showProgressDialog("");
    }







    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b = respEnvelope.body;
            if (b.teaJudgeGetInfoYearListRes != null) {
                String result = b.teaJudgeGetInfoYearListRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                ResultInfo res=gson.fromJson(result,ResultInfo.class);
                if (page==0){
                    dataList=res.getJudge_record();
                    adapter.setDataList(dataList);
                }else {
                    dataList.addAll(res.getJudge_record());
                    adapter.setDataList(dataList);
                }
                if (res.getJudge_record().size()!=TagFinal.TEN_INT){
                    toastShow(R.string.success_loadmore_end);
                    adapter.setLoadState(3);
                }else{
                    adapter.setLoadState(2);
                }
            }
        }else {
            Logger.e(StringUtils.getTextJoint("%1$s:%2$d",name,response.code()));
            toastShow(StringUtils.getTextJoint("数据错误:%1$d",response.code()));
        }

    }


    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        if (!isActivity())return;
        dismissProgressDialog();
        Logger.e("onFailure "+call.request().headers().toString());
        toastShow(R.string.fail_do_not);
    }


    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
