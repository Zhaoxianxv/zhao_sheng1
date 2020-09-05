package com.yfy.app.integral;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.example.zhao_sheng.R;
import com.yfy.app.XlistLefttTxtAdapter;
import com.yfy.app.integral.beans.ClassName;
import com.yfy.app.integral.beans.IntegralResult;
import com.yfy.final_tag.Base;
import com.yfy.base.XlistAdapter;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.dialog.LoadingDialog;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.view.swipe.xlist.XListView;
import com.yfy.view.swipe.xlist.XlistListener;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by yfyandr on 2017/6/20.
 */

public class HonorTeaActivity extends WcfActivity {


    private final String GET_AWARD_CLASS="get_award_class";
    @Bind(R.id.honor_tea_chioce_xlist)
    XListView xListView;
    private LoadingDialog dialog;

    private List<String> names=new ArrayList<>();
    private List<ClassName> classNames=new ArrayList<>();
    private XlistAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.integral_honor_tea);
        dialog=new LoadingDialog(mActivity);
        initSQToolbar();
        initView();

    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        if (xListView!=null){
            xListView.stopRefresh();
        }
        Logger.e("zxx","   "+result);
        IntegralResult re=gson.fromJson(result, IntegralResult.class);
        classNames=re.getClasses();
        if (StringJudge.isEmpty(classNames))return false;
        names.clear();
        for (ClassName name:classNames) {
            names.add(name.getClass_name());
        }
        adapter.notifyDataSetChanged(names);
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        if (xListView!=null){
            xListView.stopRefresh();
        }


    }

    public void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle("");

    }

    public void initView(){
        adapter=new XlistLefttTxtAdapter(mActivity,names);
        xListView.setAdapter(adapter);
        xListView.setPullLoadEnable(false);
        xListView.setXListViewListener(new XlistListener() {
            @Override
            public void onRefresh() {
                super.onRefresh();
                refresh();
            }


        });
        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0)return;
                String id=classNames.get(i-1).getClass_id();
                Intent intent=new Intent(mActivity,HonorTeaDetailActivity.class);
                intent.putExtra("class_id", ConvertObjtect.getInstance().getInt(id));
                startActivity(intent);
            }
        });
    }
    public void refresh(){

        Object[] params = new Object[] {
                Base.user.getSession_key(),
        };
        ParamsTask getTimid = new ParamsTask(params, GET_AWARD_CLASS, GET_AWARD_CLASS);
        execute(getTimid);
    }

}
