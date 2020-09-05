package com.yfy.app.duty;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.zhao_sheng.R;
import com.yfy.app.duty.adpater.ChoiceTypeAdapter;
import com.yfy.app.duty.bean.Addinfo;
import com.yfy.app.duty.bean.DutyRes;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.duty.DutyTypeReq;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.RecycleViewDivider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DutyTagActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = DutyTagActivity.class.getSimpleName();
    private ChoiceTypeAdapter adapter;

    private List<Addinfo> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_main);
        initSQtoolbar();
        initRecycler();
        getDutyType(true);
    }



    private void initSQtoolbar() {
        assert toolbar!=null;
        toolbar.setTitle("值周类型");
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

                getDutyType(false);

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
        adapter=new ChoiceTypeAdapter(this);
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

    public void getDutyType(boolean is) {

        ReqEnv evn = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        DutyTypeReq req = new DutyTypeReq();
        //获取参数
        reqBody.duty_typeReq = req;
        evn.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().duty_get_type(evn);
        call.enqueue(this);
		if (is)showProgressDialog("");
    }

    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();
        closeSwipeRefresh();
        if (response.code()==500){
            try {
                String s=response.errorBody().string();
//                Logger.e(TagFinal.ZXX, s);
            } catch (IOException e) {
                e.printStackTrace();
            }
            toastShow("数据出差了");
        }

        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.duty_type_sponse!=null){
                String result=b.duty_type_sponse.result;
                Logger.e(call.request().headers().toString()+result );
				DutyRes info=gson.fromJson(result,DutyRes.class );
				list=info.getDutyreport_type();
				adapter.setDataList(list);
				adapter.setLoadState(TagFinal.LOADING_COMPLETE);
            }


        }else{
            Logger.e( "evn: null" );
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
