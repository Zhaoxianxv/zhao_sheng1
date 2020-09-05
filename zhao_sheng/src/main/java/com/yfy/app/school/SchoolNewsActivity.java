/**
 *
 */
package com.yfy.app.school;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.TableLayout;

import butterknife.Bind;

import com.example.zhao_sheng.R;
import com.yfy.app.net.*;
import com.yfy.app.net.affiche.SchoolGetMenuReq;
import com.yfy.app.school.bean.SchoolMenu;
import com.yfy.app.school.bean.MenuRes;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yfy1
 * @Date 2015年12月7日
 * @version 1.0
 * @Desprition
 */
public class SchoolNewsActivity extends BaseActivity implements Callback<ResEnv> {

    private final static String TAG = SchoolNewsActivity.class.getSimpleName();


    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.pager_view)
    ViewPager viewPager;

    TabNewsFragmentAdapter adapter;

    List<NewsPagerFragment> fragments=new ArrayList<>();
    List<String> titles=new ArrayList<>();

    private String no="02",title="校园新闻";//党建0201
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_pager_main);
        getData();
        initView();

    }

    private void getData(){
        initSQToolbar(title);
        getSchoolMenu();
    }

    private void initSQToolbar(String title){
        assert toolbar!=null;
        toolbar.setTitle(title);
    }

    private void initView() {


        tabLayout.setTabTextColors( Color.GRAY,getResources().getColor(R.color.app_base_color ));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.app_base_color ));
//        tabLayout.setTabMode(TabLayout.MODE_FIXED);//均匀分布整屏
		tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);//滚动样式
        TableLayout.LayoutParams params=new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        tabLayout.setLayoutParams(params);//MATCH_PARENT满屏均分布

        adapter=new TabNewsFragmentAdapter(fragments, titles,mActivity.getSupportFragmentManager(),mActivity);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager .setOffscreenPageLimit(fragments.size());
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
//				int index=tab.getPosition();

            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
    /**
     * ----------------------------retrofit-----------------------
     */



    public void getSchoolMenu() {
        ReqEnv evn = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        SchoolGetMenuReq req = new SchoolGetMenuReq();
        req.setNo(no);
        //获取参数
        reqBody.schoolGetMenuReq= req;
        evn.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().school_menu(evn);
        call.enqueue(this);
        showProgressDialog("");
    }


    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();

        ResEnv respEnvelope = response.body();
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);


        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.sChoolGetMenuRes!=null) {
                String result = b.sChoolGetMenuRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                MenuRes res=gson.fromJson(result,MenuRes.class);
                if (StringJudge.isEmpty(res.getData())){
                    toast(R.string.success_not_details);
                    return;
                }
                for (SchoolMenu menu:res.getData()){
                    titles.add(menu.getPrograma_name());
                    NewsPagerFragment fragment=new NewsPagerFragment();
                    Bundle bundle=new Bundle();
                    bundle.putString(TagFinal.ID_TAG,menu.getPrograma_id());
                    fragment.setArguments(bundle);
                    fragments.add(fragment);
                }
                adapter.setData(fragments,titles);
            }

        }else{
            Logger.e(StringUtils.getTextJoint("%1$s:%2$d",name,response.code()));
            toastShow(StringUtils.getTextJoint("数据错误:%1$d",response.code()));
        }
    }

    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        if (!isActivity())return;
        Logger.e("onFailure  :"+call.request().headers().toString());
        toast(R.string.fail_do_not);
        dismissProgressDialog();
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }


}
