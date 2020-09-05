package com.yfy.app.event;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.duty.bean.Week;
import com.yfy.app.event.bean.EventBean;
import com.yfy.app.event.bean.EventDep;
import com.yfy.app.event.bean.EventRes;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.event.EventGetWeekListReq;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventWeekActivity extends BaseActivity implements Callback<ResEnv> {
    private static final String TAG =EventWeekActivity.class.getSimpleName();

    @Bind(R.id.top_layout)
    View top_layout;
    @Bind(R.id.public_center_txt_add)
    TextView parent_name;

    private EventWeekAdapter adapter;
    private String week_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.duty_top_swip_recycler);
        parent_name.setBackground(getResources().getDrawable(R.drawable.ic_line_weight_black_24dp));
        parent_name.setTextSize(14f);
        parent_name.setTextColor(ColorRgbUtil.getBaseColor());
        parent_name.setText("");
        initSQToolbar();
        initRecycler();
        getDataEvent(false,week_id);
    }
    private TextView menuOne;
    private void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle("周报");
        menuOne=toolbar.addMenuText(TagFinal.ONE_INT, "全部");
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (position){
                    case TagFinal.ONE_INT:
                        Intent intent=new Intent(mActivity,ChoiceWeekActivity.class);
                        startActivityForResult(intent,TagFinal.UI_TAG);
                        break;

                }
            }
        });
    }

    private SwipeRefreshLayout swipeRefreshLayout;
    public RecyclerView recyclerView;
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
                getDataEvent(false,week_id);

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(onScrollListener);
        //添加分割线

        adapter=new EventWeekAdapter(this);
        recyclerView.setAdapter(adapter);

    }


    private RecyclerView.OnScrollListener onScrollListener=new RecyclerView.OnScrollListener() {
        // 用来标记是否正在向上滑动
        private boolean isSlidingUpward = false;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            isSlidingUpward = dy > 0;
            LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int first = manager.findFirstVisibleItemPosition();
            RecyclerView.ViewHolder holder;
            if (isSlidingUpward) {//list向上移动
                holder = recyclerView.findViewHolderForAdapterPosition(first + 1);
                if (holder != null) {
                    if (holder instanceof EventWeekAdapter.ParentViewHolder) {
                        int headerMoveY = holder.itemView.getTop() - top_layout.getMeasuredHeight();
                        if (headerMoveY < 0) {
                            top_layout.setTranslationY(headerMoveY);
                        }
                    }else if (holder instanceof EventWeekAdapter.ChildViewHolder) {
                        top_layout.setTranslationY(0);
                        parent_name.setText(((EventWeekAdapter.ChildViewHolder) holder).bean.getDep_name());
                    }
                }
            } else {
                holder = recyclerView.findViewHolderForAdapterPosition(first);
                if (holder != null) {
                    if (holder instanceof EventWeekAdapter.ChildViewHolder) {
                        parent_name.setText(((EventWeekAdapter.ChildViewHolder) holder).bean.getDep_name());
                    }
                }
            }
        }
    };



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_TAG://
                    if (data==null)return;
                    Week week=data.getParcelableExtra(TagFinal.OBJECT_TAG);
                    menuOne.setText(week.getWeektitle());
                    week_id=week.getWeekid();
                    getDataEvent(false,week_id);
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
    private void initAdapter(List<EventDep> list){
        datalist.clear();
        boolean first=true;
        for (EventDep info:list){
            datalist.add(new EventBean(TagFinal.TWO_INT,info.getDepartmentname()));
            initData(datalist,info.getEvenet_list(), info.getDepartmentname());
            if (first){
                parent_name.setText(info.getDepartmentname());
                first=false;
            }
        }
        adapter.setDataList(datalist);
        adapter.setLoadState(TagFinal.LOADING_COMPLETE);
    }

    private void initData(List<EventBean> datalist,List<EventBean> list,String dep_name){

        for(int i=0;i<list.size();i++){
            EventBean bean=list.get(i);
            bean.setView_type(TagFinal.ONE_INT);
            bean.setDep_name(dep_name);
            datalist.add(bean);
        }
    }





    /**
     * ----------------------------retrofit-----------------------
     */


    public void getDataEvent(boolean is,String week_id){

        if (week_id.isEmpty()){
            Intent intent=new Intent(mActivity,ChoiceWeekActivity.class);
            startActivityForResult(intent,TagFinal.UI_TAG);
            return;
        }


        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        EventGetWeekListReq req = new EventGetWeekListReq();
        //获取参数
        req.setWeekid(ConvertObjtect.getInstance().getInt(week_id));
        req.setDepid(0);

        reqBody.eventGetWeekListReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().event_get_week_list(reqEnv);
        call.enqueue(this);

        if (is)showProgressDialog("");
    }



    @Override
    public void onResponse(@NonNull Call<ResEnv> call, @NonNull Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();
        closeSwipeRefresh();

        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.eventGetWeekListRes !=null){
                String result=b.eventGetWeekListRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                EventRes res=gson.fromJson(result,EventRes.class);
                if (res.getResult().equals(TagFinal.TRUE)){
                    initAdapter(res.getEvent_deplist());
                }else{
                    toast(res.getError_code());
                }
            }
        }else{
            Logger.e(StringUtils.getTextJoint("%1$s:%2$d",name,response.code()));
            toastShow(StringUtils.getTextJoint("数据错误:%1$d",response.code()));
        }

    }

    @Override
    public void onFailure(@NonNull Call<ResEnv> call,@NonNull Throwable t) {
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
