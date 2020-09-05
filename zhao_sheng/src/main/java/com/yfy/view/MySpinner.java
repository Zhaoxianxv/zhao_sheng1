package com.yfy.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressLint("AppCompatCustomView")
public class MySpinner extends Spinner {

    private ItemClick mItemClick;

    public MySpinner(Context context) {
        super(context);
        init();
    }

    public MySpinner(Context context, int mode) {
        super(context, mode);
        init();
    }

    public MySpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MySpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MySpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
        init();
    }

    public MySpinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, int mode) {
        super(context, attrs, defStyleAttr, defStyleRes, mode);
        init();
    }

    @SuppressLint("NewApi")
    public MySpinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, int mode, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, defStyleRes, mode, popupTheme);
        init();
    }

    public void setItemClick(ItemClick mItemClick) {
        this.mItemClick = mItemClick;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void init() {
        Class<?> myClass = Spinner.class;
        try {
            Class<?>[] params = new Class[1];
            params[0] = OnItemClickListener.class;
            Method m = myClass.getDeclaredMethod("setOnItemClickListenerInt", params);
            m.setAccessible(true);
            m.invoke(this, new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Class<?> myClass = AdapterView.class;
                    try {
                        Field field = myClass.getDeclaredField("mOldSelectedPosition");
                        field.setAccessible(true);
                        field.setInt(MySpinner.this, AdapterView.INVALID_POSITION);

                        mItemClick.onClick(position);

                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public interface ItemClick {
        void onClick(int position);
    }
}
