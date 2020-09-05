/**
 * 
 */
package com.yfy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.zhao_sheng.R;
import com.yfy.base.ViewAdapter;

/**
 * @author yfy
 * @date 2015-11-8
 * @version 1.0
 * @description MenuLayout
 */
public class MenuLayout extends LinearLayout {
	
	private final static String TAG=MenuLayout.class.getSimpleName();

	private int itemId = -1;
	private int itemSpacing;
	private int itemCount = -1;
	private LayoutInflater inflater;

	private ViewAdapter mAdapter;

	private int mCurPosition;

	/**
	 * @param context
	 */
	public MenuLayout(Context context) {
		super(context);

	}

	/**
	 * @param context
	 * @param attrs
	 */
	public MenuLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	/**
	 * @description
	 */
	private void init(Context context, AttributeSet attrs) {
		inflater = LayoutInflater.from(context);
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.MenuLayout);
		itemId = ta.getResourceId(R.styleable.MenuLayout_itemId, -1);
		itemSpacing = ta.getDimensionPixelSize(
				R.styleable.MenuLayout_itemSpacing, 0);
		itemCount = ta.getInteger(R.styleable.MenuLayout_itemCount, -1);
		ta.recycle();

	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		addItem();
	}

	private void initItem() {
		if (mAdapter == null) {
			return;
		}
		if (itemCount < 1) {
			return;
		}
		View view;
		mCurPosition = 0;
		for (int i = 0; i < itemCount; i++) {
			view = getChildAt(i);
			mAdapter.setViewTag(view);
			mAdapter.holdProperty(i, view);

			if (i == 0) {
				mAdapter.selectItem(0, view);
			} else {
				mAdapter.unSelectItem(i, view);
			}

			final int position = i;
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!selectItem(position)) {
						return;
					}
					if (onItemClickListener != null) {
						onItemClickListener.onClick(position, v);
					}
				}
			});
		}

	}

	private OnItemClickListener onItemClickListener;

	public static interface OnItemClickListener {
		public void onClick(int position, View view);
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	/**
	 * @description
	 */
	protected void addItem() {
		if (itemCount < 0) {
			return;
		}
		setWeightSum(itemCount);
		View view = null;
		for (int i = 0; i < itemCount; i++) {
			LayoutParams params = new LayoutParams(0, LayoutParams.FILL_PARENT,
					1.0f);
			view = inflater.inflate(itemId, null);

			if (view != null && itemCount > 1 && i > 0) {
				params.leftMargin = itemSpacing;
			}

			if (view != null) {
				addView(view, params);
			}
		}
	}

	public boolean selectItem(int position) {
		if (position == mCurPosition) {
			return false;
		}
		if (!isLeagal(position)) {
			return false;
		}

		mAdapter.selectItem(position, getChildAt(position));
		mAdapter.unSelectItem(mCurPosition, getChildAt(mCurPosition));
		mCurPosition = position;
		
		

		return true;
	}

	public void setAdapter(ViewAdapter adapter) {
		this.mAdapter = adapter;
		initItem();
	}

	public ViewAdapter getAdapter() {
		return mAdapter;
	}

	private boolean isLeagal(int position) {

		if (position < 0 || position > itemCount - 1) {
			return false;
		}
		return true;
	}

}
