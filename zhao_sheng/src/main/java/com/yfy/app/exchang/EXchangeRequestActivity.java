package com.yfy.app.exchang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.view.SQToolBar;

import butterknife.Bind;

public class EXchangeRequestActivity extends WcfActivity {
    private static final String TAG = "zxx";

    private final String REQUEST="changeschedule";

    @Bind(R.id.exchange_project_title)
    TextView title;

    @Bind(R.id.exchange_submit_1)
    TextView submit_1;

    @Bind(R.id.exchange_submit_2)
    TextView submit_2;

    @Bind(R.id.exchange_submit_3)
    TextView submit_3;

    @Bind(R.id.exchange_submit_4)
    TextView submit_4;



    @Bind(R.id.exchnage_add_edit)
    EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchange_request);
        getData();
        initSQToolbar();
    }

    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        Logger.e(TagFinal.ZXX, "onSuccess: "+result );
        String name=wcfTask.getName();
        if (name.equals(REQUEST)){

            onPageBack();
        }
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {

        toastShow(R.string.fail_do_not);
    }

    private String project_title;
    private int scheduleid,scheduleid1;
    private String date,date1,no,no1,time,time1;
    public void getData(){
        Intent intent=getIntent();
        project_title=intent.getStringExtra("title");
        scheduleid=intent.getIntExtra("scheduleid",0);
        scheduleid1=intent.getIntExtra("scheduleid1",0);
        date=intent.getStringExtra("time");
        date1=intent.getStringExtra("time1");
        no=intent.getStringExtra("no");
        no1=intent.getStringExtra("no1");
        Logger.e(TagFinal.ZXX, "$: "+date1 );

        String s="a";
        String s1=date.split(s)[0];
        String s2="("+date.split(s)[1]+")";
        String s3=date1.split(s)[0];
        String s4="("+date1.split(s)[1]+")";
        time=s1.replace("-","/");
        time1=s3.replace("-","/");
        initView(s1+s2,s3+s4);
    }



    public void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle("申请调课");
        toolbar.addMenuText(1,R.string.submit1);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {

                submitRequest(0,edit.getText().toString());
            }
        });
    }
    public void initView(String date,String date1){
        title.setText(project_title);
        submit_1.setText(date);
        submit_2.setText(date1);
        submit_3.setText(no);
        submit_4.setText(no1);
    }


    public void submitRequest(int kqid,String reason){
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                kqid,//请假id，不请假id为0
                scheduleid,
                time,
                scheduleid1,
                time1,
                reason,
        };
        ParamsTask refreshTask = new ParamsTask(params, REQUEST, REQUEST);
        execute(refreshTask);
    }

}
