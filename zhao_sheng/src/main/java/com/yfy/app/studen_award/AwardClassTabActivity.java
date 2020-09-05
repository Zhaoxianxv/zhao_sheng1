package com.yfy.app.studen_award;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.example.zhao_sheng.R;
import com.yfy.app.studen_award.adapter.AwardClassTabAdapter;
import com.yfy.app.studen_award.bean.AwardInfor;
import com.yfy.app.studen_award.bean.GradeBean;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.view.swipe.xlist.XListView;
import com.yfy.final_tag.ConvertObjtect;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class AwardClassTabActivity extends WcfActivity {


    @Bind(R.id.award_xlist_main)
    XListView xlist;
    @Bind(R.id.award_clear_et)
    EditText clear_et;
    AwardClassTabAdapter xlistAdapter;
    List<GradeBean> data=new ArrayList<>();
    private String get_award_class="get_class_all";
    List<GradeBean> grades=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.award_class_chioce);
        initSQToolbar();
        initView();


    }

    @Override
    public void onResume() {
        super.onResume();
        getClassGrade();
    }



    private void initSQToolbar() {
        assert toolbar!=null;
        toolbar.setTitle("班级列表");
//        toolbar.addMenuText(1,"审核奖励");



    }
    private void initView() {
        clear_et.setVisibility(View.GONE);
        xlist.setPullRefreshEnable(true);
        xlist.setPullLoadEnable(false);
        xlistAdapter=new AwardClassTabAdapter(mActivity,grades);
        xlist.setAdapter(xlistAdapter);
        xlist.setXListViewListener(ixListViewListener);
        xlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String class_id=grades.get(i-1).getClass_id();
                Intent intent=new Intent(mActivity,AwardChioceStuActivity.class);
                intent.putExtra("class_id", ConvertObjtect.getInstance().getInt(class_id));
                intent.putExtra("class_name",grades.get(i-1).getClass_name());
                Logger.e("zxx"," class_id "+class_id);
                startActivity(intent);
            }
        });

    }
    private XListView.IXListViewListener ixListViewListener=new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
            getClassGrade();
        }

        @Override
        public void onLoadMore() {

        }
    };


    public void getClassGrade(){
        Object[] parmas=new Object[]{
                Base.user.getSession_key()
        };
        ParamsTask grade= new ParamsTask(parmas,get_award_class,get_award_class);
        execute(grade);
    }

    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        xlist.stopRefresh();
//        Log.e("zxx", "onSuccess: "+result);
        AwardInfor infor=gson.fromJson(result,AwardInfor.class);
        String name=wcfTask.getName();
        if (name.equals(get_award_class)){
            grades=infor.getClasses();
            xlistAdapter.notifyDataSetChanged(grades);
        }
        return false;
    }
    @Override
    public void onError(WcfTask wcfTask) {
        stop();

    }

    public void stop(){
        xlist.stopRefresh();
    }
}
