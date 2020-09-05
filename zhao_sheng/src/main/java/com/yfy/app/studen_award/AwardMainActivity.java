package com.yfy.app.studen_award;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.duty.adpater.DutyDateAdapter;
import com.yfy.app.studen_award.adapter.AwardMainAdapter;
import com.yfy.app.studen_award.bean.AwardB;
import com.yfy.app.studen_award.bean.AwardBean;
import com.yfy.app.studen_award.bean.AwardInfo;
import com.yfy.app.studen_award.bean.AwardInfor;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class AwardMainActivity extends WcfActivity {

    private static final String TAG = AwardMainActivity.class.getName();

    private String stu_id;
    private int class_id;
    private String title;

    @Bind(R.id.top_layout)
    View top_layout;

    private AwardMainAdapter adapter;
//    private List<AwardBean> awardBeens=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.award_main);
        initData();
        initSQToolbar();
        initRecycler();
        getStuData(stu_id);
    }
    private void initData(){
        Intent intent=getIntent();
        stu_id=intent.getStringExtra("stu_id")==null?Base.user.getIdU():intent.getStringExtra("stu_id");
        class_id=intent.getIntExtra("class_id",-1);
        title=intent.getStringExtra("stu_name")==null?Base.user.getName():intent.getStringExtra("stu_name");

    }

    private void initSQToolbar() {
        assert toolbar!=null;
        if (Base.user.getUsertype().equals(TagFinal.USER_TYPE_TEA)){
            toolbar.addMenuText(1,R.string.award);
            toolbar.setTitle(title);
        }else{
            toolbar.setTitle(title);
        }
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (position){
                    case 1:
                        if (Base.user.getUsertype().equals(TagFinal.USER_TYPE_TEA)){
                            Intent intent=new Intent(mActivity,AwardSendActivity.class);
                            intent.putExtra("id_more","0");
                            intent.putExtra("stu_id",stu_id);
                            intent.putExtra("class_id",class_id);
                            startActivityForResult(intent,TagFinal.UI_ADD);
                        }else{
                            Intent intent=new Intent(mActivity,AwardSendActivity.class);
                            startActivityForResult(intent,TagFinal.UI_ADD);
                        }
                        break;
                }
            }
        });
    }



    private void getStuData(String stu_id){
        if (Base.user.getUsertype().equals(TagFinal.USER_TYPE_TEA)){
            getscanStuAward(stu_id);
        }else{
            getStuAward(stu_id);
        }
    }


    private TextView top_title;
    private TextView scroe;
    private AppCompatTextView yes_num;
    private AppCompatTextView no_num;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    public void initRecycler(){

        top_title= findViewById(R.id.award_parent_title);
        scroe= findViewById(R.id.award_parent_score);
        yes_num= findViewById(R.id.award_parent_yes_num);
        no_num= findViewById(R.id.award_parent_no_num);


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
                getStuData(stu_id);

            }
        });

        recyclerView.addOnScrollListener(onScrollListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        xlist.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        adapter=new AwardMainAdapter(this);
        recyclerView.setAdapter(adapter);

    }



    private RecyclerView.OnScrollListener onScrollListener=new RecyclerView.OnScrollListener() {


        // 用来标记是否正在向上滑动
        private boolean isSlidingUpward = false;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            isSlidingUpward = dy > 0;
            LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int first=manager.findFirstVisibleItemPosition();
            RecyclerView.ViewHolder holder=recyclerView.findViewHolderForAdapterPosition(first+1);
            if (holder!=null){
                if (holder instanceof DutyDateAdapter.ParentViewHolder){
                    int headerMoveY = holder.itemView.getTop() - top_layout.getMeasuredHeight();
                    AwardB week=((AwardMainAdapter.ParentViewHolder) holder).bean;
                    if (headerMoveY<0) {
                        top_layout.setTranslationY(headerMoveY);
                    }
                    top_title.setText(week.getTermname());
                    scroe.setText(tab+week.getCount());
                    yes_num.setText(week.getLaughcount());
                    no_num.setText(week.getCrycount());
                } else if (holder instanceof DutyDateAdapter.ChildViewHolder){
                    top_layout.setTranslationY(0);
                }
            }
        }
