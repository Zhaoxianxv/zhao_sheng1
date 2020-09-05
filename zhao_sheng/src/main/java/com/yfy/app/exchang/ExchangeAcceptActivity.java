package com.yfy.app.exchang;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.exchang.bean.ExchangeInfor;
import com.yfy.app.exchang.bean.MyCouyseBean;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.dialog.LoadingDialog;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;

import butterknife.Bind;
import butterknife.OnClick;

public class ExchangeAcceptActivity extends WcfActivity {
    private static final String TAG = "zxx";

    @Bind(R.id.exchange_submit_1)
    TextView submit1;
    @Bind(R.id.exchange_submit_2)
    TextView submit2;
    @Bind(R.id.exchange_submit_3)
    TextView submit3;
    @Bind(R.id.exchange_submit_4)
    TextView submit4;


    @Bind(R.id.exchange_project_title)
    TextView project_title;
    @Bind(R.id.exchange_project_content)
    TextView content;
    @Bind(R.id.exchnage_add_call_edit)
    EditText edit;
    @Bind(R.id.bottem)
    LinearLayout layout;




    private MyCouyseBean bean;
    private boolean is;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchange_accept);
        getData();
        initSQToolbar();
    }
    private void initSQToolbar() {
        assert toolbar!=null;
        toolbar.setTitle("与我调课");
    }

    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        ExchangeInfor infor=gson.fromJson(result,ExchangeInfor.class);
        if (infor.getResult().equals("true")){
            toastShow(R.string.success_do);
            onPageBack();
        }else{
            toastShow(R.string.success_not_do);
        }
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {

    }

    @OnClick(R.id.exchnage_ok)
    void setOk(){
        String s=edit.getText().toString();
        if (StringJudge.isEmpty(s)) s="";
        submit(1,s);

    }
    @OnClick(R.id.exchnage_not_ok)
    void setNotOk(){
        String s=edit.getText().toString();
        if (StringJudge.isEmpty(s)) s="";
        submit(2,s);
    }

    public void getData(){
        bean=getIntent().getParcelableExtra("data");
        is=getIntent().getBooleanExtra("is",false);

        initView();
    }

    public void initView(){
        submit1.setText(bean.getDate().replace("/","-")+"("+bean.getNo()+")");
        submit2.setText(bean.getDate1().replace("/","-")+"("+bean.getNo1()+")");
        submit3.setText(bean.getCoursename()+"("+bean.getTeachername()+")");
        submit4.setText(bean.getCoursename1()+"("+bean.getTeachername1()+")");

        project_title.setText(bean.getClassname());
        content.setText(bean.getReason());

    }


    private final String ADMIN="set_schedule_state";
    public void submit(int state,String remark){
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                ConvertObjtect.getInstance().getInt(bean.getId()),
                state,//1为通过,2为拒绝
                remark
        };
        ParamsTask able=new ParamsTask(params,ADMIN,ADMIN,new LoadingDialog(mActivity));
        execute(able);
    }
}
