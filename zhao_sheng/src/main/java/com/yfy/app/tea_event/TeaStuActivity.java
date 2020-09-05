package com.yfy.app.tea_event;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.zhao_sheng.R;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.satisfaction.SatisfactionTeaGetStuReq;
import com.yfy.app.tea_event.adapter.TeaStuAdapter;
import com.yfy.app.tea_event.bean.TeaEventRes;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.RecycleViewDivider;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TeaStuActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = TeaStuActivity.class.getSimpleName();

    private TeaStuAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_main);
        getData();
        initSQtoolbar();
        initRecycler();

    }
    private String term_id;
    private String class_id;
    private void getData(){
        class_id=getIntent().getStringExtra(TagFinal.ID_TAG);
        term_id=getIntent().getStringExtra(TagFinal.id);
    }
    private void initSQtoolbar() {
        assert toolbar!=null;
        toolbar.setTitle("学生");
        getStu(true);
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
                getStu(false);
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
        adapter=new TeaStuAdapter(mActivity);
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



    public void getStu(boolean is){
        ReqEnv evn = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        SatisfactionTeaGetStuReq request = new SatisfactionTeaGetStuReq();
        //获取参数
        request.setTermid(ConvertObjtect.getInstance().getInt(term_id));
        request.setClassid(ConvertObjtect.getInstance().getInt(class_id));
        reqBody.satisfactionTeaGetStuReq = request;
        evn.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().satisfaction_tea_get_stu(evn);
        call.enqueue(this);
        Logger.e(evn.toString());
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
            if (b.satisfactionTeaGetStuRes !=null){
                String result=b.satisfactionTeaGetStuRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                TeaEventRes res=gson.fromJson(result,TeaEventRes.class );
                if (res.getResult().equals(TagFinal.TRUE)){
                    adapter.setDataList(res.getIncompletestu());
                    adapter.setLoadState(TagFinal.LOADING_COMPLETE);
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
        dismissProgressDialog();
        Logger.e("onFailure  :"+call.request().headers().toString());
        closeSwipeRefresh();
    }


    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
