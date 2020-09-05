package com.yfy.app.answer;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.TableLayout;

import com.example.zhao_sheng.R;
import com.yfy.app.TabFragmentAdapter;
import com.yfy.app.answer.fragment.Tab1Fragment;
import com.yfy.app.answer.fragment.Tab2Fragment;
import com.yfy.app.answer.fragment.Tab3Fragment;
import com.yfy.app.answer.fragment.Tab4Fragment;
import com.yfy.base.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class AnswerMainActivity extends BaseActivity {


    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.pager_view)
    ViewPager viewPager;

    private List<String> titles=new ArrayList<>();
    private TabFragmentAdapter adapter;
    List<Fragment> fragments=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_pager_main);
        initTitle();
    }

    public void initTitle(){
        titles.add("最新提问");
        titles.add("热门提问");
        titles.add("我的提问");
        titles.add("我的回答");

        fragments.add(new Tab1Fragment());
        fragments.add(new Tab2Fragment());
        fragments.add(new Tab3Fragment());
        fragments.add(new Tab4Fragment());

        tabLayout.setTabTextColors( Color.GRAY,getResources().getColor(R.color.app_base_color ));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.app_base_color ));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);//滚动样式
        TableLayout.LayoutParams params=new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        tabLayout.setLayoutParams(params);//MATCH_PARENT满屏均分布

        adapter=new TabFragmentAdapter(fragments, titles,mActivity.getSupportFragmentManager(),mActivity);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager .setOffscreenPageLimit(fragments.size());
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
                int index = tab.getPosition();
                switch (index){
                    case 0:
                        iss.get(0).isChioce(1);
                        break;
                    case 1:
                        iss.get(1).isChioce(2);
                        break;
                    case 2:
                        iss.get(2).isChioce(3);
                        break;
                    case 3:
                        iss.get(3).isChioce(4);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    //控制班级学期选择
    public static SchoolInterface isInitData;
    public static List<SchoolInterface> iss=new ArrayList<>();
    public static void setIsInitData(int i,SchoolInterface is) {
        switch (i){
            case 0:
                iss.add(i,is);
                break;
            case 1:
                iss.add(i,is);
                break;
            case 2:
                iss.add(i,is);
                break;
            case 3:
                iss.add(i,is);
                break;
        }

    }
    public interface SchoolInterface{
        void isChioce(int id);

    }


}
