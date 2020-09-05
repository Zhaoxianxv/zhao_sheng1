package com.yfy.app.integral.subjcet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.zhao_sheng.R;
import com.yfy.app.XlistLefttTxtAdapter;
import com.yfy.app.integral.beans.Course;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.dialog.LoadingDialog;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class SubjcetTeaSetActivity extends BaseActivity {
    private static final String TAG = "zxx";

    private LoadingDialog loadingdialog;
    private ArrayList<Course> courseList=new ArrayList<>();
    private List<String> names=new ArrayList<>();
    private int classid;
    private int term_id;
    private XlistLefttTxtAdapter adapter;
    @Bind(R.id.subjcet_chioce)
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subjcet_tea_set);
        loadingdialog=new LoadingDialog(mActivity);
        getData();
        initSQToolbar();
    }

    private void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle("选择学科");
    }
    public void getData(){
        Intent intent=getIntent();
        classid=intent.getIntExtra("class_id",0);
        term_id=intent.getIntExtra("term_id",0);
        courseList=intent.getParcelableArrayListExtra("courseList");
        for (Course course:courseList){
            names.add(course.getCoursename());
        }
        adapter=new XlistLefttTxtAdapter(mActivity,names);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Logger.e(TAG, "onItemClick: "+position );
                int course_id= ConvertObjtect.getInstance().getInt(courseList.get(position).getCourseid());
                Intent intent=new Intent(mActivity,SubjcetDoActivity.class);
                intent.putExtra("class_id", classid);
                intent.putExtra("term_id", term_id);
                intent.putExtra("course_id", course_id);
                intent.putExtra("course_name", courseList.get(position). getCoursename());
                startActivity(intent);
            }
        });
    }



}
