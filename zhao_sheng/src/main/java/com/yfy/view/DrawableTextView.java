/**
 * 
 */
package com.yfy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.zhao_sheng.R;

/**
 * @version 1.0
 * @Desprition
 */
public class DrawableTextView extends TextView {

	 private final static String TAG = DrawableTextView.class.getSimpleName();

	private int drawableWidth = -1;
	private int drawableHeight = -1;
	private int topId, bottomId, leftId, rightId;

	public DrawableTextView(Context context) {
		super(context);
	}

	public DrawableTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public DrawableTextView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.DrawableTextView);

		drawableWidth = ta.getDimensionPixelSize(
				R.styleable.DrawableTextView_drawableWidth, -1);
		drawableHeight = ta.getDimensionPixelSize(
				R.styleable.DrawableTextView_drawableHeight, -1);

		topId = ta.getResourceId(R.styleable.DrawableTextView_drawableTop, -1);
		bottomId = ta.getResourceId(
				R.styleable.DrawableTextView_drawableBottom, -1);
		leftId = ta
				.getResourceId(R.styleable.DrawableTextView_drawableLeft, -1);
		rightId = ta.getResourceId(R.styleable.DrawableTextView_drawableRight,
				-1);

		ta.recycle();
	}

	public void setDrawableLeft(int resId) {
		if (drawableHeight > 0 && drawableWidth > 0 && resId > 0) {
			setCompoundDrawables(getDrawable(resId), null, null, null);
		}
	}

	public void setDrawableTop(int resId) {
		
		if (drawableHeight > 0 && drawableWidth > 0 && resId > 0) {
			setCompoundDrawables(null, getDrawable(resId), null, null);
		}
	}

	public void setDrawableRight(int resId) {
		if (drawableHeight > 0 && drawableWidth > 0 && resId > 0) {
			setCompoundDrawables(null, null, getDrawable(resId), null);
		}
	}

	public void setDrawableBottom(int resId) {
		if (drawableHeight > 0 && drawableWidth > 0 && resId > 0) {
			setCompoundDrawables(null, null, null, getDrawable(resId));
		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		addTextViewDrawable();
	}

	/**
	 * @description
	 */
	private void addTextViewDrawable() {
		if (drawableHeight > 0 && drawableWidth > 0) {
			if (topId > 0) {
				setCompoundDrawables(null, getDrawable(topId), null, null);
			}
			if (bottomId > 0) {
				setCompoundDrawables(null, null, null, getDrawable(bottomId));
			}
			if (leftId > 0) {
				setCompoundDrawables(getDrawable(leftId), null, null, null);
			}
			if (rightId > 0) {
				setCompoundDrawables(null, null, getDrawable(rightId), null);
			}
		}
	}

	/**
	 * @description
	 */
	private Drawable getDrawable(int resId) {
		Drawable drawable = getResources().getDrawable(resId);
		drawable.setBounds(0, 0, drawableWidth, drawableHeight);
		return drawable;
	}
}
