package com.yfy.app.appointment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.zhao_sheng.R;
import com.yfy.app.appointment.adpater.LogisticsAdapter;
import com.yfy.app.appointment.bean.OrderBean;
import com.yfy.app.appointment.bean.OrderRes;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.applied_order.OrderGetLogisticsListReq;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.final_tag.recycerview.EndlessRecyclerOnScrollListener;
import com.yfy.final_tag.recycerview.RecycleViewDivider;
import com.yfy.final_tag.StringJudge;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderLogisticsActivity extends WcfActivity implements Callback<ResEnv> {

    private static final String TAG = OrderLogisticsActivity.class.getSimpleName();
    private int page=0;
    private LogisticsAdapter adapter;
    private List<OrderBean> admins=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_main);
        initRecycler();
        initSQToobar();
        refresh(true,TagFinal.REFRESH );
    }

    private void initSQToobar(){
        assert toolbar!=null;
        toolbar.setTitle("后勤支持");
    }

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView =  findViewById(R.id.public_recycler);
        swipeRefreshLayout =  findViewById(R.id.public_swip);
        //AppBarLayout 展开执行刷新
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(ColorRgbUtil.getBaseColor(),ColorRgbUtil.getGreen());

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
                adapter.setLoadState(TagFinal.LOADING);
                refresh(false, TagFinal.REFRESH_MORE);
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
        adapter=new LogisticsAdapter(mActivity);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch(requestCode){
                case TagFinal.UI_CONTENT:
                    break;
            }
        }
    }


    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        if (!isActivity())return false;
        dismissProgressDialog();
        closeSwipeRefresh();
        String name=wcfTask.getName();

        OrderRes res=gson.fromJson(result,OrderRes.class);
        if (res==null)return false;
        if (name.equals(TagFinal.REFRESH)){
            if (StringJudge.isEmpty(res.getAdmin())){
                toastShow("暂没数据");
                return false;
            }
            admins.clear();
            admins.addAll(res.getAdmin());
            adapter.setDataList(admins);
            adapter.setLoadState(TagFinal.LOADING_COMPLETE);
        }
        if (name.equals(TagFinal.REFRESH_MORE)){
            if (StringJudge.isEmpty(res.getAdmin())){
                toastShow(R.string.xlist_loadmore_end_toast);
                page--;
                return false;
            }
            admins.addAll(res.getAdmin());
            adapter.setDataList(admins);
            if (res.getAdmin().size()<PAGE_NUM){
                toastShow(R.string.xlist_loadmore_bottom_toast);
                adapter.setLoadState(TagFinal.LOADING_END);
            }else{
                adapter.setLoadState(TagFinal.LOADING_COMPLETE);
            }

        }
        return false;
    }


    @Override
    public void onError(WcfTask wcfTask) {
        if (!isActivity())return ;
        dismissProgressDialog();
        closeSwipeRefresh();
        toastShow("网络异常,刷新失败");
    }














    /**
     * @param is 显示 true: showProgressDialog("正在登录"); false:null
     * @param loadType 上啦，下拉
     */


    public void refresh(boolean is,String loadType){

        if (loadType.equals(TagFinal.REFRESH)){
            page=0;
        }else{
            page++;
        }
        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        OrderGetLogisticsListReq req = new OrderGetLogisticsListReq();
        //获取参数
        req.setPage(page);
        req.setSize(TagFinal.FIFTEEN_INT);


        reqBody.orderGetLogisticsListReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().order_get_logistice_list(reqEnv);
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

        ResEnv resEnv = response.body();
        if (resEnv != null) {
            ResBody b=resEnv.body;

            if (b.orderGetLogisticsListRes!=null){
                String result=b.orderGetLogisticsListRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                OrderRes res=gson.fromJson(result,OrderRes.class);
                if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
                    if (page==0){
                        admins.clear();
                        admins=res.getAdmin();
                    }else{
                        admins.addAll(res.getAdmin());
                    }
                    adapter.setDataList(admins);
                    if (res.getAdmin().size()!=TagFinal.FIFTEEN_INT){
                        toastShow(R.string.success_loadmore_end);
                        adapter.setLoadState(3);
                    }else{
                        adapter.setLoadState(2);
                    }
                } else {
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
        toast(R.string.fail_do_not);
        closeSwipeRefresh();
        dismissProgressDialog();
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }

}





