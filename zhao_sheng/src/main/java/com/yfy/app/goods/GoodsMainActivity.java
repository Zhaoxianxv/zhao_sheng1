package com.yfy.app.goods;

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
import butterknife.Bind;
import butterknife.OnClick;
import com.example.zhao_sheng.R;
import com.yfy.app.goods.adapter.GoodsMainAdapter;
import com.yfy.app.goods.bean.GoodsBean;
import com.yfy.app.goods.bean.GoodsRes;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.goods.GoodGetRecordListUserReq;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;


public class GoodsMainActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = GoodsMainActivity.class.getSimpleName();


    private int pager=0;

    private GoodsMainAdapter adapter;
    private List<GoodsBean> users=new ArrayList<>();

    @Bind(R.id.coor_count)
    TextView count;
    @Bind(R.id.two_count)
    TextView count_two;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_coor_recycler_main);
        initSQtoolbar();
        initCollapsing();
        initToolbar();
        initRecycler();
        refresh(true,TagFinal.REFRESH);


    }
    private void initSQtoolbar() {
        assert toolbar!=null;
        toolbar.setTitle("办公用品申请");

    }


    /**
     * Toolbar 的NavigationIcon大小控制mipmap
     */
    public Toolbar mToolbar;
    public void initToolbar() {

        mToolbar =  findViewById(R.id.coor_top_text);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationIcon(null);  //隐藏掉返回键比如首页，可以调用
        mToolbar.setVisibility(View.GONE);

    }

    //配置CollapsingToolbarLayout布局
    public CollapsingToolbarLayout mCollapsingToolbarLayout;
    public void initCollapsing() {
        mCollapsingToolbarLayout =  findViewById(R.id.coor_main_collapsing);
        mCollapsingToolbarLayout.setTitle(" ");
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);//设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后Toolbar上字体的颜色
        mCollapsingToolbarLayout.setVisibility(View.GONE);

    }

    private SwipeRefreshLayout swipeRefreshLayout;
    public RecyclerView recyclerView;
    public AppBarLayout appBarLayout;
    public void initRecycler(){

        RelativeLayout one_layout=findViewById(R.id.coor_one_layout);
        RelativeLayout two_layout=findViewById(R.id.coor_two_layout);
        TextView one_name=findViewById(R.id.coor_one_name);
        TextView two_name=findViewById(R.id.coor_two_name);
        TextView detail_name=findViewById(R.id.coor_detail_name);

        two_name.setText("分管领导审核");
        one_name.setText("管理审核");
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


        appBarLayout.setVisibility(View.GONE);
        one_layout.setVisibility(View.GONE);
        two_layout.setVisibility(View.GONE);
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
                refresh(false,TagFinal.REFRESH);

            }
        });
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
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
                ColorRgbUtil.getGrayText()));
        adapter=new GoodsMainAdapter(this);
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
                    refresh(false,TagFinal.REFRESH);
                    break;
                case TagFinal.UI_ADMIN:
                    refresh(false,TagFinal.REFRESH);
                    break;
                case TagFinal.UI_ADD:
                    refresh(false,TagFinal.REFRESH);
                    break;
                case TagFinal.UI_CONTENT:
                    refresh(false,TagFinal.REFRESH);
                    break;

            }
        }

    }






    @OnClick(R.id.coor_one_layout)
    void setOneLayout(){
        Intent intent=new Intent(mActivity,GoodsADminActivity.class);
        startActivityForResult(intent,TagFinal.UI_ADMIN);
    }
    @OnClick(R.id.coor_two_layout)
    void setTwoLayout(){

        Intent inte=new Intent(mActivity,GoodsMasterActivity.class);
        startActivityForResult(inte,TagFinal.UI_CONTENT);
    }
    /**
     * ----------------------------retrofit-----------------------
     */


    public void refresh(boolean is,String loadType){
        if (loadType.equals(TagFinal.REFRESH)){
            pager=0;
        }else{
            adapter.setLoadState(TagFinal.LOADING);
            pager++;
        }

        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        GoodGetRecordListUserReq  req = new GoodGetRecordListUserReq();
        //获取参数
        req.setPage(pager);
        req.setSize(TagFinal.FIFTEEN_INT);

        reqBody.goodGetRecordListUserReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().goods_get_record_list_user(reqEnv);
        call.enqueue(this);
        if (is) showProgressDialog("");
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

            if (b.goodGetCountAdminRes !=null) {
                String result = b.goodGetCountAdminRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                GoodsRes infor=gson.fromJson(result,GoodsRes.class);
                if (StringJudge.isEmpty(infor.getCount())){
                    count.setVisibility(View.GONE);
                }else{
                    if (infor.getCount().equals("0")){
                        count.setVisibility(View.GONE);
                    }
                    count.setVisibility(View.VISIBLE);
                    if (infor.getCount().length()>2) count.setText("99");
                    count.setText(infor.getCount());
                }
            }
            if (b.goodGetRecordListUserRes !=null) {
                String result = b.goodGetRecordListUserRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                GoodsRes res=gson.fromJson(result,GoodsRes.class);
                if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
                    if (pager==0){
                        users.clear();
                        users=res.getOffice_supply_record();
                    }else{
                        users.addAll(res.getOffice_supply_record());
                    }
                    adapter.setDataList(users);
                    if (res.getOffice_supply_record().size()!=TagFinal.FIFTEEN_INT){
                        toastShow(R.string.success_loadmore_end);
                        adapter.setLoadState(3);
                    }else{
                        adapter.setLoadState(2);
                    }
                }else {
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
        Logger.e("onFailure  :"+call.request().headers().toString());
        dismissProgressDialog();
        closeSwipeRefresh();
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }


}
