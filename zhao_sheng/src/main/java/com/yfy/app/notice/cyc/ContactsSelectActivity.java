package com.yfy.app.notice.cyc;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.zhao_sheng.R;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.notice.NoticeGetStuListReq;
import com.yfy.app.net.notice.NoticeGetTeaListReq;
import com.yfy.app.notice.adapter.SelectContactsAdapter;
import com.yfy.app.notice.bean.ChildBean;
import com.yfy.app.notice.bean.ContactsGroup;
import com.yfy.app.notice.bean.NoticeRes;
import com.yfy.app.notice.bean.OnScrollToListener;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.RecycleViewDivider;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactsSelectActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = ContactsSelectActivity.class.getSimpleName();
    private SelectContactsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_main);
        initSQToolbar();
        initRecycler();
        getContacts(true);
    }

    private void initSQToolbar() {
        assert toolbar!=null;
        String title=getData();
        toolbar.setTitle(title);
        toolbar.addMenuText(TagFinal.ONE_INT, R.string.ok);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (position==TagFinal.ONE_INT){
                    getContactsChild(adapter.getAllData());
                }
            }
        });
    }

    public void getContactsChild(List<ChildBean> groups){
        select_user.clear();
        for (ChildBean group:groups){
            if (group.isChick()){
                select_user.add(group);
            }
        }
        Intent intent=new Intent();
        intent.putParcelableArrayListExtra(TagFinal.OBJECT_TAG, select_user);
        setResult(RESULT_OK,intent);
        onPageBack();
    }



    private String type;
    private ArrayList<ChildBean> select_user=new ArrayList<>();
    private String getData() {
        type = getIntent().getExtras().getString(TagFinal.TYPE_TAG);
        select_user = getIntent().getExtras().getParcelableArrayList(TagFinal.OBJECT_TAG);
        if (type.equals(TagFinal.USER_TYPE_TEA)) {
            return "学校教师";
        } else {
            return "学生";
        }
    }

    private void getContacts(boolean is){
        if (is){
            getTea();
        }else{
            getStu();
        }
    }

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView = (RecyclerView) findViewById(R.id.public_recycler);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.public_swip);
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#4DB6AC"));
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                getContacts(true);
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
        recyclerView.getItemAnimator().setAddDuration(100);
        recyclerView.getItemAnimator().setRemoveDuration(100);
        recyclerView.getItemAnimator().setMoveDuration(200);
        recyclerView.getItemAnimator().setChangeDuration(100);
        adapter=new SelectContactsAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setType(type);
        adapter.setOnScrollToListener(new OnScrollToListener() {
            @Override
            public void scrollTo(int position) {
                recyclerView.scrollToPosition(position);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_TAG:
                    setResult(RESULT_OK,data);
                    onPageBack();
                    break;
            }
        }
    }

    /**
     * -----------------retrofit-----------------
     */


    private void getTea() {
        ReqEnv reqenv=new ReqEnv();
        ReqBody reqbody=new ReqBody();
        NoticeGetTeaListReq req_tea=new NoticeGetTeaListReq();
        //参数
        reqbody.noticeGetTeaListReq =req_tea;
        reqenv.body=reqbody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().notice_get_tea(reqenv);
        call.enqueue(this);
        showProgressDialog("正在加载...");

    }
    private void getStu() {
        ReqEnv reqenv=new ReqEnv();
        ReqBody reqbody=new ReqBody();
        NoticeGetStuListReq req_tea=new NoticeGetStuListReq();
        //参数

        reqbody.noticeGetStuListReq =req_tea;
        reqenv.body=reqbody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().notice_get_stu(reqenv);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {

        if (!isActivity())return;
        dismissProgressDialog();
        closeSwipeRefresh();
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.noticeGetTeaListRes !=null){
                String result=b.noticeGetTeaListRes.result;
                Logger.e(TagFinal.ZXX, "retrofit: "+result );
                NoticeRes info=gson.fromJson(result, NoticeRes.class);
                initCreat(info.getUserinfotx());

            }
            if (b.noticeGetStuListRes !=null){
                String result=b.noticeGetStuListRes.result;
                NoticeRes info=gson.fromJson(result, NoticeRes.class);
                initCreat(info.getUserinfotx());
            }

        }else{
            Logger.e(TagFinal.ZXX, "onResponse: 22" );
        }
    }

    private void initCreat(List<ContactsGroup> groups){
        List<ChildBean> alldata=new ArrayList<>();
        alldata.clear();
        int i=0;
        for(ContactsGroup group:groups){
            List<ChildBean> childs=group.getUserinfob();
            ChildBean parent=new ChildBean(group.getDepid(),group.getDepname(),TagFinal.TYPE_PARENT);
            parent.setExpand(false);
            parent.setAllNum(childs.size());
            parent.setSelectNum(0);
            parent.setGroup_index(i);
            i++;
            alldata.add(parent);
            initCreat(alldata, childs,group.getDepid());
        }
        initSelcetUser(alldata);
        adapter.setAllData(alldata);
        adapter.setLoadState(2);
    }
    private void initSelcetUser(List<ChildBean> alldata){
        if (StringJudge.isEmpty(select_user))return;
        for (ChildBean bean:select_user){
            for (ChildBean allbean:alldata){
                if (bean.getUserid().equals(allbean.getUserid())){
                    allbean.setChick(bean.isChick());
                    refreshParent(bean, alldata);
                    break;
                }

            }
        }
    }
    private void refreshParent(ChildBean childBean,List<ChildBean> alldata) {
        List<String> tags=childBean.getGroup_tag();
        for (String i:tags){
            for (ChildBean all:alldata){
                if (all.getType()==TagFinal.TYPE_CHILD)continue;
                if (all.getDepid().equals(i)){
                    int num=all.getSelectNum();
                    all.setSelectNum(num+1);
                    if (all.getAllNum()<=all.getSelectNum()){
                        all.setGroupChick(true);
                        all.setSelectNum(all.getAllNum());
                    }else if (all.getSelectNum()<=0){
                        all.setGroupChick(false);
                        all.setSelectNum(0);
                    }else{
                        all.setGroupChick(false);
                    }
                }
            }
        }

    }

    private void initCreat(List<ChildBean> inits,List<ChildBean> groups,String group_tag){
        for(ChildBean child:groups){
            boolean is_type=false;
            for (ChildBean add:inits){
                if (add.getType()==TagFinal.TYPE_PARENT)continue;

                if (add.getUserid().equals(child.getUserid())){
                    is_type=true;
                    List<String> list=add.getGroup_tag();
                    list.add(group_tag);
                    add.setGroup_tag(list);
                    break;
                }

            }
            if (!is_type){
                List<String> list=new ArrayList<>();
                list.add(group_tag);
                child.setGroup_tag(list);
                child.setType(TagFinal.TYPE_CHILD);
                child.setChick(false);
                inits.add(child);
            }

        }
    }


    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        if (!isActivity())return;
        Logger.e("onFailure  :"+call.request().headers().toString());
        dismissProgressDialog();
        closeSwipeRefresh();
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
