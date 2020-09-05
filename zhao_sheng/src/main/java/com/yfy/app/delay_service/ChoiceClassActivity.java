package com.yfy.app.delay_service;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.bean.DateBean;
import com.yfy.app.delay_service.adapter.ChoiceClassAdapter;
import com.yfy.app.delay_service.adapter.EventMainListViewAdapter;
import com.yfy.app.delay_service.bean.EventGrade;
import com.yfy.app.delay_service.bean.DelayServiceRes;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.delay_service.DelayGetClassListReplaceReq;
import com.yfy.app.net.delay_service.DelayGetClassListTeaReq;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.dialog.ConfirmContentWindow;
import com.yfy.final_tag.dialog.ConfirmDateWindow;
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
import com.yfy.final_tag.recycerview.RecycleViewDivider;
import com.yfy.view.SQToolBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoiceClassActivity extends BaseActivity implements Callback<ResEnv> {
    private static final String TAG = ChoiceClassActivity.class.getSimpleName();

    private ChoiceClassAdapter adapter;

    private String canedit;
    @Bind(R.id.public_bottom_button)
    Button bottom;
    @Bind(R.id.public_bottom_button_one)
    Button bottomone;
    @Bind(R.id.event_list_left)
    ListView listview;
    private String isdateclass=TagFinal.TRUE;
    private DateBean dateBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delay_service_main);
        bottom.setText("查看全部班级");
        isdateclass=TagFinal.TRUE;
        dateBean=new DateBean();
        dateBean.setValue_long(System.currentTimeMillis(),true);
        initSQtoobar();
        initListView();
        initDialog();
        initDateDialog();
        initRecycler();
        getData();
    }



    private boolean isEleate=false;
    private boolean isadmin=false;
    private void getData(){
        isEleate=getIntent().getExtras().getBoolean(TagFinal.TYPE_TAG);
        isadmin=getIntent().getExtras().getBoolean("admin");
        rep();
    }


    @OnClick(R.id.public_bottom_button_one)
    void setOne(){
        if (isrep){
            if(!isadmin)
            isEleate=false;
            bottomone.setText("代课考勤模式");
            isrep=false;
        }else{
            isEleate=true;
            isrep=true;
            bottomone.setText("取消代课考勤模式");
        }
        rep();
    }
    @OnClick(R.id.public_bottom_button)
    void setButton(){
        if (isdateclass.equals(TagFinal.FALSE)){
            isdateclass=TagFinal.TRUE;
            bottom.setText("查看全部班级");
        }else{
            isdateclass=TagFinal.FALSE;
            bottom.setText("查看所选日期班级");
        }
       rep();
    }
    private TextView meun,title;
    private void initSQtoobar() {
        assert toolbar!=null;
        title=toolbar.setTitle("托管班级");
        meun=toolbar.addMenuText(TagFinal.ONE_INT,"" );
        meun.setText(dateBean.getName());
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                datedialog.showAtBottom();
            }
        });
    }

    private ConfirmDateWindow datedialog;
    private void initDateDialog(){
        datedialog = new ConfirmDateWindow(mActivity);
        datedialog.setOnPopClickListenner(new ConfirmDateWindow.OnPopClickListenner() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.set:
                        dateBean.setValue_long(datedialog.getTimeLong());
                        meun.setText(dateBean.getName());
                        datedialog.dismiss();
                        rep();
                        break;
                    case R.id.cancel:
                        dialog.dismiss();
                        break;
                }

            }
        });
    }

    private String phone_s;

    public SwipeRefreshLayout swipeRefreshLayout;
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
                rep();
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
        adapter=new ChoiceClassAdapter(this);
        adapter.setDotype(new ChoiceClassAdapter.Dotype() {
            @Override
            public void tellPhone(String phone) {
                phone_s=phone;
               showDialog("拨打电话",phone ,"确定" );
            }
        });
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






    private boolean isrep=false;//true代课
    private void rep(){
        if (isrep){
            getRep(true);
        }else{
            getClassGrade(true);
        }

    }



    private EventMainListViewAdapter listViewAdapter;
    private List<EventGrade> grades=new ArrayList<>();
    private void initListView(){
        listViewAdapter=new EventMainListViewAdapter(mActivity);
        listview.setAdapter(listViewAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index_position=position;
                adapter.setDataList(grades.get(position).getElective_list());
                adapter.setLoadState(TagFinal.LOADING_COMPLETE);
                title.setText(grades.get(position).getGradename());

                //赋值 需要
                adapter.setCanedit(canedit);
                adapter.setDateBean(dateBean);
                adapter.setIndex_position(index_position);

            }
        });

    }



    private int index_position=0;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_REFRESH:
                    index_position=data.getIntExtra("index_position",0 );
                    rep();
                    break;
            }
        }
    }








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









    /**
     * --------------------------retrofit--------------------------
     */





    private void getRep(boolean is){




        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        DelayGetClassListReplaceReq req = new DelayGetClassListReplaceReq();
        //获取参数
        req.setDate(dateBean.getValue());
        req.setIsdateclass(isdateclass.equals(TagFinal.TRUE));

        reqBody.delayGetClassListReplaceReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().delay_get_class_list_replace(reqEnv);
        call.enqueue(this);
        if (is)showProgressDialog("");
    }




    private void getClassGrade(boolean is){




        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        DelayGetClassListTeaReq req = new DelayGetClassListTeaReq();
        //获取参数
        req.setDate(dateBean.getValue());
        req.setIsadmin(isEleate);
        req.setIsdateclass(isdateclass.equals(TagFinal.TRUE));

        reqBody.delayGetClassListTeaReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().delay_get_class_list_tea(reqEnv);
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
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.delayGetClassListTeaRes !=null){
                String result=b.delayGetClassListTeaRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                DelayServiceRes res=gson.fromJson(result,DelayServiceRes.class );
                canedit=res.getCanedit();
                grades=res.getElective_class();
                listViewAdapter.setDatas(grades);
                listview.performItemClick(null, index_position,0 );
            }
            if (b.delayGetClassListReplaceRes !=null){
                String result=b.delayGetClassListReplaceRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                DelayServiceRes res=gson.fromJson(result,DelayServiceRes.class );
                canedit=res.getCanedit();
                grades=res.getElective_class();
                listViewAdapter.setDatas(grades);
                listview.performItemClick(null, index_position,0 );
            }

        }else{
            try {
                String s=response.errorBody().string();
                Logger.e(StringUtils.getTextJoint("%1$s:%2$d:%3$s",name,response.code(),s));
            } catch (IOException e) {
                Logger.e(TagFinal.ZXX, "onResponse: IOException");
                e.printStackTrace();
            }
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
