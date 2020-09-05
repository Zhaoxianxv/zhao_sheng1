package com.yfy.app.process;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.bean.BaseRes;
import com.yfy.app.login.bean.UserRes;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.process.ProcessGetUserCountReq;
import com.yfy.app.net.process.ProcessGetUserListReq;
import com.yfy.app.process.adapter.ProcessMainAdapter;
import com.yfy.app.process.bean.ProcessMainBean;
import com.yfy.app.process.bean.ProcessMainRes;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.db.GreenDaoManager;
import com.yfy.greendao.User;
import com.yfy.db.UserPreferences;
import com.yfy.final_tag.dialog.CPWBean;
import com.yfy.final_tag.dialog.CPWMatchListView;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.EndlessRecyclerOnScrollListener;
import com.yfy.final_tag.recycerview.RecycleViewDivider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class ProcessMainActivity extends BaseActivity {

    private static final String TAG = ProcessMainActivity.class.getSimpleName();


    private int page=0;
    private ProcessMainAdapter adapter;
    private List<ProcessMainBean> mainBeens=new ArrayList<>();

    @Bind(R.id.process_bottom_admin_count)
    TextView admin_count;
    @Bind(R.id.process_bottom_admin_name)
    TextView admin_name;
    @Bind(R.id.process_bottom_add)
    TextView add_title;
    @Bind(R.id.process_bottom_admin)
    RelativeLayout admin_layout;

    @Bind(R.id.process_bottom_line_layout)
    LinearLayout bottom_layout;

    @Bind(R.id.process_main_scan_layout)
    LinearLayout scan_layout;
    @Bind(R.id.process_main_scan_selected_name)
    TextView scan_selected_name;
    @Bind(R.id.process_main_scan_line)
    View scan_line;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.process_main);
        getData();
        initSQToolbar();
        getCount();
        initRecycler();
        refresh(true,TagFinal.REFRESH);
    }


    private int id;
    private String title,group_parent,group_child,type,admin_right;
    private List<CPWBean> scanStateList=new ArrayList<>();
    private String scan_state="0";
    private void  getData(){
        Intent intent=getIntent();
        id=intent.getIntExtra(Base.id,0);
        title=intent.getStringExtra(Base.title);
        admin_right=intent.getStringExtra(Base.content);
        type=intent.getStringExtra(Base.type);
        scanStateList=intent.getParcelableArrayListExtra(Base.data);
        group_parent=intent.getStringExtra("group_parent");
        group_child=intent.getStringExtra("group_child");
        Base.process_type=type;
        Base.process_title=title;
        Base.process_id=id;
        Base.process_group=group_parent;
        Base.process_secondgroup=group_child;


        if (!StringJudge.isEmpty(scanStateList)){
            scanStateList.add(new CPWBean("全部","0"));
            scan_layout.setVisibility(View.VISIBLE);
            scan_line.setVisibility(View.VISIBLE);
        }else{
            scan_layout.setVisibility(View.GONE);
            scan_line.setVisibility(View.GONE);
        }

        Logger.e(String.valueOf(scanStateList.size()));
        //权限
        List<String> adminList=StringUtils.listToStringSplitCharacters(admin_right,",");
        for (String s:adminList){
            switch (s){
                case "view":
                    break;
                case "add":
                    add_title.setVisibility(View.VISIBLE);
                    break;
                case "admin":
                    admin_layout.setVisibility(View.VISIBLE);
                    break;
            }
        }
        if (add_title.getVisibility()==View.GONE&&admin_layout.getVisibility()==View.GONE){
            bottom_layout.setVisibility(View.GONE);
        }else{
            bottom_layout.setVisibility(View.VISIBLE);
        }

    }
    public void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle(title);
        toolbar.setOnNaviClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    public void finish() {
        setResult(RESULT_OK);//在super后面无效
        super.finish();
    }

    /**
     * ----------
     */
    //选择state
    @OnClick(R.id.process_main_scan_selected_name)
    void setScanSelectedState(){
        CPWMatchListView confirmPopWindow=new CPWMatchListView(mActivity,scanStateList);
        confirmPopWindow.setOnPopClickListenner(new CPWMatchListView.OnPopClickListenner() {
            @Override
            public void onClick(String type, CPWBean bean) {
                scan_selected_name.setText(bean.getName());
                scan_state=bean.getId();
                refresh(true,TagFinal.REFRESH);

            }
        });

        confirmPopWindow.showAtBottom(scan_line);
    }


    @OnClick(R.id.process_bottom_admin)
    void setOneLayout(){
        Intent intent=new Intent(mActivity,ProcessAdminListActivity.class);
        intent.putParcelableArrayListExtra(Base.data, (ArrayList<? extends Parcelable>) scanStateList);
        startActivityForResult(intent,TagFinal.UI_REFRESH);
    }



    @OnClick(R.id.process_bottom_add)
    void setAddLayout(){
        Intent intent;
        switch (Base.process_type){
            case "functionrooms":
                intent=new Intent(mActivity,ProcessRoomAddActivity.class);
                intent.putExtra(Base.id,"0");
                startActivityForResult(intent,TagFinal.UI_REFRESH);
                break;
            default:
                intent=new Intent(mActivity,ProcessAddSealActivity.class);
                intent.putExtra(Base.id,"0");
                startActivityForResult(intent,TagFinal.UI_REFRESH);
                break;
        }
    }



    public SwipeRefreshLayout swipeRefreshLayout;
    public RecyclerView recyclerView;
    public void initRecycler(){
        admin_name.setText("待我处理");
        add_title.setText("添加申请");
        recyclerView =  findViewById(R.id.public_recycler);
        swipeRefreshLayout =  findViewById(R.id.public_swip);
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#4DB6AC"));
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                refresh(false,TagFinal.REFRESH);
                getCount();
            }
        });
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                adapter.setLoadState(TagFinal.LOADING);
                refresh(false,TagFinal.REFRESH_MORE);

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mActivity,
                LinearLayoutManager.HORIZONTAL,
                1,
                getResources().getColor(R.color.gray)));
        adapter=new ProcessMainAdapter(mActivity,mainBeens);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_REFRESH:
                    refresh(false,TagFinal.REFRESH);
                    getCount();
                    break;
            }
        }
    }
    /**
     * ----------------------------retrofit-----------------------
     */


    public void refresh(boolean is,String loadType){
        if (loadType.equals(TagFinal.REFRESH)){
            page=0;
        }else{
            page++;
        }
        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        ProcessGetUserListReq req = new ProcessGetUserListReq();
        //获取参数
        req.setSession_key(Base.user.getSession_key());
        req.setType(type);
        req.setSearchkey("");
        req.setState(ConvertObjtect.getInstance().getInt(scan_state));
        req.setGroupid(0);
        req.setSecondgroupid(0);
        req.setStartdate("");
        req.setEnddate("");
        req.setPage(page);
        req.setPagesize(TagFinal.TEN_FIVE);

        reqBody.setProcessGetUserListReq(req);
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().process_get_user_list(reqEnv);
        call.enqueue(this);
        if (is) showProgressDialog("");
    }



    public void getCount(){
        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        ProcessGetUserCountReq req = new ProcessGetUserCountReq();
        //获取参数
        req.setType(Base.process_type);
        reqBody.processGetUserCountReq=req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().process_get_user_count(reqEnv);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        closeSwipeRefresh();
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.processGetUserListRes !=null){
                String result=b.processGetUserListRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                ProcessMainRes res=gson.fromJson(result,ProcessMainRes.class);
                if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
                    dismissProgressDialog();
                    if (page==0){
                        mainBeens.clear();
                        mainBeens=res.getData();
                    }else{
                        mainBeens.addAll(res.getData());
                    }
                    adapter.setDataList(mainBeens);
                    if (res.getData().size()!=TagFinal.TEN_FIVE){
                        toastShow(R.string.success_loadmore_end);
                        adapter.setLoadState(3);
                    }else{
                        adapter.setLoadState(2);
                    }
                }else{
                    Logger.e(res.getError_code()+"-"+Base.error_code);
                    switch (res.getError_code()){
                        case Base.error_code:
                            toast("登录过期");
                            Base.user=null;
                            GreenDaoManager.getInstance().clearUser();
                            setResult(RESULT_OK);
                            finish();
                            break;
                        default:
                            dismissProgressDialog();
                            toast(res.getError_code());
                            break;
                    }
                }

            }
            if (b.processGetUserCountRes !=null){
                String result=b.processGetUserCountRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                ProcessMainRes res=gson.fromJson(result,ProcessMainRes.class);
                if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
                    if (StringJudge.isEmpty(res.getCount())){
                        admin_count.setVisibility(View.GONE);
                    }else{
                        admin_count.setVisibility(View.VISIBLE);
                        if (res.getCount().length()>2) {
                            admin_count.setText("99+");
                        }else{
                            admin_count.setText(res.getCount());
                        }
                        if (res.getCount().equals("0")) admin_count.setVisibility(View.GONE);
                    }
                }else{
                    admin_count.setVisibility(View.GONE);
                    dismissProgressDialog();
                    toast(res.getError_code());
                }
            }
            if (b.baseGetTookenRes !=null){
                String result=b.baseGetTookenRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                BaseRes res=gson.fromJson(result,BaseRes.class);
                if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
                    if (Base.user.getIsDuplication().equalsIgnoreCase(TagFinal.FALSE)){
                        login(res.getTooken());
                    }else{
                        duplicationLogin(res.getTooken());
                    }
                }else{
                    dismissProgressDialog();
                    toast(res.getError_code());
                }
            }
            if (b.userLoginRes !=null){
                String result=b.userLoginRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                UserRes res= gson.fromJson(result,UserRes.class);
                if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)) {
                    resetLogin(res);
                } else {
                    dismissProgressDialog();
                    toast(res.getError_code());
                }
            }
            if (b.userDuplicationLoginRes !=null){
                String result=b.userDuplicationLoginRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                UserRes res= gson.fromJson(result,UserRes.class);
                if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)) {
                    resetLogin(res);
                } else {
                    dismissProgressDialog();
                    toast(res.getError_code());
                }
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
            toastShow(StringUtils.getTextJoint("数据错误:%1$d",response.code()));
        }
    }

    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        if (!isActivity())return;
        Logger.e("onFailure  :"+call.request().headers().toString());
        toast(R.string.fail_do_not);
        dismissProgressDialog();
        closeSwipeRefresh();
    }



    private void resetLogin(UserRes res){
        User user=Base.user;
        user.setSession_key(res.getSession_key());
        user.setToken(res.getToken());
        UserPreferences.getInstance().saveUserID(user.getIdU());
        GreenDaoManager.getInstance().saveUser(user);
        switch (token){
            case "process":
                refresh(true,TagFinal.REFRESH);
                break;
        }
    }
    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }



}
