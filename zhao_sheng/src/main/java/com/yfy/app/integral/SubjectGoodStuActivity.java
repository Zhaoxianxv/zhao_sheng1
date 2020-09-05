package com.yfy.app.integral;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.integral.adapter.SubjectStuAdapter;
import com.yfy.app.integral.beans.IntegralResult;
import com.yfy.app.integral.beans.SubjectStu;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.db.UserPreferences;
import com.yfy.final_tag.dialog.LoadingDialog;
import com.yfy.final_tag.StringJudge;
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

public class SubjectGoodStuActivity extends WcfActivity {
    private static final String TAG = "zxx";
    private final int TERM_ID=9;
    private String mothed="get_stu_award";//学生type
    private LoadingDialog loadingDialog;
    private int term_id;

    @Bind(R.id.term_list_commt)
    XListView xlist;
    private boolean loading=false;
    private SubjectStuAdapter adapter;
    private List<SubjectStu> subjectStus=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.termid_commt);
        loadingDialog=new LoadingDialog(mActivity);
        term_id=ConvertObjtect.getInstance().getInt(UserPreferences.getInstance().getTermId());
        initSQToolbar();
        initView();
        refresh();
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
                refresh();
            }
        }
    }
    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        Logger.e(TAG, "onSuccess: " + result);
        if (xlist != null) {
            xlist.stopLoadMore();
            xlist.stopRefresh();
        }
        String name = wcfTask.getName();
        IntegralResult info = gson.fromJson(result, IntegralResult.class);
        if (name.equals(mothed)){

            if (StringJudge.isSuccess(gson,result )){
                subjectStus.clear();
                if (info.getAward().size()==0){
                    toastShow(R.string.success_not_details);
                }
                subjectStus.addAll(info.getAward());
            }else{
                toastShow(info.getError_code());
            }

        }
        adapter.notifyDataSetChanged(subjectStus);
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        toastShow(R.string.fail_do_not);

    }

    private TextView menu1;
    public void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle("");
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
        adapter=new SubjectStuAdapter(mActivity,subjectStus);

        xlist.setPullLoadEnable(false);
        xlist.setPullRefreshEnable(true);
        xlist.setAdapter(adapter);
        xlist.setXListViewListener(new XlistListener() {
            @Override
            public void onRefresh() {
                super.onRefresh();
                refresh();
            }
        });
    }

    public void refresh(){
        Object[] params = new Object[] {
            Base.user.getSession_key(),
            term_id
        };
        ParamsTask task = new ParamsTask(params, mothed, mothed, loadingDialog);
        execute(task);
    }


}
