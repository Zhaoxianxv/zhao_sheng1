package com.yfy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioGroup;

import com.yfy.final_tag.Logger;

/**
 * Created by Hear on 2016/5/6.
 */
public class MyRadioGroup extends RadioGroup {
    public MyRadioGroup(Context context) {
        this(context, null);
    }

    public MyRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    /**
     * 复写测量的方法
     *
     * @param widthMeasureSpec  宽度
     * @param heightMeasureSpec 高度
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //获取到孩子的个数
        int childCount = getChildCount();
        if (childCount==0){

            return ;
        }
        int x = 0;
        int y = 0;
        int row = 0;
        //获取到最大宽度
        int maxWidth = View.MeasureSpec.getSize(widthMeasureSpec);


        Logger.e("zxx","孩子的个数"+childCount);
        //遍历孩子，并对它们进行测量
        for (int i = 0; i < childCount; i++) {
            //获取到孩子
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {

                child.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight(); //计算出大小
                y = height * (row + 1);
                x += width;

                if (x > maxWidth) {
                    row++;
                    //如果大于了最大的宽度，那么就换行
                    x = width;
                    y = height * (row + 1) + height / 2;
                }
            }
            setMeasuredDimension(maxWidth, y+y+y);
            Logger.e("message", "y = " + y);
        }
    }

    /**
     * 复写布局方法
     *
     * @param changed 是否改变
     * @param l       左
     * @param t       上
     * @param r       右
     * @param b       下
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //获取到孩子的个数
        int childCount = getChildCount();
        int x = 0;
        int y = 0;
        int row = 0;
        //间隙view之间
        int weight = 50;
        //计算最大宽度
        int maxWidth = r - l;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();
                //计算XY
                x += width+width/3;
                if (row!=0){
                    y = (row + 1) * height+height/2;
                }else {
                    y = (row + 1) * height;
                }
                if (x > maxWidth) {
                    //换行
                    row++;
                    x = width+width/3;
                    y = (row + 1) * height+height/2;
                }
                //设置孩子的位置
                child.layout(x - width, y - height, x, y);
            }
        }

    }
}
