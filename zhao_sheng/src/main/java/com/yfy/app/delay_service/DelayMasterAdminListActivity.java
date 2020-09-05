package com.yfy.app.delay_service;

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
import com.yfy.app.bean.DateBean;
import com.yfy.app.delay_service.adapter.DelayMasterAdminAdapter;
import com.yfy.app.delay_service.bean.DelayEventBean;
import com.yfy.app.delay_service.bean.DelayServiceRes;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.delay_service.DelayAdminGetTodayListReq;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.dialog.ConfirmDateWindow;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.RecycleViewDivider;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DelayMasterAdminListActivity extends BaseActivity implements Callback<ResEnv> {
    private static final String TAG = DelayMasterAdminListActivity.class.getSimpleName();



    public List<DelayEventBean> delayEventBeans=new ArrayList<>();
    public DelayMasterAdminAdapter adapter;
    @Bind(R.id.public_deleted_text)
    TextView del_bottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_delete_main);
        initDialog();
        del_bottom.setText("巡查");
        del_bottom.setTextColor(ColorRgbUtil.getMaroon());
        initSQToobar("巡查记录");
        initRecycler();
        getAdminList();
    }


   private TextView onemenu;
    public void initSQToobar(String title) {
        assert toolbar!=null;
        toolbar.setTitle(title);
        toolbar.setOnNaviClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
        onemenu=toolbar.addMenuText(TagFinal.ONE_INT,"" );
        onemenu.setText(dateBean.getName());
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                dialog.showAtBottom();
            }
        });

    }

    @OnClick(R.id.public_deleted_text)
    void setDelay(){
        Intent intent=new Intent(mActivity,DelayServiceSetActivity.class);
        startActivityForResult(intent, TagFinal.UI_ADD);
    }

    private DateBean dateBean;
    private ConfirmDateWindow dialog;
    private void initDialog(){
        dateBean=new DateBean();
        dateBean.setValue_long(System.currentTimeMillis(),true);
        dialog = new ConfirmDateWindow(mActivity);
        dialog.setOnPopClickListenner(new ConfirmDateWindow.OnPopClickListenner() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.set:
                        dateBean.setValue(dialog.getTimeValue());
                        dateBean.setName(dialog.getTimeName());
                        onemenu.setText(dateBean.getName());
                        getAdminList();
                        dialog.dismiss();
                        break;
                    case R.id.cancel:
                        dialog.dismiss();
                        break;
                }

            }
        });
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
                closeSwipeRefresh();
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
        adapter=new DelayMasterAdminAdapter(mActivity);
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
                case TagFinal.UI_ADD:
                    getAdminList();
                    break;
            }
        }
    }


    /**
     * ----------------------------------------retrofit-------------
     */


    public void getAdminList(){

        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        DelayAdminGetTodayListReq req = new DelayAdminGetTodayListReq();
        //获取参数
        req.setDate(dateBean.getValue());

        reqBody.delayAdminGetTodayListReq =req;
        reqEnvelop.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().delay_admin_get_today_list(reqEnvelop);
        call.enqueue(this);

        Logger.e(reqEnvelop.toString());
        showProgressDialog("");
    }


    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();
        ResEnv respEnvelope = response.body();
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.delayAdminGetTodayListRes !=null) {
                String result = b.delayAdminGetTodayListRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                DelayServiceRes res=gson.fromJson(result, DelayServiceRes.class);
                if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
                    delayEventBeans=res.getElective_admin();
                    if (StringJudge.isEmpty(delayEventBeans)){
                        toastShow("暂无巡查");
                    }
                    adapter.setDataList(delayEventBeans);
                    adapter.setLoadState(TagFinal.LOADING_END);
                    adapter.setDateBean(dateBean);
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
        Logger.e("onFailure  :"+call.request().headers().toString());
        toastShow(R.string.fail_do_not);
        dismissProgressDialog();
        closeSwipeRefresh();
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
