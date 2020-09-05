package com.yfy.app.integral;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.zhao_sheng.R;
import com.yfy.app.XlistLefttTxtAdapter;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.Logger;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

public class IntegralEditActivity extends BaseActivity {



    private String edit_type;//输入数据类型
    private String content;//输入数据类型

    @Bind(R.id.integral_edit)
    EditText edit;
    @Bind(R.id.integral_chioce)
    ListView list;
    private List<String> chioces=new ArrayList<>();
    private String chioce_string;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.integral_edit);
        getData();

        initSQToolbar();
    }

    private void initSQToolbar() {
        assert toolbar!=null;
        toolbar.setTitle("");
        toolbar.addMenuText(1,R.string.ok);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent=new Intent();
                String content=edit.getText().toString().trim();
                if (StringJudge.isEmpty(content)){
                    toastShow("未填写内容！不能提交");
                    return;
                }
                intent.putExtra("data",content);
                setResult(RESULT_OK,intent);
                onPageBack();
            }
        });


    }

    private void initView() {
        final int type_numbar=8194;
        if (StringJudge.isNotEmpty(content)){
            edit.setText(content);
        }
        int i=0;//文本
        if (StringJudge.isEmpty(edit_type)){

        }else {
            if (edit_type.equals("文字")) i=0;
            if (edit_type.equals("数字")) i=1;
            if (edit_type.equals("手机号")) i=2;
            if (edit_type.equals("email")) i=3;
            if (edit_type.equals("小数")) i=4;
            if (edit_type.equals("选择")) {
                edit.setVisibility(View.GONE);
                list.setVisibility(View.VISIBLE);
            }else{
                list.setVisibility(View.GONE);
                edit.setVisibility(View.VISIBLE);
            }
        }
        switch (i){
            case 0:
                edit.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case 1:
                edit.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case 2:
                edit.setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            case 3:
                edit.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT);
                break;
            case 4:
                edit.setInputType(type_numbar);
                edit.addTextChangedListener(new MyEdit());
                break;
        }

        XlistLefttTxtAdapter adapter=new XlistLefttTxtAdapter(mActivity,chioces);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent();
                intent.putExtra("data",chioces.get(i));
                setResult(RESULT_OK,intent);
                onPageBack();
            }
        });


    }

    public void getData(){
        Intent intent=getIntent();
        Bundle b=intent.getExtras();
        if (StringJudge.isContainsKey(b,"edit_type")){
            edit_type=b.getString("edit_type");
        }else{
            edit_type="";
        }
        if (StringJudge.isContainsKey(b,"data")){
            content=b.getString("data");
        }else{
            content="";
        }
        if (StringJudge.isContainsKey(b,"list")){
            chioce_string=b.getString("list");
        }else{
            chioce_string="";
        }
        Logger.e("zxx",chioce_string);
        if (StringJudge.isNotEmpty(chioce_string)){

            String[] s=chioce_string.split(",");


            chioces=Arrays.asList(s);
        }

        initView();
    }



    class MyEdit implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void afterTextChanged(Editable editable) {
            String edit_String=editable.toString();
            int posDot=edit_String.indexOf(".");
            if (posDot<=0)return;
            if (edit_String.length()-posDot-1>1)
            editable.delete(posDot+2,edit_String.length());//限制1位小数
        }
    }

}
