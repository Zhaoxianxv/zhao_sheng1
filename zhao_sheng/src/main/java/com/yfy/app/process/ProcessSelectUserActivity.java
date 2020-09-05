package com.yfy.app.process;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.yfy.app.notice.bean.OnScrollToListener;
import com.yfy.app.process.adapter.ProcessSelectUserAdapter;
import com.yfy.app.process.bean.ProBeanChild;
import com.yfy.app.process.bean.ProBeanGroup;
import com.yfy.app.process.bean.ProBeanRes;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.RecycleViewDivider;
import com.yfy.view.SQToolBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ProcessSelectUserActivity extends BaseActivity {

    private static final String TAG = ProcessSelectUserActivity.class.getSimpleName();


    public ProcessSelectUserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_main);
        getData();

    }


    private String user_type,title,max_num,process_id;
    private int index_position;
    private void  getData(){
        Intent intent=getIntent();
        user_type=intent.getStringExtra(Base.type);
        title=intent.getStringExtra(Base.title);
        process_id=intent.getStringExtra(Base.id);
        max_num=intent.getStringExtra(Base.num);
        index_position=intent.getIntExtra(Base.index,0);

        initSQToolbar();
        initRecycler();
        adapter.setMax_num(max_num);
        adapter.setIndex_position(index_position);
        getContants();
    }
    private void getContants(){
        if (user_type.equalsIgnoreCase(TagFinal.USER_TYPE_TEA)){
            getTea(Base.process_type);
        }else{
            getStu(Base.process_type);
        }
    }


    public void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle(title);
        if (max_num.equalsIgnoreCase("1"))return;

        toolbar.addMenuText(TagFinal.ONE_INT,"确定");
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent=new Intent();

                intent.putExtra(Base.type,max_num);
                intent.putExtra(Base.index,index_position);
                setResult(RESULT_OK);
                finish();
            }
        });

    }



    /**
     * ----------
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
                getContants();
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mActivity,
                LinearLayoutManager.HORIZONTAL,
                1,
                getResources().getColor(R.color.gray)));
        adapter=new ProcessSelectUserAdapter(this);
        recyclerView.setAdapter(adapter);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_REFRESH:
                    break;
            }
        }
    }
    /**
     * ---------------------------retrofit-----------------------
     */

    private void getTea(String type) {
        ReqEnv reqenv=new ReqEnv();
        ReqBody reqbody=new ReqBody();
        NoticeGetTeaListReq req=new NoticeGetTeaListReq();
        //参数
        req.setType(type);
        req.setId(process_id);

        reqbody.noticeGetTeaListReq =req;
        reqenv.body=reqbody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().notice_get_tea(reqenv);
        call.enqueue(this);
        showProgressDialog("");

    }
    private void getStu(String type) {
        ReqEnv reqenv=new ReqEnv();
        ReqBody reqbody=new ReqBody();
        NoticeGetStuListReq req=new NoticeGetStuListReq();
        //参数
        req.setType(type);
        reqbody.noticeGetStuListReq =req;
        reqenv.body=reqbody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().notice_get_stu(reqenv);
        call.enqueue(this);
        showProgressDialog("");
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

            if (b.noticeGetTeaListRes !=null){
                String result=b.noticeGetTeaListRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                ProBeanRes res=gson.fromJson(result, ProBeanRes.class);
                initCreat(res.getData());
            }
            if (b.noticeGetStuListRes !=null){
                String result=b.noticeGetStuListRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                ProBeanRes res=gson.fromJson(result, ProBeanRes.class);
                initCreat(res.getData());
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

    private ArrayList<ProBeanChild> select_user=new ArrayList<>();

    private void initCreat(List<ProBeanGroup> groups){
        List<ProBeanChild> alldata=new ArrayList<>();


        alldata.clear();
        int i=0;
        for(ProBeanGroup group:groups){
            List<ProBeanChild> childs=group.getUserinfo();
            ProBeanChild parent=new ProBeanChild(group.getDepartid(),group.getDepartname(),TagFinal.TYPE_PARENT);
            parent.setExpand(false);
            parent.setAllNum(childs.size());
            parent.setSelectNum(0);
            parent.setGroup_index(i);
            i++;
            if (StringJudge.isEmpty(childs)){
                parent.setView_type(TagFinal.TYPE_CHECK);
                alldata.add(parent);
            }else{
                alldata.add(parent);
                initCreat(alldata, childs,group.getDepartid());
            }

        }
//        initSelcetUser(alldata);
        adapter.setAllData(alldata);
        adapter.setLoadState(2);
    }
    private void initSelcetUser(List<ProBeanChild> alldata){
        if (StringJudge.isEmpty(select_user))return;
        for (ProBeanChild bean:select_user){
            for (ProBeanChild allbean:alldata){
                if (bean.getUserid().equals(allbean.getUserid())){
                    allbean.setChick(bean.isChick());
                    refreshParent(bean, alldata);
                    break;
                }

            }
        }
    }
    private void refreshParent(ProBeanChild childBean,List<ProBeanChild> alldata) {
        List<String> tags=childBean.getGroup_tag();
        for (String i:tags){
            for (ProBeanChild all:alldata){
                if (all.getView_type()==TagFinal.TYPE_CHILD||all.getView_type()==TagFinal.TYPE_CHECK)continue;
                if (all.getDepartid().equals(i)){
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

    private void initCreat(List<ProBeanChild> inits,List<ProBeanChild> groups,String group_tag){
        for(ProBeanChild child:groups){
            boolean is_type=false;
            for (ProBeanChild add:inits){
                if (add.getView_type()==TagFinal.TYPE_PARENT||add.getView_type()==TagFinal.TYPE_CHECK)continue;
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
                child.setDepartid(group_tag);
                child.setView_type(TagFinal.TYPE_CHILD);
                child.setChick(false);
                inits.add(child);
            }

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
