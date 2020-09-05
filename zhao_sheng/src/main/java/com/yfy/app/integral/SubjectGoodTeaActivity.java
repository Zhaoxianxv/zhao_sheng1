package com.yfy.app.integral;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.XlistLefttTxtAdapter;
import com.yfy.app.integral.beans.IntegralResult;
import com.yfy.app.integral.subjcet.ClassName;
import com.yfy.final_tag.Base;
import com.yfy.base.XlistAdapter;
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
import com.yfy.final_tag.StringJudge;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by yfyandr on 2017/6/20.
 */

public class SubjectGoodTeaActivity extends WcfActivity {
    private static final String TAG = SubjectGoodTeaActivity.class.getSimpleName();

    @Bind(R.id.term_list_commt)
    XListView xListView;
    private LoadingDialog dialog;
    private final int TERM_ID=7;
    private List<String> names=new ArrayList<>();
    private List<ClassName> classNames=new ArrayList<>();
    private XlistAdapter adapter;
    private int term_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.termid_commt);
        term_id=ConvertObjtect.getInstance().getInt(UserPreferences.getInstance().getTermId());
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
        if (xListView!=null){
            xListView.stopRefresh();
        }
        Logger.e("zxx","   "+result);
        String name_wcf=wcfTask.getName();
        if (name_wcf.equals(TagFinal.AWARD_TEA_CLASS_LIST)){
            IntegralResult re=gson.fromJson(result, IntegralResult.class);
            classNames=re.getCourseclass();
            if (StringJudge.isEmpty(classNames))return false;
            names.clear();
            for (ClassName name:classNames) {
                names.add(name.getClassname());
            }
            adapter.notifyDataSetChanged(names);
        }

        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        if (xListView!=null){
            xListView.stopRefresh();
        }
    }
    private TextView menu1;
    public void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle("班级列表");
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
                String id=classNames.get(i-1).getClassid();
                Intent intent=new Intent(mActivity,SubjectGoodDetailActivity.class);
                intent.putExtra("class_id", ConvertObjtect.getInstance().getInt(id));
                intent.putExtra("title", classNames.get(i-1).getClassname());
                intent.putExtra("term_id", term_id);
                intent.putParcelableArrayListExtra("courseList", (ArrayList<? extends Parcelable>) classNames.get(i-1).getCourses());
                startActivity(intent);
            }
        });
    }
    public void refresh(){

        Object[] params = new Object[] {
                Base.user.getSession_key(),
                term_id
        };
        ParamsTask getTimid = new ParamsTask(params, TagFinal.AWARD_TEA_CLASS_LIST, TagFinal.AWARD_TEA_CLASS_LIST);
        execute(getTimid);
    }

}
