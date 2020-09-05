/**
 * 
 */
package com.yfy.app.school;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.google.gson.Gson;
import com.yfy.app.WebActivity;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.affiche.SchoolGetBannerReq;
import com.yfy.app.net.affiche.SchoolGetNewsListReq;
import com.yfy.app.school.bean.BannerRes;
import com.yfy.app.school.bean.NewsRes;
import com.yfy.app.school.bean.Newsbanner;
import com.yfy.app.school.bean.SchoolNews;
import com.yfy.final_tag.Base;
import com.yfy.base.fragment.BaseFragment;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.lib.PullToRefreshBase;
import com.yfy.lib.PullToRefreshBase.Mode;
import com.yfy.lib.PullToRefreshBase.OnRefreshListener2;
import com.yfy.lib.PullToRefreshListView;
import com.yfy.view.DotPointerLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author yfy1
 * @version 1.0
 * @Desprition
 */
public class NewsPagerFragment extends BaseFragment implements Callback<ResEnv> {


	private View headerView;

	@Bind(R.id.refresh_lv)
    PullToRefreshListView refresh_lv;
	private ListView listView;
	private List<SchoolNews> schoolNewsList = new ArrayList<>();
	private NewsListAdapter listAdapter;

	private DotPointerLayout dotPointerLayout;
	private ViewPager news_cover_viewpager;
	private FlipPagerAdapter pagerAdapter;
	private TextView news_cover_title;

	private String programa_id;
	private int position;
	private boolean outRefreshed;

	private int page = 0;

	@Override
	public void onCreateView(Bundle savedInstanceState) {
		setContentView(R.layout.school_pager_fragment);
		initAll();
	}

	private void initAll() {
		getData();
		initListView();
		initCover();
		initViews();
	}

	private void getData() {
		programa_id = getArguments().getString(TagFinal.ID_TAG);
		position = getArguments().getInt("position");
		getPics(programa_id);
	}


	private void initCover() {
		addHeader();
		pagerAdapter = new FlipPagerAdapter(getContext(), sc, news_cover_viewpager);
		news_cover_viewpager.setAdapter(pagerAdapter);
		pagerAdapter.setOnExtraPageChangeListener(onExtraPageChangeListener);
		news_cover_viewpager.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
	}

	private void addHeader() {
		headerView = LayoutInflater.from(getActivity()).inflate(R.layout.school_layout_newslist_header, null);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		listView.addHeaderView(headerView);
		news_cover_viewpager = (ViewPager) headerView.findViewById(R.id.news_cover_viewpager);
		dotPointerLayout = (DotPointerLayout) headerView.findViewById(R.id.dotPointerLayout);
		news_cover_title = (TextView) headerView.findViewById(R.id.news_cover_title);

		headerView.setVisibility(View.GONE);
	}


	private void initListView() {
		listAdapter = new NewsListAdapter(getActivity(), schoolNewsList);
		refresh_lv.setAdapter(listAdapter);
		refresh_lv.setOnRefreshListener(onRefreshListener);
		refresh_lv.setOnItemClickListener(onItemClickListener);

		listView = refresh_lv.getRefreshableView();
	}


	private void initViews() {
		if (position == 0) {
			outSideRefresh();
		}
	}

