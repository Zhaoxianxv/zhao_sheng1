/**
 * 
 */
package com.yfy.app.school;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.example.zhao_sheng.R;
import com.yfy.app.school.bean.Newsbanner;
import com.yfy.final_tag.Base;
import com.yfy.final_tag.glide.GlideTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yfy1
 * @version 1.0
 * @Desprition
 */
public class FlipPagerAdapter extends PagerAdapter {

	private Map<Integer, View> instantMap = new HashMap<Integer, View>();
	private List<View> mListViews = new ArrayList<View>();
	private List<Newsbanner> schoolNewsList;
	private LayoutInflater inflater;



	private ViewPager mPager;
	private OnExtraPageChangeListener onExtraPageChangeListener = null;
	private MyPageChangeListener myPageChangeListener = new MyPageChangeListener();
	private Context context;

	public FlipPagerAdapter(Context context, List<Newsbanner> schoolNewsList, ViewPager pager) {

		this.context=context;
		mPager = pager;
		pager.setOnPageChangeListener(myPageChangeListener);

		this.schoolNewsList = schoolNewsList;
		inflater = LayoutInflater.from(context);
		reSetData();

	}

	private void reSetData() {
		mListViews.clear();
		for (int i = 0; i < getCount(); i++) {
			View view = inflater.inflate(R.layout.school_item_news_top_viewpager, null);
			mListViews.add(view);
		}
	}

	@Override
	public Object instantiateItem(View container, int position) {

		int facPos = getFactPos(position);

		View view = mListViews.get(position);
		ImageView news_pic = (ImageView) view.findViewById(R.id.news_cover_pic);
		GlideTools.loadImage(context,Base.RETROFIT_URI+schoolNewsList.get(facPos).getImgurl(),news_pic);
		view.setOnClickListener(onClickListener);
		instantMap.put(position, view);

		if (view.getParent() == null) {
			((ViewPager) container).addView(view);
		}
		return view;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(instantMap.get(position));
	}

	public void notifyDataSetChanged(List<Newsbanner> schoolNewsList) {
		this.schoolNewsList = schoolNewsList;
		mListViews.clear();
		for (int i = 0; i < getCount(); i++) {
			View view = inflater.inflate(R.layout.school_item_news_top_viewpager, null);
			mListViews.add(view);
		}
		super.notifyDataSetChanged();
	}

	public String getCurrentTitle(int position) {
		int factPos = getFactPos(position);
		return schoolNewsList.get(factPos).getTit();
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public int getCount() {
		int dataCount = getDataCount();
		if (dataCount == 0) {
			return 0;
		}
		if (dataCount == 1) {
			return 1;
		}
		return dataCount + 2;
	}

	public int getDataCount() {
		if (schoolNewsList.size() < 4) {
			return schoolNewsList.size();
		} else {
			return 4;
		}
	}

	public int getFactPos(int position) {
		int dataCount = getDataCount();
		int factPos = 0;
		if (position == 0) {
			factPos = dataCount - 1;
		} else if (position == getCount() - 1) {
			factPos = 0;
		} else {
			factPos = position - 1;
		}
		return factPos;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == (arg1);
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

		}
	};

	class MyPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
			if (null != onExtraPageChangeListener) {
				onExtraPageChangeListener.onExtraPageScrollStateChanged(arg0);
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			if (null != onExtraPageChangeListener) {
				onExtraPageChangeListener.onExtraPageScrolled(arg0, arg1, arg2);
			}
		}

		@Override
		public void onPageSelected(int position) {
			if (position == 0) {
				mPager.setCurrentItem(getCount() - 2,false);
			} else if (position == getCount() - 1) {
				mPager.setCurrentItem(1,false);
			}
			if (null != onExtraPageChangeListener) {
				onExtraPageChangeListener.onExtraPageSelected(position);
			}
		}
	}

	public static class OnExtraPageChangeListener {
		public void onExtraPageScrolled(int i, float v, int i2) {

		}

		public void onExtraPageSelected(int position) {
		}

		public void onExtraPageScrollStateChanged(int i) {

		}
	}

	public OnExtraPageChangeListener getOnExtraPageChangeListener() {
		return onExtraPageChangeListener;
	}

	public void setOnExtraPageChangeListener(
			OnExtraPageChangeListener onExtraPageChangeListener) {
		this.onExtraPageChangeListener = onExtraPageChangeListener;
	}

}
