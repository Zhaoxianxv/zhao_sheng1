package com.yfy.view.mult;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;

import com.yfy.final_tag.Logger;

public class RoundImageDrawable extends Drawable {

	private final static String TAG = RoundImageDrawable.class.getSimpleName();

	private Paint mPaint;
	private Bitmap mBitmap;
	private int mRadiusPix;
	private RectF rectF;

	public RoundImageDrawable(Bitmap bitmap, int radiusPix) {
		mBitmap = bitmap;
		mRadiusPix = radiusPix;
		BitmapShader bitmapShader = new BitmapShader(bitmap, TileMode.CLAMP, TileMode.CLAMP);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setShader(bitmapShader);
	}

	@Override
	public void setBounds(int left, int top, int right, int bottom) {
		super.setBounds(left, top, right, bottom);
		rectF = new RectF(left, top, right, bottom);
		Rect rect = new Rect(left, top, right, bottom);
		copyBounds(rect);
		Logger.e( rectF.toString());
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawRoundRect(rectF, mRadiusPix, mRadiusPix, mPaint);
	}

	@Override
	public int getIntrinsicWidth() {
		Logger.e(TAG, "mBitmap.getWidth()=" + mBitmap.getWidth());
		return mBitmap.getWidth();
	}

	@Override
	public int getIntrinsicHeight() {
		Logger.e(TAG, "mBitmap.getHeight()=" + mBitmap.getHeight());
		return mBitmap.getHeight();
	}

	@Override
	public void setAlpha(int alpha) {
		mPaint.setAlpha(alpha);
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		mPaint.setColorFilter(cf);
	}

	@Override
	public int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}

}
