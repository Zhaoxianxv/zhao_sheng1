package com.yfy.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.yfy.lib.hellocharts.view.LineChartView;

public class Chartline extends LineChartView {
    public Chartline(Context context) {
        super(context);
    }

    public Chartline(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Chartline(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
