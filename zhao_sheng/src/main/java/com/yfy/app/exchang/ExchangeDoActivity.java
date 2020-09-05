package com.yfy.app.exchang;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.exchang.adapter.ExchangeDoAdapter;
import com.yfy.app.exchang.bean.CourseInfor;
import com.yfy.app.exchang.bean.ExchangeInfor;
import com.yfy.app.exchang.bean.ScheduleInfor;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
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
import com.yfy.view.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

public class ExchangeDoActivity extends WcfActivity implements TimePickerDialog.TimePickerDialogInterface{

    private int page=0;
    @Bind(R.id.exchange_do_xlist)
    XListView xlist;
    private ConvertObjtect convert;
    private ExchangeDoAdapter adapter;
    private List<Map<String,CourseInfor>> course_itmes=new ArrayList<>();
    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchange_do);
        convert=ConvertObjtect.getInstance();
        dialog=new LoadingDialog(mActivity);
        getData();
        initSQToolbar();
        initView();

    }

    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        Logger.e(TagFinal.ZXX, "onSuccess: "+result );
        String name=wcfTask.getName();
        if (xlist!=null){
            xlist.stopRefresh();
        }
        if (name.equals(method)){
            ExchangeInfor infor=gson.fromJson(result,ExchangeInfor.class);
            refreshView(infor.getSchedule());
        }
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {

    }

    @Override
    public void positiveListener() {
        int year=timePickerDialog.getYear();
        int month=timePickerDialog.getMonth();
        int day=timePickerDialog.getDay();
        date=year+"-"+month+"-"+day;
        title_time.setText(date);

        refresh(date,true);
    }

    @Override
    public void negativeListener() {

    }
    //选择时间dialog
    private TimePickerDialog timePickerDialog;
    public void mDialog(){
        if (timePickerDialog==null){
            timePickerDialog=new TimePickerDialog(mActivity);
        }

        timePickerDialog.showDatePickerDialog();

    }



    private String date,class_name,no,time;
    private int class_id,scheduleid,maxno;
    public void getData(){
        Intent intent=getIntent();
        date=intent.getStringExtra("date");
        time=intent.getStringExtra("time");
        no=intent.getStringExtra("no");
        class_name=intent.getStringExtra("class_name");
        class_id=intent.getIntExtra("class_id",0);
        scheduleid=intent.getIntExtra("scheduleid",0);
        maxno=intent.getIntExtra("maxno",0);

        refresh(date,true);


    }
    private TextView title_time;
    public void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle("调课");
        title_time=toolbar.addMenuText(1,date.replace("/","-"));
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                mDialog();
            }
        });
    }
    private TextView table_1,table_2,table_3,table_4,table_5;
    public void initView(){
        xlist.setPullLoadEnable(false);
        adapter=new ExchangeDoAdapter(mActivity,course_itmes);

        adapter.setOnCourse(doTbale);
        xlist.setAdapter(adapter);
        View v= LayoutInflater.from(mActivity).inflate(R.layout.exchange_table_xlist_head,null);
        table_1= (TextView) v.findViewById(R.id.exchange_table_1);
        table_2= (TextView) v.findViewById(R.id.exchange_table_2);
        table_3= (TextView) v.findViewById(R.id.exchange_table_3);
        table_4= (TextView) v.findViewById(R.id.exchange_table_4);
        table_5= (TextView) v.findViewById(R.id.exchange_table_5);
        xlist.addHeaderView(v);

        xlist.setXListViewListener(new XlistListener() {
            @Override
            public void onRefresh() {
                super.onRefresh();
                refresh(date,false);
            }
        });
    }


    private ExchangeDoAdapter.ExchangeDoTbale doTbale=new ExchangeDoAdapter.ExchangeDoTbale() {
        @Override
        public void onCourse(String date1, String scheduleid1,String no1,String time1) {
            Intent intent=new Intent(mActivity,EXchangeRequestActivity.class);
            intent.putExtra("title",class_name);
            intent.putExtra("scheduleid1",convert.getInt(scheduleid1));
            intent.putExtra("scheduleid",scheduleid);
            intent.putExtra("time",time);
            intent.putExtra("time1",getTime(time1));
            intent.putExtra("no",no);
            intent.putExtra("no1",no1);
            startActivity(intent);
        }
    };


    public void refreshView( List<ScheduleInfor> data){

        course_itmes.clear();
        for (int n=0;n<maxno;n++){
            Map<String ,CourseInfor> map= new HashMap<String ,CourseInfor>();
            for (ScheduleInfor inf:data){
                List<CourseInfor> course=inf.getCourse();
                if (n==0) initListHead(inf.getDayid(),inf.getDate().replace("/","-")+"\n"+inf.getDay());

                if (StringJudge.isEmpty(course)){
                    //没课
                    continue;
                }else{
                    //有课
                    for (CourseInfor in:course){
                        int i=convert.getInt(in.getNo());
                        if (i==n+1){
                            map.put(inf.getDayid(),in);
                            continue;
                        }
                    }
                }
            }
            course_itmes.add(map);
        }

        adapter.notifyDataSetChanged(course_itmes);

    }
    public void initListHead(String dayid,String name){
        int index=convert.getInt(dayid);
        switch (index){
            case 1:
                table_1.setText(name);
            case 2:
                table_2.setText(name);
            case 3:
                table_3.setText(name);
            case 4:
                table_4.setText(name);
            case 5:
                table_5.setText(name);
        }
    }


    private final String method="get_class_schedule";
    public void refresh(String date,boolean is){
        String time=date.replace("-","/");
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                class_id,
                scheduleid,
                time,

        };
        ParamsTask able;
        if (is){
            able= new ParamsTask(params, method, method,dialog);
        }else{
            able = new ParamsTask(params, method, method);
        }


        execute(able);
    }

    public String getTime(String num){

        int in=convert.getInt(num);
        String time="";
        switch (in){
            case 1:
                time=table_1.getText().toString().replace("\n","a");
                break;
            case 2:
                time=table_2.getText().toString().replace("\n","a");
                break;
            case 3:
                time=table_3.getText().toString().replace("\n","a");
                break;
            case 4:
                time=table_4.getText().toString().replace("\n","a");
                break;
            case 5:
                time=table_5.getText().toString().replace("\n","a");
                break;
        }

        
        return time;
    }

}
