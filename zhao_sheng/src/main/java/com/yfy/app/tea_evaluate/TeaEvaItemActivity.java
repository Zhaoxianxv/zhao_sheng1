package com.yfy.app.tea_evaluate;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.zhao_sheng.R;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.judge.TeaJudgeGetInfoYearListReq;
import com.yfy.app.tea_evaluate.adpter.EvaluateTwoAdapter;
import com.yfy.app.tea_evaluate.bean.ItemBean;
import com.yfy.app.tea_evaluate.bean.ResultInfo;
import com.yfy.final_tag.Base;
import com.yfy.base.Variables;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.final_tag.recycerview.EndlessRecyclerOnScrollListener;
import com.yfy.final_tag.recycerview.RecycleViewDivider;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeaEvaItemActivity extends WcfActivity implements Callback<ResEnv> {

    private static final String TAG = TeaEvaItemActivity.class.getSimpleName();
    private int  page=0;
    private String param_id="0";
    private EvaluateTwoAdapter adapter;
    private List<ItemBean> dataList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_main);
        getData();
        initRecycler();

    }
    private void getData(){
        Bundle bundle=getIntent().getExtras();
        if (StringJudge.isContainsKey(bundle,TagFinal.ID_TAG)){
            param_id = bundle.getString(TagFinal.ID_TAG);
            Variables.type_id=param_id;
            refresh(true,TagFinal.REFRESH);
        }
        if (StringJudge.isContainsKey(bundle,TagFinal.NAME_TAG)){
            String name = bundle.getString(TagFinal.NAME_TAG);
            Variables.type=name;
            initSQToolbar(name);
        }else{
            initSQToolbar("");

        }

    }


    private SwipeRefreshLayout swipeRefreshLayout;
    public RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView = findViewById(R.id.public_recycler);
        swipeRefreshLayout = findViewById(R.id.public_swip);
        //AppBarLayout 展开执行刷新
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(ColorRgbUtil.getBaseColor());
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                refresh(false, TagFinal.REFRESH);
            }
        });
        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener() {
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
        adapter=new EvaluateTwoAdapter(this,dataList);
        recyclerView.setAdapter(adapter);

    }



    private  void initSQToolbar(final String title){
        assert toolbar!=null;
        toolbar.setTitle(title);
        toolbar.addMenu(TagFinal.ONE_INT,R.drawable.add);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent=new Intent(mActivity, TeaEvaluateAddActivity.class);
                Bundle b=new Bundle();
                b.putString(TagFinal.ID_TAG,param_id);
                b.putString(TagFinal.NAME_TAG,title);
                intent.putExtras(b);
                startActivityForResult(intent,TagFinal.UI_CONTENT);
            }
        });

    }

    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        if (!isActivity()) return false;
        dismissProgressDialog();
        closeSwipeRefresh();
        String name=wcfTask.getName();

        if (name.equals(TagFinal.REFRESH)){
            ResultInfo info=gson.fromJson(result,ResultInfo.class);
            if (info.getJudge_record().size()<TagFinal.TEN_INT){
                toastShow("加载结束");
            }
            dataList=info.getJudge_record();
            adapter.setDataList(dataList);
            adapter.setLoadState(2);
            return false;
        }
        if (name.equals(TagFinal.REFRESH)){
            ResultInfo info=gson.fromJson(result,ResultInfo.class);
            if (info.getJudge_record().size()<TagFinal.TEN_INT){
                toastShow("加载结束");
            }
            dataList.addAll(info.getJudge_record());
            adapter.setDataList(dataList);
            adapter.setLoadState(2);
            return false;
        }

        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            refresh(false,TagFinal.REFRESH);
//            switch (requestCode){
//                case TagFinal.UI_CONTENT:
//                    refresh(false,TagFinal.REFRESH);
//                    break;
//            }
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
     * --------------------retrofit-------------
     */


    public void refresh(boolean is,String loadType){

        if (loadType.equals(TagFinal.REFRESH)){
            page=0;
        }else{
            adapter.setLoadState(TagFinal.LOADING);
            page++;
        }


        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        TeaJudgeGetInfoYearListReq req = new TeaJudgeGetInfoYearListReq();
        //获取参数
        req.setId(ConvertObjtect.getInstance().getInt(param_id));
        req.setYear(ConvertObjtect.getInstance().getInt(Base.year));
        req.setPage(page);
        req.setSize(TagFinal.TEN_INT);

        reqBody.teaJudgeGetInfoYearListReq = req;
        reqEnv.body= reqBody;

        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().judge_get_info_year_list(reqEnv);
        call.enqueue(this);
        if (is) showProgressDialog("");
    }







    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b = respEnvelope.body;
            if (b.teaJudgeGetInfoYearListRes != null) {
                String result = b.teaJudgeGetInfoYearListRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                ResultInfo res=gson.fromJson(result,ResultInfo.class);
                if (page==0){
                    dataList=res.getJudge_record();
                    adapter.setDataList(dataList);
                }else {
                    dataList.addAll(res.getJudge_record());
                    adapter.setDataList(dataList);
                }
                if (res.getJudge_record().size()!=TagFinal.TEN_INT){
                    toastShow(R.string.success_loadmore_end);
                    adapter.setLoadState(3);
                }else{
                    adapter.setLoadState(2);
                }
            }
        }else {
            Logger.e(StringUtils.getTextJoint("%1$s:%2$d",name,response.code()));
            toastShow(StringUtils.getTextJoint("数据错误:%1$d",response.code()));
        }

    }


    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        if (!isActivity())return;
        dismissProgressDialog();
        Logger.e("onFailure "+call.request().headers().toString());
        toastShow(R.string.fail_do_not);
    }


    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
