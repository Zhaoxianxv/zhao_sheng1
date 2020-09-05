package com.yfy.app.studen_award;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.studen_award.adapter.AwardStuAdapter;
import com.yfy.app.studen_award.bean.AwardStuBean;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.dialog.LoadingDialog;
import com.yfy.view.swipe.xlist.XListView;
import com.yfy.final_tag.CharacterParser;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class AwardSearchStuActivity extends BaseActivity {

    private String get_award_students="get_award_students";
    private String class_id,class_name;
    @Bind(R.id.award_xlist_main)
    XListView xlist;
    @Bind(R.id.award_clear_et)
    EditText clear_et;
    LoadingDialog dialog;
    private ArrayList<AwardStuBean> stubeans=new ArrayList<>();
    private List<AwardStuBean> filterDateList = new ArrayList<>();
    AwardStuAdapter xlistAdapter;

    private TextView menu1,menu2;
    private boolean isSeletor=false;
    private boolean tag=false;
    private String selectorStu;


    private CharacterParser characterParser ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.award_class_chioce);
        dialog=new LoadingDialog(mActivity);
        characterParser= new CharacterParser();
        getData();
        initSQToolbar();
        initView();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPageBack() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        closeKeyWord();
    }

    public void getData(){

        stubeans=getIntent().getParcelableArrayListExtra("data");
        tag=getIntent().getBooleanExtra("tag",false);
    }

    private void initSQToolbar() {
        assert toolbar!=null;
//        toolbar.setTitle(class_name);
        toolbar.addMenuText(1,R.string.ok);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {

                filterData(clear_et.getText().toString().trim());
            }
        });

    }
    private void initView() {
        xlist.setPullLoadEnable(false);
        xlist.setPullRefreshEnable(false);
        xlist.setVisibility(View.GONE);
        xlistAdapter=new AwardStuAdapter(mActivity,stubeans);
        xlist.setAdapter(xlistAdapter);
        xlist.setOnItemClickListener(chiocelistenner);

    }



    private AdapterView.OnItemClickListener chiocelistenner=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            String id=filterDateList.get(i-1).getId();
            if (tag){
                Intent intent=new Intent(mActivity,AwardMainActivity.class);
                intent.putExtra("stu_id",id);

                intent.putExtra("stu_name",filterDateList.get(i-1).getName());
                intent.putExtra("isStu",true);
                startActivity(intent);
            }else{
                Intent intent=new Intent();
                for (AwardStuBean stu : stubeans) {
                    if(stu.getId().equals(id)){
                        stu.setChick(true);
                    }
                }
                intent.putParcelableArrayListExtra("data",stubeans);
                setResult(RESULT_OK,intent);
                onPageBack();
            }



        }
    };

    private void filterData(String filterStr) {

        filterDateList.clear();
        if (!TextUtils.isEmpty(filterStr)) {
            for (AwardStuBean contactMember : stubeans) {
                String name = contactMember.getName();
                if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(contactMember);
                }
            }
        } else {

            xlist.setVisibility(View.VISIBLE);
            xlistAdapter.notifyDataSetChanged(filterDateList);
            return;
        }

        if (filterDateList.size() == 0) {
            xlist.setVisibility(View.GONE);
        } else {
            xlist.setVisibility(View.VISIBLE);
            xlistAdapter.notifyDataSetChanged(filterDateList);
        }
    }


}
