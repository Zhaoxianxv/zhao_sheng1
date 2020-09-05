/**
 * 
 */
package com.yfy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.zhao_sheng.R;
import com.yfy.final_tag.Logger;


/**
 * @author yfy1

 * @version 1.0
 * @Desprition
 */
public class DotPointerLayout extends LinearLayout {

	private final static String TAG = DotPointerLayout.class.getSimpleName();

	private int selectedId = -1;
	private int unSelectedId = -1;
	private int mDotNum = -1;
	private int horizonalSpacing = -1;
	private int dotWidth = 0;
	private int dotHeight = 0;
	private int maxNum = -1;

	private ImageView curImageView;

	public DotPointerLayout(Context context) {
		super(context);
	}

	public DotPointerLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);

	}

	private void init(Context context, AttributeSet attrs) {
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.DotPointerLayout);
		selectedId = ta.getResourceId(R.styleable.DotPointerLayout_selectedId,
				-1);
		unSelectedId = ta.getResourceId(
				R.styleable.DotPointerLayout_unSelectedId, -1);
		mDotNum = ta.getInteger(R.styleable.DotPointerLayout_dotNum, -1);
		horizonalSpacing = ta.getDimensionPixelSize(
				R.styleable.DotPointerLayout_horizonalSpacing, -1);

		dotWidth = ta.getDimensionPixelSize(
				R.styleable.DotPointerLayout_dotWidth, 0);

		dotHeight = ta.getDimensionPixelSize(
				R.styleable.DotPointerLayout_dotHeight, 0);

		maxNum = ta.getInteger(R.styleable.DotPointerLayout_maxNum, -1);
		ta.recycle();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (mDotNum != -1) {
			setDotNum(mDotNum);
		}
	}

	public void setDotDrawable(int selectedId, int unSelectedId) {
		this.selectedId = selectedId;
		this.unSelectedId = unSelectedId;
	}

	public void setHorizonalSpacing(int horizonalSpacing) {
		this.horizonalSpacing = horizonalSpacing;
	}

	public void setSelectedItem(int position) {
		if (position >= getChildCount() || position < 0) {
			Logger.e(TAG, "position is unLegal");
			return;
		}

		ImageView iv = (ImageView) getChildAt(position);
		if (iv == curImageView) {
			return;
		}

		iv.setBackgroundResource(selectedId);
		curImageView.setBackgroundResource(unSelectedId);
		curImageView = iv;
	}

	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}

	public void setDotNum(int dotNum) {
		removeAllViews();
		if (dotNum==1) {
			return;
		}
		if (maxNum > -1 && dotNum > maxNum) {
			mDotNum = maxNum;
		} else {
			mDotNum = dotNum;
		}
		LayoutParams params = null;
		for (int i = 0; i < mDotNum; i++) {
			params = new LayoutParams(dotWidth, dotHeight);
			ImageView iv = new ImageView(getContext());
			if (i != 0) {
				params.leftMargin = horizonalSpacing;
				iv.setBackgroundResource(unSelectedId);
			} else {
				iv.setBackgroundResource(selectedId);
				curImageView = iv;
			}
			addView(iv, params);
		}
	}

	/**
	 * @description
	 */
	public void singleDot() {
		removeAllViews();
	}
}
