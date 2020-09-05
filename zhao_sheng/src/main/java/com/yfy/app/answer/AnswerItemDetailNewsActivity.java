package com.yfy.app.answer;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.album.SingePicShowActivity;
import com.yfy.app.answer.bean.AnswerListBean;
import com.yfy.app.answer.bean.AnswerResult;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.dialog.LoadingDialog;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.dialog.DialogTools;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.glide.GlideTools;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.final_tag.recycerview.DefaultItemAnimator;
import com.yfy.final_tag.recycerview.EndlessRecyclerOnScrollListener;
import com.yfy.final_tag.recycerview.RecycleViewDivider;


import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

public class AnswerItemDetailNewsActivity extends WcfActivity implements View.OnClickListener{


    RecyclerView xlist;
    ImageView head;
    ImageView bg;
    TextView content;
    TextView time;
    FloatingActionButton fab;
    private AnswerListBean pro_bean;

    private final String GET_ALL = "get_QA_answer_list";
    private final String PRAISE = "QA_praise_anwser";
    private final String ADOPT = "QA_accept_anwser";//采纳
    private final String DEL_QA = "del_QA_anwser";//del
    private boolean loading = false;//加载数据状态
    private int page = 0;

    private LoadMoreAdapter adapter;
    private List<AnswerListBean> answerListBeens = new ArrayList<>();


    private LoadingDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answer_item_detail_new);
        dialog = new LoadingDialog(mActivity);
        getData();



    }


    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        loading = false;
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
        AnswerResult infor = gson.fromJson(result, AnswerResult.class);
        String name = wcfTask.getName();
        if (name.equals(TagFinal.REFRESH)) {
            answerListBeens = infor.getQA_list();
            adapter.setDataList(answerListBeens);
            if (answerListBeens.size()< TagFinal.TEN_INT){
                adapter.setLoadState(2);
            }else{
                adapter.setLoadState(2);
            }
        }
        if (name.equals(TagFinal.REFRESH_MORE)) {
            answerListBeens.addAll(infor.getQA_list());
            adapter.setDataList(answerListBeens);
            if (infor.getQA_list().size()< TagFinal.TEN_INT){
                adapter.setLoadState(3);
            }else{
                adapter.setLoadState(2);
            }
        }
        if (name.equals(PRAISE)) {
            if (infor.getResult().equals("false")){
                toastShow(infor.getError_code());
                return false;
            }
            refresh();
        }
        if (name.equals(ADOPT)) {
            if (infor.getResult().equals("false")){
                toastShow(infor.getError_code());
                return false;
            }
            refresh();
        }
        if (name.equals(DEL_QA)) {
            if (infor.getResult().equals("false")){
                toastShow(infor.getError_code());
                return false;
            }
            refresh();
        }
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
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
        adapter.setLoadState(3);
        loading = false;

    }


    //菜单 返回true(显示) false（）
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    //点击菜单
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                Toast.makeText(this, "点击了item1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.item2:
                Toast.makeText(this, "点击了item2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.item3:
                Toast.makeText(this, "点击了item3", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.answer_fab)
    void setFab() {
        Intent intent = new Intent(mActivity, AddAnswerActivity.class);
        intent.putExtra("id",pro_bean.getId());
        startActivity(intent);
    }

    public void getData() {
        pro_bean = getIntent().getParcelableExtra("item");
        initView();
        initToolbar();
        initCollapsing();
    }
    //配置Toolbar布局

    /**
     * Toolbar 的NavigationIcon大小控制mipmap
     */
    public void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.answer_title_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(

                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    //配置CollapsingToolbarLayout布局
    public void initCollapsing() {
        //使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，设置到Toolbar上则不会显示
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.answer_collapsing);
        mCollapsingToolbarLayout.setTitle(pro_bean.getUser_name());
        //通过CollapsingToolbarLayout修改字体颜色
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);//设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后Toolbar上字体的颜色
    }

    public void initView() {
        xlist= (RecyclerView) findViewById(R.id.answer_detail_xlist);
        head= (ImageView) findViewById(R.id.answer_head_bg);
        bg= (ImageView) findViewById(R.id.answer_head);
        content= (TextView) findViewById(R.id.answer_head_content);
        time= (TextView) findViewById(R.id.answer_head_time);
        fab= (FloatingActionButton) findViewById(R.id.answer_fab);
        GlideTools.chanCircle(mActivity,pro_bean.getUser_avatar(),head, R.drawable.head_user);


        content.setText(pro_bean.getContent());
        time.setText(pro_bean.getTime());
        if (StringJudge.isEmpty(pro_bean.getImage())) {
        } else {
            GlideTools.loadImage(mActivity,pro_bean.getImage(),bg);
        }

        initViews();
    }
    private SwipeRefreshLayout swipeRefreshLayout;
    private AppBarLayout appBarLayout;
    public void initViews(){
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (verticalOffset >= 0) {
                    swipeRefreshLayout.setEnabled(true);
                } else {
                    swipeRefreshLayout.setEnabled(false);
                }
            }
        });
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#4DB6AC"));
        // 模拟获取数据

        adapter = new LoadMoreAdapter(mActivity,answerListBeens,pro_bean.getUser_name());
        xlist.setLayoutManager(new LinearLayoutManager(this));
