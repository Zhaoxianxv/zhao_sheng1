package com.yfy.app.notice.cyc;


import android.graphics.Color;
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
import com.yfy.app.net.notice.NoticeGetSendBoxListReq;
import com.yfy.app.notice.adapter.SendBoxAdapter;
import com.yfy.app.notice.bean.NoticeRes;
import com.yfy.app.notice.bean.SendNotice;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
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

public class SendBoxActivity extends BaseActivity implements Callback<ResEnv> {



    private final static String TAG = SendBoxActivity.class.getSimpleName();

    private SendBoxAdapter adapter;
    private List<SendNotice> sendNoticeList = new ArrayList<>();
    private int page = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_main);
        initSQToolbar();
        initRecycler();
        refresh(TagFinal.REFRESH,true);

    }

    private void initSQToolbar() {
        assert toolbar!=null;
        toolbar.setTitle(R.string.notice_send_box);
    }

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView =  findViewById(R.id.public_recycler);
        swipeRefreshLayout =  findViewById(R.id.public_swip);
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#4DB6AC"));
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                refresh( TagFinal.REFRESH,false);
            }
        });
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                refresh(TagFinal.REFRESH_MORE,false);
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
        adapter=new SendBoxAdapter(mActivity);
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
     * --------------------------retrofit------------------
     */



    public void refresh(String loadType,boolean is){
        if (loadType.equals(TagFinal.REFRESH)){
            page=0;
        }else{
            adapter.setLoadState(TagFinal.LOADING);
            page++;
        }
        ReqEnv evn=new ReqEnv();
        ReqBody reqbody=new ReqBody();
        NoticeGetSendBoxListReq req=new NoticeGetSendBoxListReq();

        req.setPage(page);
        req.setSize(TagFinal.FIFTEEN_INT);

        reqbody.noticeGetSendBoxListReq =req;
        evn.body=reqbody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().notice_get_send_list(evn);
        call.enqueue(this);
        if (is) showProgressDialog("正在加载");
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

            if (b.noticeGetSendBoxListRes !=null){
                String result=b.noticeGetSendBoxListRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                NoticeRes res=gson.fromJson(result,NoticeRes.class);
                if (page==0) {
                    sendNoticeList = res.getNoticelist();
                    adapter.setDataList(sendNoticeList);
                }else {
                    sendNoticeList.addAll(res.getNoticelist());
                    adapter.setDataList(sendNoticeList);
                }
                if (res.getNoticelist().size()!=TagFinal.FIFTEEN_INT){
                    toastShow(R.string.success_loadmore_end);
                    adapter.setLoadState(3);
                }else{
                    adapter.setLoadState(2);
                }
            }

        }else{
            Logger.e(StringUtils.getTextJoint("%1$s:%2$d",name,response.code()));
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
