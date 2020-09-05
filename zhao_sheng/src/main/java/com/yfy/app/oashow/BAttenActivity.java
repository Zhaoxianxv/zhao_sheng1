package com.yfy.app.oashow;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.zhao_sheng.R;
import com.yfy.app.attennew.bean.AttenBean;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.atten.BAttenMainReq;
import com.yfy.app.oashow.adapter.BAttenAdapter;
import com.yfy.app.oashow.bean.BossRes;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
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

public class BAttenActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = BAttenActivity.class.getSimpleName();

    private BAttenAdapter adapter;
    private List<AttenBean> lateBeans=new ArrayList<>();
    private int page=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_main);
        initSQToolbar();
        initRecycler();
        refresh(true,TagFinal.REFRESH);
    }


    public void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle(R.string.atten);
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
                ColorRgbUtil.getGrayText()));
        adapter=new BAttenAdapter(mActivity,lateBeans);
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

    public void refresh(boolean is,String loadType){
        if (loadType.equals(TagFinal.REFRESH)){
            page=0;
        }else{
            page++;
        }
        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        BAttenMainReq request = new BAttenMainReq();
        //获取参数
        request.setPage(page);

        reqBody.bAttenMainReq = request;
        reqEnvelop.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().atten_get_b_main(reqEnvelop);
        call.enqueue(this);
        if (is) showProgressDialog("");
        Logger.e(reqEnvelop.toString());
    }



    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();
        closeSwipeRefresh();
        if (response.code()==500){
            toastShow("数据出差了");
        }
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;

            if (b.bAttenMainRes !=null){
                String result=b.bAttenMainRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                BossRes res=gson.fromJson(result,BossRes.class);
                if (page==0){
                    lateBeans.clear();
                    lateBeans=res.getAttendance_lists();
                }else{
                    lateBeans.addAll(res.getAttendance_lists());
                }
                adapter.setDataList(lateBeans);
                if (res.getAttendance_lists().size()!=TagFinal.TEN_FIVE){
                    toastShow(R.string.success_loadmore_end);
                    adapter.setLoadState(3);
                }else{
                    adapter.setLoadState(2);
                }
            }
        }else{
            Logger.e(name+"---ResEnv:null");
        }
    }

    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        if (!isActivity())return;
        Logger.e("onFailure  :"+call.request().headers().toString());
        toast(R.string.fail_do_not);
        dismissProgressDialog();
        closeSwipeRefresh();
    }


    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
