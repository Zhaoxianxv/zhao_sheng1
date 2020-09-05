package com.yfy.app.tea_event;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.google.gson.Gson;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.satisfaction.SatisfactionStuGetTeaReq;
import com.yfy.app.tea_event.adapter.TeaAdapter;
import com.yfy.app.tea_event.bean.TeaEventRes;
import com.yfy.base.fragment.BaseFragment;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.json.JsonParser;
import com.yfy.final_tag.recycerview.DefaultItemAnimator;
import com.yfy.final_tag.recycerview.RecycleViewDivider;


import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yfyandr on 2017/8/1.
 */

public class TeaFragment extends BaseFragment implements Callback<ResEnv> {

    private Gson gson=new Gson();

    @Bind(R.id.choice_fragment_recycler)
    RecyclerView mRecyclerView;
    @Bind(R.id.choice_fragment_swip)
    SwipeRefreshLayout swip;


    @Bind(R.id.public_deleted_text)
    TextView del;

    private TeaAdapter adapter;
    @Override
    public void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.choice_fragment);
        del.setVisibility(View.GONE);
        initRecycler();
        getTea();
    }



    public void closeSwipeRefresh(){
        if (swip!=null){
            swip.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (swip != null && swip.isRefreshing()) {
                        swip.setRefreshing(false);
                    }
                }
            }, 200);
        }
    }


    public void initRecycler(){

        // 设置刷新控件颜色
        swip.setColorSchemeColors(Color.parseColor("#4DB6AC"));
        // 设置下拉刷新
        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                getTea();
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线



        //添加分割线
        mRecyclerView.addItemDecoration(new RecycleViewDivider(
                mActivity,
                LinearLayoutManager.HORIZONTAL,
                1,
                getResources().getColor(R.color.gray)));
        adapter=new TeaAdapter(TeaFragment.this);
        mRecyclerView.setAdapter(adapter);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==Activity.RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_REFRESH:
                    break;
                case TagFinal.UI_TAG:
                    getTea();
                    break;
            }
        }
    }



    /**
     * ----------------------------retrofit-----------------------
     */



    public void getTea() {

        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        SatisfactionStuGetTeaReq req = new SatisfactionStuGetTeaReq();
        //获取参数
        reqBody.satisfactionStuGetTeaReq = req;
        reqEnvelop.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().satisfaction_get_tea_list(reqEnvelop);
        call.enqueue(this);
    }
    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        dismissProgressDialog();
        closeSwipeRefresh();


        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);

        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.satisfactionStuGetTeaRes !=null){
                String result=b.satisfactionStuGetTeaRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                TeaEventRes res=gson.fromJson(result, TeaEventRes.class);
                if (res.getResult().equals(TagFinal.TRUE)){
                    adapter.setDataList(res.getTeachers());
                    adapter.setLoadState(TagFinal.LOADING_COMPLETE);
                }else{
                    toastShow(JsonParser.getErrorCode(result));
                }
            }

        }else{
            Logger.e(StringUtils.getTextJoint("%1$s:%2$d",name,response.code()));
            toastShow(StringUtils.getTextJoint("数据错误:%1$d",response.code()));
        }

    }

    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        toast(R.string.fail_do_not);
        Logger.e("onFailure  :"+call.request().headers().toString());
        dismissProgressDialog();
        closeSwipeRefresh();
    }
}
