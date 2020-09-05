package com.yfy.view.textView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by yfyandr on 2017/9/15.
 */

public class TextViewMarquee extends TextView {
    public TextViewMarquee(Context context) {
        super(context);
    }

    public TextViewMarquee(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewMarquee(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean isFocused() {
        return true;
    }
}
