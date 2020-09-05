package com.yfy.app.tea_event;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.satisfaction.SatisfactionGetDetailReq;
import com.yfy.app.net.satisfaction.SatisfactionStuSetTeaScoreReq;
import com.yfy.app.tea_event.adapter.TeaDetailAdapter;
import com.yfy.app.tea_event.bean.TeaDe;
import com.yfy.app.tea_event.bean.TeaDeBean;
import com.yfy.app.tea_event.bean.TeaDeInfo;
import com.yfy.app.tea_event.bean.TeaEventRes;
import com.yfy.app.tea_event.bean.Teacher;
import com.yfy.base.activity.BaseActivity;
import com.yfy.db.UserPreferences;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TeaDetailsActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = TeaDetailsActivity.class.getSimpleName();



    private TeaDetailAdapter adapter;

    @Bind(R.id.public_deleted_text)
    TextView del;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_delete_main);
        getData();
        del.setText("提交");
        del.setTextColor(ColorRgbUtil.getGreen());
        initSQtoolbar();
        initRecycler();

    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }

    private void initSQtoolbar() {
        assert toolbar!=null;
        toolbar.setTitle("师德满意率评测");
        toolbar.setOnNaviClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private Teacher teacher;
    private void getData(){
        teacher=getIntent().getParcelableExtra(TagFinal.OBJECT_TAG);
        getTeaDetail(true);
    }

    private SwipeRefreshLayout swipeRefreshLayout;
    public RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView =  findViewById(R.id.public_delete_recycler);
        swipeRefreshLayout =  findViewById(R.id.public_deleted_swipe);
        //AppBarLayout 展开执行刷新
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(ColorRgbUtil.getBaseColor());
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                getTeaDetail(false);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        xlist.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
//        recyclerView.addItemDecoration(new RecycleViewDivider(
//                mActivity,
//                LinearLayoutManager.HORIZONTAL,
//                1,
//                getResources().getColor(R.color.gray)));
        adapter=new TeaDetailAdapter(mActivity);
        adapter.setTeacher(teacher);
        recyclerView.setAdapter(adapter);

    }



    @OnClick(R.id.public_deleted_text)
    void setAdd(){
        List<String> event_id=new ArrayList<>();
        List<String> content_item=new ArrayList<>();
        if (StringJudge.isNull(adapter.getRes())){
            toastShow("未获取到评测项目");
            return;
        }
        TeaEventRes res=adapter.getRes();
        if (StringJudge.isEmpty(res.getEvaluatelist())){
            toastShow("未获取到评测项目");
            return;
        }
        for (TeaDeInfo info:res.getEvaluatelist()){
            switch (info.getType()){
                case "单选":
                    String tag="",item_tag="",event_item_id="";
                    List<String> child_id=new ArrayList<>();
                    event_id.add(info.getEvaluateid());
                    tag=info.getEvaluatetitle();
                    for (TeaDe teade:info.getEvaluateselect()){
                        if(teade.getIscheck().equals(TagFinal.TRUE)){
                            event_item_id=teade.getId();
                            item_tag=teade.getTitle();
                            if (!StringJudge.isEmpty(teade.getEvaluatelast())){
                                for (TeaDeBean bean:teade.getEvaluatelast()){
                                    if (bean.getSelectischeck().equals(TagFinal.TRUE))
                                    child_id.add(bean.getSelectid());
                                }
                                if (StringJudge.isEmpty(child_id)){
                                    toastShow("至少选择一项:"+item_tag);
                                    return;
                                }
                            }

                        }
                    }
                    if (StringJudge.isEmpty(event_item_id)){
                        toastShow("请选择"+tag);
                        return;
                    }
                    if (StringJudge.isEmpty(child_id)){
                        content_item.add(event_item_id);
                    }else{
                        content_item.add(StringUtils.stringToArraysGetString(child_id,"," ));
                    }
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
        setTeaScore(StringUtils.stringToArraysGetString(event_id,"*" ), StringUtils.stringToArraysGetString(content_item,"*"));
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

    public void setTeaScore(String event_id, String content){



        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        SatisfactionStuSetTeaScoreReq req = new SatisfactionStuSetTeaScoreReq();
        //获取参数
        req.setTeacherid(ConvertObjtect.getInstance().getInt(teacher.getTeacherid()));
        req.setCoureseid(ConvertObjtect.getInstance().getInt(teacher.getCourseid()));
        req.setEvaluateid(event_id);
        req.setContent(StringUtils.stringToUpJson(content));
        reqBody.satisfactionStuSetTeaScoreReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().satisfaction_stu_set_tea_score(reqEnv);
        call.enqueue(this);
        showProgressDialog("");
    }


    public void getTeaDetail(boolean is){

        if (teacher==null){
            toastShow("数据 出错");
            return;
        }
        String term_id=UserPreferences.getInstance().getTermId();

        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        SatisfactionGetDetailReq req = new SatisfactionGetDetailReq();
        //获取参数
        req.setTeacherid(ConvertObjtect.getInstance().getInt(teacher.getTeacherid()));
        req.setCoureseid(ConvertObjtect.getInstance().getInt(teacher.getCourseid()));
        req.setTermid(ConvertObjtect.getInstance().getInt(term_id));
        reqBody.satisfactionGetDetailReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().satisfaction_get_detail(reqEnv);
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
            if (b.satisfactionGetDetailRes !=null){
                String result=b.satisfactionGetDetailRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                TeaEventRes res=gson.fromJson(result, TeaEventRes.class);
                if (res.getResult().equals(TagFinal.TRUE)){
                    if (StringJudge.isEmpty(res.getEvaluatelist())){
                        toastShow("未获取到评测项目");
                    }
                    adapter.initData(res);
                }else{
                    toastShow(res.getError_code());
                }
            }
            if (b.satisfactionStuSetTeaScoreRes !=null){
                String result=b.satisfactionStuSetTeaScoreRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                TeaEventRes res=gson.fromJson(result, TeaEventRes.class);
                if (res.getResult().equals(TagFinal.TRUE)){
                    toastShow("成功修改！");
                    finish();
                }else{
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
        toastShow(R.string.fail_do_not);
        Logger.e("onFailure  :"+call.request().headers().toString());
        dismissProgressDialog();
        closeSwipeRefresh();
    }
    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
