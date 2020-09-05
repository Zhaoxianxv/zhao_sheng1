package com.yfy.app.studen_award;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.zhao_sheng.R;
import com.yfy.app.studen_award.adapter.AwardDialogAdapter;
import com.yfy.app.studen_award.adapter.AwardDialogTypeAdapter;
import com.yfy.app.studen_award.bean.AwardInfor;
import com.yfy.app.studen_award.bean.AwardType;
import com.yfy.app.studen_award.bean.GradeBean;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class AwardDialogActivity extends WcfActivity {

    private String get_award_class="get_award_class";
    private String get_award_type="get_award_type";

    @Bind(R.id.award_dialog_grade_gride)
    GridView grade_grid;
    @Bind(R.id.award_dialog_type_gride)
    GridView type_grid;
    AwardDialogAdapter grade_adapter;
    AwardDialogTypeAdapter type_adapter;
    List<AwardType> types=new ArrayList<>();
    List<GradeBean> grades=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.award_dialog);
        initSQToolbar();
        initView();
        getClassGrade();
        getAwardTepy();
    }



    private void initSQToolbar() {


    }
    private void initView() {
        type_adapter=new AwardDialogTypeAdapter(mActivity,types);
        grade_adapter=new AwardDialogAdapter(mActivity,grades);
//        grade_grid.setAdapter(grade_adapter);
        type_grid.setAdapter(type_adapter);
        type_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent=new Intent();
                intent.putExtra("type_id",types.get(i).getType_id());
                setResult(RESULT_OK,intent);
                onPageBack();
            }
        });
    }


    public void getClassGrade(){
        Object[] parmas=new Object[]{
                Base.user.getSession_key()
        };
        ParamsTask grade= new ParamsTask(parmas,get_award_class,get_award_class);
        execute(grade);
    }
    public void getAwardTepy(){
        Object[] parmas=new Object[]{
                Base.user.getSession_key()
        };
        ParamsTask type= new ParamsTask(parmas,get_award_type,get_award_type);
        execute(type);
    }


    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        Logger.i(TagFinal.ZXX, "onSuccess: "+result);
        AwardInfor infor=gson.fromJson(result,AwardInfor.class);
        String name=wcfTask.getName();
        if (name.equals(get_award_class)){
            grades=infor.getClasses();
            grade_adapter.notifyDataSetChanged(grades);
        }
        if (name.equals(get_award_type)){
            types=infor.getTypes();
            type_adapter.notifyDataSetChanged(types);
        }
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {

    }


    @OnClick(R.id.award_window_bg)
    void setDialogBg(){
        onPageBack();
    }
    @OnClick(R.id.award_layout)
    void setLayout(){

    }
}
