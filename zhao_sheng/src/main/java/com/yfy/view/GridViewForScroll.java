/**
 * 
 */
package com.yfy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @author yfy1
 * @Date 2015��11��30��
 * @version 1.0
 * @Desprition
 */
public class GridViewForScroll extends GridView {

	public GridViewForScroll(Context context) {
		super(context);
	}

	public GridViewForScroll(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
