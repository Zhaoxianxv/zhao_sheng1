package com.yfy.app.maintainnew;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.base.activity.BaseActivity;
import com.yfy.db.UserPreferences;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.TagFinal;

import butterknife.Bind;
import butterknife.OnClick;

public class MaintainEditActivity extends BaseActivity {

    private static final String TAG = "zxx";
    @Bind(R.id.edit_title)
    TextView title;
    @Bind(R.id.edit_edit_text)
    EditText edit_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintain_edit);
        getData();
        initView();

    }

    private void initView() {
        edit_content.setText(UserPreferences.getInstance().getContent());
    }

    public void getData(){
        Intent intent=getIntent();
        title.setText(intent.getStringExtra(TagFinal.OBJECT_TAG));
    }

    @OnClick(R.id.maintain_edit)
    void setLayout(){
       closeKeyWord();
    }
    @OnClick(R.id.edit_title)
    void setTitle(){
        closeKeyWord();
    }
    @OnClick(R.id.edit_cancel)
    void setCancel(){
        UserPreferences.getInstance().saveContent(edit_content.getText().toString().trim());
        onPageBack();
    }
    @OnClick(R.id.edit_ok)
    void setOk(){
        Intent intent=new Intent();
        String content=edit_content.getText().toString().trim();
        if (StringJudge.isEmpty(content)){
            content="";
        }
        intent.putExtra(TagFinal.OBJECT_TAG,content);
        setResult(RESULT_OK,intent);
        UserPreferences.getInstance().saveContent("");
        onPageBack();
    }
}
