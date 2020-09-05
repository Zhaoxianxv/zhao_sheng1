package com.yfy.app.integral;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.studen_award.adapter.AwardStuAdapter;
import com.yfy.app.studen_award.bean.AwardInfor;
import com.yfy.app.studen_award.bean.AwardStuBean;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.calendar.CustomDate;
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

public class HonorTeaChioceStuActivity extends WcfActivity {


    private String get_award_students="get_award_students";

    @Bind(R.id.honor_tea_chioce_xlist)
    XListView xListView;
    private LoadingDialog dialog;

    private List<AwardStuBean> stubeans=new ArrayList<>();
    AwardStuAdapter adapter;
    private int class_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.integral_honor_tea);
        getData();
        dialog=new LoadingDialog(mActivity);
        initSQToolbar();
        initView();

    }

    @Override
    public void onResume() {
        super.onResume();
        refreshfrist();
    }

    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        if (xListView!=null){
            xListView.stopRefresh();
        }
        Logger.e("zxx","   "+result);
        AwardInfor infor=gson.fromJson(result, AwardInfor.class);
        if (StringJudge.isEmpty(infor.getStudents()))return false;
        stubeans.clear();
        stubeans.addAll(infor.getStudents());
        adapter.notifyDataSetChanged(stubeans);
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        if (xListView!=null){
            xListView.stopRefresh();
        }


    }

    public void getData(){
        class_id=getIntent().getIntExtra("class_id",0);
    }
    public void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle("");

    }

    public void initView(){
        adapter=new AwardStuAdapter(mActivity,stubeans);
        xListView.setAdapter(adapter);
        View v= LayoutInflater.from(mActivity).inflate(R.layout.selected_item_lefttxt_back,null);
        TextView  title= (TextView) v.findViewById(R.id.public_xlist_left_txt);
        title.setText("全部");
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("stu_id", 0);
                setResult(RESULT_OK,intent);
                onPageBack();
            }
        });
        xListView.addHeaderView(v);
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
                if (i<2)return;
                String id=stubeans.get(i-2).getId();
                Intent intent=new Intent();
                intent.putExtra("stu_id", ConvertObjtect.getInstance().getInt(id));
                setResult(RESULT_OK,intent);
                onPageBack();
            }
        });
    }
    public void refresh(){
        CustomDate d=new CustomDate();
        String time=d.toString();
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                class_id,
                time,
                ""
        };
        ParamsTask getTimid = new ParamsTask(params, get_award_students, get_award_students);
        execute(getTimid);
    }
    public void refreshfrist(){
        CustomDate d=new CustomDate();
        String time=d.toString();
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                class_id,
                time,
                ""
        };
        ParamsTask getTimid = new ParamsTask(params, get_award_students, get_award_students,dialog);
        execute(getTimid);
    }

}
