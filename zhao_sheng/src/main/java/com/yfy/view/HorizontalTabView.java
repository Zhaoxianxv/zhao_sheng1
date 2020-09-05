/**
 *
 */
package com.yfy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yfy
 * @date 2015-10-25
 * @version 1.0
 * @description HorizontalTabView
 */
public abstract class HorizontalTabView extends HorizontalScrollView {

  // private final static String TAG =
  // HorizontalTabView.class.getSimpleName();

  private List<TextView> viewList = new ArrayList<TextView>();
  private List<Integer> widthList = new ArrayList<Integer>();
  private LinearLayout layout = null;
  private Scroller mScroller;
  private TextView mCurTextView;
  private int marginLeft = 0;
  private float mDensity;

  // private Resources resources;

  /**
   * @param context
   */
  public HorizontalTabView(Context context) {
    super(context);
  }

  /**
   * @param context
   * @param attrs
   */
  public HorizontalTabView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initViews(context);
  }

  /**
   * @param context
   * @param attrs
   * @param defStyle
   */
  public HorizontalTabView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    initViews(context);
  }

  /**
   * @description
   */
  private void initViews(Context context) {
    mScroller = new Scroller(context);
    layout = new LinearLayout(getContext());
    @SuppressWarnings("deprecation")
    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.FILL_PARENT);
    layout.setOrientation(LinearLayout.HORIZONTAL);
    addView(layout, params);
    mDensity=context.getResources().getDisplayMetrics().density;

  }

  public void addItems(List<String> texts) {
    mCurTextView = null;
    marginLeft = (int) (100 * mDensity);
    viewList.clear();
    widthList.clear();
    layout.removeAllViews();

    for (String text : texts) {
      TextView item = new TextView(getContext());
      initTextViews(item, texts);
      @SuppressWarnings("deprecation")
      LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
              LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
      item.setGravity(Gravity.CENTER);
      item.setText(text);
      item.setPadding((int) (7 *mDensity), 0,
              (int) (7 * mDensity), 0);
      layout.addView(item, params);
      viewList.add(item);
      item.setOnClickListener(onClickListener);
    }
    measureItem();
    if (widthList.size()==0){

    }else{
      setItemChecked(0);
    }

  }

  private void measureItem() {
    int w = MeasureSpec.makeMeasureSpec(0,
            MeasureSpec.UNSPECIFIED);
    int h = MeasureSpec.makeMeasureSpec(0,
            MeasureSpec.UNSPECIFIED);
    for (TextView textView : viewList) {
      textView.measure(w, h);
      widthList.add(textView.getMeasuredWidth());
    }
  }

  private OnClickListener onClickListener = new OnClickListener() {

    @Override
    public void onClick(View v) {
      int position = viewList.indexOf((TextView) v);
      if (!v.equals(mCurTextView) && listener != null) {
        listener.OnSelected(position);
      }
      setItemChecked(position);
    }
  };

  protected abstract void itemChecked(TextView tv, boolean b);

  protected abstract void initTextViews(TextView tv, List<String> texts);

  public void setItemChecked(int position) {
    TextView tv = viewList.get(position);
    itemChecked(tv, true);
    if (mCurTextView != null && mCurTextView != tv) {
      itemChecked(mCurTextView, false);
    }
    mCurTextView = tv;

    int mWidth = 0;
    int canScroll = 1;
    for (Integer width : widthList) {
      mWidth += width;
      if (mWidth < marginLeft) {
        canScroll = canScroll + 1;
      }
    }

    if (position >= canScroll) {
      if (!isScrollEnd(position)) {
        int offset = 0;
        for (int i = 0; i < position; i++) {
          offset += widthList.get(i);
        }
        offset -= marginLeft;
        smoothScroll(offset);
      }
    } else {
      smoothScroll(0);
    }
  };

  public boolean isScrollEnd(int position) {
    return false;
  }

  private OnItemSelectedListener listener = null;

  public void setOnItemSelectedListener(OnItemSelectedListener listener) {
    this.listener = listener;
  }

  public static interface OnItemSelectedListener {
    public void OnSelected(int position);
  }


  @Override
  public void computeScroll() {
    // TODO Auto-generated method stub
    super.computeScroll();
    if (!mScroller.isFinished()) {
      if (mScroller.computeScrollOffset()) {
        int oldX = getScrollX();
        int oldY = getScrollY();
        int x = mScroller.getCurrX();
        int y = mScroller.getCurrY();
        if (oldX != x || oldY != y) {
          scrollTo(x, y);
        }
        invalidate();
      }
    }
  }

  /**
   *
   * @param desX
   * @param
   */
  public void smoothScroll(int desX) {
    int oldScrollX = getScrollX();
    int oldScrollY = getScrollY();
    int dxX = desX - oldScrollX;
    int dxY = oldScrollY;
    mScroller.startScroll(oldScrollX, oldScrollY, dxX, dxY, 500);
    invalidate();
  }
}
