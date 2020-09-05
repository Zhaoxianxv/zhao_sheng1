package com.yfy.app.duty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.duty.adpater.DutyDateAdapter;
import com.yfy.app.duty.bean.DutyRes;
import com.yfy.app.duty.bean.Week;
import com.yfy.app.duty.bean.WeekBean;
import com.yfy.app.duty.bean.WeekInfo;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.duty.WeekAllReq;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.RecycleViewDivider;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DutyDateActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = DutyDateActivity.class.getSimpleName();


    private DutyDateAdapter adapter;
    @Bind(R.id.top_layout)
    View top_layout;
    @Bind(R.id.public_center_txt_add)
    TextView parent_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.duty_top_swip_recycler);
        parent_name.setBackgroundColor(ColorRgbUtil.getWhite());
        parent_name.setTextSize(14f);
        initRecycler();
        initSQtoolbar();
        getWeekAll(true);

    }


    private void initSQtoolbar() {
        assert toolbar!=null;
        toolbar.setTitle("选择时间");
        toolbar.addMenuText(TagFinal.ONE_INT,"全部时间" );
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent =new Intent();
                WeekBean bean=new WeekBean();
                bean.setWeekname("全部时间");
                bean.setStartdate("");
                bean.setEnddate("");
                intent.putExtra(TagFinal.TYPE_TAG,false);
                intent.putExtra(TagFinal.OBJECT_TAG,bean);
                setResult(Activity.RESULT_OK,intent);
                finish();
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
                getWeekAll(false);

            }
        });

        recyclerView.addOnScrollListener(onScrollListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        xlist.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mActivity,
                LinearLayoutManager.HORIZONTAL,
                1,
                getResources().getColor(R.color.gray)));
        adapter=new DutyDateAdapter(this);
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
            int first=manager.findFirstVisibleItemPosition();
            RecyclerView.ViewHolder holder=recyclerView.findViewHolderForAdapterPosition(first+1);
            if (holder!=null){
                if (holder instanceof DutyDateAdapter.ParentViewHolder){
                    int headerMoveY = holder.itemView.getTop() - top_layout.getMeasuredHeight();
                    WeekBean week=((DutyDateAdapter.ParentViewHolder) holder).bean;
                    parent_name.setText(week.getTermname());
                    if (headerMoveY<0) {
                        top_layout.setTranslationY(headerMoveY);
                    }
                    if (week.getIsCurrentTerm().equals(TagFinal.FALSE)){
                        parent_name.setTextColor(ColorRgbUtil.getGrayText());
                    }else{
                        parent_name.setTextColor(ColorRgbUtil.getBaseColor());
                    }
                } else if (holder instanceof DutyDateAdapter.ChildViewHolder){
                    top_layout.setTranslationY(0);
                    parent_name.setTextColor(ColorRgbUtil.getGrayText());
                    parent_name.setText(((DutyDateAdapter.ChildViewHolder) holder).bean.getTermname());
                }else{
                    top_layout.setTranslationY(0);
                }
            }
        }
//        @Override
//        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//            super.onScrollStateChanged(recyclerView, newState);
//            LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
//            // 当不滑动时
//            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                // 获取最后一个完全显示的itemPosition
//                int lastItemPosition = manager.findLastCompletelyVisibleItemPosition();
//                int itemCount = manager.getItemCount();
//
//                // 判断是否滑动到了最后一个item，并且是向上滑动
//                if (lastItemPosition == (itemCount - 1) && isSlidingUpward) {
//                    // 加载更多
//                    refresh(false,startdate ,enddate , TagFinal.REFRESH_MORE);
//                }
//            }
//        }


    };





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


    private void initData(List<WeekInfo> infos){
        List<WeekBean> weekBeans=new ArrayList<>();

        for (WeekInfo info:infos){
            weekBeans.add(new WeekBean(info.getStartdate(),info.getEnddate() ,info.getTermid() ,info.getTermname() ,info.getIsCurrentTerm() ));
            initBean(weekBeans,info.getWeeks() ,info.getTermname());
        }
        adapter.setDataList(weekBeans);
        adapter.setLoadState(TagFinal.LOADING_COMPLETE);
    }

    private void initBean(List<WeekBean> data,List<Week> list,String termname){
        for (Week week:list){
            WeekBean bean=new WeekBean();
            bean.setType(TagFinal.TWO_INT);
            bean.setWeekid(week.getWeekid());
            bean.setWeekname(week.getWeekname());
            bean.setStartdate(week.getStartdate());
            bean.setEnddate(week.getEnddate());
            bean.setTermname(termname);
            data.add(bean);
        }
    }



    /**
     * ----------------------------retrofit-----------------------
     */

    public void getWeekAll(boolean is) {

        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        WeekAllReq request = new WeekAllReq();
        //获取参数
        reqBody.weekAllReq = request;
        reqEnvelop.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().get_week_all(reqEnvelop);
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

        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.week_all_sponse!=null){
                String result=b.week_all_sponse.weekRes;
                Logger.e(TagFinal.ZXX, "onResponse: "+result );
                DutyRes info=gson.fromJson(result,DutyRes.class );
                initData(info.getTerms());
//                list=info.getDutyreport_type();

            }


        }else{
            Logger.e(TagFinal.ZXX, "onResponse: 22" );
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
