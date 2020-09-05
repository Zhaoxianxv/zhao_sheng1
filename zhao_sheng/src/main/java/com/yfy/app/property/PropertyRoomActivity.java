package com.yfy.app.property;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.zhao_sheng.R;
import com.yfy.app.property.adapter.PropertyRoomDatlieAdapter;
import com.yfy.app.property.bean.ArticleRoom;
import com.yfy.app.property.bean.BadObj;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.TagFinal;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.final_tag.recycerview.EndlessRecyclerOnScrollListener;
import com.yfy.final_tag.recycerview.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

public class PropertyRoomActivity extends WcfActivity {

    private PropertyRoomDatlieAdapter adapter;
    private List<ArticleRoom> articleRoomList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_main);
        initRecycler();
    }
    public void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle("教室名(负责人+学期)");

    }
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView = (RecyclerView) findViewById(R.id.public_recycler);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.public_swip);
        //AppBarLayout 展开执行刷新


        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#992429"));
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
//				refresh(false);
            }
        });
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
//				loadMore();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        xlist.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mActivity,
                LinearLayoutManager.HORIZONTAL,
                1,
                getResources().getColor(R.color.gray)));

        articleRoomList.add(new ArticleRoom("座子","id_22","10","5","4"));
        ArticleRoom room=new ArticleRoom("椅子","id_322","10","1","4");
        List<BadObj> badObjs=new ArrayList<>();
        badObjs.add(new BadObj("3","椅子腿坏的"));
        badObjs.add(new BadObj("2","椅子散架"));
        room.setBad_num(badObjs);
        articleRoomList.add(room);
        articleRoomList.add(new ArticleRoom("扫帚","id_24","5","1","4"));
        adapter=new PropertyRoomDatlieAdapter(PropertyRoomActivity.this,articleRoomList);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {

        closeSwipeRefresh();
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        closeSwipeRefresh();
        toastShow("网络异常,获取班级列表失败");
    }


    public void closeSwipeRefresh(){
        if (swipeRefreshLayout!=null){
            swipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            }, 200);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){

                case TagFinal.UI_REFRESH:
                    ArrayList<ArticleRoom> articleRooms = data.getParcelableArrayListExtra("data");
                    adapter.setRooms(articleRooms);
                    adapter.setLoadState(3);
                    break;
            }
        }
    }
}
