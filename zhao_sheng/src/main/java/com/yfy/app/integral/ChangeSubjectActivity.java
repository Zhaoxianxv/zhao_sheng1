package com.yfy.app.integral;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.example.zhao_sheng.R;
import com.yfy.app.XlistLefttTxtAdapter;
import com.yfy.app.integral.beans.Course;
import com.yfy.app.integral.beans.IntegralResult;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.dialog.LoadingDialog;
import com.yfy.final_tag.Logger;
import com.yfy.json.JsonParser;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.view.swipe.xlist.XListView;
import com.yfy.view.swipe.xlist.XlistListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ChangeSubjectActivity extends WcfActivity {

    private XlistLefttTxtAdapter adapter;
    private LoadingDialog dialog;
    private List<Course> courses=new ArrayList<>();
    private List<String> names=new ArrayList<>();
    @Bind(R.id.integral_term_change_list)
    XListView xlist;
    private int term_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.integral_change_term);
        dialog=new LoadingDialog(mActivity);
        initSQToolbar();
        initView();
        getData();
    }
    public void getData(){
        term_id=getIntent().getIntExtra("term_id",0);
        getSubject();
    }

    private void initView() {
        xlist.setPullLoadEnable(false);
        adapter=new XlistLefttTxtAdapter(mActivity,names);
        xlist.setAdapter(adapter);
        xlist.setXListViewListener(xlistener);
        xlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent();
                intent.putExtra("data_id",courses.get(i-1).getCourseid());
                intent.putExtra("data_name",courses.get(i-1).getCoursename());
                setResult(RESULT_OK,intent);
                onPageBack();
            }
        });
    }

    private void initSQToolbar() {
        assert toolbar!=null;
        toolbar.setTitle("");


    }
    public void getAdapterData(){
        Course c=new Course(getResources().getString(R.string.all_subject),"0");
        courses.add(0,c);
        for (Course b:courses) {
            names.add(b.getCoursename());
        }
        adapter.notifyDataSetChanged(names);
    }


    private XlistListener xlistener=new XlistListener() {
        @Override
        public void onRefresh() {
            getSubject();

        }
    };

    //获取学期列表
    public void getSubject(){
        final String TerMId = "get_course";
        Object[] params = new Object[] {
                Base.user.getSession_key()
        };
        WcfTask getTimid = new ParamsTask(params, TerMId, TerMId,dialog);
        execute(getTimid);

    }

    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        xlist.stopRefresh();
        String name=wcfTask.getName();
        if (name.equals("get_course")) {
            Logger.e("zxx","get_course"+result);
            IntegralResult inter=gson.fromJson(result, IntegralResult.class);
            if (inter.getResult().equals("true")){
                courses=inter.getCourses();
                getAdapterData();
            }else{
                toastShow(JsonParser.getErrorCode(result));
            }
            return false;
        }
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        xlist.stopRefresh();
        toastShow(R.string.fail_do_not);
    }
}
