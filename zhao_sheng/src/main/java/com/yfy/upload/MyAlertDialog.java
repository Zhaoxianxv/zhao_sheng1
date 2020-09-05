package com.yfy.upload;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by yfy1 on 2016/9/23.
 */
public class MyAlertDialog extends AlertDialog {
    protected MyAlertDialog(Context context) {
        super(context);
    }

    protected MyAlertDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected MyAlertDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public void dismiss(boolean is){

    }

}
