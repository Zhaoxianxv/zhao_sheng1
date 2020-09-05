package com.yfy.app.exchang;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.exchang.bean.MyCouyseBean;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.dialog.LoadingDialog;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.final_tag.ConvertObjtect;

import butterknife.Bind;

public class ExchangeDetailActivity extends WcfActivity {
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
    @Bind(R.id.exchange_project_call)
    TextView call;
    @Bind(R.id.exchange_tag_red_text)
    TextView tag_red;



    private MyCouyseBean bean;
    private boolean is;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchange_detail);
        getData();
        initSQToolbar();
    }

    private void initSQToolbar() {
        assert toolbar!=null;
        toolbar.setTitle("调课详情");
    }

    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        Logger.e(TagFinal.ZXX, "onSuccess: "+result );
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {

    }



    public void getData(){
        bean=getIntent().getParcelableExtra("data");
        is=getIntent().getBooleanExtra("is",false);

        Logger.e(TagFinal.ZXX, "getData: "+bean.getTeachername());
        initView();
    }

    public void initView(){
        submit1.setText(bean.getDate().replace("/","-")+"("+bean.getNo()+")");
        submit2.setText(bean.getDate1().replace("/","-")+"("+bean.getNo1()+")");
        submit3.setText(bean.getCoursename()+"("+bean.getTeachername()+")");
        submit4.setText(bean.getCoursename1()+"("+bean.getTeachername1()+")");

        project_title.setText(bean.getClassname());
        content.setText(bean.getReason());

        if (bean.getState().equals("通过")){
            tag_red.setTextColor(getResources().getColor(R.color.green));
            tag_red.setText(bean.getState());
            tag_red.setBackgroundResource(R.drawable.oval_line1_green);
        }else if (bean.getState().equals("拒绝")){
            tag_red.setText(bean.getState());
            tag_red.setTextColor(getResources().getColor(R.color.red));
            tag_red.setBackgroundResource(R.drawable.oval_line1_red);
        }else{
            tag_red.setVisibility(View.GONE);
        }
        call.setText(bean.getRemark());
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
