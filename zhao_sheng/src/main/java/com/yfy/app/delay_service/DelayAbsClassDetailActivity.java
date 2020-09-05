package com.yfy.app.delay_service;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.bean.DateBean;
import com.yfy.app.delay_service.adapter.DelayAbsStuAdapter;
import com.yfy.app.delay_service.bean.AbsStu;
import com.yfy.app.delay_service.bean.DelayAbsenteeismClass;
import com.yfy.app.delay_service.bean.DelayServiceRes;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.delay_service.DelayAdminClassSetReq;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.dialog.CPWBean;
import com.yfy.final_tag.dialog.ConfirmContentWindow;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.CallPhone;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.permission.PermissionFail;
import com.yfy.final_tag.permission.PermissionGen;
import com.yfy.final_tag.permission.PermissionSuccess;
import com.yfy.final_tag.permission.PermissionTools;
import com.yfy.final_tag.recycerview.RecycleViewDivider;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DelayAbsClassDetailActivity extends BaseActivity implements Callback<ResEnv> {
    private static final String TAG = DelayAbsClassDetailActivity.class.getSimpleName();
    private DelayAbsStuAdapter adapter;


    @Bind(R.id.public_bottom_button_two)
    Button button_two;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delay_class_detail);
        button_two.setText("行政考勤");
        initConfirmContentWindow();
        initRecycler();
        getData();
    }


    private String title="";
    private List<AbsStu> absStus=new ArrayList<>();
    private List<AbsStu> delayTeaBeans=new ArrayList<>();
    private void getData(){
        absStus=getIntent().getParcelableArrayListExtra(Base.data);
        delayTeaBeans=getIntent().getParcelableArrayListExtra(Base.reason);
        bean = getIntent().getParcelableExtra(Base.title);
        title = bean.getElectivename();
        AbsStu abs=new AbsStu();
        abs.setView_type(TagFinal.TYPE_CHILD);
        if(StringJudge.isEmpty(absStus)){

        }else {
            delayTeaBeans.add(abs);
        }
        delayTeaBeans.addAll(absStus);
        if (StringJudge.isNotEmpty(delayTeaBeans)){
            cpwBean=new CPWBean(delayTeaBeans.get(0).getTeachername(),delayTeaBeans.get(0).getTeacherid());
        }
        adapter.setDataList(delayTeaBeans);
        adapter.setLoadState(TagFinal.LOADING_END);
        if (StringJudge.isEmpty(absStus))toastShow("无人缺勤");
        dateBean=getIntent().getParcelableExtra(Base.date);
        initSQtoobar(title);
    }
    public DelayAbsenteeismClass bean;

    private DateBean dateBean;
    @OnClick(R.id.public_bottom_button_two)
    void setTea(){
        Intent intent=new Intent(mActivity,DelayAdminToTeaEventActivity.class);
        Bundle b=new Bundle();
//        b.putString(TagFinal.CLASS_ID, bean_id);//Electiveid
        b.putParcelable(Base.date,dateBean);//date
        b.putParcelable(Base.data,bean);//date
        b.putString(TagFinal.TYPE_TAG,"1");//type

        intent.putExtras(b);
        startActivityForResult(intent,TagFinal.UI_ADD);
    }


    private CPWBean cpwBean;
    private void initSQtoobar(String title) {
        assert toolbar!=null;
        toolbar.setTitle(title);
        toolbar.addMenuText(TagFinal.ONE_INT,"记录");
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(mActivity, DelayGetAdminToClassEventDetailActivity.class);
                intent.putExtra(Base.date,dateBean);
                intent.putExtra(Base.state,cpwBean);
                intent.putExtra(Base.id,bean.getElectiveid());
                intent.putParcelableArrayListExtra(Base.data, (ArrayList<? extends Parcelable>) bean.getTeacher_list());
                startActivity(intent);
            }
        });

    }

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView =  findViewById(R.id.public_top_recycler);
        swipeRefreshLayout =  findViewById(R.id.public_swip);
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
        adapter=new DelayAbsStuAdapter(mActivity);
        recyclerView.setAdapter(adapter);
        adapter.setDotype(new DelayAbsStuAdapter.Dotype() {
            @Override
            public void tellPhone(String phone) {
                phone_s=phone;
                showDialog("拨打电话",phone ,"确定" );
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



    /**
     * ----------------------------------------retrofit-------------
     */



    public void setContent(){

        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        DelayAdminClassSetReq req = new DelayAdminClassSetReq();
        //获取参数
        req.setDate(dateBean.getValue());


        reqBody.delayAdminClassSetReq =req;
        reqEnvelop.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().delay_admin_class_set(reqEnvelop);
        call.enqueue(this);

        Logger.e(reqEnvelop.toString());
        showProgressDialog("");
    }


    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();
//        closeSwipeRefresh();

        ResEnv respEnvelope = response.body();
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.delayAdminClassSetRes !=null) {
                String result = b.delayAdminClassSetRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                DelayServiceRes res=gson.fromJson(result, DelayServiceRes.class);
                if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
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
//        closeSwipeRefresh();
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }











    private String phone_s;
    private ConfirmContentWindow confirmContentWindow;
    private void initConfirmContentWindow(){
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
