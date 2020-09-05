package com.yfy.app.tea_event;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.zhao_sheng.R;
import com.yfy.app.tea_event.adapter.TeaDAdapter;
import com.yfy.app.tea_event.bean.TeaClass;
import com.yfy.app.tea_event.bean.TeaDe;
import com.yfy.app.tea_event.bean.TeaDeBean;
import com.yfy.app.tea_event.bean.TeaDeInfo;
import com.yfy.base.activity.BaseActivity;
import com.yfy.db.UserPreferences;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.recycerview.RecycleViewDivider;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TeaDActivity extends BaseActivity {

    private static final String TAG = TeaDActivity.class.getSimpleName();



    private TeaDAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_main);
        initRecycler();
        getData();



    }

    private TeaClass teaClass;
    private String term_id;
    private void getData(){
        teaClass = getIntent().getParcelableExtra(TagFinal.OBJECT_TAG);
        term_id=getIntent().getStringExtra(TagFinal.ID_TAG);
        if (teaClass!=null){
            initSQtoolbar();
            initData(teaClass.getCheckresault());
        }
    }

    private void initData(List<TeaDeInfo>  res){
        if (StringJudge.isEmpty(res))return;
        List<TeaDeBean> list=new ArrayList<>();
        List<TeaDeBean> badlist=new ArrayList<>();
        for (TeaDeInfo info:res){

            TeaDeBean bean;
            switch (info.getType()){
                case "单选":
                    bean=new TeaDeBean();
                    bean.setView_type(TagFinal.TYPE_TOP);
                    bean.setType(info.getType());
                    bean.setEvaluatetitle(info.getEvaluatetitle());
                    bean.setEvaluateid(info.getEvaluateid());
                    list.add(bean);
                    for (TeaDe teade:info.getEvaluateselect()){
                        if (teade.getTitle().trim().equals("满意率")){
                            bean = new TeaDeBean();
                            bean.setTitle("");
                            bean.setView_type(TagFinal.TYPE_FOOTER);
                            list.add(bean);
                        }
                        bean=new TeaDeBean();
                        bean.setView_type(TagFinal.TYPE_PARENT);
                        bean.setTitle(teade.getTitle());
                        bean.setPercentage(teade.getPercentage());
                        bean.setMaxcount(teade.getMaxcount());
                        bean.setCheckcount(teade.getCheckcount());
                        bean.setId(teade.getId());
                        bean.setIscheck(teade.getIscheck());
                        list.add(bean);
                        if (StringJudge.isEmpty(teade.getEvaluatelast())){
                        }else{
                            bean=new TeaDeBean();
                            bean.setTitle(teade.getTitle()+"选项详情");
                            bean.setView_type(TagFinal.TYPE_FOOTER);
                            badlist.add(bean);
                            for (TeaDeBean tag:teade.getEvaluatelast()){
                                tag.setView_type(TagFinal.TYPE_CHILD);
                                badlist.add(tag);
                            }
                        }
                    }
                    list.addAll(badlist);
                    break;
                case "文本":
                    TeaDeBean te=new TeaDeBean();
                    te.setView_type(TagFinal.TYPE_ITEM);
                    te.setType(info.getType());
                    te.setEvaluatetitle(info.getEvaluatetitle());
                    te.setEvaluateid(info.getEvaluateid());


                    String content=StringUtils.stringToArraysGetString(info.getWords(), "\n\n");
                    te.setContent(content);
                    te.setMaxword(info.getMaxword());
                    list.add(te);
                    break;

            }
        }
        adapter.setDataList(list);
        adapter.setLoadState(TagFinal.LOADING_COMPLETE);
    }

    private void initSQtoolbar() {
        assert toolbar!=null;
        toolbar.setTitle(teaClass.getClassname()+" "+teaClass.getCoursename());

        if (StringJudge.isEmpty(UserPreferences.getInstance().getClassIds())){
            return;
        }else{
            String class_ids=UserPreferences.getInstance().getClassIds();
            List<String> list = Arrays.asList(class_ids.split(","));
            for (String id:list){
                if (id.equals(teaClass.getClassid())){
                    toolbar.addMenuText(TagFinal.ONE_INT,"未完名单" );
                    toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
                        @Override
                        public void onClick(View view, int position) {

                            Intent intent=new Intent(mActivity,TeaStuActivity.class);
                            intent.putExtra(TagFinal.ID_TAG,teaClass.getClassid() );
                            intent.putExtra(TagFinal.id,term_id );
                            startActivity(intent);
                        }
                    });
                }
            }
        }

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
        adapter=new TeaDAdapter(mActivity);
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







    /**
     * ----------------------------retrofit-----------------------
     */



    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
