package com.yfy.app.attennew;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.zhao_sheng.R;
import com.yfy.app.attennew.adapter.MaintainAdminAdapter;
import com.yfy.app.attennew.bean.AttenBean;
import com.yfy.app.attennew.bean.AttenRes;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.atten.AttenGetAdminListReq;
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

public class AttenNewAdminActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = AttenNewAdminActivity.class.getSimpleName();
    private int page=0;
    private MaintainAdminAdapter adapter;
    private List<AttenBean> mainBeens=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_main);
        initSQToolbar();
        initRecycler();
        refresh(true,TagFinal.REFRESH);
    }


    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }

    public void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle("请假审批");
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
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                adapter.setLoadState(TagFinal.LOADING);
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
                getResources().getColor(R.color.Gray)));
        adapter=new MaintainAdminAdapter(mActivity,mainBeens);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_REFRESH:
                    refresh(false,TagFinal.REFRESH);
                    break;
            }
        }
    }







    /**
     * --------------------------retrofit--------------------------
     */

    private void refresh(boolean is,String type_load){
        if (type_load.equals(TagFinal.REFRESH)){
            page=0;
        }else{
            page++;
        }
        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        AttenGetAdminListReq request = new AttenGetAdminListReq();
        //获取参数
        request.setPage(page);

        reqBody.attenGetAdminListReq = request;
        reqEnvelop.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().atten_get_admin_list(reqEnvelop);
        call.enqueue(this);
        if (is) showProgressDialog("");
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
            if (b.attenGetAdminListRes !=null){
                String result=b.attenGetAdminListRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));

                AttenRes res=gson.fromJson(result,AttenRes.class);
                if (page==0){
                    mainBeens.clear();
                    mainBeens=res.getMaintains();
                }else{
                    if (res.getMaintains().size()==TagFinal.ZERO_INT)return ;
                    mainBeens.addAll(res.getMaintains());
                }
                adapter.setDataList(mainBeens);
                if (res.getMaintains().size()!=TagFinal.TEN_INT){
                    toastShow(R.string.success_loadmore_end);
                    adapter.setLoadState(3);
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
        dismissProgressDialog();
        closeSwipeRefresh();
        toastShow(R.string.fail_do_not);
    }



    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
