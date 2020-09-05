package com.yfy.view.button;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Aj Liao on 2016/3/7.
 */
public abstract class BottomItem extends RelativeLayout {

    public BottomItem(Context context) {
        super(context);
    }

    public BottomItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public abstract BottomItem init();

    public abstract void switchViewStatus(boolean checked);
    public abstract void switchViewStatus(boolean checked,final int i);
    public abstract void switchViewStatus(boolean checked,final int un,final int dwon);


    //小红点
    public abstract void showBadge();

    public abstract void hideBadge();
    //小红点（带数量）
    public abstract void showBadge(int count);
    //
    public abstract  void setbottomText(String name);
}
