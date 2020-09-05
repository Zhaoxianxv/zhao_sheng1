/**
 *
 */
package com.yfy.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.zhao_sheng.R;

import java.util.List;


/**
 * @author yfy
 * @date 2015-10-25
 * @version 1.0
 * @description MyHorizontalTabView
 */
public class MyHorizontalTabView extends HorizontalTabView {

  private int base_color;

  /**
   * @param context
   * @param attrs
   */
  public MyHorizontalTabView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initRes(context);
  }

  /**
   * @description
   */
  private void initRes(Context context) {
    base_color = context.getResources().getColor(R.color.main_red);
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * com.yfy.longjianglu.ui.view.HorizontalTabView#itemChecked(android.widget
   * .TextView, boolean)
   */
  @Override
  protected void itemChecked(TextView tv, boolean b) {
    if (b) {
      tv.setTextColor(base_color);
    } else {
      tv.setTextColor(Color.BLACK);
    }
  }

  @Override
  protected void initTextViews(TextView tv, List<String> texts) {

  }
}
