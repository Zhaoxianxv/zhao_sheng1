package com.yfy.app.appointment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.zhao_sheng.R;
import com.google.gson.Gson;
import com.yfy.app.appointment.adpater.AdminListAdapter;
import com.yfy.app.appointment.bean.OrderBean;
import com.yfy.app.appointment.bean.OrderRes;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.applied_order.OrderGetAdminListReq;
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

public class AdminOrderActivity extends BaseActivity implements Callback<ResEnv> {
    private static final String TAG = AdminOrderActivity.class.getSimpleName();


    private AdminListAdapter adapter;


    private int pager=0;
    private List<OrderBean> rooms=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_change_application);
        initSQToolbar();
        initRecycler();
        gson=new Gson();
        refresh(true,TagFinal.REFRESH);

    }



    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private  void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle(R.string.andmin_order);
    }
    private SwipeRefreshLayout swipeRefreshLayout;
    public RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView =  findViewById(R.id.admin_xlist);
        swipeRefreshLayout =  findViewById(R.id.order_admin_swip);
        //AppBarLayout 展开执行刷新
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#4DB6AC"));
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                refresh(false,TagFinal.REFRESH);
            }
        });
        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {

                refresh(false,TagFinal.REFRESH_MORE);
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
        adapter=new AdminListAdapter(this,rooms);
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
     * --------------------------retrofit------------------
     */






    public void refresh(boolean is,String loadType){

        if (loadType.equals(TagFinal.REFRESH)){
            pager=0;
        }else{
            adapter.setLoadState(TagFinal.LOADING);
            pager++;
        }


        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        OrderGetAdminListReq req = new OrderGetAdminListReq();
        //获取参数
        req.setPage(pager);
        req.setSize(TagFinal.FIFTEEN_INT);

        reqBody.orderGetAdminListReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().order_get_admin_list(reqEnv);
        call.enqueue(this);
        if (is)showProgressDialog("");

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
            if (b.orderGetAdminListRes!=null){
                String result=b.orderGetAdminListRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                OrderRes res=gson.fromJson(result,OrderRes.class);
                if (pager==0){
                    if (rooms.size()!=0){
                        rooms.clear();
                    }
                    rooms.addAll(res.getAdmin());
                    adapter.setDataList(rooms);
                }else {
                    rooms.addAll(res.getAdmin());
                    adapter.setDataList(rooms);
                }
                if (res.getAdmin().size()!=TagFinal.FIFTEEN_INT){
                    toastShow(R.string.success_loadmore_end);
                    adapter.setLoadState(TagFinal.LOADING_END);
                }else{
                    adapter.setLoadState(2);
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
        toast(R.string.fail_do_not);
        closeSwipeRefresh();
        dismissProgressDialog();
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }





}
