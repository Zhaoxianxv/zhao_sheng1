package com.yfy.app.seal;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.seal.SealMasterCountReq;
import com.yfy.app.net.seal.SealUserListReq;
import com.yfy.app.seal.adapter.SealMainAdapter;
import com.yfy.app.seal.bean.SealMainBean;
import com.yfy.app.seal.bean.SealRes;
import com.yfy.base.activity.BaseActivity;
import com.yfy.db.UserPreferences;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.EndlessRecyclerOnScrollListener;
import com.yfy.final_tag.recycerview.RecycleViewDivider;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SealMainListActivity extends BaseActivity implements Callback<ResEnv> {
    private static final String TAG = SealMainListActivity.class.getSimpleName();

    private SealMainAdapter adapter;
    private List<SealMainBean> sealMainBeans=new ArrayList<>();
    @Bind(R.id.two_count)
    TextView two_count;
    @Bind(R.id.coor_count)
    TextView coor_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_coor_recycler_main);

        initCollapsing();
        initToolbar();
        initRecycler();
        initSQtoobar("申请记录");

        getUserList(true,TagFinal.REFRESH);
        getadminCount();

    }


    private void initSQtoobar(String title) {
        assert toolbar!=null;
        toolbar.setTitle(title);
        toolbar.addMenuText(TagFinal.ONE_INT, "用章申请");
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent=new Intent(mActivity,SealApplyActivity.class);
                startActivityForResult(intent, TagFinal.UI_TAG);
            }
        });
    }





    /**
     * Toolbar 的NavigationIcon大小控制mipmap
     */
    private Toolbar mToolbar;
    public void initToolbar() {

        mToolbar = (Toolbar) findViewById(R.id.coor_top_text);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationIcon(null);  //隐藏掉返回键比如首页，可以调用
//        mToolbar.setVisibility(View.GONE);

    }

    //配置CollapsingToolbarLayout布局
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    public void initCollapsing() {
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.coor_main_collapsing);
        mCollapsingToolbarLayout.setTitle(" ");
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);//设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后Toolbar上字体的颜色
//        mCollapsingToolbarLayout.setVisibility(View.GONE);

    }
    @OnClick(R.id.coor_one_layout)
    void setOneLayout(){
        Intent intent=new Intent(mActivity,SealAdminListActivity.class);
        startActivityForResult(intent,TagFinal.UI_TAG);
    }
//    @OnClick(R.id.coor_two_layout)
//    void setTwoLayout(){
//
//        Intent inte=new Intent(mActivity,SealMasterListActivity.class);
//        startActivityForResult(inte,TagFinal.UI_TAG);
//    }



    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private AppBarLayout appBarLayout;
    public void initRecycler(){

        RelativeLayout one_layout=findViewById(R.id.coor_one_layout);
        RelativeLayout two_layout=findViewById(R.id.coor_two_layout);
        TextView one_name=findViewById(R.id.coor_one_name);
        TextView two_name=findViewById(R.id.coor_two_name);
        TextView detail_name=findViewById(R.id.coor_detail_name);

        two_name.setText("领导审核");
        one_name.setText("待处理的用章申请");
        detail_name.setText("我的申请记录");





        appBarLayout = (AppBarLayout) findViewById(R.id.coor_appbar_layout);
        //AppBarLayout 展开执行刷新
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset >= 0) {
                    swipeRefreshLayout.setEnabled(true);
                    mToolbar.setAlpha(0);
                    mToolbar.setVisibility(View.GONE);
                } else {
                    swipeRefreshLayout.setEnabled(false);
                    mToolbar.setAlpha(1);
                    mToolbar.setVisibility(View.VISIBLE);
                }
            }
        });

        one_layout.setVisibility(View.VISIBLE);
        two_layout.setVisibility(View.GONE);
        //        two_layout.setVisibility(View.VISIBLE);
//        if (UserPreferences.getInstance().getUserAdmin().getIsoffice_supply_master().equals(TagFinal.TRUE)){
//            two_layout.setVisibility(View.VISIBLE);
//        }else{
//            two_layout.setVisibility(View.GONE);
//        }

        if (UserPreferences.getInstance().getUserAdmin().getIssignetadmin().equals(TagFinal.TRUE)){//Variables.admin.getIssignetadmin()
            one_layout.setVisibility(View.VISIBLE);
        }else{
            one_layout.setVisibility(View.GONE);
        }
        if (one_layout.getVisibility()==View.VISIBLE||two_layout.getVisibility()==View.VISIBLE){
            appBarLayout.setVisibility(View.VISIBLE);
        }else{
            appBarLayout.setVisibility(View.GONE);
        }


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
                getUserList(false,TagFinal.REFRESH);


            }
        });
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                getUserList(false,TagFinal.REFRESH_MORE);
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
        adapter=new SealMainAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setDo_tpe("user");




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
                case TagFinal.UI_REFRESH:
                    getUserList(false,TagFinal.REFRESH);
                    getadminCount();

                    break;
                case TagFinal.UI_TAG:
                    getUserList(false,TagFinal.REFRESH);
                    getadminCount();
                    break;
            }
        }
    }





    /**
     * ----------------------------retrofit-----------------------
     */


    private int page=0;
    public void getUserList(boolean is,String loadType){
        if (loadType.equals(TagFinal.REFRESH)){
            page=0;
        }else{
            page++;
        }
        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        SealUserListReq req = new SealUserListReq();
        //获取参数
        req.setPage(page);
        reqBody.sealUserListReq = req;
        reqEnvelop.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().seal_get_user_list(reqEnvelop);
        call.enqueue(this);
    }

    public void getadminCount() {
        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        SealMasterCountReq req = new SealMasterCountReq();
        //获取参数
        reqBody.sealMasterCountReq = req;
        reqEnvelop.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().seal_get_admin_count(reqEnvelop);
        Logger.e(reqEnvelop.toString());
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();
        closeSwipeRefresh();
        if (response.code()==500){
            toastShow("数据出差了");
        }
        ResEnv respEnvelope = response.body();
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.sealUserListRes!=null) {
                String result =b.sealUserListRes.result;
//                String result =StringUtils.string2Json(b.sealUserListRes.result);
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
//                Logger.e(one);
                SealRes res=gson.fromJson(result,SealRes.class );
                if (res.getResult().equals(TagFinal.TRUE)){
                    if (page==0){
                        sealMainBeans=res.getSignetlist();
                    }else{
                        sealMainBeans.addAll(res.getSignetlist());
                    }
                    adapter.setDataList(sealMainBeans);
                    adapter.setLoadState(TagFinal.LOADING_COMPLETE);
                }else{
                    toastShow(res.getError_code());
                }
            }

            if (b.sealMasterCountRes!=null) {
                String result = b.sealMasterCountRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                SealRes res=gson.fromJson(result,SealRes.class );
                if (res.getResult().equals(TagFinal.TRUE)){
                    if (StringJudge.isEmpty(res.getCount())){
                        coor_count.setVisibility(View.GONE);
                    }else{
                        coor_count.setVisibility(View.VISIBLE);
                        if (res.getCount().length()>2) coor_count.setText("99");
                        coor_count.setText(res.getCount());
                        if (res.getCount().equals("0")) coor_count.setVisibility(View.GONE);
                    }
                }else{
                    toastShow(res.getError_code());
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
        toastShow(R.string.fail_do_not);
        closeSwipeRefresh();
        dismissProgressDialog();
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
