package com.yfy.app.check;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.bean.DateBean;
import com.yfy.app.check.adapter.ChoiceStuAdapter;
import com.yfy.app.check.bean.CheckRes;
import com.yfy.app.check.bean.CheckState;
import com.yfy.app.check.bean.CheckStu;
import com.yfy.app.check.bean.ClasslistBean;
import com.yfy.app.check.bean.IllType;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.check.CheckGetStuReq;
import com.yfy.app.net.check.CheckSubimtYesReq;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.dialog.CPWListView;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.DefaultItemAnimator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckStuActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = CheckStuActivity.class.getSimpleName();


    private DateBean dateBean;
    private ChoiceStuAdapter adapter;
    @Bind(R.id.public_deleted_text)
    TextView delete_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_delete_main);

        initTeaDialog();
        getData();
        initRecycler();
        getCheckClass(true);

    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_REFRESH:
                    getCheckClass(false);
                    break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private ClasslistBean bean;
    private CheckState state;
    private void getData(){
        dateBean=getIntent().getParcelableExtra(TagFinal.CLASS_BEAN);
        bean=getIntent().getParcelableExtra(TagFinal.OBJECT_TAG);
        state=getIntent().getParcelableExtra(TagFinal.ID_TAG);
        if(state.getState().equals("已检测")){
            delete_button.setText("已巡查");
            delete_button.setTextColor(ColorRgbUtil.getForestGreen());
        }else{
            delete_button.setText("设为已巡查");
            delete_button.setTextColor(ColorRgbUtil.getMaroon());
        }
        initSQtoolbar(StringUtils.getTextJoint("%1$s(%2$s)",bean.getGroupname(),state.getStatetitle()));
    }
    private TextView one_menu;
    private void initSQtoolbar(String title) {
        assert toolbar!=null;
        toolbar.setTitle(title);
        toolbar.setOnNaviClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView = (RecyclerView) findViewById(R.id.public_delete_recycler);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.public_deleted_swipe);
        //AppBarLayout 展开执行刷新
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(ColorRgbUtil.getBaseColor());
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                getCheckClass(false);
            }
        });
        GridLayoutManager manager = new GridLayoutManager(mActivity,4, LinearLayoutManager.VERTICAL,false);
//        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return select_stu.get(position).getSpan_size();
//            }
//        });
        recyclerView.setLayoutManager(manager);
//        xlist.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
//        recyclerView.addItemDecoration(new RecycleViewDivider(
//                mActivity,
//                LinearLayoutManager.HORIZONTAL,
//                1,
//                getResources().getColor(R.color.gray)));
        adapter=new ChoiceStuAdapter(mActivity);
        recyclerView.setAdapter(adapter);
        adapter.setBean(bean);
        adapter.setState(state);


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



    @OnClick(R.id.public_deleted_text)
    void setSubmit(){
        if(state.getState().equals("已检测")){
            return;
        }
        submitAll();
    }




    private CPWListView cpwListView;
    private List<String> name=new ArrayList<>();
    private List<IllType> dailog_list_beans=new ArrayList<>();
    private IllType select_bean;
    private void initTeaDialog(){
        cpwListView = new CPWListView(mActivity);
//        for (TeaBean tea:users){
//            name.add(tea.getTeachername());
//        }
//        cpwListView.setDatas(name);
        cpwListView.setOnPopClickListenner(new CPWListView.OnPopClickListenner() {

            @Override
            public void onClick(int index, String type) {
                select_bean=dailog_list_beans.get(index);
                one_menu.setText(select_bean.getInspecttypename());
                cpwListView.dismiss();
            }
        });

    }


    /**
     * ----------------------------retrofit-----------------------
     */


//    public void getCheckType(boolean is) {
//
//        ReqEnv evn = new ReqEnv();
//        ReqBody reqBody = new ReqBody();
//        CheckGetTypeReq request = new CheckGetTypeReq();
//        //获取参数
//
//        reqBody.checkGetTypeReq= request;
//        evn.body = reqBody;
//        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().check_get_type(evn);
//        call.enqueue(this);
//        if (is)showProgressDialog("正在加载");
//        Logger.e(evn.toString());
//
//    }


    public void submitAll(){


        ReqEnv evn = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        CheckSubimtYesReq request = new CheckSubimtYesReq();
        //获取参数
        request.setGroupid(ConvertObjtect.getInstance().getInt(bean.getGroupid()));
        request.setDate(dateBean.getValue());
        request.setInspecttypeid(ConvertObjtect.getInstance().getInt(state.getStateid()));

        reqBody.checkSubimtYesReq= request;
        evn.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().check_submit_yes(evn);
        call.enqueue(this);
        showProgressDialog("正在加载");
//        Logger.e(evn.toString());
    }


    public void getCheckClass(boolean is) {

        ReqEnv evn = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        CheckGetStuReq request = new CheckGetStuReq();
        //获取参数
        request.setGrouid(bean.getGroupid());

        request.setDate(dateBean.getValue());
        reqBody.checkGetStuReq= request;
        evn.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().check_get_stu(evn);
        call.enqueue(this);
        if (is)showProgressDialog("正在加载");
//        Logger.e(evn.toString());

    }
    private List<CheckStu> check_stu=new ArrayList<>();

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

            if (b.checkGetStuRes!=null) {
                String result = b.checkGetStuRes.result;
                Logger.e(call.request().headers().toString() + result);
                CheckRes res=gson.fromJson(result,CheckRes.class);
                if (res.getResult().equals(TagFinal.TRUE)){
                    check_stu.clear();
                    check_stu.addAll(res.getUsers());
                    adapter.setDataList(check_stu);
                    adapter.setLoadState(TagFinal.LOADING_COMPLETE);
                    adapter.setDateBean(dateBean);
                }else{
                    toastShow(res.getError_code());
                }
            }

//            if (b.checkGetTypeRes!=null) {
//                String result = b.checkGetTypeRes.result;
//                Logger.e(call.request().headers().toString() + result);
//                CheckRes res=gson.fromJson(result,CheckRes.class);
//                if (res.getResult().equals(TagFinal.TRUE)){
//                    name.clear();
//                    dailog_list_beans=res.getInspecttype();
//
//                    for (IllType tea:res.getInspecttype()){
//                        name.add(tea.getInspecttypename());
//                    }
//                    cpwListView.setDatas(name);
//                }else{
//                    toast(res.getError_code());
//                }
//
//            }
            if (b.checkSubimtYesRes!=null){
                String result = b.checkSubimtYesRes.result;
                Logger.e(call.request().headers().toString() + result);
                CheckRes res=gson.fromJson(result,CheckRes.class);
                if (res.getResult().equals(TagFinal.TRUE)){
                    toastShow(state.getStatetitle()+"完成");
                    setResult(RESULT_OK);
                    finish();
                }else{
                    toastShow(res.getError_code());
                }
            }
        }else{
            Logger.e("evn: null" +call.request().headers().toString());
        }
    }

    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        if (!isActivity())return;
        Logger.e("onFailure  "+call.request().headers().toString() );
        dismissProgressDialog();
        closeSwipeRefresh();
        toastShow(R.string.fail_do_not);
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
