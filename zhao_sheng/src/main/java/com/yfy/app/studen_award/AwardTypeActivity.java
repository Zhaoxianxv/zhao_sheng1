package com.yfy.app.studen_award;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.example.zhao_sheng.R;
import com.yfy.app.studen_award.adapter.AwardDialogTypeAdapter;
import com.yfy.app.studen_award.bean.AwardInfor;
import com.yfy.app.studen_award.bean.AwardType;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.TagFinal;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.view.swipe.xlist.XListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class AwardTypeActivity extends WcfActivity {


    @Bind(R.id.award_xlist_main)
    XListView xlist;
    @Bind(R.id.award_clear_et)
    EditText clear_et;
    AwardDialogTypeAdapter xlistAdapter;
    List<AwardType> data=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.award_class_chioce);
        initSQToolbar();
        initView();


    }
    public int getData(){
        return getIntent().getIntExtra("class_id",-1);
    }

    @Override
    public void onResume() {
        super.onResume();
        getAwardType();
    }



    private void initSQToolbar() {
        assert toolbar!=null;
        toolbar.setTitle("");

    }
    private void initView() {
        clear_et.setVisibility(View.GONE);
        xlist.setPullRefreshEnable(true);
        xlist.setPullLoadEnable(false);
        xlistAdapter=new AwardDialogTypeAdapter(mActivity,data);
        xlist.setAdapter(xlistAdapter);
        xlist.setXListViewListener(ixListViewListener);
        xlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String class_id=data.get(i-1).getType_id();
                Intent intent=new Intent(mActivity,AwardChioceStuActivity.class);
                intent.putExtra("award_id",class_id);
                intent.putExtra("award_name",data.get(i-1).getType_name());
                setResult(RESULT_OK,intent);
                onPageBack();
            }
        });

    }
    private XListView.IXListViewListener ixListViewListener=new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
            getAwardType();
        }

        @Override
        public void onLoadMore() {

        }
    };


    public void getAwardType(){
        Object[] parmas=new Object[]{
                Base.user.getSession_key(),
                getData()//增加classid参数 因为根据不同的年级 返回不同奖励类型
        };
        ParamsTask grade= new ParamsTask(parmas,TagFinal.AWARD_GET_TYPE,TagFinal.AWARD_GET_TYPE);
        execute(grade);
    }

    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        xlist.stopRefresh();
        AwardInfor infor=gson.fromJson(result,AwardInfor.class);
        String name=wcfTask.getName();
        if (name.equals(TagFinal.AWARD_GET_TYPE)){
            data=infor.getTypes();
            xlistAdapter.notifyDataSetChanged(data);
        }
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        stop();

    }

    public void stop(){
        xlist.stopRefresh();
    }
}
