package com.yfy.app.duty;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.ContactSingeActivity;
import com.yfy.app.duty.bean.PlaneInfo;
import com.yfy.app.notice.bean.ChildBean;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.view.SQToolBar;
import com.yfy.view.textView.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class DutyChangeActivity extends WcfActivity {

    private static final String TAG = DutyChangeActivity.class.getSimpleName();
    private String time="";


    @Bind(R.id.duty_change_parent)
    TextView parent;
    @Bind(R.id.duty_change_type)
    TextView duty_type;
    @Bind(R.id.duty_change_content)
    TextView edit_content;
    @Bind(R.id.duty_change_time_flow)
    FlowLayout flow_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.duty_change);
        initSQtoolbar();
        setChild(new String[]{"点击选择值周日期"},flow_layout ,false);
    }

    private void initSQtoolbar(){
        assert toolbar!=null;
        toolbar.setTitle("临时调整");
        toolbar.addMenuText(TagFinal.ONE_INT,R.string.submit);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (position){
                    case TagFinal.ONE_INT:
                        changeDuty();
                        break;
                }
            }
        });
    }



    private int user_id=-1;
    private String type_id="";

    public void changeDuty(){
        if (user_id==-1){
            toastShow("请选择代理人！");
            return;
        }
        if (StringJudge.isEmpty(type_id)){
            toastShow("请选择值周类型！");
            return;
        }
        if (StringJudge.isEmpty(time)){
            toastShow("请选择值周时间!");
            return;
        }
        String reason=edit_content.getText().toString().trim();
        if (StringJudge.isEmpty(reason)){
            toastShow("请填写临调原因!");
            return;
        }
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                user_id,
                type_id,
                time,
                reason,
        };
        ParamsTask get_plane = new ParamsTask(params, TagFinal.DUTY_CHANGE, TagFinal.DUTY_CHANGE);
        execute(get_plane);
        showProgressDialog("");

    }

    @OnClick(R.id.duty_change_parent)
    void setParent(){
        Intent intent=new Intent(mActivity,ContactSingeActivity.class);
        startActivityForResult(intent, TagFinal.UI_ADMIN);
    }

    @OnClick(R.id.duty_change_type)
    void setDutyType(){
        Intent intent=new Intent(mActivity,DutyTagActivity.class);
        startActivityForResult(intent, TagFinal.UI_ADD);
    }
    @OnClick(R.id.duty_change_time_layout)
    void setChoiceTime(){
        //time 多选
        if (StringJudge.isEmpty(type_id)){
            toastShow("请选择值周类型！");
            return;
        }
        Intent intent=new Intent(mActivity,DutyChoiceTimeActivity.class);
        intent.putExtra(TagFinal.TYPE_TAG,type_id );
        startActivityForResult(intent, TagFinal.UI_TAG);
    }


    public void setChild(String[] names, FlowLayout flow,Boolean is){
        LayoutInflater mInflater = LayoutInflater.from(mActivity);
        if (flow.getChildCount()!=0){
            flow.removeAllViews();
        }
        for (int i=0;i<names.length;i++){
            final TextView tv = (TextView) mInflater.inflate(R.layout.duty_flowlayout_tv,flow, false);
            tv.setText(names[i]);
            if (is)
            tv.setTextColor(ColorRgbUtil.getTeaOne());
            flow.addView(tv);
        }
    }



    private List<String> times=new ArrayList<>();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch(requestCode){
                case TagFinal.UI_ADMIN:
                    ChildBean bean=data.getParcelableExtra(TagFinal.OBJECT_TAG);
                    parent.setText(bean.getUsername());
                    String name_id=bean.getUserid();
                    user_id=ConvertObjtect.getInstance().getInt(name_id.substring(3,name_id.length()));
                    break;
                case TagFinal.UI_TAG:
                    List<PlaneInfo> infos = data.getParcelableArrayListExtra(TagFinal.OBJECT_TAG);
                    times=getDate(infos);
                    setChild(StringJudge.arraysToList(times),flow_layout,true );
                    time=StringJudge.listToString(times);
                    break;
                case TagFinal.UI_ADD:
                    duty_type.setText(data.getStringExtra(TagFinal.NAME_TAG));
                    type_id=data.getStringExtra(TagFinal.ID_TAG);
                    duty_type.setTextColor(ColorRgbUtil.getBaseText());
                    break;
            }
        }
    }

    private List<String> getDate(List<PlaneInfo> infos){
        List<String> times=new ArrayList<>();
        for (PlaneInfo info:infos){
            times.add(info.getDate());
        }
        return times;
    }

    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        if (!isActivity())return false;
        dismissProgressDialog();
        Logger.e( result );
        String name=wcfTask.getName();
        if (name.equals(TagFinal.DUTY_CHANGE)){
            if (StringJudge.isSuccess(gson,result )){
                toastShow("临调成功");
                setResult(RESULT_OK);
                finish();
            }else{
                toastShow(R.string.success_not_do);
            }
        }
        return false;
    }



    @Override
    public void onError(WcfTask wcfTask) {
        if (!isActivity())return;
        dismissProgressDialog();
        toastShow(R.string.fail_do_not);
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
