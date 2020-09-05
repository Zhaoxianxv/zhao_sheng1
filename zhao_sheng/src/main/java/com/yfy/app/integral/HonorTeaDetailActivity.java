package com.yfy.app.integral;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.integral.adapter.HonorTeaAdapter;
import com.yfy.app.integral.beans.HonorStu;
import com.yfy.app.integral.beans.IntegralResult;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.dialog.LoadingDialog;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.view.swipe.xlist.XListView;
import com.yfy.view.swipe.xlist.XlistListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by yfyandr on 2017/6/20.
 */

public class HonorTeaDetailActivity extends WcfActivity {


    private final String TEA_REWARD="get_teacher_reward";
    private final int STU_ID=2;
    @Bind(R.id.honor_tea_chioce_xlist)
    XListView xListView;
    private TextView xlistHeader;
    private LoadingDialog dialog;

    private List<HonorStu> honorStus=new ArrayList<>();

    private HonorTeaAdapter adapter;


    private int page=0;
    private boolean isxlist=false;
    private int class_id;
    private int stu_id=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.integral_honor_tea);
        dialog=new LoadingDialog(mActivity);
        getData();
        initSQToolbar();
        initView();
        refresh_1();


    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        isxlist=false;
        if (xListView!=null){
            xListView.stopRefresh();
            xListView.stopLoadMore();
        }
        Logger.e("zxx","   "+result);
        IntegralResult re=gson.fromJson(result, IntegralResult.class);
        String name=wcfTask.getName();

        if (name.equals("refresh")){
            honorStus=re.getReward();
        }
        if (name.equals("more")){
            if (re.getReward().size()!=PAGE_NUM){
                toastShow(R.string.success_loadmore_end);
            }
            honorStus.addAll(re.getReward());
        }
        adapter.notifyDataSetChanged(honorStus);
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        isxlist=false;
        if (xListView!=null){
            xListView.stopRefresh();
            xListView.stopLoadMore();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case STU_ID:
                    stu_id=data.getIntExtra("stu_id",0);
                    refresh_1();
                    break;
            }
        }


    }

    public void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle("");

    }

    public void getData(){
        class_id=getIntent().getIntExtra("class_id",0);
    }
    public void initView(){
        adapter=new HonorTeaAdapter(mActivity,honorStus);
        xListView.setAdapter(adapter);
        View v=LayoutInflater.from(mActivity).inflate(R.layout.public_item_txt_center,null);
        xlistHeader= (TextView) v.findViewById(R.id.public_center_txt);
        xlistHeader.setText("全部");
        xListView.addHeaderView(v);
        xListView.setPullLoadEnable(true);
        xListView.setXListViewListener(new XlistListener() {
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
        });
        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==1){
                    Intent intent=new Intent(mActivity,HonorTeaChioceStuActivity.class);
                    intent.putExtra("class_id",class_id);
                    startActivityForResult(intent,STU_ID);
                }

            }
        });
    }
    public void refresh_1(){
        if (isxlist){
            xListView.stopRefresh();
        }
        page=0;
        isxlist=true;
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                class_id,
                stu_id,
                page,
                PAGE_NUM
        };
        ParamsTask getTimid = new ParamsTask(params, TEA_REWARD, "refresh",dialog);
        execute(getTimid);
    }
    public void refresh(){
        if (isxlist){
            xListView.stopRefresh();
        }
        page=0;
        isxlist=true;
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                class_id,
                stu_id,
                page,
                PAGE_NUM
        };
        ParamsTask getTimid = new ParamsTask(params, TEA_REWARD, "refresh");
        execute(getTimid);
    }
    public void loadMore(){
        if (isxlist){
            xListView.stopLoadMore();
        }
        page++;
        isxlist=true;
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                class_id,
                stu_id,
                page,
                PAGE_NUM
        };
        ParamsTask getTimid = new ParamsTask(params, TEA_REWARD, "more");
        execute(getTimid);
    }

}