//        @Override
//        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//            super.onScrollStateChanged(recyclerView, newState);
//            LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
//            // 当不滑动时
//            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                // 获取最后一个完全显示的itemPosition
//                int lastItemPosition = manager.findLastCompletelyVisibleItemPosition();
//                int itemCount = manager.getItemCount();
//
//                // 判断是否滑动到了最后一个item，并且是向上滑动
//                if (lastItemPosition == (itemCount - 1) && isSlidingUpward) {
//                    // 加载更多
//                    refresh(false,startdate ,enddate , TagFinal.REFRESH_MORE);
//                }
//            }
//        }
    };


//
//    private AwardMainAdapter.OnAwardClickListener onImage=new AwardMainAdapter.OnAwardClickListener() {
//        @Override
//        public void onPictureClick(int index, List<String> pictureList) {
//            Intent intent=new Intent(mActivity, SingePicShowActivity.class);
//            Bundle b=new Bundle();
//            b.putString(TagFinal.ALBUM_SINGE_URI, pictureList.get(index));
//            intent.putExtras(b);
//            startActivity(intent);
//        }
//    };




    public void getStuAward(String stu_id ){
        Object[] params=new Object[]{
                Base.user.getSession_key(),
                0,//奖励类型
                "",//时间
                stu_id,//
                0,

        };
        ParamsTask getStuAward=new ParamsTask(params,TagFinal.AWARD_STU_GET_INFO,TagFinal.AWARD_STU_GET_INFO);
        execute(getStuAward);
        showProgressDialog("");
    }
    public void getscanStuAward(String stu_id ){
        Object[] params=new Object[]{
                Base.user.getSession_key(),
                0,
                "",
                stu_id,
                0,
        };
        ParamsTask scanStuAward=new ParamsTask(params,TagFinal.AWARD_TEA_GET_STU_INFO,TagFinal.AWARD_TEA_GET_STU_INFO);
        execute(scanStuAward);
        showProgressDialog("");
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


    private String tab="得分次数：";
    private void getAdapter(List<AwardInfo> terms){
        List<AwardB> awardBS=new ArrayList<>();
        for (AwardInfo info:terms){
            awardBS.add(new AwardB(info.getTermname(),info.getCount(),info.getLaughcount(),info.getCrycount()));
            initBean(awardBS,info.getAwardinfo());
        }
        if (StringJudge.isEmpty(awardBS))return;
        top_title.setText(awardBS.get(0).getTermname());
        scroe.setText(tab+awardBS.get(0).getCount());
        yes_num.setText(awardBS.get(0).getLaughcount());
        no_num.setText(awardBS.get(0).getCrycount());
        adapter.setDataList(awardBS);
        adapter.setLoadState(TagFinal.LOADING_COMPLETE);
    }
//  public AwardB(String date, String type, String content, String score, List<String> images, String teacher)
    private void initBean(List<AwardB> awardBS,List<AwardBean> list){
        for (AwardBean info:list){
            awardBS.add(new AwardB(info.getDate(),info.getType(),info.getContent(),info.getScore(),info.getImages(),info.getTeacher()));
        }
    }
    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        if (!isActivity())return false;
        dismissProgressDialog();
        closeSwipeRefresh();
        Logger.e(result);
        AwardInfor infor=gson.fromJson(result,AwardInfor.class);
        String name=wcfTask.getName();
        if (name.equals(TagFinal.AWARD_TEA_GET_STU_INFO)){
            getAdapter(infor.getTerms());
        }
        if (name.equals(TagFinal.AWARD_STU_GET_INFO)){
            getAdapter(infor.getTerms());
        }
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        if (!isActivity())return ;
        dismissProgressDialog();
        closeSwipeRefresh();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_ADD:
                    getStuData(stu_id);
                    break;
//                case AWARD_TYPE:
//                    typeid=data.getIntExtra("type_id",0);
//                    getscanStuAward();
//                    break;
            }

        }
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
