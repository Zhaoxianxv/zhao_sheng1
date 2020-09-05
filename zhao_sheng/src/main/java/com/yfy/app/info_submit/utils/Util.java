package com.yfy.app.info_submit.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.InputType;
import android.view.WindowManager;
import android.widget.EditText;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {



	@SuppressLint("SimpleDateFormat")
	public static String getData() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd-HH:mm:ss");

		return sdf.format(new Date());
	}



	public static String getId(String url) {
		if (url != null && !url.equals("")) {
			url = url.substring(url.lastIndexOf("=") + 1, url.length());
		}
		return url;
	}


	public static void hideSoftInputMethod(Activity activity, EditText ed) {
		activity.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		int currentVersion = android.os.Build.VERSION.SDK_INT;
		String methodName = null;
		if (currentVersion >= 16) {
			// 4.2
			methodName = "setShowSoftInputOnFocus";
		} else if (currentVersion >= 14) {
			// 4.0
			methodName = "setSoftInputShownOnFocus";
		}

		if (methodName == null) {
			ed.setInputType(InputType.TYPE_NULL);
		} else {
			Class<EditText> cls = EditText.class;
			Method setShowSoftInputOnFocus;
			try {
				setShowSoftInputOnFocus = cls.getMethod(methodName,
						boolean.class);
				setShowSoftInputOnFocus.setAccessible(true);
				setShowSoftInputOnFocus.invoke(ed, false);
			} catch (NoSuchMethodException e) {
				ed.setInputType(InputType.TYPE_NULL);
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}






	
}
