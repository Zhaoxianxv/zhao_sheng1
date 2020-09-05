package com.yfy.app.appointment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.zhao_sheng.R;
import com.yfy.app.appointment.adpater.ChioceRoomAdapter;
import com.yfy.app.appointment.bean.OrderRes;
import com.yfy.app.appointment.bean.Rooms;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.applied_order.OrderGetRoomNameReq;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChioceRoomActivity extends BaseActivity implements Callback<ResEnv> {


	private final static String TAG = ChioceRoomActivity.class.getSimpleName();
	private ChioceRoomAdapter adapter;
	private List<Rooms> room_names=new ArrayList<>();




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.swip_recycler_main);
		iniySQToolbar();
		initRecycler();
		getRoomName(true);
	}

	private void iniySQToolbar() {
		assert toolbar!=null;
		toolbar.setTitle("预约地点");
	}



	@Override
	public void finish() {
		super.finish();
	}



	private SwipeRefreshLayout swipeRefreshLayout;
	private RecyclerView recyclerView;
	public void initRecycler(){

		recyclerView = (RecyclerView) findViewById(R.id.public_recycler);
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.public_swip);
		//AppBarLayout 展开执行刷新
		// 设置刷新控件颜色
		swipeRefreshLayout.setColorSchemeColors(ColorRgbUtil.getBaseColor(),ColorRgbUtil.getGreen());
//		ViewTools.setSwipeRefreshLayoutColor(swipeRefreshLayout);

		// 设置下拉刷新
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				// 刷新数据
				getRoomName(false);
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
		adapter=new ChioceRoomAdapter(mActivity);
		recyclerView.setAdapter(adapter);
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


	/**
	 * ----------------------------retrofit-----------------------
	 */
	//查询功能室名字
	public void getRoomName(boolean is) {
		ReqEnv reqEnvelop = new ReqEnv();
		ReqBody reqBody = new ReqBody();
		OrderGetRoomNameReq request = new OrderGetRoomNameReq();
		//获取参数

		reqBody.orderGetRoomNameReq = request;
		reqEnvelop.body = reqBody;
		Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().order_get_room_name(reqEnvelop);
		call.enqueue(this);
		if (is)showProgressDialog("正在登录");

	}




	@Override
	public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
		Logger.e( "onResponse: "+response.code());

		if (response.code()==500){
			toastShow("数据出差了");
		}
		if (!isActivity())return;
		dismissProgressDialog();
		ResEnv respEnvelope = response.body();
		if (respEnvelope != null) {
			ResBody b=respEnvelope.body;

			if (b.orderGetRoomNameRes!=null){
				String result=b.orderGetRoomNameRes.result;
				Logger.e( "get_funcRoom_name: "+result );
				OrderRes room=gson.fromJson(result, OrderRes.class);
				room_names.clear();
				room_names=room.getRooms();
				if (StringJudge.isEmpty(room_names)){
					toastShow("无数据");
				}else{
					adapter.setDataList(room_names);
					adapter.setLoadState(TagFinal.LOADING_COMPLETE);
				}
			}

		}else{
			Logger.e( "onResponse: 22" );
		}

	}

	@Override
	public void onFailure(Call<ResEnv> call, Throwable t) {
		if (!isActivity())return;
		Logger.e("onFailure  :"+call.request().headers().toString());
		dismissProgressDialog();
		closeSwipeRefresh();
		toastShow(R.string.fail_do_not);
	}

	@Override
	public boolean isActivity() {
		return AppLess.isTopActivy(TAG);
	}
}
