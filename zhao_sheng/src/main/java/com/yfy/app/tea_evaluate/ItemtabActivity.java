package com.yfy.app.tea_evaluate;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.example.zhao_sheng.R;
import com.yfy.base.Variables;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.view.button.BottomItem;

import butterknife.Bind;
import butterknife.OnClick;

public class ItemtabActivity extends BaseActivity {


    Fragment currFragment;

    ChartItemfragment fragment1;
    ItemSmartfragment fragment2;
    YearItemfragment fragment3;


    @Bind(R.id.home_bottom_school)
    BottomItem my_timetable;//school
    @Bind(R.id.home_bottom_class)
    BottomItem my_exchange;//class
    @Bind(R.id.home_bottom_work)
    BottomItem exchange_detail;//work
    private String id="1";
    public String title;


    public String getTitl() {
        return title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tea_teatab);

        getData();

        initBottom();
        initFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        my_exchange = null;
        my_timetable = null;
        exchange_detail = null;
        Logger.e("zxx","onDestroy");
    }


    private void getData(){
        id=getIntent().getStringExtra(TagFinal.OBJECT_TAG);
        title = getIntent().getStringExtra("title");
        Variables.type=title;
        Variables.type_id=id;
        assert toolbar!=null;
        toolbar.setTitle(title);
    }



    private void initFragment() {

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
        switch (position) {
            case 0:
                if (fragment1 == null) {
                    fragment1 = new ChartItemfragment();
                    transition.add(R.id.main_fragment, fragment1, id);
                }
                currFragment = fragment1;
                break;
            case 1:
                if (fragment2 == null) {
                    fragment2 = new ItemSmartfragment();
                    transition.add(R.id.main_fragment, fragment2, id);
                }
                currFragment = fragment2;
                break;
            case 2:
                if (fragment3 == null) {
                    fragment3 = new YearItemfragment()  ;
                    transition.add(R.id.main_fragment, fragment3, id);
                }
                currFragment = fragment3;
                break;

        }
        transition.show(currFragment);
        transition.commit();
    }



    private void initBottom() {
        my_timetable.init();
        my_timetable.setbottomText("图表统计");
        my_timetable.hideBadge();

        my_exchange.init();
        my_exchange.hideBadge();
        my_exchange.setbottomText("详细统计");

        exchange_detail.init();
        exchange_detail.hideBadge();
        exchange_detail.setbottomText("年度点评");
    }


    private void switchStatus(BottomItem currSelectedView) {
        my_timetable.switchViewStatus(false, Color.rgb(153,36,41),Color.rgb(128,128,128));
        my_exchange.switchViewStatus(false, Color.rgb(153,36,41),Color.rgb(128,128,128));
        exchange_detail.switchViewStatus(false, Color.rgb(153,36,41),Color.rgb(128,128,128));
        currSelectedView.switchViewStatus(true, Color.rgb(153,36,41),Color.rgb(128,128,128));

    }

    @OnClick(R.id.home_bottom_school)
    void setSchool(BottomItem bottomItem){
        showFragment(0);

        switchStatus(bottomItem);
    }
    @OnClick(R.id.home_bottom_class)
    void setClass(BottomItem bottomItem){
        showFragment(1);
        switchStatus(bottomItem);
    }
    @OnClick(R.id.home_bottom_work)
    void setWork(BottomItem bottomItem){
        showFragment(2);
        switchStatus(bottomItem);
    }




}
