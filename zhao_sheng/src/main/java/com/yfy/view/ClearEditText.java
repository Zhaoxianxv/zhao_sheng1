package com.yfy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

import com.example.zhao_sheng.R;

public class ClearEditText extends EditText implements OnFocusChangeListener,
		TextWatcher {
	private final static String TAG = ClearEditText.class.getSimpleName();
	/**
	 * 删除按钮的引用
	 */
	private Drawable mLeftDrawable, mClearDrawable;
	private int mLeftRes, mRightRes;

	private int lWidth = -1;
	private int lHeight = -1;
	private int rWidth = -1;
	private int rHeight = -1;

	private final static int LEFT = 3;
	private final static int RIGHT = 4;

	public ClearEditText(Context context) {
		this(context, null);
	}

	public ClearEditText(Context context, AttributeSet attrs) {
		// 这里构造方法也很重要，不加这个很多属性不能再XML里面定义
		this(context, attrs, android.R.attr.editTextStyle);
	}

	public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		intDrawableDimension(context, attrs);
		init();
	}

	private void intDrawableDimension(Context context, AttributeSet attrs) {
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ClearEditText);

		mLeftRes = ta.getResourceId(R.styleable.ClearEditText_l_drawable, -1);
		mRightRes = ta.getResourceId(R.styleable.ClearEditText_r_drawable, -1);

		lWidth = ta.getDimensionPixelSize(R.styleable.ClearEditText_l_Width, -1);
		lHeight = ta.getDimensionPixelSize(R.styleable.ClearEditText_l_Height, -1);

		rWidth = ta.getDimensionPixelSize(R.styleable.ClearEditText_r_Width, -1);
		rHeight = ta.getDimensionPixelSize(R.styleable.ClearEditText_r_Height, -1);
		ta.recycle();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

	}

	private void init() {
		mLeftDrawable = getDrawable(mLeftRes, LEFT);
		mClearDrawable = getDrawable(mRightRes, RIGHT);

		if (mLeftDrawable != null) {
			mLeftDrawable.setBounds(0, 0, lWidth, lHeight);
		}

		if (mClearDrawable != null) {
			mClearDrawable.setBounds(0, 0, rWidth, rHeight);
		}
		setClearIconVisible(false);
		setOnFocusChangeListener(this);
		addTextChangedListener(this);
	}

	private Drawable getDrawable(int resId, int location) {
		@SuppressWarnings("deprecation")
		Drawable drawable = getResources().getDrawable(resId);
		switch (location) {
			case LEFT:
				drawable.setBounds(0, 0, lWidth, lHeight);
				break;
			case RIGHT:
				drawable.setBounds(0, 0, rWidth, rHeight);
				break;
		}
		return drawable;
	}

	/**
	 * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 当我们按下的位置 在 EditText的宽度 -
	 * 图标到控件右边的间距 - 图标的宽度 和 EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向没有考虑
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (getCompoundDrawables()[2] != null) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				boolean touchable = event.getX() > (getWidth()
						- getPaddingRight() - rWidth)
						&& (event.getX() < ((getWidth() - getPaddingRight())));
				if (touchable) {
					this.setText("");
				}
			}
		}

		return super.onTouchEvent(event);
	}

	/**
	 * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
	 */
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			setClearIconVisible(getText().length() > 0);
		} else {
			setClearIconVisible(false);
		}
	}

	/**
	 * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
	 *
	 * @param visible
	 */
	protected void setClearIconVisible(boolean visible) {
		Drawable right = visible ? mClearDrawable : null;
		setCompoundDrawables(mLeftDrawable, getCompoundDrawables()[1], right,
				getCompoundDrawables()[3]);
	}

	/**
	 * 当输入框里面内容发生变化的时候回调的方法
	 */
	@Override
	public void onTextChanged(CharSequence s, int start, int count, int after) {
		setClearIconVisible(s.length() > 0);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
								  int after) {

	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	/**
	 * 设置晃动动画
	 */
	public void setShakeAnimation() {
		this.setAnimation(shakeAnimation(5));
	}

	/**
	 * 晃动动画
	 *
	 * @param counts
	 *            1秒钟晃动多少下
	 * @return
	 */
	public static Animation shakeAnimation(int counts) {
		Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
		translateAnimation.setInterpolator(new CycleInterpolator(counts));
		translateAnimation.setDuration(1000);
		return translateAnimation;
	}

}
