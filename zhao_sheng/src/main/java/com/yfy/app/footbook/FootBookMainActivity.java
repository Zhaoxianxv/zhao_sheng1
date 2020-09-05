package com.yfy.app.footbook;

import android.graphics.Color;
import android.os.IInterface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;

import com.example.zhao_sheng.R;
import com.yfy.app.TabFragmentAdapter;
import com.yfy.base.activity.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

public class FootBookMainActivity extends BaseActivity {


    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.pager_view)
    ViewPager viewPager;

    TabFragmentAdapter adapter;
    List<Fragment> fragments=new ArrayList<>();
    List<String> titles=new ArrayList<>();
    private Foot1Fragment foot1;
    private Foot2Fragment foot2;
    private Foot3Fragment foot3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_pager_main);
        initSQToolbar();
        initView();
    }
    private void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle(R.string.foot_book);
    }

    private void initView() {

        titles.add("本周食谱");
        titles.add("总排行");
        titles.add("本周排行");
        foot1=new Foot1Fragment();
        foot2=new Foot2Fragment();
        foot3=new Foot3Fragment();
        fragments.add(foot1);
        fragments.add(foot2);
        fragments.add(foot3);
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
                int index=tab.getPosition();
                switch (index){
                    case 0:
//                        map.get(index).foot(1);
                        break;
                    case 1:
                        map.get(index).foot(1);
                        break;
                    case 2:
                        map.get(index).foot(0);
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


    public static FootBookInterFace footBookInterFace;
    public static Map<Integer,FootBookInterFace> map=new HashMap<>();

    public static void setFootBookInterFace(int i,FootBookInterFace footBookInterFace) {
        map.put(i,footBookInterFace);
    }
    public interface FootBookInterFace{
        void foot(int i);
    }

}
