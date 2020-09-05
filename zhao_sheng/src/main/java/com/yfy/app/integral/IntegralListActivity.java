package com.yfy.app.integral;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.example.zhao_sheng.R;
import com.yfy.app.integral.adapter.IntegralScroeAdapter;
import com.yfy.app.integral.beans.IntegralResult;
import com.yfy.app.integral.beans.IntegralScroe;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.dialog.LoadingDialog;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.view.swipe.xlist.XListView;
import com.yfy.view.swipe.xlist.XlistListener;
import com.yfy.final_tag.StringJudge;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class IntegralListActivity extends WcfActivity {


    @Bind(R.id.integral_list_xlist)
    XListView list;


    private IntegralScroeAdapter adapter;
    private List<IntegralScroe> scroes=new ArrayList<>();


    private final String GET_MY="get_my_points";
    private LoadingDialog dialog;
    private int pager=0;
    private boolean isloading=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.integral_list);
        dialog=new LoadingDialog(mActivity);
        initSQToolbar();

        initView();

    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    private void initSQToolbar() {

    }



    private void initView() {

        list.setPullLoadEnable(true);
        list.setPullRefreshEnable(true);


        adapter=new IntegralScroeAdapter(mActivity,scroes);
        list.setAdapter(adapter);
        list.setXListViewListener(xlistListener);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==adapterView.getCount()){
                    loadMore();
                }

            }
        });


    }
    private XlistListener xlistListener=new XlistListener() {
        @Override
        public void onRefresh() {
            super.onRefresh();
            refresh();
        }

        @Override
        public void onLoadMore() {
            super.onLoadMore();
            loadMore();
        }
    };

    private void refresh(){
        if (isloading){
            return;
        }
        pager=0;
        Object[] params=new Object[]{
                Base.user.getSession_key(),
                pager,
                PAGE_NUM
        };
        ParamsTask refresh=new ParamsTask(params,GET_MY,"refresh",dialog);
        execute(refresh);
    }
    private void loadMore(){
        if (isloading){
            return;
        }
        pager++;
        Object[] params=new Object[]{
                Base.user.getSession_key(),
                pager,
                PAGE_NUM
        };
        ParamsTask refresh=new ParamsTask(params,GET_MY,"more",dialog);
        execute(refresh);
    }

    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        if (list!=null){
            list.stopRefresh();
            list.stopLoadMore();
        }
        Logger.e("zxx","get_my_points  "+result);
        if (StringJudge.isEmpty(result)){
            return false;
        }
        IntegralResult info=gson.fromJson(result, IntegralResult.class);

        String name=wcfTask.getName();
        if (name.equals("more")){
            if (StringJudge.isEmpty(info.getPoints())){
                toastShow(R.string.success_loadmore_end);
            }
            if (info.getPoints().size()!=PAGE_NUM){
                toastShow(R.string.success_loadmore_end);
            }
            scroes.addAll(info.getPoints());

        }
        if (name.equals("refresh")){
            if (StringJudge.isEmpty(info.getPoints())){
                toastShow(R.string.success_not_details);
            }
            if (info.getPoints().size()!=PAGE_NUM){
                toastShow(R.string.success_loadmore_end);
            }
            scroes.clear();
            scroes.addAll(info.getPoints());

        }
        adapter.notifyDataSetChanged(scroes);
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        if (list!=null){
            list.stopRefresh();
            list.stopLoadMore();
        }
        String name=wcfTask.getName();
        if (name.equals("more")){
//            pager--;
        }
    }

}
