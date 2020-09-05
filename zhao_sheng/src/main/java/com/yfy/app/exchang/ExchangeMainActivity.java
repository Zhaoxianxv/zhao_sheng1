package com.yfy.app.exchang;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.base.activity.BaseActivity;
import com.yfy.calendar.CustomDate;
import com.yfy.final_tag.StringJudge;
import com.yfy.view.SQToolBar;
import com.yfy.view.button.BottomItem;
import com.yfy.view.time.TimePickerDialog;

import butterknife.Bind;
import butterknife.OnClick;

public class ExchangeMainActivity extends BaseActivity implements TimePickerDialog.TimePickerDialogInterface{

    Fragment currFragment;

    MyTimetableFragment fragment1;
    MyExchangeFragment  fragment2;
    ExchangeDetailFragment fragment3;

    @Bind(R.id.exchange_bottom_1)
    BottomItem my_timetable;//school
    @Bind(R.id.exchange_bottom_2)
    BottomItem my_exchange;//class
    @Bind(R.id.exchange_bottom_3)
    BottomItem exchange_detail;//work
    String time="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchange_main);
        getData();
        initSQToolbar();
        initBottom();
        initFragment();


    }

    public void getData(){
        CustomDate cu=new CustomDate();
        String s= getIntent().getStringExtra("time");
        if (StringJudge.isEmpty(s)){
            time=cu.toString().replace("/","-");
        }else{
            time=s.replace("/","-");
        }
    }
    private TextView title,title_time;

    private void initSQToolbar() {
        assert toolbar!= null;
        title=toolbar.setTitle("调课");
        title_time=toolbar.addMenuText(1,time.replace("/","-"));
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {

                mDialog();

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        my_exchange = null;
        my_timetable = null;
        exchange_detail = null;
    }

    @OnClick(R.id.exchange_bottom_1)
    void setSchool(BottomItem bottomItem){
        showFragment(0);
        title_time.setVisibility(View.VISIBLE);
        switchStatus(bottomItem);
    }
    @OnClick(R.id.exchange_bottom_2)
    void setClass(BottomItem bottomItem){
        showFragment(1);
        title_time.setVisibility(View.GONE);
        switchStatus(bottomItem);
    }
    @OnClick(R.id.exchange_bottom_3)
    void setWork(BottomItem bottomItem){
        showFragment(2);
        title_time.setVisibility(View.GONE);
        switchStatus(bottomItem);
    }




    private void initFragment() {
        initBottom();
        //show he 配置要一至
        showFragment(0);
        my_timetable.switchViewStatus(false, Color.rgb(153,36,41),Color.rgb(128,128,128));
        my_timetable.performClick();


    }

    private void showFragment(int position) {
        FragmentTransaction transition = getSupportFragmentManager().beginTransaction();
        if (currFragment != null) {
            transition.hide(currFragment);
        }
        switch (position){
            case 0:
                if (fragment1 == null) {
                    fragment1 = new MyTimetableFragment();
                    transition.add(R.id.main_fragment, fragment1, time);
                }
                currFragment = fragment1;
                break;
            case 1:
                if (fragment2 == null) {
                    fragment2 = new MyExchangeFragment();
                    transition.add(R.id.main_fragment, fragment2, "school");
                }
                currFragment = fragment2;
                break;
            case 2:
                if (fragment3 == null) {
                    fragment3 = new ExchangeDetailFragment();
                    transition.add(R.id.main_fragment, fragment3, "school");
                }
                currFragment = fragment3;
                break;
        }

        transition.show(currFragment);
        transition.commit();
    }

    private void initBottom() {
        my_timetable.init();
        my_timetable.setbottomText("我的课表");
        my_timetable.hideBadge();

        my_exchange.init();
        my_exchange.hideBadge();
        my_exchange.setbottomText("与我调课");

        exchange_detail.init();
        exchange_detail.hideBadge();
        exchange_detail.setbottomText("调课记录");
    }


    private void switchStatus(BottomItem currSelectedView) {
        my_timetable.switchViewStatus(false, Color.rgb(153,36,41),Color.rgb(128,128,128));
        my_exchange.switchViewStatus(false, Color.rgb(153,36,41),Color.rgb(128,128,128));
        exchange_detail.switchViewStatus(false, Color.rgb(153,36,41),Color.rgb(128,128,128));
        currSelectedView.switchViewStatus(true, Color.rgb(153,36,41),Color.rgb(128,128,128));

    }


    //***************************
    public TitleTimeChange onchange;



    interface TitleTimeChange {
        void change(String time);
    }
    public void setOnchange(TitleTimeChange onchange) {
        this.onchange = onchange;
    }

    //选择时间dialog

    private TimePickerDialog timePickerDialog;
    public void mDialog(){
        if (timePickerDialog==null){
            timePickerDialog=new TimePickerDialog(mActivity);
        }

        timePickerDialog.showDatePickerDialog();

    }
    @Override
    public void positiveListener() {

        int year=timePickerDialog.getYear();
        int month=timePickerDialog.getMonth();
        int day=timePickerDialog.getDay();
        title_time.setText(year+"-"+month+"-"+day);
        if (onchange!=null){
            onchange.change(title_time.getText().toString());
        }
    }

    @Override
    public void negativeListener() {

    }
}
