package com.yfy.app.answer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.google.gson.Gson;
import com.yfy.app.answer.AddQuestionActivity;
import com.yfy.app.answer.AnswerItemDetailNewsActivity;
import com.yfy.app.answer.AnswerMainActivity;
import com.yfy.app.answer.bean.AnswerListBean;
import com.yfy.app.answer.bean.AnswerResult;
import com.yfy.final_tag.Base;
import com.yfy.base.fragment.WcfFragment;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.view.swipe.xlist.XListView;
import com.yfy.view.swipe.xlist.XlistListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by yfyandr on 2017/8/1.
 */

public class Tab3Fragment extends WcfFragment {
    private static final String TAG = "zxx";

    @Bind(R.id.answer_tab1_xlist)
    XListView xlist;
    private Tab1Adapter adapter;
    private List<AnswerListBean> answers=new ArrayList<>();

    private int sort;
    private int isanswer=0;//isanswer 0 所有 1已解决 2未解决
    private int page=0;
    private String mothed="get_QA_list";
    private boolean loading=false;//加载数据状态
    private Gson gson=new Gson();



    @Override
    public void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.answer_tab1_fragment);
        AnswerMainActivity.setIsInitData(2,refresh);


        initView();

    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
//        Log.e(TAG, "onSuccess: "+result );
        loading=false;

        if (xlist!=null){
            xlist.stopRefresh();
            xlist.stopLoadMore();
        }
        String name=wcfTask.getName();
        AnswerResult answer=gson.fromJson(result, AnswerResult.class);

        if (name.equals("refresh")){
            int size=answer.getQA_list().size();
            if (size!=FIRST_PAGE){
                toast(R.string.success_loadmore_end);
            }
            answers.clear();
            answers=answer.getQA_list();
            adapter.notifyDataSetChanged(answers);
        }
        if (name.equals("load_more")){
            int size=answer.getQA_list().size();
            if (size!=FIRST_PAGE){
                toast(R.string.success_loadmore_end);
            }
            answers.addAll(answer.getQA_list());
            adapter.notifyDataSetChanged(answers);
        }

        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        loading=false;
        if (xlist!=null){
            xlist.stopRefresh();
            xlist.stopLoadMore();
        }


    }


    public void initView(){

        View view= LayoutInflater.from(mActivity).inflate(R.layout.public_item_singe_top_txt,null);
        view.setBackgroundColor(ColorRgbUtil.getGrayText());
        TextView toptext= (TextView) view.findViewById(R.id.public_center_txt_add);
        toptext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mActivity,AddQuestionActivity.class);
                startActivity(intent);
            }
        });
        xlist.setPullLoadEnable(true);
        xlist.setPullRefreshEnable(true);
        adapter=new Tab1Adapter(mActivity,answers);
        xlist.setAdapter(adapter);
        xlist.addHeaderView(view);
        xlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(mActivity, AnswerItemDetailNewsActivity.class);
                intent.putExtra("item",answers.get(i-1));
                startActivity(intent);
            }
        });
        xlist.setXListViewListener(new XlistListener() {
            @Override
            public void onRefresh() {
                super.onRefresh();
                refreshData();
            }

            @Override
            public void onLoadMore() {
                super.onLoadMore();
                loadMore();
            }
        });
    }



    //viewpager通知更新数据
    private AnswerMainActivity.SchoolInterface refresh=new AnswerMainActivity.SchoolInterface() {
        @Override
        public void isChioce(int id) {
            sort=id;
            refreshData();
            page=0;

        }
    };



    private void refreshData(){
        if(loading){

            xlist.stopRefresh();
            return;
        }
        loading=true;
        page = 0;
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                "",
                sort,
                isanswer,
                page,
                FIRST_PAGE
        };
        ParamsTask refreshTask = new ParamsTask(params, mothed, "refresh");
        execute(refreshTask);
    }
    private void loadMore(){
        if(loading){
            xlist.stopLoadMore();
            return;
        }
        loading=true;
        page++;
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                "",
                sort,
                isanswer,
                page,
                FIRST_PAGE
        };
        ParamsTask refreshTask = new ParamsTask(params, mothed, "load_more");
        execute(refreshTask);
    }
}
