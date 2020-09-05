/**
 * 
 */
package com.yfy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;

import com.example.zhao_sheng.R;
import com.yfy.lib.internal.ViewCompat;

/**
 * @author yfy1
 * @Date 2015��11��18��
 * @version 1.0
 * @Desprition
 */
public class IndicateMenuLayout extends MenuLayout {

	private final static String TAG = IndicateMenuLayout.class.getSimpleName();

	private int strokeWidth = 5;
	private int lineColor = Color.BLACK;
	private int lineWidth;
	private int mDuration = 200;

	private int mWidth;
	private int mHeight;

	private boolean isMeasured;

	private Paint mPaint;

	private int mStartX = 0;

	public IndicateMenuLayout(Context context) {
		super(context);
		setWillNotDraw(false);
	}

	public IndicateMenuLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWillNotDraw(false);
		initS(context, attrs);
	}

	private void initS(Context context, AttributeSet attrs) {
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.IndicateMenuLayout);
		strokeWidth = ta.getDimensionPixelSize(
				R.styleable.IndicateMenuLayout_strokeWidth, 2);
		lineColor = ta.getColor(R.styleable.IndicateMenuLayout_lineColor,
				Color.BLACK);
		mDuration = ta.getInteger(R.styleable.IndicateMenuLayout_duration, 200);

		ta.recycle();
		initPaint();
	}

	private void initPaint() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(lineColor);
		mPaint.setStrokeWidth(strokeWidth);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawLine(mStartX, mHeight, mStartX + lineWidth, mHeight, mPaint);
		super.onDraw(canvas);
	}

	@Override
	protected void addItem() {
		super.addItem();
	}

	private LineScrollRunnable scrollRunnable;

	@Override
	public boolean selectItem(int position) {
		boolean b;
		b = super.selectItem(position);
		if (!b) {
			return b;
		}

		int mEndX = position * lineWidth;
		if (scrollRunnable != null) {
			scrollRunnable.stop();
		}
		scrollRunnable = new LineScrollRunnable(mStartX, mEndX, mDuration);
		scrollRunnable.run();

		return b;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		if (!isMeasured) {
			mWidth = widthSize;
			mHeight = heightSize;
			lineWidth = Math.round(mWidth / getChildCount());
			isMeasured = true;
		}
	}

	final class LineScrollRunnable implements Runnable {
		private final Interpolator mInterpolator;
		private final int mScrollToX;
		private final int mScrollFromX;
		private final long mDuration;

		private boolean mContinueRunning = true;
		private long mStartTime = -1;
		private int mCurrentX = -1;

		public LineScrollRunnable(int fromX, int toX, long duration) {
			mScrollFromX = fromX;
			mScrollToX = toX;
			mInterpolator = new AccelerateInterpolator();
			mDuration = duration;
		}

		@Override
		public void run() {
			if (mStartTime == -1) {
				mStartTime = System.currentTimeMillis();
			} else {

				long normalizedTime = (1000 * (System.currentTimeMillis() - mStartTime))
						/ mDuration;
				normalizedTime = Math.max(Math.min(normalizedTime, 1000), 0);

				final int deltaY = Math.round((mScrollToX - mScrollFromX)
						* mInterpolator
								.getInterpolation(normalizedTime / 1000f));

				mCurrentX = mScrollFromX + deltaY;

				mStartX = mCurrentX;
				invalidate();
			}

			if (mContinueRunning && mScrollToX != mCurrentX) {
				ViewCompat.postOnAnimation(IndicateMenuLayout.this, this);
			}
		}

		public void stop() {
			mContinueRunning = false;
			removeCallbacks(this);
		}
	}

}
