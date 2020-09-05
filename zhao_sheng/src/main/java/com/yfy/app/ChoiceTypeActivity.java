package com.yfy.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.zhao_sheng.R;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.recycerview.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

public class ChoiceTypeActivity extends BaseActivity {
    private static final String TAG = ChoiceTypeActivity.class.getSimpleName();

    private ChoiceTypeAdapter adapter;
    private List<GType> gTypes=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_main);
        initRecycler();
        getData();

    }


    
    private void getData(){
        Intent intent=getIntent();
        String title=intent.getStringExtra(TagFinal.TITLE_TAG);
        initSQtoobar(title);
        gTypes=intent.getParcelableArrayListExtra(TagFinal.OBJECT_TAG);
        adapter.setDataList(gTypes);
        adapter.setLoadState(1);
        
    }
    private void initSQtoobar(String title) {
        assert toolbar!=null;
        toolbar.setTitle(title);
    }





    private RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView = (RecyclerView) findViewById(R.id.public_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        xlist.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mActivity,
                LinearLayoutManager.HORIZONTAL,
                1,
                getResources().getColor(R.color.gray)));
        adapter=new ChoiceTypeAdapter(this);
        recyclerView.setAdapter(adapter);

    }
    

}