	public void outSideRefresh() {
		if (!outRefreshed) {
			outRefreshed = true;
			refresh_lv.onRefreshComplete();
			refresh_lv.setRefreshing();
		}
	}




	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {

			if (position - 2 == schoolNewsList.size()) {
				return;
			}

			Intent intent = new Intent(getActivity(), WebActivity.class);
			Bundle b = new Bundle();
			b.putString(TagFinal.URI_TAG, Base.RETROFIT_URI+schoolNewsList.get(position - 2).getNews_info_detailed());
			b.putString(TagFinal.TITLE_TAG,  schoolNewsList.get(position - 2).getNewslist_head());
			intent.putExtras(b);
			startActivity(intent);
		}
	};

	private OnRefreshListener2<ListView> onRefreshListener = new OnRefreshListener2<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

			refresh(TagFinal.REFRESH);
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			refresh(TagFinal.REFRESH_MORE);
		}
	};

	private FlipPagerAdapter.OnExtraPageChangeListener onExtraPageChangeListener = new FlipPagerAdapter.OnExtraPageChangeListener() {
		public void onExtraPageSelected(int position) {
			news_cover_title.setText(pagerAdapter.getCurrentTitle(position));
			dotPointerLayout.setSelectedItem(pagerAdapter.getFactPos(position));
		};

		public void onExtraPageScrolled(int i, float v, int i2) {
			news_cover_viewpager.getParent()
					.requestDisallowInterceptTouchEvent(true);
		};
	};

	private List<Newsbanner> sc = new ArrayList<Newsbanner>();





	/**
	 * ----------------------------retrofit-----------------------
	 */



	public void getPics(String no) {
		ReqEnv reqEnvelop = new ReqEnv();
		ReqBody reqBody = new ReqBody();
		SchoolGetBannerReq request = new SchoolGetBannerReq();
		//获取参数
		request.setNo(no);
		request.setFxid(Base.fxid);

		reqBody.schoolBannerReq= request;
		reqEnvelop.body = reqBody;
		Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().news_banner(reqEnvelop);
		call.enqueue(this);
	}


	/**
	 * @param loadType 上啦，下拉
	 */
	public void refresh(String loadType){
		if (loadType.equals(TagFinal.REFRESH)){
			page=0;
		}else{
			page++;
		}
		ReqEnv evn = new ReqEnv();
		ReqBody reqBody = new ReqBody();
		SchoolGetNewsListReq req = new SchoolGetNewsListReq();
		//获取参数

		req.setSearchkey("");
		req.setFxid(Base.fxid);
		req.setNo(programa_id);
		req.setPage(page);
		req.setPagesize(TagFinal.TEN_FIVE);


		reqBody.schoolGetNewsListReq = req;
		evn.body = reqBody;
		Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().school_news_list(evn);
		call.enqueue(this);
		Logger.e(evn.toString());
	}






	@Override
	public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
		dismissProgressDialog();
		ResEnv respEnvelope = response.body();
		List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
		String name=names.get(names.size()-1);
		Gson gson = new Gson();
		if (respEnvelope != null) {
			ResBody b=respEnvelope.body;
			if (b.schoolBannerRes!=null) {
				String result = b.schoolBannerRes.result;
				Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
				BannerRes res=gson.fromJson(result,BannerRes.class);
				sc = res.getData();
				if (sc.size() == 0) {
					listView.removeHeaderView(headerView);
					View v = new View(getActivity());
					v.setVisibility(View.GONE);
					listView.addHeaderView(v);
					return ;
				}
				pagerAdapter.notifyDataSetChanged(sc);
				dotPointerLayout.setDotNum(sc.size());
				headerView.setVisibility(View.VISIBLE);
				news_cover_viewpager.setCurrentItem(1);
				news_cover_title.setText(pagerAdapter.getCurrentTitle(1));
			}
			if (b.schoolGetNewsListRes !=null){
				refresh_lv.onRefreshComplete();
				String result = b.schoolGetNewsListRes.result;
				Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
				NewsRes res=gson.fromJson(result,NewsRes.class);
				List<SchoolNews> list = res.getData();
				if (list.size() < TagFinal.TEN_FIVE) {
					refresh_lv.setMode(Mode.PULL_FROM_START);
				} else {
					refresh_lv.setMode(Mode.BOTH);
				}
				if (page==0) {
					schoolNewsList = list;
				} else {
					schoolNewsList.addAll(list);
					if (list.size() < TagFinal.TEN_INT) {
						Toast.makeText(getContext(),"没有更多了",Toast.LENGTH_SHORT).show();
					}
				}
				listAdapter.notifyDataSetChanged(schoolNewsList);
			}
		}else{
			refresh_lv.onRefreshComplete();
			Logger.e(StringUtils.getTextJoint("%1$s:%2$d",name,response.code()));
			toastShow(StringUtils.getTextJoint("数据错误:%1$d",response.code()));
		}
	}

	@Override
	public void onFailure(Call<ResEnv> call, Throwable t) {
		Logger.e("onFailure  :"+call.request().headers().toString());
		dismissProgressDialog();
		refresh_lv.onRefreshComplete();
	}

}
