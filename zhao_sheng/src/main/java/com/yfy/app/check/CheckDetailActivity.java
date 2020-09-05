package com.yfy.app.check;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.zhao_sheng.R;
import com.yfy.app.check.adapter.CheckDetailAdapter;
import com.yfy.app.check.bean.ChecKParent;
import com.yfy.app.check.bean.CheckChild;
import com.yfy.app.check.bean.CheckRes;
import com.yfy.app.check.bean.CheckStu;
import com.yfy.app.check.bean.IllAllBean;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.check.CheckStuDelParentReq;
import com.yfy.app.net.check.CheckStuParentDetailReq;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.dialog.ConfirmContentWindow;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.DefaultItemAnimator;
import com.yfy.final_tag.recycerview.RecycleViewDivider;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckDetailActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = CheckDetailActivity.class.getSimpleName();


    private CheckDetailAdapter adapter;
    private List<CheckChild> adapter_list=new ArrayList<>();
    @Bind(R.id.public_recycler_del)
    Button del_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_recycler_del_view);

        del_button.setVisibility(View.GONE);
        initConfirmDialog();
        getData();
        initRecycler();
        initSQtoolbar();
        getStuDetail();

    }
    private CheckStu checkStu;
    private IllAllBean illAllBean;
    private boolean isdel=false;
    private void getData(){
        isdel=getIntent().getBooleanExtra(TagFinal.TYPE_TAG,false );
        checkStu=getIntent().getParcelableExtra(TagFinal.OBJECT_TAG);
        illAllBean=getIntent().getParcelableExtra(TagFinal.id);
    }





    private void initSQtoolbar() {
        assert toolbar!=null;
        toolbar.setTitle(checkStu.getUsername());
        if (isdel) toolbar.addMenuText(TagFinal.ONE_INT,"删除记录");
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (position){
                    case TagFinal.ONE_INT:

                        closeKeyWord();
                        confirmContentWindow.showAtCenter();
                        break;
                }
            }
        });
    }


    private ConfirmContentWindow confirmContentWindow;
    private void initConfirmDialog(){

        confirmContentWindow = new ConfirmContentWindow(mActivity);
        confirmContentWindow.setTitle_s("提示","是否删除此条信息","确定");
        confirmContentWindow.setOnPopClickListenner(new ConfirmContentWindow.OnPopClickListenner() {
            @Override
            public void onClick(View view) {

                switch (view.getId()){
                    case R.id.pop_dialog_title:
                        break;
                    case R.id.pop_dialog_content:
                        break;
                    case R.id.pop_dialog_ok:
                        delAllDetail();
                        break;
                }
            }
        });
    }

//    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView = (RecyclerView) findViewById(R.id.public_recycler);
//        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.public_swip);
//        //AppBarLayout 展开执行刷新
//        // 设置刷新控件颜色
//        swipeRefreshLayout.setColorSchemeColors(ColorRgbUtil.getBaseColor());
//        // 设置下拉刷新
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // 刷新数据
//                getStuDetail();
//            }
//        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        xlist.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mActivity,
                LinearLayoutManager.HORIZONTAL,
                1,
                getResources().getColor(R.color.gray)));
        adapter=new CheckDetailAdapter(mActivity);
        adapter.setCheckStu(checkStu);
        recyclerView.setAdapter(adapter);

    }

    public void closeSwipeRefresh(){
//        if (swipeRefreshLayout!=null){
//            swipeRefreshLayout.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                }
//            }, 200);
//        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_ADD:
                    getStuDetail();
                    break;
            }
        }
    }

    /**
     * ----------------------------retrofit-----------------------
     */



    public void getStuDetail() {

        ReqEnv evn = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        CheckStuParentDetailReq req = new CheckStuParentDetailReq();
        //获取参数

        req.setUserid(ConvertObjtect.getInstance().getInt(checkStu.getUserid()));
        req.setId(illAllBean.getIllid());
//        req.setUsertype("stu");
        reqBody.checkStuParentDetailReq= req;
        evn.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().check_get_stu_parent_detail(evn);
        call.enqueue(this);
        showProgressDialog("正在加载");
        Logger.e(evn.toString());

    }

    public void delAllDetail() {

        ReqEnv evn = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        CheckStuDelParentReq req = new CheckStuDelParentReq();
        //获取参数

        req.setIllid(illAllBean.getIllid());
//        req.setUsertype("stu");
        reqBody.checkStuDelParentReq= req;
        evn.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().check_stu_del_parent(evn);
        call.enqueue(this);
        showProgressDialog("正在加载");
        Logger.e(evn.toString());

    }


    private void initCreate(CheckRes res){

        if (StringJudge.isEmpty(res.getUserstate())){
            toastShow("没有获取到数据");
            //添加生病
            return;
        }else{
            //修改生病状态
        }

        adapter_list.clear();
        for (ChecKParent parent:res.getUserstate()){
            CheckChild detail=new CheckChild();
            detail.setView_type(TagFinal.TYPE_PARENT);
            detail.setAdddate(parent.getAdddate());
            detail.setAdduser(parent.getAdduser());
            detail.setAdduserheadpic(parent.getAdduserheadpic());
            detail.setUserheadpic(parent.getUserheadpic());
            detail.setUserid(parent.getUserid());
            detail.setUsername(parent.getUsername());
            detail.setUsermobile(parent.getUsermobile());
            detail.setIllcheckdate(parent.getIllcheckdate());
            detail.setIllchecktype(parent.getIllchecktype());
            detail.setIllcheckdate(parent.getIlldate());
            detail.setIllid(parent.getIllid());
            detail.setIlltype(parent.getIlltype());
            detail.setState(parent.getState());

            adapter_list.add(detail);
            for (CheckChild child:parent.getIllstate()){
                child.setView_type(TagFinal.TYPE_CHILD);
                adapter_list.add(child);
            }

        }


        if (StringJudge.isEmpty(res.getReturndate())){

        }else{
            CheckChild detail=new CheckChild();
            detail.setView_type(TagFinal.TYPE_FOOTER);
            detail.setUsername(res.getReturndate()+"已返校");
            adapter_list.add(detail);
        }
        adapter.setDataList(adapter_list);
        adapter.setLoadState(TagFinal.LOADING_COMPLETE);
    }

    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();
//        closeSwipeRefresh();
        if (response.code()==500){
            toastShow("数据出差了");
        }
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.checkStuParentDetailRes!=null) {
                String result = b.checkStuParentDetailRes.result;
                Logger.e(call.request().headers().toString() + result);
                CheckRes res=gson.fromJson(result,CheckRes.class);
                if (res.getResult().equals(TagFinal.TRUE)){
                    initCreate(res);
                }else{
                    toastShow(res.getError_code());
                }
            }
            if (b.checkStuDelParentRes!=null) {
                String result = b.checkStuDelParentRes.result;
                Logger.e(call.request().headers().toString() + result);
                CheckRes res=gson.fromJson(result,CheckRes.class);
                if (res.getResult().equals(TagFinal.TRUE)){
                    toastShow(R.string.success_do);
                    setResult(RESULT_OK);
                    finish();
                }else{
                    toastShow(res.getError_code());
                }
            }

        }else{
            Logger.e("evn: null"+call.request().headers().toString() );
        }
    }

    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        if (!isActivity())return;
        Logger.e("onFailure  "+call.request().headers().toString() );
        dismissProgressDialog();
//        closeSwipeRefresh();
        toastShow(R.string.fail_do_not);
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
