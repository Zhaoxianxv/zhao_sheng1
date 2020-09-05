package com.yfy.app.tea_event;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.base.activity.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

public class DialogActivity extends BaseActivity {


    @Bind(R.id.dialog_ok)
    TextView ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tea_dialog);
    }
    @OnClick(R.id.dialog_ok)
    void setOk(){
        finish();
    }
}
