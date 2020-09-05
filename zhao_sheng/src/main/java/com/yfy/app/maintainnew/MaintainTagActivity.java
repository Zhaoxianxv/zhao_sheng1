package com.yfy.app.maintainnew;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.zhao_sheng.R;
import com.yfy.app.maintainnew.adapter.MaintainTagAdapter;
import com.yfy.app.maintainnew.bean.DepTag;
import com.yfy.app.maintainnew.bean.MainResult;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.maintain.MaintainGetSectionReq;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MaintainTagActivity extends BaseActivity implements Callback<ResEnv> {
    private static final String TAG = MaintainTagActivity.class.getSimpleName();


    private MaintainTagAdapter adapter;
    private List<DepTag> depTags=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_main);
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }
        initSQtoobar();
        initRecycler();
//        initView();
        getSectionList(true);

    }


    private void initSQtoobar() {
        assert toolbar!=null;
        toolbar.setTitle("选择部门");

    }



    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView =  findViewById(R.id.public_recycler);
        swipeRefreshLayout =  findViewById(R.id.public_swip);
        //AppBarLayout 展开执行刷新
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(ColorRgbUtil.getBaseColor());
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                getSectionList(false);
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
                ColorRgbUtil.getGainsboro()));
        adapter=new MaintainTagAdapter(mActivity);
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


    public void getSectionList(boolean is)  {

        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        MaintainGetSectionReq req = new MaintainGetSectionReq();
        //获取参数

        reqBody.maintainGetSectionReq = req;
        reqEnv.body = reqBody;

        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().maintain_get_section(reqEnv);
        call.enqueue(this);
        if (is)showProgressDialog("正在加载");

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
            if (b.maintainGetSectionRes !=null) {
                String result = b.maintainGetSectionRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));

                MainResult info=gson.fromJson(result, MainResult.class);
                if (StringJudge.isNotNull(info)){
                    depTags=info.getMaintainclass();
                    adapter.setDataList(depTags);
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
        toast(R.string.fail_do_not);
    }


    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
