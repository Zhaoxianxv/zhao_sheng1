package com.yfy.app.info_submit.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class MarqueeTextView extends TextView {

	public MarqueeTextView(Context context) {
		super(context);
	}
	
	public MarqueeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MarqueeTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	public boolean isFocused() {
		return true;
	}
	
	@SuppressLint("MissingSuperCall")
	@Override
	protected void onFocusChanged(boolean focused, int direction,
	   Rect previouslyFocusedRect) {  
		
	}
}
