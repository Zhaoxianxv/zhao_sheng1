package com.yfy.app.cyclopedia;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.example.zhao_sheng.R;
import com.yfy.app.cyclopedia.beans.Hot;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.dialog.LoadingDialog;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.view.ClearEditText;
import com.yfy.view.MyRadioGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


public class CycSearchActivity extends WcfActivity {



    private final String CYCLOPEDIA_TYPE="ancyclopedia_hot";
    private int pager=0;
    private LoadingDialog loadingDialog;


    @Bind(R.id.cyc_search_et)
    ClearEditText clear_et;



    private List<ArrayMap> mTexts =new ArrayList<ArrayMap>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cyc_search);
        loadingDialog=new LoadingDialog(mActivity);
        refresh();

    }

    private void initView(final List<String> data) {
//        GridAdapter adapter=new GridAdapter(mActivity,data);
//        gradview.setAdapter(adapter);
//        gradview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                goTosearch(data.get(i));
//            }
//        });

    }


    public void refresh(){
        Object[] params = new Object[] {
                Base.user.getSession_key()==null?"":Base.user.getSession_key(),
                pager,
                PAGE_NUM

        };
        ParamsTask gettype = new ParamsTask(params, CYCLOPEDIA_TYPE);
        execute(gettype);
    }

    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        Logger.e("zxx","result ;"+result);
        Hot hot=gson.fromJson(result,Hot.class);
        if (hot!=null&hot.getHot_list()!=null){
            initView(hot.getHot_list());
            addText(hot.getHot_list());
        }
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        toastShow(wcfTask.getName());

    }

    /**
     * 动态添加控件
     */

    public void addText(List<String> hot_list){


        MyRadioGroup layout= (MyRadioGroup) findViewById(R.id.hot_item);

        //获取到最大宽度


        for (String name:hot_list) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);



            RadioButton textView=new RadioButton(mActivity);
            textView.setTextColor(getResources().getColor(R.color.black));
            textView.setBackgroundResource(R.drawable.shape_line_black);
            textView.setPadding(20,5,20,5);
            textView.setText(name);
            textView.setTextSize(14);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            Bitmap a = null;
            textView.setButtonDrawable(new BitmapDrawable(a));
            textView.setLayoutParams(layoutParams);
            layout.addView(textView);
            ArrayMap<String ,Object> map=new ArrayMap<String ,Object>();
            map.put("text",textView);
            map.put("name",name);
            mTexts.add(map);
        }
        textOnClick(mTexts);
    }

    /**
     * textview listenner
     * @param mTexts
     */

    public void textOnClick(List<ArrayMap> mTexts){
        for (ArrayMap<String ,Object> map:mTexts) {

            RadioButton text= (RadioButton) map.get("text");
            final String name= (String) map.get("name");
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goTosearch(name);
                }
            });
        }

    }

//    private List<ContactMember> getMemberList(List<AncyclopediaList> groupList) {
//        List<ContactMember> memberList = new ArrayList<ContactMember>();
//        for (AncyclopediaList contactGroup : groupList) {
//            if (contactGroup.getChildCount() != 0) {
//                memberList.addAll(contactGroup.getMemberList());
//            }
//        }
//        return memberList;
//    }

    @OnClick(R.id.cyc_search_et)
    void setClear_et(){

        goTosearch("");

    }
    public void goTosearch(String name){
        Intent i=new Intent(mActivity,SearchActivity.class);
        i.putExtra("name",name);
        startActivity(i);
    }
}
