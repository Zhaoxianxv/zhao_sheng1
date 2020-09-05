package com.yfy.app.exchang;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.google.gson.Gson;
import com.yfy.app.exchang.adapter.TimeableAdapter;
import com.yfy.app.exchang.bean.CourseInfor;
import com.yfy.app.exchang.bean.ExchangeInfor;
import com.yfy.app.exchang.bean.ScheduleInfor;
import com.yfy.final_tag.Base;
import com.yfy.base.fragment.WcfFragment;
import com.yfy.calendar.CustomDate;
import com.yfy.final_tag.dialog.LoadingDialog;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.view.swipe.xlist.XListView;
import com.yfy.view.swipe.xlist.XlistListener;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by yfyandr on 2017/8/14.
 */

public class MyTimetableFragment extends WcfFragment {

    private static final String TAG = "zxx";
    @Bind(R.id.timetable_fragment_xlist)
    XListView xlist;

    TimeableAdapter adapter;
    private Gson gson;
    private ConvertObjtect convert;
    private List<Map<String,CourseInfor>> course_itmes=new ArrayList<>();
    private LoadingDialog dialog;
    @Override
    public void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.exchange_my_timetable_fragment);

        String s=getTag();
        initView();
        cu=new CustomDate();
        dialog=new LoadingDialog(mActivity);
        convert=ConvertObjtect.getInstance();
        gson=new Gson();
        if (StringJudge.isEmpty(s)){
            date=cu.toString();
        }else{
            date=s.replace("-","/");
        }
        refresh(date,true);
        ExchangeMainActivity obj= (ExchangeMainActivity) getActivity();
        obj.setOnchange(time_change);

    }



    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        Logger.e( "onSuccess: "+result );
        String name=wcfTask.getName();
        if (xlist!=null){
            xlist.stopRefresh();
        }
        if (name.equals(method)){
            ExchangeInfor infor=gson.fromJson(result,ExchangeInfor.class);
            refreshView(infor.getMaxno(),infor.getSchedule());
        }
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {

    }


    private int maxno;
    public void refreshView(String num, List<ScheduleInfor> data){
        maxno=convert.getInt(num);
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

    private TextView table_1,table_2,table_3,table_4,table_5;
    public void initView(){
        xlist.setPullLoadEnable(false);
        adapter=new TimeableAdapter(mActivity,course_itmes);
        adapter.setOnCourse(onclick);
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


    private final String method="get_user_schedule";
    public void refresh(String date,boolean is){

        Object[] params = new Object[] {
                Base.user.getSession_key(),
                date
                 };


        ParamsTask able;
        if (is){
            able= new ParamsTask(params, method, method,dialog);
        }else{
            able = new ParamsTask(params, method, method);
        }
        execute(able);
    }


    private CustomDate cu;
    private String date;
    private ExchangeMainActivity.TitleTimeChange time_change=new ExchangeMainActivity.TitleTimeChange() {
        @Override
        public void change(String time) {
            date=time;
            refresh(date.replace("-","/"),true);
        }
    };

    private TimeableAdapter.CourseTbale onclick=new TimeableAdapter.CourseTbale() {
        @Override
        public void onCourse(String class_id,String scheduleid,String name,String no,String time) {
            Intent intent=new Intent(mActivity,ExchangeDoActivity.class);
            intent.putExtra("date",date);
            intent.putExtra("class_name",name);
            intent.putExtra("time",getTime(time));
            intent.putExtra("class_id",convert.getInt(class_id));
            intent.putExtra("scheduleid",convert.getInt(scheduleid));
            intent.putExtra("maxno",maxno);
            intent.putExtra("no",no);
            startActivity(intent);


        }

        @Override
        public void did(boolean is) {
            mDialog(is);
        }
    };
    public  void mDialog(boolean is) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        if (is){
            builder.setMessage(R.string.exchange_did_not_onclick);
        }else{
            builder.setMessage(R.string.exchange_d_not_onclick);
        }

        builder.setTitle("提示");
        builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
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
