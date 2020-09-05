package com.yfy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by yfyandr on 2018/3/12.
 */

public class VListView extends ListView {
    public VListView(Context context) {
        super(context);
    }

    public VListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
