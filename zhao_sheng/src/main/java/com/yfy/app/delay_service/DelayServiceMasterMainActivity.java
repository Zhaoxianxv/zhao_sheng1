package com.yfy.app.delay_service;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.bean.DateBean;
import com.yfy.app.delay_service.adapter.DelayServiceMasterMainAdapter;
import com.yfy.app.delay_service.bean.DelayAbsenteeismClass;
import com.yfy.app.delay_service.bean.DelayServiceRes;
import com.yfy.app.duty.adpater.DutyMainAdapter;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.delay_service.DelayAdminGetClassListReq;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.dialog.CPWListView;
import com.yfy.final_tag.dialog.ConfirmContentWindow;
import com.yfy.final_tag.dialog.DialogTools;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.CallPhone;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.permission.PermissionFail;
import com.yfy.final_tag.permission.PermissionGen;
import com.yfy.final_tag.permission.PermissionSuccess;
import com.yfy.final_tag.permission.PermissionTools;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DelayServiceMasterMainActivity extends BaseActivity implements Callback<ResEnv> {
    private static final String TAG = DelayServiceMasterMainActivity.class.getSimpleName();


    private DateBean dateBean;
    private DelayServiceMasterMainAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delay_admin_main);
        getData();
        dateBean=new DateBean();
        dateBean.setValue_long(System.currentTimeMillis(),true);
        week_name=dateBean.getWeekOfDate();
        week_num.setText(week_name);
        date_button.setText(dateBean.getName());

        initListDialog();
        initDialog();
        getWeekList();
        initRecycler();
        initSQtoobar("课后服务管理");
    }
    private String admin_right="",admin_add=TagFinal.FALSE,admin_manage=TagFinal.FALSE;
    public void getData(){
        Intent intent=getIntent();
        admin_right=intent.getStringExtra(Base.content);
        //权限
        List<String> adminList=StringUtils.listToStringSplitCharacters(admin_right,",");
        for (String s:adminList){
            switch (s){
                case "view":
                    break;
                case "add":
                    admin_add=TagFinal.TRUE;
                    break;
                case "admin":
                    admin_manage=TagFinal.TRUE;
                    break;
            }
        }
    }



    private void initSQtoobar(String title) {
        assert toolbar!=null;
        toolbar.setTitle(title);
        toolbar.setOnNaviClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.addMenuText(TagFinal.ONE_INT, "巡查记录");
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {


                Intent intent=new Intent(mActivity,DelayMasterAdminListActivity.class);
                startActivityForResult(intent, TagFinal.UI_ADD);

            }
        });
    }


    private String[] weekOfDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    private CPWListView cpwListView;
    private List<String> names=new ArrayList<>();
    private void initListDialog(){
        cpwListView = new CPWListView(mActivity);
        names=Arrays.asList(weekOfDays);
        cpwListView.setDatas(names);
        cpwListView.setOnPopClickListenner(new CPWListView.OnPopClickListenner() {

            @Override
            public void onClick(int index, String type) {
                week_name=names.get(index);
                week_num.setText(week_name);
                getWeekList();
                cpwListView.dismiss();
            }
        });

    }


    @Bind(R.id.public_bottom_button)
    Button week_num;
    @Bind(R.id.public_bottom_button_two)
    Button date_button;
    @OnClick(R.id.public_bottom_button)
    void setDateBean(){
        cpwListView.showAtCenter();
    }

    @OnClick(R.id.public_bottom_button_two)
    void setDelayDetail(){
        DialogTools
                .getInstance()
                .showDatePickerDialog(mActivity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        Date time=new Date( year-1900, month, dayOfMonth);
                        dateBean.setValue_long(time.getTime());
                        date_button.setText(dateBean.getName());

                        week_name=dateBean.getWeekOfDate();
                        week_num.setText(week_name);
                        getWeekList();
                    }
                });

    }


    @Bind(R.id.public_top_layout)
    View top_layout;
    @Bind(R.id.public_top_txt)
    TextView parent_name;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    public void initRecycler(){
        parent_name.setBackgroundColor(ColorRgbUtil.getWhite());
        recyclerView = (RecyclerView) findViewById(R.id.public_top_recycler);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.public_swip);
        //AppBarLayout 展开执行刷新
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(ColorRgbUtil.getBaseColor());
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                refresh(false,startdate ,enddate , TagFinal.REFRESH);
                getWeekList();
            }
        });


        recyclerView.addOnScrollListener(onScrollListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter=new DelayServiceMasterMainAdapter(this);
        recyclerView.setAdapter(adapter);


        adapter.setDotype(new DelayServiceMasterMainAdapter.Dotype() {
            @Override
            public void tellPhone(String phone) {
                phone_s=phone;
                showDialog("拨打电话",phone ,"确定" );
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_ADD:
                    getWeekList();
                    break;
            }
        }
    }


    /**
     * ----------------------------retrofit-----------------------
     */


    private String week_name;
    public void getWeekList(){

        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        DelayAdminGetClassListReq req = new DelayAdminGetClassListReq();
        //获取参数
        req.setDate(dateBean.getValue());
        req.setWeek(week_name);

        reqBody.delayAdminGetClassListReq =req;
        reqEnvelop.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().delay_admin_get_class_list(reqEnvelop);
        call.enqueue(this);
        Logger.e(reqEnvelop.toString());

        showProgressDialog("");
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
            if (b.delayAdminGetClassListRes !=null){
                String result=b.delayAdminGetClassListRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                DelayServiceRes res=gson.fromJson(result, DelayServiceRes.class);
                if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
                    adater_list_class=res.getElective_list();
                    adapter.setDataList(adater_list_class);
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




    private List<DelayAbsenteeismClass> adater_list_class=new ArrayList<>();
    private List<DelayAbsenteeismClass> adater_list_tea=new ArrayList<>();

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






    private String phone_s;
    private ConfirmContentWindow confirmContentWindow;
    private void initDialog(){
        confirmContentWindow = new ConfirmContentWindow(mActivity);
        confirmContentWindow.setOnPopClickListenner(new ConfirmContentWindow.OnPopClickListenner() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.pop_dialog_title:
                        break;
                    case R.id.pop_dialog_content:
                        break;
                    case R.id.pop_dialog_ok:
                        PermissionTools.tryTellPhone(mActivity);
                        break;
                }
            }
        });
    }
    private void showDialog(String title,String content,String ok){
        if (confirmContentWindow==null)return;
        confirmContentWindow.setTitle_s(title,content,ok);
        confirmContentWindow.showAtCenter();
    }
    @PermissionSuccess(requestCode = TagFinal.CALL_PHONE)
    public void call(){
        CallPhone.callDirectly(mActivity, phone_s);
    }
    @PermissionFail(requestCode = TagFinal.CALL_PHONE)
    public void callFail(){
        Toast.makeText(getApplicationContext(), R.string.permission_fail_call, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

}