//        xlist.setLayoutManager(new GridLayoutManager(this, 1));

        xlist.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        xlist.addItemDecoration(new RecycleViewDivider(
                        mActivity,
                        LinearLayoutManager.HORIZONTAL,
                        1,
                        getResources().getColor(R.color.gray)));


        adapter.setViewOnClick(new LoadMoreAdapter.ViewOnClick() {
            @Override
            public void isparise(int is, String id) {
                isPraise(is, id);
            }
            @Override
            public void adopt(ImageView adopt, String id, String del, String is_answer) {
                answer_id=id;
                is_answer_this=is_answer;
                showPopupWindow(adopt,del);
            }
        });
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                refreshData();
            }
        });
        xlist.setOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                loadMore();
            }
        });

        View view = LayoutInflater.from(mActivity).inflate(R.layout.public_item_singe_top_txt_center, null);
        TextView top = (TextView) view.findViewById(R.id.public_top_text);
        top.setText(pro_bean.getAnswer_count() + " 条回答");
//        xlist.addView(view);

        xlist.setAdapter(adapter);


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
    private void refreshData() {
        if (loading) {

            return;
        }
        loading = true;
        page = 0;
        Object[] params = new Object[]{
                Base.user.getSession_key(),
                ConvertObjtect.getInstance().getInt(pro_bean.getId()),
                page,
                TagFinal.TEN_INT
        };
        ParamsTask refreshTask = new ParamsTask(params, GET_ALL, TagFinal.REFRESH);
        execute(refreshTask);
    }

    private void loadMore() {
        if (loading) {
            return;
        }
        loading = true;
        page++;
        Object[] params = new Object[]{
                Base.user.getSession_key(),
                ConvertObjtect.getInstance().getInt(pro_bean.getId()),
                page,
                TagFinal.TEN_INT
        };
        ParamsTask refreshTask = new ParamsTask(params, GET_ALL, TagFinal.REFRESH_MORE);
        adapter.setLoadState(1);
        execute(refreshTask);
    }


    private String answer_id,is_answer_this;
    //adpater 接口
    private AnswerDetailAdapter.ViewOnClick onClick = new AnswerDetailAdapter.ViewOnClick() {
        @Override
        public void isparise(int is, String id) {
            isPraise(is, id);

        }

        @Override
        public void bigImage(String url) {
            Intent intent = new Intent(mActivity, SingePicShowActivity.class);

            Bundle b=new Bundle();
            b.putString(TagFinal.ALBUM_SINGE_URI,url);
            intent.putExtras(b);
            startActivity(intent);
        }

        @Override
        public void adopt(ImageView icon, String id,String del,String is_answer ) {
            answer_id=id;
            is_answer_this=is_answer;
            showPopupWindow(icon,del);
        }
    };


    //赞
    public void isPraise(int type, String answerid) {
        Object[] params = new Object[]{
                Base.user.getSession_key(),
                type,
                answerid

        };
        ParamsTask praise = new ParamsTask(params, PRAISE, PRAISE, dialog);
        execute(praise);
    }

    //ADOPT
    public void isAdopt(String answerid) {
        if (is_answer_this.equals("true")){
            mDialog();
            return;
        }
        Object[] params = new Object[]{
                Base.user.getSession_key(),
                pro_bean.getId(),
                answerid
        };
        ParamsTask praise = new ParamsTask(params, ADOPT, ADOPT, dialog);
        execute(praise);
    }
    public  void mDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage("已采纳");
        builder.setNegativeButton(R.string.app_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }
    //delete
    public void deleteItem(String answerid) {
        Object[] params = new Object[]{
                Base.user.getSession_key(),
                answerid
        };
        ParamsTask praise = new ParamsTask(params, DEL_QA, DEL_QA, dialog);
        execute(praise);
    }

    //用于操作后的刷新
    private void refresh() {
        page = 0;
        Object[] params = new Object[]{
                Base.user.getSession_key(),
                ConvertObjtect.getInstance().getInt(pro_bean.getId()),
                page,
                PAGE_NUM
        };
        ParamsTask refreshTask = new ParamsTask(params, GET_ALL, "refresh");
        execute(refreshTask);
    }

    private PopupWindow mPopWindow;
    private void showPopupWindow(View rootview,String del) {
        //设置contentView
        View contentView;
        if (del.equals("true")){
            contentView = LayoutInflater.from(mActivity).inflate(R.layout.popuplayout, null);
            TextView tv1 = (TextView)contentView.findViewById(R.id.pop_computer);
            TextView tv2 = (TextView)contentView.findViewById(R.id.pop_financial);
            tv1.setOnClickListener(this);
            tv2.setOnClickListener(this);
        }else{
            contentView = LayoutInflater.from(mActivity).inflate(R.layout.popuplayout_singe, null);
            TextView tv3 = (TextView)contentView.findViewById(R.id.singe_adpot);
            tv3.setOnClickListener(this);
        }
        mPopWindow = new PopupWindow(
                contentView,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                true);
        mPopWindow.setContentView(contentView);
        //设置各个控件的点击响应



        //显示PopupWindow
//        mPopWindow.setOutsideTouchable(true);
//        mPopWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
//        mPopWindow.showAsDropDown(rootview, Gravity.BOTTOM, 0, 0);
        mPopWindow.showAsDropDown(rootview);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.pop_computer:{
                deleteItem(answer_id);
                mPopWindow.dismiss();
            }
                break;
            case R.id.pop_financial:{
                isAdopt(answer_id);
                mPopWindow.dismiss();
            }
                break;
            case R.id.singe_adpot:{
                isAdopt(answer_id);
                mPopWindow.dismiss();
            }
                break;

        }
    }


    @OnClick(R.id.answer_head_content)
    void setShow(){
        String s=content.getText().toString();
        if (StringJudge.isEmpty(s)){
            s="";
        }
        DialogTools.getInstance().showDialog(mActivity,"",s);
    }

}
