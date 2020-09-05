package com.yfy.app.event;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.zhao_sheng.R;
import com.yfy.app.event.bean.EventBean;
import com.yfy.app.event.bean.EventInfo;
import com.yfy.app.event.bean.EventRes;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.event.EventGetMyListReq;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.final_tag.recycerview.EndlessRecyclerOnScrollListener;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventMyActivity extends WcfActivity implements Callback<ResEnv> {
    private static final String TAG =EventMyActivity.class.getSimpleName();
    private EventMyAdapter adapter;
    private int pager=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_main);
        initSQToolbar();
        initRecycler();
        getDataEvent(true,TagFinal.REFRESH);
    }



    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }

    private void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle("我的记录");
//        toolbar.addMenuText(TagFinal.TWO_INT, "添加");
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent;
                switch (position){
                    case TagFinal.TWO_INT:
                        intent=new Intent(mActivity,EventAddActivity.class);
                        startActivityForResult(intent,TagFinal.UI_ADD);
                        break;
                }
            }
        });
    }


    private SwipeRefreshLayout swipeRefreshLayout;
    public RecyclerView recycler;
    public void initRecycler(){
        recycler =  findViewById(R.id.public_recycler);
        swipeRefreshLayout =  findViewById(R.id.public_swip);
        //AppBarLayout 展开执行刷新
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(ColorRgbUtil.getBaseColor());
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                getDataEvent(false,TagFinal.REFRESH);

            }
        });
        recycler.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                getDataEvent(false,TagFinal.REFRESH_MORE);
            }
        });

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setItemAnimator(new DefaultItemAnimator());
        //添加分割线

        adapter=new EventMyAdapter(this);
        recycler.setAdapter(adapter);

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_ADMIN:// alter
                    getDataEvent(false,TagFinal.REFRESH);
                    break;
                case TagFinal.UI_ADD://add refresh
                    getDataEvent(false,TagFinal.REFRESH);
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
    private List<EventBean> datalist = new ArrayList<>();
    private void initAdapter(boolean is,List<EventInfo> list){
        if (is){
            datalist.clear();
        }
        for (EventInfo info:list){
            initData(datalist,info.getEvent_list(),info.getWeekname(),info.getTermname() );
        }
        adapter.setDataList(datalist);
        adapter.setLoadState(TagFinal.LOADING_COMPLETE);
    }

    private void initData(List<EventBean> datalist,List<EventBean> list,String week_name,String term_name ){

        for(int i=0;i<list.size();i++){
            EventBean bean=list.get(i);
            if (i==0){
                bean.setIs(true);
            }else{
                bean.setIs(false);
            }
            bean.setWeek_name(week_name);
            bean.setTerm_name(term_name);
            datalist.add(bean);
        }
    }
    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        if (!isActivity())return false;
        dismissProgressDialog();
        closeSwipeRefresh();
        Logger.e(result);
        String name=wcfTask.getName();

        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        if (!isActivity())return ;
        dismissProgressDialog();
        closeSwipeRefresh();
        toastShow(R.string.fail_do_not);
    }






    /**
     * ----------------------------retrofit-----------------------
     */





    public void getDataEvent(boolean is,String typeload){

        if (typeload.equals(TagFinal.REFRESH)){
            pager=0;
        }else{
            pager++;
        }
        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        EventGetMyListReq req = new EventGetMyListReq();
        //获取参数
        req.setSdate("");
        req.setEdate("");
        req.setWeekid(0);
        req.setDepid(0);
        req.setPage(pager);
        req.setSize(TagFinal.FIFTEEN_INT);


        reqBody.eventGetMyListReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().event_get_user_list(reqEnv);
        call.enqueue(this);

        if (is)showProgressDialog("");
    }




    @Override
    public void onResponse(@NonNull Call<ResEnv> call, @NonNull Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();
        closeSwipeRefresh();

        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.eventGetMyListRes !=null){
                String result=b.eventGetMyListRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                EventRes res=gson.fromJson(result,EventRes.class);
                if (pager==0){
                    initAdapter(true, res.getDate_list() );
                }else {
                    initAdapter(false, res.getDate_list() );
                }
                if (res.getDate_list().size()<TagFinal.TEN_INT){
                    toastShow(R.string.success_not_more);
                }
            }
        }else{
            Logger.e(StringUtils.getTextJoint("%1$s:%2$d",name,response.code()));
            toastShow(StringUtils.getTextJoint("数据错误:%1$d",response.code()));
        }

    }

    @Override
    public void onFailure(@NonNull Call<ResEnv> call,@NonNull Throwable t) {
        if (!isActivity())return;
        Logger.e("onFailure  :"+call.request().headers().toString());
        dismissProgressDialog();
        closeSwipeRefresh();
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
