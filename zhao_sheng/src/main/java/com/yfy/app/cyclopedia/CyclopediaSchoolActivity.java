package com.yfy.app.cyclopedia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.cyclopedia.beans.Cyconut;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.view.SQToolBar;
import com.yfy.view.button.BottomItem;

import butterknife.Bind;
import butterknife.OnClick;

public class CyclopediaSchoolActivity extends WcfActivity {

    Fragment currFragment;
    SchoolFragment school;
    MyFragment my;
    NowFragment now;


    @Bind(R.id.home_bottom_school)
    BottomItem bottomschool;//school
    @Bind(R.id.home_bottom_class)
    BottomItem bottomclass;//class
    @Bind(R.id.home_bottom_work)
    BottomItem bottomwork;//work

    @Bind(R.id.cyc_item_num_tv)
    TextView item;
    @Bind(R.id.cyc_call_num_tv)
    TextView call;



    private String abcitem="词条：";
    private String sonconut="人次：";
    private String unit="个";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cyclopedia_school);
        initSQToolbar();
        initBottom();
        switchStatus(bottomschool);
        showFragment(0);
        getCyclopedia();

    }


    private void showFragment(int position) {
        FragmentTransaction transition = getSupportFragmentManager().beginTransaction();
        if (currFragment != null) {
            transition.hide(currFragment);
        }
        if (position == 0) {
            if (school == null) {
                school = new SchoolFragment();
                transition.add(R.id.main_fragment, school, "school");
            }
            currFragment = school;

        }
        if (position == 1) {
            if (now == null) {
                now = new NowFragment();
                transition.add(R.id.main_fragment, now, "now");
            }
            currFragment = now;
        }
        if (position == 2) {
            if (my == null) {
                my = new MyFragment();
                transition.add(R.id.main_fragment, my, "school");
            }
            currFragment = my;
        }
        transition.show(currFragment);
        transition.commit();
    }




    private void initSQToolbar() {

        assert  toolbar!=null;
        toolbar.setTitle(R.string.cyclopedia_school);
        toolbar.addMenu(1,R.drawable.search);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                mActivity.startActivity(new Intent(mActivity,CycSearchActivity.class));
            }
        });
    }

    private void initBottom() {
        bottomschool.init();
        bottomschool.setbottomText("最新");
        bottomclass.init();
        bottomclass.setbottomText("热门");
        bottomwork.init();
        bottomwork.setbottomText("参与");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bottomschool = null;
        bottomclass = null;
        bottomwork = null;
        Logger.e("zxx","onDestroy");
    }
    private void switchStatus(BottomItem currSelectedView) {
        bottomschool.switchViewStatus(false);
        bottomclass.switchViewStatus(false);
        bottomwork.switchViewStatus(false);
        bottomschool.setBackgroundColor(getResources().getColor(R.color.gray));
        bottomclass.setBackgroundColor(getResources().getColor(R.color.gray));
        bottomwork.setBackgroundColor(getResources().getColor(R.color.gray));
        currSelectedView.switchViewStatus(true);
        currSelectedView.setBackgroundColor(getResources().getColor(R.color.main_red));
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


    public void getCyclopedia(){
        final String methoh="ancyclopedia_count";
        Object[] params = new Object[] {
                ""
        };
        ParamsTask count = new ParamsTask(params, methoh);
        execute(count);
    }


    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
//        Log.e("zxx",""+result);
        Cyconut cont=gson.fromJson(result, Cyconut.class);
        item.setText(abcitem+cont.getEntry_count()+unit);
        call.setText(sonconut+cont.getBrowse_count()+unit);
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        toastShow(R.string.fail_load_again);

    }
}
