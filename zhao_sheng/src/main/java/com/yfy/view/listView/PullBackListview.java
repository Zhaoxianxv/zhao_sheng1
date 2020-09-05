package com.yfy.view.listView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.yfy.final_tag.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PullBackListview extends ListView implements OnScrollListener {

	private View mHeadView, mTailView;
	private boolean isRecordPullUp;
	private boolean isRecordPullDown;
	private ScheduledExecutorService schedulor;

	private int currentScrollState;
	private int firstItemIndex;
	private int lastItemIndex;

	private int PULL_BACK_REDUCE_STEP = 1;
	private int startPullDownY;
	private int startPullUpY;

	private final static int PULL_DOWN_BACK_ACTION = 99;
	private final static int PULL_UP_BACK_ACTION = -99;
	private final static float PULL_FACTOR = 0.5f;
	private static final long PULL_BACK_TASK_PERIOD = 100;

	private String TAG = "tag";

	public PullBackListview(Context context) {
		super(context);
		init(true, true);
	}

	public PullBackListview(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(true, true);
	}

	public PullBackListview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(true, true);
	}

	@SuppressWarnings("deprecation")
	private void init(boolean isHeadViewNeed, boolean isTailViewNeed) {
		if (isHeadViewNeed) {
			// 监听滚动状态
			setOnScrollListener(this);
			// 创建PullListView的HeadView
			mHeadView = new View(this.getContext());
			// 默认白色背景,可以改变颜色, 也可以设置背景图片
			mHeadView.setBackgroundColor(Color.parseColor("#00000000"));
			// 默认高度为0
			mHeadView.setLayoutParams(new AbsListView.LayoutParams(
					LayoutParams.FILL_PARENT, 0));
			this.addHeaderView(mHeadView);
		}

		if (isTailViewNeed) {
			// 监听滚动状态
			setOnScrollListener(this);
			// 创建PullListView的HeadView
			mTailView = new View(this.getContext());
			// 默认白色背景,可以改变颜色, 也可以设置背景图片
			mTailView.setBackgroundColor(Color.parseColor("#00000000"));
			// 默认高度为0
			mTailView.setLayoutParams(new AbsListView.LayoutParams(
					LayoutParams.FILL_PARENT, 0));
			this.addFooterView(mTailView);
		}
	}

	/**
	 * 覆盖onTouchEvent方法,实现下拉回弹效果
	 */
	@SuppressLint("ClickableViewAccessibility")
	@SuppressWarnings("deprecation")
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				if (!isRecordPullDown && !isRecordPullUp) {
					// it's not in pull down state or pull up state, break
					Logger.e("TAG", "ACTION_UP it's not in pull down state or pull up state, break");
					break;
				}
				if (isPullDownState()) {
					Logger.d("TAG", "isRecordPullDown=" + isRecordPullDown);
					// 以一定的频率递减HeadView的高度,实现平滑回弹
					schedulor = Executors.newScheduledThreadPool(1);
					schedulor.scheduleAtFixedRate(new Runnable() {

						@Override
						public void run() {
							mHandler.sendEmptyMessage(PULL_DOWN_BACK_ACTION);

						}
					}, 0, PULL_BACK_TASK_PERIOD, TimeUnit.NANOSECONDS);

					setPullDownState(!isRecordPullDown);
				} else if (isPullUpState()) {
					// 以一定的频率递减HeadView的高度,实现平滑回弹
					schedulor = Executors.newScheduledThreadPool(1);
					schedulor.scheduleAtFixedRate(new Runnable() {

						@Override
						public void run() {
							mHandler.sendEmptyMessage(PULL_UP_BACK_ACTION);

						}
					}, 0, PULL_BACK_TASK_PERIOD, TimeUnit.NANOSECONDS);

					setPullUpState(!isRecordPullUp);
				}

				break;

			case MotionEvent.ACTION_MOVE:

				if (!isRecordPullDown && firstItemIndex == 0) {
					Logger.d(TAG, "firstItemIndex=" + firstItemIndex
							+ " set isRecordPullDown=true");
					startPullDownY = (int) event.getY();
					setPullType(PULL_DOWN_BACK_ACTION);
				} else if (!isRecordPullUp && lastItemIndex == getCount()) {
					Logger.d(TAG, "lastItemIndex == getCount()"
							+ " set isRecordPullUp=true");
					startPullUpY = (int) event.getY();
					setPullType(PULL_UP_BACK_ACTION);
				}

				if (!isRecordPullDown && !isRecordPullUp) {
					// it's not in pull down state or pull up state, break
					Logger.d("TAG",
							"ACTION_MOVE it's not in pull down state or pull up state, break");
					break;
				}

				if (isRecordPullDown) {
					int tempY = (int) event.getY();
					int moveY = tempY - startPullDownY;
					if (moveY < 0) {
						setPullDownState(false);
						break;
					}

					mHeadView.setLayoutParams(new AbsListView.LayoutParams(
							LayoutParams.FILL_PARENT, (int) (moveY * PULL_FACTOR)));
					mHeadView.invalidate();
				} else if (isRecordPullUp) {
					int tempY = (int) event.getY();
					int moveY = startPullUpY - tempY;
					if (moveY < 0) {
						setPullUpState(false);
						break;
					}

					mTailView.setLayoutParams(new AbsListView.LayoutParams(
							LayoutParams.FILL_PARENT, (int) (moveY * PULL_FACTOR)));
					mTailView.invalidate();
				}

				break;
		}
		return super.onTouchEvent(event);
	}

	private void setPullDownState(boolean b) {
		isRecordPullDown = b;
	}

	private boolean isPullDownState() {
		return isRecordPullDown;
	}

	private void setPullUpState(boolean b) {
		isRecordPullUp = b;
	}

	private boolean isPullUpState() {
		return isRecordPullUp;
	}

	private void setPullType(int i) {
		switch (i) {
			case PULL_DOWN_BACK_ACTION:
				isRecordPullDown = true;
				break;
			case PULL_UP_BACK_ACTION:
				isRecordPullUp = true;
				break;
		}
	}

	/**
	 * 实现回弹效果的handler,用于递减HeadView的高度并请求重绘
	 */
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case PULL_DOWN_BACK_ACTION:
					AbsListView.LayoutParams headerParams = (LayoutParams) mHeadView
							.getLayoutParams();
					// 递减高度
					headerParams.height -= PULL_BACK_REDUCE_STEP;
					mHeadView.setLayoutParams(headerParams);
					// 重绘
					mHeadView.invalidate();
					// 停止回弹时递减headView高度的任务
					if (headerParams.height <= 0) {
						schedulor.shutdownNow();
					}

					break;
				case PULL_UP_BACK_ACTION:
					AbsListView.LayoutParams footerParams = (LayoutParams) mTailView
							.getLayoutParams();
					// 递减高度
					footerParams.height -= PULL_BACK_REDUCE_STEP;
					mTailView.setLayoutParams(footerParams);
					// 重绘
					mTailView.invalidate();
					// 停止回弹时递减headView高度的任务
					if (footerParams.height <= 0) {
						schedulor.shutdownNow();
					}

					break;
			}
		}
	};

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		currentScrollState = scrollState;
		Logger.d("TAG", "scrollState: " + getScrollStateString(currentScrollState));
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
						 int visibleItemCount, int totalItemCount) {
		this.firstItemIndex = firstVisibleItem;
		this.lastItemIndex = firstVisibleItem + visibleItemCount;

	}

	private String getScrollStateString(int flag) {
		String str = "";
		switch (flag) {
			case OnScrollListener.SCROLL_STATE_IDLE:
				str = "SCROLL_STATE_IDLE";
				break;
			case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				str = "SCROLL_STATE_TOUCH_SCROLL";
				break;
			case OnScrollListener.SCROLL_STATE_FLING:
				str = "SCROLL_STATE_FLING";
				break;
			default:
				str = "wrong state";
		}

		return str;
	}

}
