package com.yfy.app.attennew;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

import com.yfy.app.attennew.adapter.MaintainAdapter;
import com.yfy.app.attennew.bean.AttenBean;
import com.yfy.app.attennew.bean.AttenRes;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.atten.AttenGetCountReq;
import com.yfy.app.net.atten.AttenGetUserListReq;
import com.yfy.app.net.base.ReadNoticeReq;
import com.yfy.base.activity.BaseActivity;
import com.yfy.db.UserPreferences;
import com.yfy.final_tag.AppLess;
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

public class AttenNewActivity extends BaseActivity implements Callback<ResEnv>{

    private static final String TAG =AttenNewActivity.class.getSimpleName();


    private int page=0;
    private String dealstate;//user：0; admin:-1

    private MaintainAdapter adapter;
    private List<AttenBean> mainBeens=new ArrayList<>();

    @Bind(R.id.two_count)
    TextView two_count;
    @Bind(R.id.coor_count)
    TextView coor_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_coor_recycler_main);
        initSQToolbar();
        initToolbar();
        initCollapsing();
        initRecycler();
        getCountNum();//attendance
        readNotice("attendance");
        refresh(true,TagFinal.REFRESH);
    }



    public void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle(R.string.atten);
        toolbar.addMenuText(TagFinal.ONE_INT,"申请");
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent=new Intent(mActivity,AttenAddActivity.class);
                startActivityForResult(intent,TagFinal.UI_REFRESH);
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
        Intent intent=new Intent(mActivity,AttenNewAdminActivity.class);
        startActivityForResult(intent,TagFinal.UI_REFRESH);
    }
//    @OnClick(R.id.coor_two_layout)
//    void setTwoLayout(){
//
//        Intent inte=new Intent(mActivity,SealMasterListActivity.class);
//        startActivityForResult(inte,TagFinal.UI_TAG);
//    }


    private SwipeRefreshLayout swipeRefreshLayout;
    private AppBarLayout appBarLayout;
    private RecyclerView recyclerView;
    public void initRecycler(){



        RelativeLayout one_layout=findViewById(R.id.coor_one_layout);
        RelativeLayout two_layout=findViewById(R.id.coor_two_layout);
        TextView one_name=findViewById(R.id.coor_one_name);
        TextView two_name=findViewById(R.id.coor_two_name);
        TextView detail_name=findViewById(R.id.coor_detail_name);

        two_name.setText("领导审核");
        one_name.setText("待处理的请假申请");
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

        if (UserPreferences.getInstance().getUserAdmin().getIsqjadmin().equals(TagFinal.TRUE)){//Variables.admin.getIssignetadmin()
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
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#4DB6AC"));
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                refresh(false,TagFinal.REFRESH);
                getCountNum();
            }
        });
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                adapter.setLoadState(TagFinal.LOADING);
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
                getResources().getColor(R.color.gray)));
        adapter=new MaintainAdapter(this,mainBeens);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_REFRESH:
                    refresh(false,TagFinal.REFRESH);
                    getCountNum();
                    break;
            }
        }
    }



    /**
     * --------------------------retrofit--------------------------
     */
    public  void readNotice(String key){

        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        ReadNoticeReq request = new ReadNoticeReq();
        //获取参数
        request.setType(key);
        reqBody.readnotice = request;
        reqEnvelop.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().read_notice(reqEnvelop);
        call.enqueue(this);
    }


    /**
     * @param is 显示 true: showProgressDialog("正在登录"); false:null
     * @param loadType 上啦，下拉
     */
    public void refresh(boolean is,String loadType){
        if (loadType.equals(TagFinal.REFRESH)){
            page=0;
        }else{
            page++;
        }
        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        AttenGetUserListReq request = new AttenGetUserListReq();
        //获取参数
        request.setPage(page);

        reqBody.attenGetUserListReq = request;
        reqEnvelop.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().atten_get_user_list(reqEnvelop);
        call.enqueue(this);
//        Logger.e(reqEnvelop.toString());
        if (is) showProgressDialog("");
    }

    public void getCountNum()  {
        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        AttenGetCountReq request = new AttenGetCountReq();
        //获取参数

        reqBody.attenGetCountReq = request;
        reqEnvelop.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().atten_get_count(reqEnvelop);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();
        closeSwipeRefresh();

        ResEnv respEnvelope = response.body();
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.attenGetCountRes !=null){
                String result=b.attenGetCountRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                AttenRes res=gson.fromJson(result,AttenRes.class);
                if (StringJudge.isEmpty(res.getCount())){
                    coor_count.setVisibility(View.GONE);
                }else{
                    coor_count.setVisibility(View.VISIBLE);
                    if (res.getCount().length()>2) coor_count.setText("99");
                    coor_count.setText(res.getCount());
                    if (res.getCount().equals("0")) coor_count.setVisibility(View.GONE);
                }
            }
            if (b.attenGetUserListRes !=null){
                String result=b.attenGetUserListRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                AttenRes res=gson.fromJson(result,AttenRes.class);

                if (page==0){
                    mainBeens.clear();
                    mainBeens=res.getMaintains();
                }else{
                    if (res.getMaintains().size()==TagFinal.ZERO_INT)return ;
                    mainBeens.addAll(res.getMaintains());
                }
                adapter.setDataList(mainBeens);
                if (res.getMaintains().size()!=TagFinal.TEN_INT){
                    toastShow(R.string.success_loadmore_end);
                    adapter.setLoadState(3);
                }else{
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
        toastShow(R.string.fail_do_not);
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }



}
