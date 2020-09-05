package com.yfy.app.allclass;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.allclass.adapter.PersonAdapter;
import com.yfy.app.allclass.beans.ReInfor;
import com.yfy.app.allclass.beans.ShapeMainList;
import com.yfy.app.allclass.beans.Top;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.dialog.LoadingDialog;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.final_tag.recycerview.EndlessRecyclerOnScrollListener;
import com.yfy.final_tag.recycerview.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class ShapeUserDetailActivity extends WcfActivity {
    private static final String TAG = "zxx";
    private String user_id,user_type;
    private int page=0;
    private LoadingDialog loadingDialog;
    private ShapeMainList bean;
    private final int classId = 0;
    private String tag_id="";
    private String search="";

    @Bind(R.id.shape_type_header_person)
    TextView hradertypy;
    @Bind(R.id.shape_main_recycler_person)
    RecyclerView recyclerView;
    private  boolean isRefresh=false;
    private PersonAdapter adapter;
    private List<ShapeMainList> beans=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shape_user_detail);
        loadingDialog=new LoadingDialog(mActivity);
        getData();

    }



    public void getData(){
        Intent intent=getIntent();
        user_type=intent.getStringExtra("user_type");
        user_id=intent.getStringExtra("user_id");
        bean=intent.getParcelableExtra("bean");
        getTopData();

        initRecycler();

    }



    private void getTopData() {
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                user_id,
                user_type
        };

        ParamsTask task;
        task = new ParamsTask(params, TagFinal.SHAPE_PERSON_DETAIL, TagFinal.SHAPE_PERSON_DETAIL);
        execute(task);
    }
    private void refresh(boolean is) {
        if (!is){
            page=0;
        }else{
            page++;
        }
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                user_id,
                user_type,
                classId,
                tag_id,
                search,
                page,
                TagFinal.TEN_INT
        };

        ParamsTask task;
        if (is){
            task = new ParamsTask(params, TagFinal.SHAPE_PERSON_GET_CLASS, TagFinal.REFRESH_MORE,loadingDialog);
        }else{
            task = new ParamsTask(params, TagFinal.SHAPE_PERSON_GET_CLASS, TagFinal.REFRESH);
        }
        execute(task);
    }
    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {

        closeSwipeRefresh();
        ReInfor infor;
        Logger.e(TAG, "onSuccess: "+result );
        String name=wcfTask.getName();


        if (name.equals(TagFinal.REFRESH)){
            infor= gson.fromJson(result, ReInfor.class);
            beans.clear();
            beans.addAll(infor.getWbs());
            adapter.setDataList(beans);
            adapter.setLoadState(2);
            return false;
        }
        if (name.equals(TagFinal.REFRESH_MORE)){
            infor= gson.fromJson(result, ReInfor.class);
            beans.addAll(infor.getWbs());
            adapter.setDataList(beans);
            if (infor.getWbs().size()== TagFinal.TEN_INT){
                adapter.setLoadState(2);
            }else{
                adapter.setLoadState(3);
                toastShow(R.string.success_loadmore_end);
            }
            return false;
        }

        if (name.equals(TagFinal.SHAPE_PERSON_DETAIL)){

            Top top=gson.fromJson(result,Top.class);
            if (StringJudge.isNotEmpty(top.getWbs()))
            adapter.setList(top.getWbs());
            refresh(false);
            return false;
        }
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        closeSwipeRefresh();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_REFRESH:

                    refresh(false);
                    break;
                case TagFinal.ONE_INT:
                    tag_id=data.getStringExtra("shape_kind")==null?"":data.getStringExtra("shape_kind");
                    hradertypy.setText(data.getStringExtra("shape_name")==null?"":data.getStringExtra("shape_name"));
                    refresh(false);
                    break;
            }
        }
    }

    @OnClick(R.id.shape_type_header_person)
    void setHeader(){
        Intent ent = new Intent(mActivity, ShapeChioceTagActivity.class);
        startActivityForResult(ent,TagFinal.ONE_INT);
    }

    private SwipeRefreshLayout swipeRefreshLayout;
    public void initRecycler(){
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.shape_swipe_person);
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#4DB6AC"));
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                refresh(false);
            }
        });
        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                refresh(true);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mActivity,
                LinearLayoutManager.HORIZONTAL,
                0,
                Color.alpha(0)));
        adapter = new PersonAdapter(mActivity,beans);
        recyclerView.setAdapter(adapter);
    }
    public void closeSwipeRefresh(){
        isRefresh=false;
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
}
