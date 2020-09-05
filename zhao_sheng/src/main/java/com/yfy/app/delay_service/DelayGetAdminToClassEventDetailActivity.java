package com.yfy.app.delay_service;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.bean.DateBean;
import com.yfy.app.delay_service.adapter.ChoiceAbsentAdapter;
import com.yfy.app.delay_service.bean.AbsStu;
import com.yfy.app.delay_service.bean.AbsentInfo;
import com.yfy.app.delay_service.bean.EventClass;
import com.yfy.app.delay_service.bean.EventRes;
import com.yfy.app.duty.adpater.DutyMainAdapter;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.delay_service.DelayAdminGetToClassEventDetailReq;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.dialog.CPWBean;
import com.yfy.final_tag.dialog.CPWListBeanView;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DelayGetAdminToClassEventDetailActivity extends BaseActivity implements Callback<ResEnv> {
    private static final String TAG = DelayGetAdminToClassEventDetailActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delay_admin_main);
        initView();
        initRecycler();
        initSQtoobar("行政考勤");
        initDialogList();
        getdata();


    }



    private void initView(){
        button_left.setVisibility(View.GONE);
        button_right.setVisibility(View.GONE);
    }
    private DateBean dateBean;
    private String Electiveid;
    private ArrayList<AbsStu> tea_list=new ArrayList<>();
    private void getdata(){

        Intent intent=getIntent();
        dateBean=intent.getParcelableExtra(Base.date);
        Electiveid =intent.getStringExtra(Base.id);
        tea_list=intent.getParcelableArrayListExtra(Base.data);
        selcet_CPWBean=intent.getParcelableExtra(Base.state);


        if (StringJudge.isEmpty(tea_list)){
            toast("班级尚未安排老师");
            return;
        }else {
            setCPWlListBeanData();
            menu_one.setText("选择老师");
        }


        if (selcet_CPWBean==null) {

        }else{
            menu_one.setText(selcet_CPWBean.getName());
            getAdminToClassEvent();
        }
    }



    private TextView menu_one;
    private void initSQtoobar(String title) {
        assert toolbar!=null;
        toolbar.setTitle(title);
        menu_one=toolbar.addMenuText(TagFinal.ONE_INT,"");
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (cpwListBeanView!=null)
                    cpwListBeanView.showAtCenter();
            }
        });

    }



    private CPWListBeanView cpwListBeanView;
    List<CPWBean> cpwBeans=new ArrayList<>();
    private CPWBean selcet_CPWBean;
    private void setCPWlListBeanData(){
        if (StringJudge.isEmpty(tea_list)){
            toast(R.string.success_not_details);
            return;
        }else{
            cpwBeans.clear();
            for(AbsStu opear:tea_list){
                CPWBean cpwBean =new CPWBean();
                cpwBean.setName(opear.getTeachername());
                cpwBean.setId(opear.getTeacherid());
                cpwBeans.add(cpwBean);
            }
        }
        closeKeyWord();
        cpwListBeanView.setDatas(cpwBeans);

    }
    private void initDialogList(){
        cpwListBeanView = new CPWListBeanView(mActivity);
        cpwListBeanView.setOnPopClickListenner(new CPWListBeanView.OnPopClickListenner() {
            @Override
            public void onClick(CPWBean cpwBean,String type) {
                menu_one.setText(cpwBean.getName());
                selcet_CPWBean=cpwBean;
                getAdminToClassEvent();
                cpwListBeanView.dismiss();
            }
        });
    }





    @Bind(R.id.public_bottom_button)
    Button button_left;
    @Bind(R.id.public_bottom_button_two)
    Button button_right;
    @OnClick(R.id.public_bottom_button)
    void setButtonLeft(){
    }

    @OnClick(R.id.public_bottom_button_two)
    void setButtonRight(){

    }


    @Bind(R.id.public_top_layout)
    View top_layout;
    @Bind(R.id.public_top_txt)
    TextView parent_name;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    public void initRecycler(){
        parent_name.setBackgroundColor(ColorRgbUtil.getWhite());
        recyclerView =  findViewById(R.id.public_top_recycler);
        swipeRefreshLayout =  findViewById(R.id.public_swip);
        //AppBarLayout 展开执行刷新
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(ColorRgbUtil.getBaseColor());
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            }
        });


        recyclerView.addOnScrollListener(onScrollListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter=new ChoiceAbsentAdapter(this);
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
            RecyclerView.ViewHolder holder;
            if (isSlidingUpward){
                holder = recyclerView.findViewHolderForAdapterPosition(first+1);
                if (holder!=null){
                    if (holder instanceof DutyMainAdapter.TopViewHolder){
                        int headerMoveY = holder.itemView.getTop() - top_layout.getMeasuredHeight();
                        if (headerMoveY<0) {
                            top_layout.setTranslationY(headerMoveY);
                        }
                    } else if (holder instanceof DutyMainAdapter.ParentViewHolder){
                        top_layout.setTranslationY(0);
                        parent_name.setText(((DutyMainAdapter.ParentViewHolder) holder).bean.getDate());
                    }else if (holder instanceof DutyMainAdapter.ChildViewHolder){
                        top_layout.setTranslationY(0);
                        parent_name.setText(((DutyMainAdapter.ChildViewHolder) holder).bean.getDate());
                    }
                }
            }else{
                holder = recyclerView.findViewHolderForAdapterPosition(first);
                if (holder!=null){
                    if (holder instanceof DutyMainAdapter.ChildViewHolder){
                        parent_name.setText(((DutyMainAdapter.ChildViewHolder) holder).bean.getDate());
                    }
                }
            }

        }
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
            // 当不滑动时
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                // 获取最后一个完全显示的itemPosition
                int lastItemPosition = manager.findLastCompletelyVisibleItemPosition();
                int itemCount = manager.getItemCount();

                // 判断是否滑动到了最后一个item，并且是向上滑动
                if (lastItemPosition == (itemCount - 1) && isSlidingUpward) {
                    // 加载更多
//                    refresh(false,startdate ,enddate , TagFinal.REFRESH_MORE);
                }
            }
        }
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


    /**
     * ----------------------------retrofit-----------------------
     */



    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }


    private void getAdminToClassEvent(){
        if (selcet_CPWBean==null){
            toast("未选择教师");
            if (cpwListBeanView!=null)
                cpwListBeanView.showAtCenter();
            return;
        }
        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        DelayAdminGetToClassEventDetailReq req = new DelayAdminGetToClassEventDetailReq();
        //获取参数

        req.setElectiveid(ConvertObjtect.getInstance().getInt(Electiveid));
        req.setTeacherid(ConvertObjtect.getInstance().getInt(selcet_CPWBean.getId()));
        req.setDate(dateBean.getValue());
        reqBody.delayAdminGetToClassEventDetailReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().delay_admin_get_to_class_detail(reqEnv);
        call.enqueue(this);
        showProgressDialog("");
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
            if (b.delayAdminGetToClassEventDetailRes !=null) {
                String result = b.delayAdminGetToClassEventDetailRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                EventRes res=gson.fromJson(result,EventRes.class );
                if (res.getResult().equals(TagFinal.TRUE)){
                    initCeate(res);
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
    public void onFailure(Call<ResEnv> call, Throwable t) {
        if (!isActivity())return;
        Logger.e("onFailure  :"+call.request().headers().toString());
        dismissProgressDialog();
        closeSwipeRefresh();
        toast(R.string.fail_do_not);
    }




    private List<EventClass> datalist=new ArrayList<>();
    private ChoiceAbsentAdapter adapter;
    private void initCeate(EventRes res){
        datalist.clear();
        if (StringJudge.isEmpty(res.getElective_list())){
            toast(R.string.success_not_more);
            EventClass class_b=new EventClass();
            class_b.setElectivename("班级考勤");
            class_b.setView_type(TagFinal.TYPE_PARENT);
            datalist.add(class_b);
            EventClass class_=new EventClass();
            class_.setElectivename("异常考勤");
            class_.setView_type(TagFinal.TYPE_PARENT);
            datalist.add(class_);

            adapter.setDataList(datalist);
            adapter.setLoadState(TagFinal.LOADING_COMPLETE);
            return ;
        }

        for (AbsentInfo info:res.getElective_list()){
            createClass(info.getElectiveid(),info.getElective_classdetail());
            createBean(info.getElectiveid(),info.getElective_stuetail());

        }

        adapter.setDataList(datalist);
        adapter.setLoadState(TagFinal.LOADING_COMPLETE);
    }
    private void createClass(String parennt_id,List<EventClass> list){
        EventClass class_b=new EventClass();
        class_b.setElectivename("班级考勤");
        class_b.setView_type(TagFinal.TYPE_PARENT);
        datalist.add(class_b);
        for (EventClass bean:list){
            bean.setView_type(TagFinal.TYPE_ITEM);
            bean.setElectiveid(parennt_id);
            datalist.add(bean);
        }
    }
    private void createBean(String parennt_id,List<EventClass> list){
        EventClass class_b=new EventClass();
        class_b.setElectivename("异常考勤");
        class_b.setView_type(TagFinal.TYPE_PARENT);
        datalist.add(class_b);
        for (EventClass bean:list){
            bean.setView_type(TagFinal.TYPE_CHILD);
            bean.setElectiveid(parennt_id);
            datalist.add(bean);
        }
    }
}
