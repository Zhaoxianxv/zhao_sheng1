package com.yfy.app.duty;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.zhao_sheng.R;
import com.yfy.app.duty.adpater.ChoiceTimeAdapter;
import com.yfy.app.duty.bean.DutyRes;
import com.yfy.app.duty.bean.PlaneInfo;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.duty.DutyPlaneReq;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.DateUtils;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.RecycleViewDivider;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DutyChoiceTimeActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = DutyChoiceTimeActivity.class.getSimpleName();
    private ChoiceTimeAdapter adapter;

    private PlaneInfo planeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_main);
        getData();
        initRecycler();
    }

    private String type_id;
    private void getData(){
        Intent data=getIntent();
        type_id=data.getStringExtra(TagFinal.TYPE_TAG );
        planeInfo=new PlaneInfo();
        initSQtoolbar();
    }
    private void initSQtoolbar() {
        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH);
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        getPlane(DateUtils.getDate(year, month, 1),type_id);
        planeInfo.setDate(DateUtils.getDate(year, month));
        assert toolbar!=null;
        toolbar.setTitle("值周日期");
        toolbar.addMenuText(TagFinal.TWO_INT,R.string.ok);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(final View view, int position) {
                switch (position){

                    case TagFinal.TWO_INT:
                        ArrayList<PlaneInfo> plans=adapter.getDataList();
                        if (StringJudge.isEmpty(plans)){
                            toastShow("未选取值周时间");
                        }else{
                            Intent data=new Intent();
                            data.putParcelableArrayListExtra(TagFinal.OBJECT_TAG,plans);
                            setResult(RESULT_OK,data);
                            finish();
                        }
                        break;
                }

            }
        });
    }

    private int month;
    private int year;
    private int day;
    @SuppressWarnings("ResourceType")
    public void showDatePickerDialog(Context mActivity, DatePickerDialog.OnDateSetListener liste){
        DatePickerDialog picker = new DatePickerDialog(
                mActivity,
                android.app.AlertDialog.THEME_HOLO_LIGHT,//样式
                liste ,
                year,
                month,
                day);
        picker.setCancelable(true);
        picker.setCanceledOnTouchOutside(true);
        picker.show();
        //只显示年月，隐藏掉日
        DatePicker dp = findDatePicker((ViewGroup) picker.getWindow().getDecorView());
        if (dp != null) {
            ((ViewGroup)((ViewGroup)dp.getChildAt(0)).getChildAt(0))
                    .getChildAt(2).setVisibility(View.GONE);
            //如果想隐藏掉年，将getChildAt(2)改为getChildAt(0)
        }
    }
    private DatePicker findDatePicker(ViewGroup group) {
        if (group != null) {
            for (int i = 0, j = group.getChildCount(); i < j; i++) {
                View child = group.getChildAt(i);
                if (child instanceof DatePicker) {
                    return (DatePicker) child;
                } else if (child instanceof ViewGroup) {
                    DatePicker result = findDatePicker((ViewGroup) child);
                    if (result != null)
                        return result;
                }
            }
        }
        return null;
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
//                getUserList(false);
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        closeSwipeRefresh();
                    }
                }, 1000);
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
        adapter=new ChoiceTimeAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setTimeChioce(new ChoiceTimeAdapter.TimeChioce() {
            @Override
            public void chioceTime() {
                showDatePickerDialog(mActivity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        planeInfo.setDate(DateUtils.getDate(year, month));
                        getPlane(DateUtils.getDate(year, month, 1),type_id);

                    }
                });
            }
        });

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




    private List<PlaneInfo> list=new ArrayList<>();







    /**
     * ----------------------------retrofit-----------------------
     */





    public void getPlane(String date,String type_id){
        ReqEnv evn = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        DutyPlaneReq req = new DutyPlaneReq();
        //获取参数
        req.setDate(date);
        req.setTypeid(ConvertObjtect.getInstance().getInt(type_id));
        reqBody.dutyPlaneReq = req;
        evn.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().duty_get_plane(evn);
        call.enqueue(this);
        showProgressDialog("");
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
            if (b.dutyPlaneRes!=null){
                String result=b.dutyPlaneRes.result;
                Logger.e(call.request().headers().toString()+result );
                list.clear();
                DutyRes infoRes=gson.fromJson(result,DutyRes.class);
                list.add(planeInfo);
                list.addAll(infoRes.getDutyreport_plane());
                adapter.setDataList(list);
                adapter.setLoadState(2);


            }


        }else{
            Logger.e( "evn: null" );
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
