package com.yfy.app.affiche;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.zhao_sheng.R;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.affiche.SchoolGetNewsListReq;
import com.yfy.app.school.bean.NewsRes;
import com.yfy.app.school.bean.SchoolNews;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.EndlessRecyclerOnScrollListener;
import com.yfy.final_tag.recycerview.RecycleViewDivider;
import com.yfy.final_tag.StringJudge;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AfficheActivity extends BaseActivity implements Callback<ResEnv> {


	private final static String TAG = AfficheActivity.class.getSimpleName();
	private AfficheAdapter adapter;
	private List<SchoolNews> schoolNewsList = new ArrayList<SchoolNews>();

	private String no = "022102";
	private int page = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.swip_recycler_main);
		iniySQToolbar();
		initRecycler();
		refresh(true,TagFinal.REFRESH);
	}

	private void iniySQToolbar() {
		assert toolbar!=null;
		toolbar.setTitle(R.string.affiche);
	}


	@Override
	public void finish() {
		super.finish();
	}



	private SwipeRefreshLayout swipeRefreshLayout;
	private RecyclerView recyclerView;
	public void initRecycler(){

		recyclerView =  findViewById(R.id.public_recycler);
		swipeRefreshLayout =  findViewById(R.id.public_swip);
		swipeRefreshLayout.setColorSchemeColors(ColorRgbUtil.getBaseColor(),ColorRgbUtil.getGreen());

		// 设置下拉刷新
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				// 刷新数据
				refresh(false,TagFinal.REFRESH);
			}
		});
		recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
			@Override
			public void onLoadMore() {
				refresh(false, TagFinal.REFRESH_MORE);
			}
		});

		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		//添加分割线
		recyclerView.addItemDecoration(new RecycleViewDivider(
				mActivity,
				LinearLayoutManager.HORIZONTAL,
				1,
				getResources().getColor(R.color.gray)));
		adapter=new AfficheAdapter(mActivity);
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
	 * ------------------retrofit------------------
	 */


	public void refresh(boolean is,String loadType){
		if (loadType.equals(TagFinal.REFRESH)){
			page=0;
		}else{
			page++;
		}
		ReqEnv evn = new ReqEnv();
		ReqBody reqBody = new ReqBody();
		SchoolGetNewsListReq req = new SchoolGetNewsListReq();
		//获取参数

		req.setNo(no);
		req.setPage(page);
		reqBody.schoolGetNewsListReq = req;
		evn.body = reqBody;
		Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().school_news_list(evn);
		call.enqueue(this);
		Logger.e(evn.toString());
		if (is) showProgressDialog("正在加载");
	}






	@Override
	public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
		if (!isActivity())return;
		dismissProgressDialog();
		if (response.code()==500){
			toastShow("数据出差了");
		}
		closeSwipeRefresh();
		ResEnv respEnvelope = response.body();
		if (respEnvelope != null) {
			ResBody b=respEnvelope.body;
			if (b.schoolGetNewsListRes !=null) {
				String result = b.schoolGetNewsListRes.result;
				Logger.e(call.request().headers().toString()+result);
				NewsRes res=gson.fromJson(result,NewsRes.class);
				if (StringJudge.isEmpty(res.getData())){
					toast(R.string.success_not_more);
					return;
				}
				List<SchoolNews> list =res.getData();
				if (list.size() < TagFinal.TEN_INT) {
					toastShow("全部加载完毕！");
				}
				if (page==0) {
					schoolNewsList = list;
				}else{
					schoolNewsList.addAll(list);
				}

				adapter.setDataList(schoolNewsList);
				adapter.setLoadState(2);
			}
		}else{
			List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
			String name=names.get(names.size()-1);
			Logger.e(name+"--------");
		}

	}

	@Override
	public void onFailure(Call<ResEnv> call, Throwable t) {
		if (!isActivity())return;
		Logger.e("onFailure  :"+call.request().headers().toString());
		dismissProgressDialog();
		closeSwipeRefresh();
		toast(R.string.fail_do_not);
	}


	@Override
	public boolean isActivity() {
		return AppLess.isTopActivy(TAG);
	}
}
