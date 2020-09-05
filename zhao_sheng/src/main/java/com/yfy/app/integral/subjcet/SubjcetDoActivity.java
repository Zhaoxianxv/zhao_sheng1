package com.yfy.app.integral.subjcet;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.dialog.LoadingDialog;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.final_tag.recycerview.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class SubjcetDoActivity extends WcfActivity {
    private static final String TAG = "zxx";


    private int classid;
    private int term_id;
    private int course_id;
    private DoAdapter adapter;
    private List<StuAwrad> stuAwrad=new ArrayList<>();
    @Bind(R.id.subjcet_do_stulist_xlist)
    RecyclerView xlist;
    private LoadingDialog loaddingdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subjcet_do);
        loaddingdialog=new LoadingDialog(mActivity);
        iniSQToolbar();
        getData();
        initView();

    }
    private TextView title;
    private void iniSQToolbar(){
        assert toolbar!=null;
        title=toolbar.setTitle("");
    }

    public void getData(){
        Intent intent=getIntent();
        classid=intent.getIntExtra("class_id",0);
        term_id=intent.getIntExtra("term_id",0);
        course_id=intent.getIntExtra("course_id",0);
        String name=intent.getStringExtra("course_name");
        if (StringJudge.isEmpty(name)){
            name="";
        }
        title.setText(name);
        refresh();
    }


    private void initView(){


        adapter=new DoAdapter(mActivity,stuAwrad);
        xlist.setLayoutManager(new LinearLayoutManager(this));
//        xlist.setLayoutManager(new GridLayoutManager(this, 1));
        xlist.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        xlist.addItemDecoration(new RecycleViewDivider(
                mActivity,
                LinearLayoutManager.HORIZONTAL,
                1,
                Color.rgb(20,20,20)));
        xlist.setAdapter(adapter);

    }

    private void refresh(){
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                classid,
                course_id,//
                term_id,

        };
        ParamsTask refreshTask = new ParamsTask(params, TagFinal.AWARD_GET_COURSE, TagFinal.AWARD_GET_COURSE);
        execute(refreshTask);
    }
    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        Logger.e(TAG, "onSuccess: "+result );

        String name=wcfTask.getName();
        if (name.equals(TagFinal.AWARD_GET_COURSE)){
            SubInfo info=gson.fromJson(result,SubInfo.class);
            stuAwrad=info.getAward();
            adapter.setDataList(stuAwrad);
            adapter.setLoadState(3);
            return false;
        }
        if (name.equals(TagFinal.AWARD_ADD_UTIL)){
            if (StringJudge.isSuccess(gson,result)) refresh();
            return false;

        }
        if (name.equals(TagFinal.AWARD_DEL_UTIL)){
            if (StringJudge.isSuccess(gson,result)) refresh();
            return false;
        }

        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        Logger.e(TAG, "onSuccess: " );
        toastShow(R.string.fail_do_not);
        adapter.setLoadState(3);
    }



    @OnClick(R.id.subjcet_do_del)
    void setDel(){

        if (StringJudge.isNotEmpty(che(adapter.getDataList()))){
            shoWDialog("", "是否确定取消优生", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    del(che(adapter.getDataList()));
                }
            },true);
        }else{
            shoWDialog("", dialog_detail, null,false);
        }
    }
    @OnClick(R.id.subjcet_do_add)
    void setAdd(){
        if (StringJudge.isNotEmpty(che(adapter.getDataList()))){
            shoWDialog("", "是否确定添加优生", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    add(che(adapter.getDataList()));
                }
            },true);
        }else{
            shoWDialog("", dialog_detail, null,false);
        }
    }


    private String che(List<StuAwrad> list){
        StringBuilder sb=new StringBuilder();
        String util=",";
        if (StringJudge.isNotEmpty(list)){
            for (StuAwrad stu:list) {
                if (stu.isSelecter()){
                    sb=sb.append(stu.getStuid()).append(util);
                }
            }
        }
        if (sb.length()<2)return "";
        return sb.substring(0,sb.length()-1);
    }
    private void add(String stu_id){
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                stu_id,
                course_id,//
                "",
                term_id,

        };
        ParamsTask refreshTask = new ParamsTask(params, TagFinal.AWARD_ADD_UTIL, TagFinal.AWARD_ADD_UTIL,loaddingdialog);
        execute(refreshTask);
    }
    private void del(String stu_id){
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                stu_id,
                course_id,//
                term_id,

        };
        ParamsTask refreshTask = new ParamsTask(params, TagFinal.AWARD_DEL_UTIL, TagFinal.AWARD_DEL_UTIL,loaddingdialog);
        execute(refreshTask);
    }



    private String dialog_detail="请至少选择一个学生";
    private   AlertDialog.Builder dialog;
    public void shoWDialog(String title,String detail,DialogInterface.OnClickListener ok,boolean is) {
        if (dialog==null)
        dialog = new AlertDialog.Builder(mActivity);
        dialog.setMessage(detail);
        dialog.setTitle(title);
        if (is){
            dialog.setPositiveButton("确定", ok);
        }
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.create().show();
    }

}
