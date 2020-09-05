package com.yfy.app.allclass;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.allclass.adapter.ShapeMainAdapter;
import com.yfy.app.allclass.beans.ReInfor;
import com.yfy.app.allclass.beans.ShapeMainList;
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
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class ShapeSingleActivity extends WcfActivity {

    private final String setPraise = TagFinal.SHAPE_DID_PRAISE;//点赞
    private final String getclass_id = "get_award_class";//获取班级
    private final String deleteDynamic = TagFinal.SHAPE_DID_DELETE;//删

    private final static int CHIOCE_KIND = 11;
    private  int dialogint ;

    private int page = 0;
    private final int classId = 0;
    private final int ismyself = 0;//0为查看所有，1为查看自己的
    private  boolean isRefresh=false;
    private LoadingDialog loadingDialog;
    private String kind_id="";
    private String search="";
    @Bind(R.id.shape_main_recycler)
    RecyclerView recyclerView;
    @Bind(R.id.shape_type_header)
    TextView hradertypy;
    private List<ShapeMainList> beans=new ArrayList<>();
    private ShapeMainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shape_main);
        loadingDialog=new LoadingDialog(mActivity);
        initSQtoolbar();
        initRecycler();
        refresh(true);
    }


    @Override
    public void onResume() {
        super.onResume();

    }
    private void initSQtoolbar() {
        assert toolbar!=null;
        toolbar.setTitle("个人分享");
        toolbar.addMenu(1,R.drawable.add);
        toolbar.addMenu(2,R.drawable.vector_search_white_24dp);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (position){
                    case 1:
                        Intent intent = new Intent(mActivity, ShapeDynamicActivity.class);
                        Bundle b = new Bundle();
                        b.putString(TagFinal.CLASS_BEAN, "");
                        intent.putExtras(b);
                        startActivityForResult(intent, TagFinal.UI_REFRESH);
                        break;
                    case 2:
                        startActivityForResult(new Intent(mActivity,ShapeSearchActivity.class),TagFinal.UI_REFRESH);
                        break;
                }

            }
        });

    }
    private SwipeRefreshLayout swipeRefreshLayout;
    public void initRecycler(){

//        appBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.shape_swipe);
        //AppBarLayout 展开执行刷新
//        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                if (verticalOffset >= 0) {
//                    swipeRefreshLayout.setEnabled(true);
//                } else {
//                    swipeRefreshLayout.setEnabled(false);
//                }
//            }
//        });
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
                loadMore();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        xlist.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mActivity,
                LinearLayoutManager.HORIZONTAL,
                0,
                Color.alpha(0)));
//        getResources().getColor(R.color.light_gray01)
        adapter = new ShapeMainAdapter(mActivity,beans);
        recyclerView.setAdapter(adapter);
        adapter.setViewOnClick(new ShapeMainAdapter.ViewOnClick() {
            @Override
            public void delete(final ShapeMainList bean) {
                String name=Base.user.getName();
                Logger.e("zxx",name+"   "+bean.getName());
                if (bean.getName().equals(name)){
                    mDialog("是否要删除这条动态",new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteDynamic(bean.getId());
                        }
                    });
                }
            }

            @Override
            public void praise(String dynamicId, int position) {
                setPraise(dynamicId,position);
            }
        });
    }









    private void refresh(boolean is) {
        if (isRefresh){
            return;
        }
        isRefresh=true;
        page=TagFinal.ZERO_INT;
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                ismyself,
                classId,
                kind_id,
                search,
                page,
                TagFinal.TEN_INT };

        ParamsTask task;
        if (is){
            task = new ParamsTask(params, TagFinal.SHAPE_MAIN_LIST, TagFinal.REFRESH,loadingDialog);
        }else{
            task = new ParamsTask(params, TagFinal.SHAPE_MAIN_LIST, TagFinal.REFRESH);
        }
        execute(task);
    }

    private void loadMore() {
        if (isRefresh){

            return;
        }
        isRefresh=true;
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                ismyself,
                classId,
                kind_id,
                search,
                ++page,
                PAGE_NUM };
        ParamsTask task = new ParamsTask(params, TagFinal.SHAPE_MAIN_LIST,TagFinal.REFRESH_MORE);
        execute(task);
    }


    private void deleteDynamic(String dynamicid) {
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                dynamicid
        };
        ParamsTask task = new ParamsTask(params, deleteDynamic, TagFinal.DELETE_TAG,loadingDialog);
        execute(task);
    }


    private void setPraise(String dynamicId,int praise) {
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                Base.user.getName(),
                praise,
                dynamicId
        };
        ParamsTask task = new ParamsTask(params, setPraise, TagFinal.PRAISE_TAG,loadingDialog);
        execute(task);
    }


    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        Logger.e("zxx"," class  "+result);
        ReInfor infor;
        closeSwipeRefresh();

        String nameS=wcfTask.getName();
        if (nameS.equals(TagFinal.REFRESH)){
            infor= gson.fromJson(result, ReInfor.class);
            beans.clear();
            beans.addAll(infor.getWbs());
            adapter.setDataList(beans);
            adapter.setLoadState(2);
            return false;
        }
        if (nameS.equals(TagFinal.REFRESH_MORE)){
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
        if (nameS.equals(TagFinal.PRAISE_TAG)){
            infor= gson.fromJson(result, ReInfor.class);
            if (infor.getResult().equals("true")){
                toastShow(R.string.success_do);
            }else{
                toastShow(result);
            }
            refresh(false);
            return false;
        }
        if (nameS.equals(TagFinal.DELETE_TAG)){
            if (StringJudge.isSuccess(gson,result)){
                toastShow(R.string.success_do);
            }else{
                toastShow(result);
            }
            refresh(false);
            return false;
        }
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        adapter.setLoadState(2);
        toastShow(R.string.fail_loadmore);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_REFRESH:
                    refresh(false);
                    break;
                case CHIOCE_KIND:
                    kind_id=data.getStringExtra("shape_kind")==null?"":data.getStringExtra("shape_kind");
                    hradertypy.setText(data.getStringExtra("shape_name")==null?"":data.getStringExtra("shape_name"));
                    refresh(false);
                    break;
            }
        }
    }
    @OnClick(R.id.shape_type_header)
    void setHeader(){
        Intent ent = new Intent(mActivity, ShapeChioceTagActivity.class);
        startActivityForResult(ent,CHIOCE_KIND);
    }
}
