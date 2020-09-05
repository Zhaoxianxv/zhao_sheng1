package com.yfy.exafunction;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.yfy.lib.PullToRefreshBase;
import com.yfy.final_tag.DateUtils;


public class PullToRefreshFunction extends BaseFunction {

	public final static String LAST_REFRESH_TIME = "lastRefreshTime";

	private Context mContext;
	protected PullToRefreshInfo mPullToRefreshInfo;
	private SharedPreferences mPreference;

	public PullToRefreshFunction(Context context,
								 PullToRefreshInfo pullToRefreshInfo) {
		mContext = context;
		mPullToRefreshInfo = pullToRefreshInfo;
	}

	public PullToRefreshInfo getPullToRefreshInfo() {
		return mPullToRefreshInfo;
	}

	@Override
	public void onStart() {
		super.onStart();
		IllegalException();
		if (mPreference == null) {
			initPrefence();
		}
		initRefreshTime();
	}

	public boolean saveLastRefreshTime(long lastRefreshTime) {
		return saveLastRefreshTime("", lastRefreshTime);
	}

	public String getLastRefreshTime() {
		return getLastRefreshTime("");
	}

	public boolean saveLastRefreshTime(String name, long lastRefreshTime) {
		SharedPreferences.Editor editor = mPreference.edit();
		editor.putLong(LAST_REFRESH_TIME + name, lastRefreshTime);
		return editor.commit();
	}

	public String getLastRefreshTime(String name) {
		String lastRefreshTime = "";
		long lastTime = mPreference.getLong(LAST_REFRESH_TIME + name, 0);
		if (lastTime == 0) {
			return "";
		}
		if (DateUtils.isToday(lastTime)) {
			lastRefreshTime = "今日" + DateUtils.getTime(lastTime);
		} else {
			lastRefreshTime = DateUtils.getDateTime(lastTime);
		}
		return lastRefreshTime;

	}

	public boolean clearPref() {
		SharedPreferences.Editor editor = mPreference.edit();
		return editor.clear().commit();
	}

	private void initPrefence() {
		mPreference = mContext.getSharedPreferences(
				mPullToRefreshInfo.timePrefName, Context.MODE_PRIVATE);
	}

	public void initRefreshTime() {
		String showLabel = getLastRefreshTime();
		if (!TextUtils.isEmpty(showLabel)) {
			mPullToRefreshInfo.refresh_lv.getLoadingLayoutProxy(true, false)
					.setLastUpdatedLabel("最后刷新：" + showLabel);
		}
	}

	private void IllegalException() {
		if (mPullToRefreshInfo == null) {
			throw new IllegalArgumentException(
					"PullToRefreshInfo can't be null");
		}
		if (mPullToRefreshInfo.refresh_lv == null) {
			throw new IllegalArgumentException("refresh_lv can't be null");
		}
		if (TextUtils.isEmpty(mPullToRefreshInfo.timePrefName)) {
			throw new IllegalArgumentException("timePrefName can't be null");
		}
	}

	public static class PullToRefreshInfo {
		public PullToRefreshInfo() {
		}

		public String timePrefName;
		public PullToRefreshBase<?> refresh_lv;

		public boolean isLeagal() {
			return !TextUtils.isEmpty(timePrefName) && refresh_lv != null;
		}
	}

	@Override
	public boolean isLeagal() {
		return super.isLeagal() && mPullToRefreshInfo != null
				&& mPullToRefreshInfo.isLeagal();
	}
}
