package com.yfy.app.event;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.event.bean.EventRes;
import com.yfy.app.integral.ChangeTermActivity;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.event.EventGetWeekReq;
import com.yfy.base.activity.BaseActivity;
import com.yfy.db.UserPreferences;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.RecycleViewDivider;
import com.yfy.view.SQToolBar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChoiceWeekActivity extends BaseActivity implements Callback<ResEnv> {
    private static final String TAG = ChoiceWeekActivity.class.getSimpleName();
    private ChoiceWeekAdapter adapter;
    private String term_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_main);
        term_id=UserPreferences.getInstance().getTermId();
        initRecycler();
        initSQtoolbar();
        getWeek(true,term_id);
    }

    private TextView menuOne;
    private void initSQtoolbar() {
        assert toolbar!=null;
        toolbar.setTitle("选择时间");
        menuOne=toolbar.addMenuText(TagFinal.ONE_INT,UserPreferences.getInstance().getTermName());
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent =new Intent(mActivity,ChangeTermActivity.class);
                startActivityForResult(intent,TagFinal.UI_TAG );
            }
        });

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
                getWeek(false,term_id);

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
        adapter=new ChoiceWeekAdapter(this);
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
            switch (requestCode){
                case TagFinal.UI_TAG:
                    term_id=data.getStringExtra("data_id");
                    menuOne.setText(data.getStringExtra("data_name"));
                    getWeek(false, term_id);
                    break;
            }
        }
    }

    /**
     * ----------------------------retrofit-----------------------
     */



    public void getWeek(boolean is,String term_id){

        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        EventGetWeekReq req = new EventGetWeekReq();

        req.setTermid(ConvertObjtect.getInstance().getInt(term_id));
        //获取参数
        reqBody.eventGetWeekReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().event_get_week(reqEnv);
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
            if (b.eventGetWeekRes !=null){
                String result=b.eventGetWeekRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                EventRes res=gson.fromJson(result,EventRes.class);
                adapter.setDataList(res.getEvenet_week());
                adapter.setLoadState(TagFinal.LOADING_COMPLETE);
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
    }


    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }

}
