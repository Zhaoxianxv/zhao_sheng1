package com.yfy.app.process;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.process.ProcessAdminGetListReq;
import com.yfy.app.process.adapter.ProcessAdminAdapter;
import com.yfy.app.process.bean.ProcessMainBean;
import com.yfy.app.process.bean.ProcessMainRes;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class ProcessAdminListActivity extends BaseActivity  {

    private static final String TAG = ProcessAdminListActivity.class.getSimpleName();


    private int page=0;
    private ProcessAdminAdapter adapter;
    private List<ProcessMainBean> mainBeens=new ArrayList<>();
    @Bind(R.id.process_main_scan_layout)
    LinearLayout scan_layout;
    @Bind(R.id.process_main_scan_selected_name)
    TextView scan_selected_name;
    @Bind(R.id.process_main_scan_line)
    View scan_line;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.process_admin_main);
        getData();
        initSQToolbar();
        initRecycler();
        refresh(true,TagFinal.REFRESH);
    }


    @Override
    public void finish() {
        setResult(RESULT_OK);//在super后面无效
        super.finish();
    }

    private int id;
    private String title,group_parent,group_child;
    private void  getData(){
        Intent intent=getIntent();
        group_parent=intent.getStringExtra("group_parent");
        group_child=intent.getStringExtra("group_child");
        scanStateList=intent.getParcelableArrayListExtra(Base.data);

        if (!StringJudge.isEmpty(scanStateList)){
            scan_layout.setVisibility(View.VISIBLE);
            scan_line.setVisibility(View.VISIBLE);
        }else{
            scan_layout.setVisibility(View.GONE);
            scan_line.setVisibility(View.GONE);
        }
    }
    public void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle(Base.process_title);
        toolbar.setOnNaviClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    private List<CPWBean> scanStateList=new ArrayList<>();
    private String scan_state="0";

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





    /**
     * ----------set
     */

    public SwipeRefreshLayout swipeRefreshLayout;
    public RecyclerView recyclerView;
    public void initRecycler(){
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
        adapter=new ProcessAdminAdapter(mActivity);
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
                    break;
            }
        }
    }
    /**
     * ----------------------------retrofit-----------------------second
     */


    public void refresh(boolean is,String loadType){
        if (loadType.equals(TagFinal.REFRESH)){
            page=0;
        }else{
            page++;
        }
        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        ProcessAdminGetListReq req = new ProcessAdminGetListReq();

        //获取参数
        req.setType(Base.process_type);
        req.setSearchkey("");
        req.setState(ConvertObjtect.getInstance().getInt(scan_state));
        req.setGroupid(0);
        req.setSecondgroupid(0);
        req.setStartdate("");
        req.setEnddate("");
        req.setPage(page);
        req.setSize(TagFinal.TEN_FIVE);

        reqBody.processAdminGetListReq=req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().process_admin_get_list(reqEnv);
        call.enqueue(this);
        if (is) showProgressDialog("");
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
            if (b.processAdminGetListRes !=null){
                String result=b.processAdminGetListRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                ProcessMainRes res=gson.fromJson(result,ProcessMainRes.class);
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
        toast(R.string.fail_do_not);
        dismissProgressDialog();
        closeSwipeRefresh();
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }

}
