package com.yfy.app.appointment;

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
import com.yfy.app.appointment.adpater.OrderMainAdapter;
import com.yfy.app.appointment.bean.OrderBean;
import com.yfy.app.appointment.bean.OrderRes;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.applied_order.OrderGetCountReq;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.applied_order.OrderGetUserListReq;
import com.yfy.app.net.base.ReadNoticeReq;
import com.yfy.base.activity.WcfActivity;
import com.yfy.db.UserPreferences;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
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

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends WcfActivity implements Callback<ResEnv> {

    private static final String TAG = OrderActivity.class.getSimpleName();
    private int pager=0;

    OrderMainAdapter adapter;
    private List<OrderBean> list=new ArrayList<>();

    @Bind(R.id.order_count)
    TextView count;


    @Bind(R.id.order_admin_do)
    RelativeLayout admin_layout;
    @Bind(R.id.order_maintain_do)
    RelativeLayout maintain_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_main);
        initSQToolbar();
        initToolbar();

        initCollapsing();
        initRecycler();
        initView();
        getCountNum();
        readNotice("function_room");
        refresh(true,TagFinal.REFRESH);
    }

    public void initView(){
        appBarLayout.setVisibility(View.VISIBLE);
        if (UserPreferences.getInstance().getUserAdmin().getIsfuncRoom().equals(TagFinal.TRUE)){
            admin_layout.setVisibility(View.VISIBLE);
            if (UserPreferences.getInstance().getUserAdmin().getIslogistics().equals(TagFinal.TRUE)){
                maintain_layout.setVisibility(View.VISIBLE);
            }else{
                maintain_layout.setVisibility(View.GONE);
            }
        }else{
            admin_layout.setVisibility(View.GONE);
            if (UserPreferences.getInstance().getUserAdmin().getIslogistics().equals(TagFinal.TRUE)){
                maintain_layout.setVisibility(View.VISIBLE);
            }else{
                appBarLayout.setVisibility(View.GONE);
            }
        }
    }
    private void initSQToolbar() {
        assert  toolbar!=null;
        toolbar.setTitle(R.string.order_room);
        toolbar.addMenuText(TagFinal.ONE_INT,R.string.add);
//        if (Variables.admin.getIslogistics().equals(TagFinal.TRUE)){
//            toolbar.addMenuText(TagFinal.TWO_INT,"后勤");
//        }
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (position){
                    case TagFinal.ONE_INT:
                        Intent intent=new Intent(mActivity,OrderApplicationActivity.class);
                        startActivityForResult(intent,TagFinal.UI_ADD);
                        break;
//                    case TagFinal.TWO_INT:
//                        Intent inte=new Intent(mActivity,OrderLogisticsActivity.class);
//                        startActivity(inte);
//                        break;
                }

            }
        });
    }

    /**
     * Toolbar 的NavigationIcon大小控制mipmap
     */
    private Toolbar mToolbar;
    public void initToolbar() {

        mToolbar = (Toolbar) findViewById(R.id.order_admin_top_text);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationIcon(null);  //隐藏掉返回键比如首页，可以调用
//        mToolbar.setNavigationOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        onBackPressed();
//                    }
//                });
    }

    //配置CollapsingToolbarLayout布局
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    public void initCollapsing() {
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.order_main_collapsing);
        mCollapsingToolbarLayout.setTitle(" ");
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);//设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后Toolbar上字体的颜色
    }

    private SwipeRefreshLayout swipeRefreshLayout;
    private AppBarLayout appBarLayout;
    private RecyclerView recyclerView;
    public void initRecycler(){
        recyclerView = (RecyclerView) findViewById(R.id.order_recycler);
        appBarLayout = (AppBarLayout) findViewById(R.id.order_appbar_layout);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.order_new_swip);
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
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#4DB6AC"));
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                refresh(false,TagFinal.REFRESH);
            }
        });
        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener() {
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
                ColorRgbUtil.getGrayText()));
        adapter=new OrderMainAdapter(this,list);
        recyclerView.setAdapter(adapter);

    }





    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        if (!isActivity())return false;
        closeSwipeRefresh();
        dismissProgressDialog();

        String name=wcfTask.getName();
        if (name.equals(TagFinal.REFRESH)){
            Logger.e("zxx"," "+result);
            OrderRes re=gson.fromJson(result,OrderRes.class);
            if (list.size()!=0){
                list.clear();
            }
            list=re.getAdmin();
            adapter.setDataList(list);
            adapter.setLoadState(2);
            return true;

        }
        if (name.equals(TagFinal.REFRESH_MORE)){
            OrderRes re=gson.fromJson(result,OrderRes.class);
            if (re.getAdmin().size()!=PAGE_NUM){
                toastShow(R.string.success_loadmore_end);
            }
            list.addAll(re.getAdmin());

            adapter.setDataList(list);
            adapter.setLoadState(2);
            return true;
        }

        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        if (!isActivity())return ;
        closeSwipeRefresh();
        dismissProgressDialog();
        toastShow(R.string.fail_loadmore);

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

