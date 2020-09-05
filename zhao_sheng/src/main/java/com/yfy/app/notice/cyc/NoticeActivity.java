package com.yfy.app.notice.cyc;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.zhao_sheng.R;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.base.ReadNoticeReq;
import com.yfy.app.net.notice.NoticeGetUserListReq;
import com.yfy.app.notice.adapter.NoticeAdapter;
import com.yfy.app.notice.bean.NoticeBean;
import com.yfy.app.notice.bean.NoticeRes;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.EndlessRecyclerOnScrollListener;
import com.yfy.final_tag.recycerview.RecycleViewDivider;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoticeActivity extends BaseActivity implements Callback<ResEnv>{

    private final static String TAG =NoticeActivity.class.getSimpleName();
    private NoticeAdapter adapter;
    private int page = 0;

    private List<NoticeBean> receiveNoticeList=new ArrayList<>();

    private NoticeBean top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_main);
        top=new NoticeBean();
        top.setView_type(TagFinal.TYPE_TOP);
        initSQToolBar();
        initRecycler();
        readNotice("private_notice");
        refresh(true,TagFinal.REFRESH);
    }





    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView =  findViewById(R.id.public_recycler);
        swipeRefreshLayout =  findViewById(R.id.public_swip);
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(ColorRgbUtil.getBaseColor());
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                refresh(false,TagFinal.REFRESH);
            }
        });
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
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
        adapter=new NoticeAdapter(mActivity);
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


    private void initSQToolBar() {
        assert toolbar!=null;
        toolbar.setTitle(R.string.notice);
        if (Base.adminDb.getIsnoticeadmin().equals(TagFinal.TRUE)){
            toolbar.addMenuText(1,"发通知");
        }
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {

                if(position==1){
                    Intent intent=new Intent(mActivity,NoticeAddActivity.class);
                    startActivityForResult(intent,TagFinal.UI_ADD);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_ADD:
                    refresh(false,TagFinal.REFRESH);
                    break;
                case TagFinal.UI_ADMIN:
                    refresh(false, TagFinal.REFRESH);
                    break;
                case TagFinal.UI_TAG:
                    refresh(false, TagFinal.REFRESH);
                    break;
                default:
                    break;
            }
        }
    }





    /**
     * --------------------------retrofit------------------
     */




    public  void readNotice(String key){

        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        ReadNoticeReq request = new ReadNoticeReq();
        //获取参数
        request.setType(key);
        reqBody.readnotice = request;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().read_notice(reqEnv);
        call.enqueue(this);
    }




    /**
     * @param is 显示 true: showProgressDialog("正在登录"); false:null
     * @param loadType 上啦，下拉
     */
    public void refresh(boolean is,String loadType){

        if (loadType.equals(TagFinal.REFRESH)){
            page=0;
        }else{
            adapter.setLoadState(TagFinal.LOADING);
            page++;
        }
        ReqEnv evn=new ReqEnv();
        ReqBody reqbody=new ReqBody();
        NoticeGetUserListReq req=new NoticeGetUserListReq();

        req.setPage(page);
        req.setSize(TagFinal.FIFTEEN_INT);
        reqbody.noticeGetUserListReq =req;
        evn.body=reqbody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().notice_get_user_list(evn);
        call.enqueue(this);
        if (is) showProgressDialog("正在加载");
    }


    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();
        closeSwipeRefresh();
        ResEnv respEnvelope = response.body();
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.noticeGetUserListRes !=null){
                String result=b.noticeGetUserListRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                NoticeRes res=gson.fromJson(result,NoticeRes.class);

                List<NoticeBean> list=res.getMynotice();
                for (NoticeBean bean:list){
                    bean.setView_type(TagFinal.TYPE_ITEM);
                }
                if (page==0){
                    receiveNoticeList.clear();
                    if (Base.adminDb.getIsnoticeadmin().equals(TagFinal.TRUE)){
                        receiveNoticeList.add(top);
                    }
                    receiveNoticeList.addAll(list);
                }else{
                    receiveNoticeList.addAll(list);
                }

                adapter.setDataList(receiveNoticeList);
                if (res.getMynotice().size()!=TagFinal.FIFTEEN_INT){
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
        Logger.e("onFailure  :"+call.request().headers().toString());
        dismissProgressDialog();
        closeSwipeRefresh();
        toast(R.string.fail_do_not);
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }

}
