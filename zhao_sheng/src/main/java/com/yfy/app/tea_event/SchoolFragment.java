package com.yfy.app.tea_event;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.google.gson.Gson;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.satisfaction.SatisfactionStuGetSchoolReq;
import com.yfy.app.net.satisfaction.SatisfactionStuSetSchoolScoreReq;
import com.yfy.app.tea_event.adapter.TeaSchoolAdapter;
import com.yfy.app.tea_event.bean.TRes;
import com.yfy.app.tea_event.bean.TeaDe;
import com.yfy.app.tea_event.bean.TeaDeInfo;
import com.yfy.app.tea_event.bean.TeaEventRes;
import com.yfy.base.fragment.BaseFragment;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.json.JsonParser;
import com.yfy.final_tag.recycerview.DefaultItemAnimator;
import com.yfy.final_tag.recycerview.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yfyandr on 2017/8/1.
 */

public class SchoolFragment extends BaseFragment implements Callback<ResEnv> {

    private Gson gson=new Gson();

    @Bind(R.id.choice_fragment_recycler)
    RecyclerView mRecyclerView;
    @Bind(R.id.choice_fragment_swip)
    SwipeRefreshLayout swip;


    @Bind(R.id.public_deleted_text)
    TextView del;

    private TeaSchoolAdapter adapter;
    @Override
    public void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.choice_fragment);
        initRecycler();
        del.setText("提交");
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
        adapter=new TeaSchoolAdapter(SchoolFragment.this);
        mRecyclerView.setAdapter(adapter);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==Activity.RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_REFRESH:
                    break;
            }
        }
    }



    @OnClick(R.id.public_deleted_text)
    void setAdd(){

        List<TeaDeInfo> list=adapter.getDataList();

        if (StringJudge.isEmpty(list)){
            toast("未获取到评测项目");
            return;
        }

        List<String> event_id=new ArrayList<>();
        List<String> content_item=new ArrayList<>();
        for (TeaDeInfo info:list){
            switch (info.getType()){
                case "单选":
                    event_id.add(info.getEvaluateid());
                    String tag="",event_item_id="";
                    tag=info.getEvaluatetitle();
                    for (TeaDe teade:info.getEvaluateselect()){
                        if(teade.getIscheck().equals(TagFinal.TRUE)){
                            event_item_id=teade.getId();
                        }
                    }
                    if (StringJudge.isEmpty(event_item_id)){
                        toast("请选择"+tag);
                        return;
                    }
                    content_item.add(event_item_id);
                    break;
                case "文本":
                    event_id.add(info.getEvaluateid());
                    if (StringJudge.isEmpty(info.getContent())){
                        content_item.add("");
                    }else{
                        content_item.add(info.getContent().replace("\"","“" ));
                    }
                    break;
            }
        }
        setSchoolScore(StringUtils.stringToArraysGetString(event_id,"*" ), StringUtils.stringToArraysGetString(content_item,"*"));
    }








    /**
     * ----------------------------retrofit-----------------------
     */




    public void setSchoolScore(String event_id, String content){


        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        SatisfactionStuSetSchoolScoreReq req = new SatisfactionStuSetSchoolScoreReq();
        //获取参数
        req.setContent(StringUtils.stringToUpJson(content));
        req.setEvaluateid(event_id);
        reqBody.satisfactionStuSetSchoolScoreReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().satisfaction_stu_set_school_score(reqEnv);
        call.enqueue(this);
        showProgressDialog("");
    }




    public void getTea() {

        int term_id=getArguments().getInt(TagFinal.id, -1);
        if (term_id==-1){
            toast("没有获取到当前学期");
            return;
        }
        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        SatisfactionStuGetSchoolReq req = new SatisfactionStuGetSchoolReq();
        //获取参数
        req.setTermid(term_id);
        reqBody.satisfactionStuGetSchoolReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().satisfaction_get_school(reqEnv);
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
            if (b.satisfactionStuGetSchoolRes !=null){
                String result=b.satisfactionStuGetSchoolRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                TeaEventRes res=gson.fromJson(result, TeaEventRes.class);
                if (res.getResult().equals(TagFinal.TRUE)){
                    adapter.setDataList(res.getEvaluatelist());
                    adapter.setLoadState(TagFinal.LOADING_COMPLETE);
                }else{
                    toastShow(JsonParser.getErrorCode(result));
                }
            }
            if (b.satisfactionStuSetSchoolScoreRes !=null){
                String result=b.satisfactionStuSetSchoolScoreRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                TRes res=gson.fromJson(result,TRes.class);
                if (res.getResult().equals(TagFinal.TRUE)){
                    toast("成功打分！");
                    getTea();
                }else{
                    toast(res.getError_code());
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
