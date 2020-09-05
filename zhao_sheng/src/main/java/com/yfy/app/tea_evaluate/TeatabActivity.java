package com.yfy.app.tea_evaluate;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.example.zhao_sheng.R;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.judge.TeaJudgeGetYearReq;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.Logger;
import com.yfy.view.button.BottomItem;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeatabActivity extends BaseActivity  implements Callback<ResEnv> {


    private static final String TAG = TeatabActivity.class.getSimpleName();
    Fragment currFragment;

    AdminMainfragment fragment1;
    Smartfragment fragment2;
    Yearfragment fragment3;


    @Bind(R.id.home_bottom_school)
    BottomItem my_timetable;//school
    @Bind(R.id.home_bottom_class)
    BottomItem my_exchange;//class
    @Bind(R.id.home_bottom_work)
    BottomItem exchange_detail;//work
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tea_teatab);
        getShapeKind();
        initFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        my_exchange = null;
        my_timetable = null;
        exchange_detail = null;
    }




    private void initFragment() {
        initBottom();
        //show he 配置要一至
        showFragment(0);
        my_timetable.switchViewStatus(false, Color.rgb(153,36,41),Color.rgb(128,128,128));
        my_timetable.performClick();


    }


    private void showFragment(int position)
    {
        FragmentTransaction transition = getSupportFragmentManager().beginTransaction();
        if (currFragment != null) {
            transition.hide(currFragment);
        }
        switch (position) {
            case 0:
                if (fragment1 == null) {
                    fragment1 = new AdminMainfragment();
                    transition.add(R.id.main_fragment, fragment1, "3");
                }
                currFragment = fragment1;
                break;
            case 1:
                if (fragment2 == null) {
                    fragment2 = new Smartfragment();
                    transition.add(R.id.main_fragment, fragment2, "3");
                }
                currFragment = fragment2;
                break;
            case 2:
                if (fragment3 == null) {
                    fragment3 = new Yearfragment()  ;
                    transition.add(R.id.main_fragment, fragment3, "3");
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





    /**
     * ----------------------------retrofit-----------------------
     */


    public void getShapeKind()  {

        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        TeaJudgeGetYearReq request = new TeaJudgeGetYearReq();
        //获取参数

        reqBody.teaJudgeGetYearReq = request;
        reqEnvelop.body = reqBody;

        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().judge_get_year(reqEnvelop);
        call.enqueue(this);
        showProgressDialog("正在加载");

    }

    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b = respEnvelope.body;
            if (b.teaJudgeGetYearRes != null) {
                String result = b.teaJudgeGetYearRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
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
        dismissProgressDialog();
    }


    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
