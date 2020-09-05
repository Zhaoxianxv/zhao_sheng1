package com.yfy.app.integral;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.integral.adapter.SubjectStuAdapter;
import com.yfy.app.integral.beans.Course;
import com.yfy.app.integral.beans.IntegralResult;
import com.yfy.app.integral.beans.SubjectStu;
import com.yfy.app.integral.subjcet.SubjcetTeaSetActivity;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
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

public class SubjectGoodDetailActivity extends WcfActivity {
    private static final String TAG = "zxx";
    private final int SUBJECT_ID=7;
//    private String mothed="get_class_award";//班级学科优生
    private LoadingDialog loadingDialog;
    private int term_id;
    private int page=0;
    @Bind(R.id.term_list_commt)
    XListView xlist;
    private boolean loading=false;
    private SubjectStuAdapter adapter;
    private List<SubjectStu> subjectStus=new ArrayList<>();
    private TextView topText;
    private ArrayList<Course> courseList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.termid_commt);
        getData();
        loadingDialog=new LoadingDialog(mActivity);
        initSQToolbar();
        initView();
        refresh(true);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case SUBJECT_ID:
                    String name,id;
                    name=data.getStringExtra("data_name");
                    id=data.getStringExtra("data_id");
                    topText.setText(name);
                    courseid=ConvertObjtect.getInstance().getInt(id);
                    refresh(true);
                    break;
                case TagFinal.UI_REFRESH:
                    refresh(true);
                    break;
            }

        }
    }
    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        loading=false;
        loading = false;
        Logger.e(TAG, "onSuccess: " + result);
        if (xlist != null) {
            xlist.stopLoadMore();
            xlist.stopRefresh();
        }
        String name = wcfTask.getName();
        IntegralResult info = gson.fromJson(result, IntegralResult.class);
        if (name.equals("refresh")){
            subjectStus.clear();
            if (info.getAward().size()==0){
                toastShow(R.string.success_not_details);
            }
            subjectStus.addAll(info.getAward());
        }
        if (name.equals("loadmore")){
            if (info.getAward().size()!=PAGE_NUM){
                toastShow(R.string.success_loadmore_end);
            }
            subjectStus.addAll(info.getAward());
        }
        adapter.notifyDataSetChanged(subjectStus);
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        loading=false;

    }


    private String title;
    private int classid,courseid=0;
    public void getData(){
        Intent intent=getIntent();
        title=intent.getStringExtra("title");
        classid=intent.getIntExtra("class_id",0);
        term_id=intent.getIntExtra("term_id",0);
        courseList=intent.getParcelableArrayListExtra("courseList");
    }


    public void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle(title+"学科优生");
        toolbar.addMenuText(TagFinal.ONE_INT,"管理");
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent=new Intent(mActivity, SubjcetTeaSetActivity.class);
                intent.putExtra("class_id", classid);
                intent.putExtra("term_id", term_id);
                intent.putParcelableArrayListExtra("courseList",courseList);
                startActivityForResult(intent,TagFinal.UI_REFRESH);
            }
        });

    }
    public void initView(){

        adapter=new SubjectStuAdapter(mActivity,subjectStus);

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

        View v= LayoutInflater.from(mActivity).inflate(R.layout.public_item_singe_top_txt,null);
        v.setBackgroundColor(getResources().getColor(R.color.DarkGray));
        topText= (TextView) v.findViewById(R.id.public_center_txt_add);
        topText.setText(R.string.all_subject);
        topText.setTextColor(Color.rgb(153,36,41));
        topText.setBackgroundResource(R.color.gray);
        xlist.addHeaderView(v);
        topText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mActivity,ChangeSubjectActivity.class);
                intent.putExtra("term_id",term_id);
                startActivityForResult(intent,SUBJECT_ID);
            }
        });

    }


    public void refresh(boolean is){
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
                classid,
                courseid,//
                term_id,
                page,
                PAGE_NUM
        };
        ParamsTask refreshTask;
        if (is) {
            refreshTask = new ParamsTask(params, TagFinal.AWARD_CLASS_AWARD_DETAIL, "refresh");
        } else {
            refreshTask = new ParamsTask(params, TagFinal.AWARD_CLASS_AWARD_DETAIL, "loadmore");
        }
        execute(refreshTask);
    }




}
