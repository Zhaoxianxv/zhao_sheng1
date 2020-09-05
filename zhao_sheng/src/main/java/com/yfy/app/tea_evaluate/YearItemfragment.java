package com.yfy.app.tea_evaluate;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.zhao_sheng.R;
import com.google.gson.Gson;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.tea_evaluate.adpter.AdminMainAdapter;
import com.yfy.app.tea_evaluate.bean.ChartBean;
import com.yfy.app.tea_evaluate.bean.ChartInfo;
import com.yfy.app.tea_evaluate.bean.ResultInfo;
import com.yfy.app.net.judge.TeaJudgeGetTjDataReq;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.base.fragment.WcfFragment;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.final_tag.recycerview.DividerDefault;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YearItemfragment extends WcfFragment implements Callback<ResEnv> {
    private static final String TAG = "zxx";


    private AdminMainAdapter adapter;
    private Gson json=new Gson();

    private List<ChartInfo> chartinfo=new ArrayList<>();


    @Override
    public void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.tea_main_prise);
        initRecycler();
        getChartData();

    }


    @Bind(R.id.admin_swip)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.tea_main_recycler)
    RecyclerView recyclerView;
    public void initRecycler(){

//        recyclerView = (RecyclerView)mActivity.findViewById(R.id.tea_main_recycler);
//        swipeRefreshLayout = (SwipeRefreshLayout)mActivity.findViewById(R.id.admin_swip);
        //AppBarLayout 展开执行刷新
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(ColorRgbUtil.getBaseColor());
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                getChartData();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
//        xlist.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        recyclerView.addItemDecoration(new DividerDefault(
                mActivity,
                LinearLayoutManager.HORIZONTAL,
                1,
                ColorRgbUtil.getGrayText()

        ));
        adapter=new AdminMainAdapter(mActivity,chartinfo);
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



    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {

    }


    private List<ChartInfo> initData(List<ChartInfo> chartinfo){
        for (ChartInfo chart:chartinfo){
            List<ChartBean> chartBeans=chart.getInfo();
            for (ChartBean bean:chartBeans){
                if (bean.getId().equals(getTag())){
                    chart.setComments(bean.getComments());
                    break;
                }
            }
        }
        return chartinfo;

    }

    /**
     * ----------------------------retrofit-----------------------
     */
    public void getChartData()  {

        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        TeaJudgeGetTjDataReq request = new TeaJudgeGetTjDataReq();
        //获取参数

        reqBody.teaJudgeGetTjDataReq = request;
        reqEnvelop.body= reqBody;

        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().judge_get_statistics_data(reqEnvelop);
        call.enqueue(this);
        showProgressDialog("正在加载");

    }



    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        dismissProgressDialog();
        closeSwipeRefresh();
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b = respEnvelope.body;
            if (b.teaJudgeGetTjDataRes != null) {
                String result = b.teaJudgeGetTjDataRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                ResultInfo info=json.fromJson(result, ResultInfo.class);
                chartinfo=info.getJudge_statistics();
                adapter.setDataList(initData(chartinfo));
                adapter.setLoadState(2);
            }
        }else{

            Logger.e(StringUtils.getTextJoint("%1$s:%2$d",name,response.code()));
            toastShow(StringUtils.getTextJoint("数据错误:%1$d",response.code()));
        }
    }

    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {

        Logger.e("onFailure  :"+call.request().headers().toString());
        dismissProgressDialog();
        closeSwipeRefresh();


    }



}
