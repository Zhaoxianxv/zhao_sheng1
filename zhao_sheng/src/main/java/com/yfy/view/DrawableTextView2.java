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
public class DrawableTextView2 extends TextView {

	private int topId, bottomId, leftId, rightId;

	private int tWidth = -1, tHeight = -1;
	private int bWidth = -1, bHeight = -1;
	private int lWidth = -1, lHeight = -1;
	private int rWidth = -1, rHeight = -1;

	private final static int TOP = 1;
	private final static int BOTTOM = 2;
	private final static int LEFT = 3;
	private final static int RIGHT = 4;

	private Drawable topDrawable, bottomDrawable, leftDrawable, rightDrawable;

	public DrawableTextView2(Context context) {
		super(context);
	}

	public DrawableTextView2(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public DrawableTextView2(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.DrawableEditText);

		tWidth = ta.getDimensionPixelSize(R.styleable.DrawableEditText_tWidth,
				-1);
		tHeight = ta.getDimensionPixelSize(
				R.styleable.DrawableEditText_tHeight, -1);

		bWidth = ta.getDimensionPixelSize(R.styleable.DrawableEditText_bWidth,
				-1);
		bHeight = ta.getDimensionPixelSize(
				R.styleable.DrawableEditText_bHeight, -1);

		lWidth = ta.getDimensionPixelSize(R.styleable.DrawableEditText_lWidth,
				-1);
		lHeight = ta.getDimensionPixelSize(
				R.styleable.DrawableEditText_lHeight, -1);

		rWidth = ta.getDimensionPixelSize(R.styleable.DrawableEditText_rWidth,
				-1);
		rHeight = ta.getDimensionPixelSize(
				R.styleable.DrawableEditText_rHeight, -1);

		topId = ta.getResourceId(R.styleable.DrawableEditText_drawableTops, -1);
		bottomId = ta.getResourceId(
				R.styleable.DrawableEditText_drawableBottoms, -1);
		leftId = ta.getResourceId(R.styleable.DrawableEditText_drawableLefts,
				-1);
		rightId = ta.getResourceId(R.styleable.DrawableEditText_drawableRights,
				-1);

		ta.recycle();
	}

	public void setDrawableLeft(int resId) {
		if (lHeight > 0 && lWidth > 0 && resId > 0) {
			Drawable drawable = getDrawable(resId, LEFT);
			setCompoundDrawables(drawable, topDrawable, rightDrawable,
					bottomDrawable);
			recycleDrawable(leftDrawable);
			leftDrawable = drawable;
		}
	}

	public void setDrawableTop(int resId) {
		if (tHeight > 0 && tWidth > 0 && resId > 0) {
			Drawable drawable = getDrawable(resId, TOP);
			setCompoundDrawables(leftDrawable, drawable, rightDrawable,
					bottomDrawable);
			recycleDrawable(topDrawable);
			topDrawable = drawable;
		}
	}

	public void setDrawableRight(int resId) {
		if (rHeight > 0 && rWidth > 0 && resId > 0) {
			Drawable drawable = getDrawable(resId, RIGHT);
			setCompoundDrawables(leftDrawable, topDrawable, drawable,
					bottomDrawable);
			recycleDrawable(rightDrawable);
			rightDrawable = drawable;
		}
	}

	public void setDrawableBottom(int resId) {
		if (bHeight > 0 && bWidth > 0 && resId > 0) {
			Drawable drawable = getDrawable(resId, BOTTOM);
			setCompoundDrawables(leftDrawable, topDrawable, rightDrawable,
					drawable);
			recycleDrawable(bottomDrawable);
			bottomDrawable = drawable;
		}
	}

	private void recycleDrawable(Drawable drawable) {
		if (drawable == null)
			return;
		drawable.setCallback(null);
		drawable = null;
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

		if (topId > 0 && tHeight > 0 && tWidth > 0) {
			topDrawable = getDrawable(topId, TOP);
		}
		if (bottomId > 0 && bHeight > 0 && bWidth > 0) {
			bottomDrawable = getDrawable(bottomId, BOTTOM);
		}
		if (leftId > 0 && lHeight > 0 && lWidth > 0) {
			leftDrawable = getDrawable(leftId, LEFT);
		}
		if (rightId > 0 && rHeight > 0 && rWidth > 0) {
			rightDrawable = getDrawable(rightId, RIGHT);
		}

		setCompoundDrawables(leftDrawable, topDrawable, rightDrawable,
				bottomDrawable);
	}

	/**
	 * @description
	 */
	private Drawable getDrawable(int resId, int location) {
		@SuppressWarnings("deprecation")
		Drawable drawable = getResources().getDrawable(resId);
		switch (location) {
		case TOP:
			drawable.setBounds(0, 0, tWidth, tHeight);
			break;
		case BOTTOM:
			drawable.setBounds(0, 0, bWidth, bHeight);
			break;
		case LEFT:
			drawable.setBounds(0, 0, lWidth, lHeight);
			break;
		case RIGHT:
			drawable.setBounds(0, 0, rWidth, rHeight);
			break;
		}
		return drawable;
	}

}
