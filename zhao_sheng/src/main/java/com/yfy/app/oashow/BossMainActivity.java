package com.yfy.app.oashow;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.zhao_sheng.R;
import com.yfy.base.activity.BaseActivity;

import butterknife.OnClick;

public class BossMainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boss_main);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        initSQTolbar();
    }


    private void initSQTolbar(){
        assert toolbar!=null;
        toolbar.setTitle("行政查看");
    }

    @OnClick(R.id.base_layout_one)
    void setOne(){

        Intent intent=new Intent(mActivity,BAttenActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.base_layout_two)
    void setTwo(){

        Intent intent=new Intent(mActivity,BMaintianActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.base_layout_three)
    void setThree(){

        Intent intent=new Intent(mActivity,BOrderActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.base_layout_four)
    void setFour(){
//        Intent intent=new Intent(mActivity,BLateActivity.class);
//        startActivity(intent);
    }
}
