package com.yfy.app.integral;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.integral.adapter.TermCommtAdapter;
import com.yfy.app.integral.beans.IntegralResult;
import com.yfy.app.integral.beans.TermCommt;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.db.UserPreferences;
import com.yfy.final_tag.dialog.LoadingDialog;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.view.swipe.xlist.XListView;
import com.yfy.view.swipe.xlist.XlistListener;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class TermidCommtActivity extends WcfActivity {
    private final int TERM_ID=6;
    private String mothed="get_stu_award";//学优生
    private LoadingDialog loadingDialog;
    private int term_id;
    private int page=0;
    @Bind(R.id.term_list_commt)
    XListView xlist;
    private boolean loading=false;
    private TermCommtAdapter adapter;
    private List<TermCommt> termcommts=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.termid_commt);
        loadingDialog=new LoadingDialog(mActivity);
        term_id=ConvertObjtect.getInstance().getInt(UserPreferences.getInstance().getTermId());
        initSQToolbar();
        initView();
        refresh(true);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            if (requestCode==TERM_ID){
                String name,id;
                name=data.getStringExtra("data_name");
                id=data.getStringExtra("data_id");
                menu1.setText(name);
                term_id= ConvertObjtect.getInstance().getInt(id);
                refresh(true);
            }
        }
    }
    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        loading=false;
        loading = false;
        Logger.e(TagFinal.ZXX, "onSuccess: " + result);
        if (xlist != null) {
            xlist.stopLoadMore();
            xlist.stopRefresh();
        }
        String name = wcfTask.getName();
        IntegralResult info = gson.fromJson(result, IntegralResult.class);
        if (name.equals("refresh")){
            termcommts.clear();
            if (info.getTeawords().size()==0){
                toastShow(R.string.success_not_details);
            }
            termcommts.addAll(info.getTeawords());
        }
        if (name.equals("loadmore")){
            if (info.getTeawords().size()!=PAGE_NUM){
                toastShow(R.string.success_loadmore_end);
            }
            termcommts.addAll(info.getTeawords());
        }
        adapter.notifyDataSetChanged(termcommts);
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        loading=false;

    }

    private TextView menu1;
    public void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle("学期评语");
        menu1=toolbar.addMenuText(1,"");
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent=new Intent(mActivity,ChangeTermActivity.class);
                startActivityForResult(intent,TERM_ID);
            }
        });
    }
    public void initView(){
        menu1.setText(UserPreferences.getInstance().getTermName());
        adapter=new TermCommtAdapter(mActivity,termcommts);

        xlist.setPullLoadEnable(true);
        xlist.setPullRefreshEnable(true);
        xlist.setAdapter(adapter);
        xlist.setXListViewListener(new XlistListener() {
            @Override
            public void onRefresh() {
                super.onRefresh();
                refresh(true);
            }

            @Override
            public void onLoadMore() {
                super.onLoadMore();
                refresh(false);
            }
        });
    }


    public void refresh(boolean is){
        if (Base.user.getUsertype().equals(TagFinal.USER_TYPE_TEA)){
            mothed="get_teawords_tea";
        }else{
            mothed="get_teawords_stu";
        }
        if (is) {
            if (loading) {
                xlist.stopRefresh();
                return;
            }
            page = 0;
        } else {
            if (loading) {
                xlist.stopLoadMore();
                return;
            }
            page++;
        }

        loading=true;
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                term_id,
                page,
                PAGE_NUM
        };
        ParamsTask refreshTask;
        if (is) {
            refreshTask = new ParamsTask(params, mothed, "refresh");
        } else {
            refreshTask = new ParamsTask(params, mothed, "loadmore");
        }
        execute(refreshTask);
    }

}
