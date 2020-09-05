package com.yfy.app.info_submit.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.yfy.app.info_submit.infos.WriteItem;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.StringJudge;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WriteInfoView extends View {

	private List<WriteItem> list = new ArrayList<WriteItem>();

	private Paint paint;
	private Rect mBounds;

	private float paddingTop, paddingBottom, paddingLeft, paddingRight;
	private float h_spacing, v_spacing;
	private float marginTotal;

	private float nameWidth;
	private float nameHeight;

	private float height;
	private float width;


	public WriteInfoView(Context context){
		super(context);
		initDrawTools();
	}

	public WriteInfoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initDrawTools();
	}

	private void initDrawTools() {
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mBounds = new Rect();
		paint.setTextSize(20);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		height = paddingTop;
		width = paddingLeft;

		for (Iterator<WriteItem> iterator = list.iterator(); iterator.hasNext();) {
			WriteItem item = iterator.next();
			String itemName = item.getItemName() + ":";
			String itemValue = item.getItemValue();
			if (StringJudge.isEmpty(itemValue)||itemValue.equals("null")) {
				itemValue = "未填写";
			}

			paint.getTextBounds(itemName, 0, itemName.length(), mBounds);
			nameWidth = getTextWidth(paint, itemName);
			nameHeight = mBounds.height();
			canvas.drawText(itemName, width, height + nameHeight, paint);

			paint.getTextBounds(itemValue, 0, itemValue.length(), mBounds);
			String[] result = change(paint, itemValue,
					(int) (BaseActivity.mScreenWidth - paddingLeft - paddingRight
							- marginTotal - h_spacing - nameWidth));

			for (int k = 0; k < result.length; k++) {
				canvas.drawText(result[k], nameWidth + width + h_spacing,
						height + nameHeight, paint);
				height = height + nameHeight + v_spacing;
			}

		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
		int measureHeight = (int) getAfterDrawHeight();
		setMeasuredDimension(measureWidth, measureHeight);
	}

	public float getTextWidth(Paint paint, String str) {
		float iRet = 0;
		if (str != null && str.length() > 0) {
			int len = str.length();
			float[] widths = new float[len];
			paint.getTextWidths(str, widths);
			for (int j = 0; j < len; j++) {
				iRet += (float) Math.ceil(widths[j]);
			}
		}
		return iRet;
	}

	public String[] change(Paint paint, String str, int width) {
		List<String> list = new ArrayList<String>();

		int iRet = 0;
		int start = 0;
		if (str != null && str.length() > 0) {
			int len = str.length();
			float[] widths = new float[len];
			paint.getTextWidths(str, widths);
			for (int j = 0; j < len; j++) {
				iRet += (int) Math.ceil(widths[j]);
				if (iRet > width) {
					String s = str.substring(start, j + 1);
					list.add(s);
					start = j + 1;
					iRet = 0;
				}

				if (j == len - 1 && start != j + 1) {
					String s = str.substring(start, len);
					list.add(s);
				}
			}
		}

		String[] result = new String[list.size()];
		int i = 0;
		for (Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
			result[i] = iterator.next();
			i++;
		}
		return result;
	}

	public float getAfterDrawHeight() {
		float totalHeight = paddingTop;
		float nameWidth, nameHeight;
		for (Iterator<WriteItem> iterator = list.iterator(); iterator.hasNext();) {
			WriteItem item = iterator.next();
			String itemName = item.getItemName();
			String itemValue = item.getItemValue();
			if (StringJudge.isEmpty(itemValue)||itemValue.equals("null")) {
				itemValue = "未填写";
			}

			paint.getTextBounds(itemName, 0, itemName.length(), mBounds);
			nameWidth = getTextWidth(paint, itemName);
			nameHeight = mBounds.height();


			paint.getTextBounds(itemValue, 0, itemValue.length(), mBounds);
			String[] result = change(paint, itemValue,
					(int) (BaseActivity.mScreenWidth - paddingLeft - paddingRight
							- marginTotal - h_spacing - nameWidth));
			for (int k = 0; k < result.length; k++) {
				totalHeight = totalHeight + nameHeight + v_spacing;
			}
		}
		return totalHeight;
	}

	public void setData(List<WriteItem> list) {
		this.list = list;
		invalidate();
	}

	public void setTextSize(int dp) {
		paint.setTextSize(dp * BaseActivity.mDensity);
	}

	public void setMarginTotal(float f) {
		this.marginTotal = f * BaseActivity.mDensity;
	}

	public void setPadding(float paddingTop, float paddingBottom,
						   float paddingLeft, float paddingRight, float spacing,
						   float h_spacing) {

		this.paddingTop = paddingTop * BaseActivity.mDensity;
		this.paddingBottom = paddingBottom *BaseActivity.mDensity;
		this.paddingLeft = paddingLeft * BaseActivity.mDensity;
		this.paddingRight = paddingRight * BaseActivity.mDensity;
		this.v_spacing = spacing * BaseActivity.mDensity;
		this.h_spacing = h_spacing * BaseActivity.mDensity;

	}
}