//

    @OnClick(R.id.order_admin_do)
    void setAudit(){
        Intent intent=new Intent(mActivity,AdminOrderActivity.class);
        startActivityForResult(intent,TagFinal.UI_ADMIN);
    }
    @OnClick(R.id.order_maintain_do)
    void setMaintainDo(){

        Intent inte=new Intent(mActivity,OrderLogisticsActivity.class);
        startActivity(inte);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_ADD:
                    refresh(true,TagFinal.REFRESH);
                    break;
                case TagFinal.UI_ADMIN:
                    refresh(true,TagFinal.REFRESH);
                    getCountNum();
                    break;
            }
        }
    }

    /**
     * --------------------------retrofit------------------
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

    public void getCountNum()  {

        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        OrderGetCountReq req = new OrderGetCountReq();
        //获取参数




        reqBody.orderGetCountReq = req;
        reqEnvelop.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().order_get_count(reqEnvelop);

        call.enqueue(this);
//		showProgressDialog("正在登录");

    }



    /**
     * @param is 显示 true: showProgressDialog("正在登录"); false:null
     * @param loadType 上啦，下拉
     */

    public void refresh(boolean is,String loadType){

        if (loadType.equals(TagFinal.REFRESH)){
            pager=0;
        }else{
            pager++;
        }
        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        OrderGetUserListReq req = new OrderGetUserListReq();
        //获取参数
        req.setPage(pager);
        req.setStatus(-1);

        reqBody.orderGetUserListReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().order_get_user_list(reqEnv);
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

        ResEnv resEnv = response.body();
        if (resEnv != null) {
            ResBody b=resEnv.body;
            if (b.orderGetCountRes!=null){
                String result=b.orderGetCountRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                OrderRes res=gson.fromJson(result,OrderRes.class);
                if (StringJudge.isEmpty(res.getCount())){
                    count.setVisibility(View.GONE);
                }else{
                    count.setVisibility(View.VISIBLE);
                    if (res.getCount().length()>2) count.setText("99");
                    count.setText(res.getCount());
                    if (res.getCount().equals("0")) count.setVisibility(View.GONE);
                }
            }
            if (b.orderGetUserListRes!=null){
                String result=b.orderGetUserListRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                OrderRes res=gson.fromJson(result,OrderRes.class);
                if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
                    if (pager==0){
                        list.clear();
                        list=res.getAdmin();
                    }else{
                        list.addAll(res.getAdmin());
                    }
                    adapter.setDataList(list);
                    if (res.getAdmin().size()!=TagFinal.FIFTEEN_INT){
                        toastShow(R.string.success_loadmore_end);
                        adapter.setLoadState(3);
                    }else{
                        adapter.setLoadState(2);
                    }
                }else {
                    toastShow(res.
                            getError_code());
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
        toast(R.string.fail_do_not);
        closeSwipeRefresh();
        dismissProgressDialog();
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }


}
