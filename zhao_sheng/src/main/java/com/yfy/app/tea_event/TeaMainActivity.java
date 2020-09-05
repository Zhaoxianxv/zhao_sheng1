package com.yfy.app.tea_event;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.integral.ChangeTermActivity;
import com.yfy.app.tea_event.adapter.TabFragmentAdapter;
import com.yfy.base.Variables;
import com.yfy.base.activity.BaseActivity;
import com.yfy.db.UserPreferences;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.TagFinal;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;



//type stu
public class TeaMainActivity extends BaseActivity {

    private static final String TAG = TeaMainActivity.class.getSimpleName();
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private TabFragmentAdapter adapter;
    private List<Fragment> fragments=new ArrayList<>();
    private List<String> titles=new ArrayList<>();
    private int term_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_pager_main);
        term_id=ConvertObjtect.getInstance().getInt(UserPreferences.getInstance().getTermId());
        initSQtoolbar();

        initTab();
    }

    private TextView onemenu;
    private void initSQtoolbar() {
        assert toolbar!=null;
        String title=UserPreferences.getInstance().getTermName();
        toolbar.setTitle(title.equals("")?"评测":title);
        onemenu=toolbar.addMenuText(TagFinal.ONE_INT,"说明");
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (position){
                    case TagFinal.ONE_INT:
                        startActivity(new Intent(mActivity,DialogActivity.class));
                        break;
                    case TagFinal.TWO_INT:
//                        Intent intent=new Intent(mActivity,ChangeTermActivity.class);
//                        startActivityForResult(intent,TagFinal.UI_TAG);
                        break;
                }

            }
        });
        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.pager_view);
        onemenu.performClick();


    }

    public void initTab(){

        Fragment tea=new TeaFragment();
        Fragment school=new SchoolFragment();

        Bundle b=new Bundle();
        b.putInt(TagFinal.id, term_id);
        school.setArguments(b);
        titles.add("师德满意率评测");
        titles.add("学校满意率度测");//黄敏
        fragments.add(tea);
        fragments.add(school);

        tabLayout.setTabTextColors( Color.GRAY,getResources().getColor(R.color.app_base_color ));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.app_base_color ));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);//滚动样式
        TableLayout.LayoutParams params=new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        tabLayout.setLayoutParams(params);//MATCH_PARENT满屏均分布
        adapter=new TabFragmentAdapter(fragments, titles,getSupportFragmentManager(),TeaMainActivity.this);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(fragments.size());
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
                int index = tab.getPosition();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_TAG:
//                    String name,id;
//                    name=data.getStringExtra("data_name");
//                    id=data.getStringExtra("data_id");
//                    menu1.setText(name);
//                    term_id= ConvertObjtect.getInstance().getInt(id);
                    break;

            }
        }
    }

    /**
     * ----------------------------retrofit-----------------------
     */



    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
