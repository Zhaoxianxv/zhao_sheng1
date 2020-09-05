/**
 * 
 */
package com.yfy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.zhao_sheng.R;
import com.yfy.net.loading.interf.Dialog;


/**
 * @author yfy
 * @date 2015-11-8
 * @version 1.0
 * @description LoadingDialog2
 */
public class LoadingView extends FrameLayout implements Dialog {

	private int bar_width = 50;
	private int bar_height = 50;

	private Animation[] animations;
	private int i = 0;
	private boolean isRotate;

	private int sourceId = -1;
	private int perCount = 12;
	private int intervalTime = 100;

	private ImageView imageView;

	public LoadingView(Context context) {
		super(context);
	}

	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public LoadingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.LoadingView);

		bar_width = ta.getDimensionPixelSize(R.styleable.LoadingView_bar_width,
				50);
		bar_height = ta.getDimensionPixelSize(
				R.styleable.LoadingView_bar_height, 50);
		sourceId = ta.getResourceId(R.styleable.LoadingView_sourceId, -1);
		perCount = ta.getInteger(R.styleable.LoadingView_perCount, 12);
		intervalTime = ta.getInteger(R.styleable.LoadingView_intervalTime, 100);
		ta.recycle();

		if (perCount > -1) {
			initAnimation();
		}

		if (intervalTime < 50) {
			intervalTime = 100;
		}
	}

	private void initAnimation() {
		animations = new Animation[perCount];
		float fromDegrees = 0;
		float toDegrees = 0;
		float perDegree = 360 / perCount;
		for (int i = 0; i < perCount; i++) {
			fromDegrees = perDegree * i;
			toDegrees = perDegree * (i + 1);
			animations[i] = new RotateAnimation(fromDegrees, toDegrees,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			animations[i].setDuration(0);
			animations[i].setRepeatCount(-1);
			animations[i].setFillAfter(false);
		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		addProgressBar();
	}

	/**
	 * @description
	 */
	private void addProgressBar() {
		imageView = new ImageView(getContext());
		if (sourceId != -1) {
			imageView.setImageResource(sourceId);
		}
		LayoutParams params = new LayoutParams(bar_width, bar_height);
		params.gravity = Gravity.CENTER;
		addView(imageView, params);
	}

	@Override
	public void show() {
		setVisibility(View.VISIBLE);
		isRotate = true;
		post(new Runnable() {
			@Override
			public void run() {
				if (isRotate) {
					imageView.startAnimation(animations[i++]);
					postDelayed(this, intervalTime);
					if (i == perCount) {
						i = 0;
					}
				} else {
					setVisibility(View.GONE);
				}
			}
		});
	}

	@Override
	public void dismiss() {
		isRotate = false;
	}

}
