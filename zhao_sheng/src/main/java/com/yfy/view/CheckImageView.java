/**
 * 
 */
package com.yfy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.zhao_sheng.R;


/**
 * @author yfy1
 * @version 1.0
 * @Desprition
 */
public class CheckImageView extends ImageView {
	// private final static String TAG = CheckImageView.class.getSimpleName();
	private Drawable checkedDrawable;
	private Drawable unCheckedDrawable;
	private int checkedRes;
	private int unCheckedRes;
	private boolean isChecked;

	public CheckImageView(Context context) {
		super(context);
	}

	public CheckImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAttrs(context, attrs);
	}

	public CheckImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initAttrs(context, attrs);
	}

	private void initAttrs(Context context, AttributeSet attrs) {
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.CheckImageView);
		checkedRes = ta.getResourceId(
				R.styleable.CheckImageView_checked_drawable, -1);
		unCheckedRes = ta.getResourceId(
				R.styleable.CheckImageView_un_checked_drawable, -1);
		ta.recycle();
		initDrawable();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
	}

	@SuppressWarnings("deprecation")
	private void initDrawable() {
		checkedDrawable = getResources().getDrawable(checkedRes);
		unCheckedDrawable = getResources().getDrawable(unCheckedRes);
		setChecked(false);
	}

	public void setChecked(boolean b) {
		if (b) {
			setImageDrawable(checkedDrawable);
		} else {
			setImageDrawable(unCheckedDrawable);
		}
		isChecked = b;
	}

	public boolean isChecked() {
		return isChecked;
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		super.setOnClickListener(l);
	}
}
