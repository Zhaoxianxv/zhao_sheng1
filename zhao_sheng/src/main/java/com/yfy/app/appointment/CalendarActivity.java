package com.yfy.app.appointment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Window;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.base.activity.BaseActivity;
import com.yfy.calendar.CalendarCard;
import com.yfy.calendar.CalendarCard.OnCellClickListener;
import com.yfy.calendar.CalendarViewAdapter;
import com.yfy.calendar.CustomDate;
import com.yfy.final_tag.TagFinal;

import butterknife.OnClick;

public class CalendarActivity extends BaseActivity implements OnCellClickListener{

	private final static String TAG = CalendarActivity.class.getSimpleName();

	private ViewPager mViewPager;
	private int mCurrentIndex = 500;
	private CalendarViewAdapter adapter;
	private TextView monthText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.calendar);
		initSQtoobar();
		getData();

		mViewPager = (ViewPager) this.findViewById(R.id.vp_calendar);
		monthText = (TextView) this.findViewById(R.id.tvCurrentMonth);
		CustomDate customDate=new CustomDate();
		monthText.setText(customDate.year + " - " + customDate.month + "");
		CalendarCard[] views = new CalendarCard[5];
		for (int i = 0; i < views.length; i++) {
			views[i] = new CalendarCard(this, this);
		}
		adapter = new CalendarViewAdapter(views);
		setViewPager();
	}



	private void initSQtoobar() {
		assert toolbar!=null;
		toolbar.setTitle("选择日期");
	}


	private void getData() {
		CalendarCard.mSelectedDate = (CustomDate) getIntent().getSerializableExtra("date");
	}

	private void setViewPager() {
		adapter.setDatePos(mCurrentIndex, new CustomDate());
		mViewPager.setAdapter(adapter);
		mViewPager.setCurrentItem(mCurrentIndex);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				CustomDate customDate = adapter.getCustomDate(position);
				monthText.setText(customDate.year + " - " + customDate.month);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}



	@OnClick(R.id.btnPreMonth)
	void setPreMonth(){
		mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
	}
	@OnClick(R.id.btnNextMonth)
	void setNextMonth(){
		mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
	}


	@Override
	public void clickDate(CustomDate date) {
		Intent intent = new Intent();
		intent.putExtra("date", date);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public void changeDate(CustomDate date) {
	}

}
