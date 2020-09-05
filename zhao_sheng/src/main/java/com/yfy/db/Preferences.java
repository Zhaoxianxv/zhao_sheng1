package com.yfy.db;

import android.content.SharedPreferences;

/**
 * Created by Aj Liao
 * <p/>
 * 抽象类，封装了基本数据类型；
 */
public abstract class Preferences {

    public void clearAll() {
        SharedPreferences.Editor editor = getPreference().edit();
        editor.clear();
        editor.commit();
    }

    public abstract SharedPreferences getPreference();

    protected int getInt(String tag, int defaultValue) {
        return getPreference().getInt(tag, defaultValue);
    }

    protected void saveInt(String tag, int value) {
        SharedPreferences.Editor editor = getPreference().edit();
        editor.putInt(tag, value);

        editor.commit();
    }

    protected long getLong(String tag, long defaultValue) {
        return getPreference().getLong(tag, defaultValue);
    }

    protected void saveLong(String tag, long value) {
        SharedPreferences.Editor editor = getPreference().edit();
        editor.putLong(tag, value);
        editor.commit();
    }

    protected String getString(String tag, String defaultValue) {
        return getPreference().getString(tag, defaultValue);
    }

    protected void saveString(String tag, String value) {
        SharedPreferences.Editor editor = getPreference().edit();
        editor.putString(tag, value);
        editor.commit();
    }

    protected boolean getBoolean(String tag, boolean defaultValue) {
        return getPreference().getBoolean(tag, defaultValue);
    }

    protected void saveBooolean(String tag, boolean value) {
        SharedPreferences.Editor editor = getPreference().edit();
        editor.putBoolean(tag, value);
        editor.commit();
    }
}