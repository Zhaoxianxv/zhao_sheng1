/**
 *
 */
package com.yfy.app.property;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.property.adapter.PropertyMainAdapter;
import com.yfy.app.property.bean.BindUser;
import com.yfy.app.property.bean.PropretyRoom;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.TagFinal;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.final_tag.recycerview.EndlessRecyclerOnScrollListener;
import com.yfy.final_tag.recycerview.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;


/**
 * @author yfy1
 * @Date 2016年1月12日
 * @version 1.0
 * @Desprition
 */
public class  PropertyMainActivity extends WcfActivity  {

	private List<PropretyRoom> rooms=new ArrayList<>();

	private PropertyMainAdapter adapter;
	private TextView menu;
	private String term_id,term_name;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.swip_recycler_main);
		initSQRoolbar();
		initRecycler();
	}


	private void initSQRoolbar(){
		assert toolbar!=null;
		toolbar.setTitle(R.string.proprety_main);
//		term_name=UserPreferences.getInstance().getTermName();
//		term_id=UserPreferences.getInstance().getTermId();
//		menu=toolbar.addMenuText(TagFinal.ONE_INT,term_name );
//		toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
//			@Override
//			public void onClick(View view, int position) {
//				Intent intent=new Intent(mActivity,ChangeTermActivity.class);
//				startActivityForResult(intent,TagFinal.ONE_INT);
//			}
//		});

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
		recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener() {
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

		BindUser user=new BindUser();

		rooms.add(new PropretyRoom("教学楼一栋（101）",true,"id123",user,user));
		rooms.add(new PropretyRoom("教学楼一栋（102）",false,"id124",user,user));
		rooms.add(new PropretyRoom("教学楼一栋（103）",false,"id125",user,user));




		adapter=new PropertyMainAdapter(PropertyMainActivity.this,rooms);
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

				case TagFinal.ONE_INT:
//					term_id= data.getStringExtra("data_id");
//					menu.setText(data.getStringExtra("data_name"));
					break;
			}
		}
	}

}
