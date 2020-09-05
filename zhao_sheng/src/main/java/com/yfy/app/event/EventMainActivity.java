package com.yfy.app.event;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.event.bean.EventBean;
import com.yfy.app.event.bean.EventInfo;
import com.yfy.app.event.bean.EventRes;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.event.EventGetMainListReq;
import com.yfy.base.activity.BaseActivity;
import com.yfy.db.UserPreferences;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.EndlessRecyclerOnScrollListener;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventMainActivity extends BaseActivity implements Callback<ResEnv> {
    private static final String TAG =EventMainActivity.class.getSimpleName();


    private EventMainAdapter adapter;
    private int pager=0;

    @Bind(R.id.public_deleted_text)
    TextView del_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_delete_main);
        initSQToolbar();
        initRecycler();
        getDataEvent(true,TagFinal.REFRESH);
    }
    private TextView onemenu;
    private void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle("大事记");
        toolbar.addMenuText(TagFinal.ONE_INT, "周报");
        onemenu=toolbar.addMenuText(TagFinal.TWO_INT, "添加");
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent;
                switch (position){
                    case TagFinal.ONE_INT:
                        intent=new Intent(mActivity,EventWeekActivity.class);
                        startActivity(intent);
                        break;
                    case TagFinal.TWO_INT:
                        intent=new Intent(mActivity,EventAddActivity.class);
                        startActivityForResult(intent,TagFinal.UI_ADD);
                        break;
                }
            }
        });
    }

    @OnClick(R.id.public_deleted_text)
    void setDel_view(){
        Intent intent=new Intent(mActivity,EventMyActivity.class);
        startActivityForResult(intent,TagFinal.UI_ADMIN);
    }

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    public void initRecycler(){

        if (UserPreferences.getInstance().getUserAdmin().getIseventadmin().equals(TagFinal.TRUE)){
            del_view.setVisibility(View.VISIBLE);
            onemenu.setVisibility(View.VISIBLE);
        }else{
            del_view.setVisibility(View.GONE);
            onemenu.setVisibility(View.GONE);
        }
        del_view.setTextColor(ColorRgbUtil.getGreen());

        del_view.setText("查看我的记录");
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
                getDataEvent(false,TagFinal.REFRESH);

            }
        });
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                getDataEvent(false,TagFinal.REFRESH_MORE);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线

        adapter=new EventMainAdapter(this);
        recyclerView.setAdapter(adapter);

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_ADD://add refresh
                    getDataEvent(false,TagFinal.REFRESH);
                    break;
                case TagFinal.UI_ADMIN://add refresh
                    getDataEvent(false,TagFinal.REFRESH);
                    break;
            }
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
    private List<EventBean> datalist = new ArrayList<>();
    private void initAdapter(boolean is,List<EventInfo> list){
        if (is){
            datalist.clear();
        }
        for (EventInfo info:list){
            initData(datalist,info.getEvent_list(),info.getDate(),info.getWeekname(),info.getTermname());
        }
        adapter.setDataList(datalist);
        adapter.setLoadState(TagFinal.LOADING_COMPLETE);
    }

    private void initData(List<EventBean> datalist,List<EventBean> list,String date,String weekname,String term){

        for(int i=0;i<list.size();i++){
            EventBean bean=list.get(i);
            if (i==0){
                bean.setIs(true);
            }else{
                bean.setIs(false);
            }
            bean.setView_type(TagFinal.ONE_INT);
            bean.setDate(date);
            bean.setWeek_name(weekname);
            bean.setTerm_name(term);
            datalist.add(bean);
        }
    }






    /**
     * ----------------------------retrofit-----------------------
     */




    public void getDataEvent(boolean is,String typeload){

        if (typeload.equals(TagFinal.REFRESH)){
            pager=0;
        }else{
            pager++;
        }

        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        EventGetMainListReq req = new EventGetMainListReq();
        req.setEdate("");
        req.setSdate("");
        req.setDepid(0);
        req.setWeekid(0);
        req.setPage(pager);
        req.setSize(TagFinal.FIFTEEN_INT);
        //获取参数
        reqBody.eventGetMainListReq = req;
        reqEnvelop.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().event_get_main_data(reqEnvelop);
        call.enqueue(this);
        if (is)showProgressDialog("");
    }

    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();
        closeSwipeRefresh();
        if (response.code()==500){
            toastShow("数据出差了");
        }
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.eventGetMainListRes !=null){
                String result=b.eventGetMainListRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                EventRes res=gson.fromJson(result,EventRes.class);
                if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){

                    if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
                        if (pager==0){
                            initAdapter(true, res.getDate_list() );
                        }else{
                            initAdapter(false, res.getDate_list());
                        }
                        if (res.getDate_list().size()!=TagFinal.FIFTEEN_INT){
                            toastShow(R.string.success_loadmore_end);
                            adapter.setLoadState(3);
                        }else{
                            adapter.setLoadState(2);
                        }
                    }else {
                        toastShow(res.getError_code());
                    }
                }else{
                    toast(res.getError_code());
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
        dismissProgressDialog();
        closeSwipeRefresh();
    }


    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }



}
