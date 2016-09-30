package com.bjxrgz.startup.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by fd.meng on 2014/03/30
 * <p>
 * SharedPreferences的管理类
 */
public class PreferencesUtils {

    /**
     * 获取默认Preferences
     */
    public static SharedPreferences getSharedPreferences(Context context) {
        // return ((Activity) context).getPreferences(Context.MODE_PRIVATE);
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * 创建以name为名称的Preferences
     */
    public static SharedPreferences getSharedPreferences(Context context, String name) {

        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    /**
     * 是否包含Key
     */
    public static boolean contains(Context context, String key) {

        return getSharedPreferences(context).contains(key);
    }

    /**
     * 是否包含Key
     */
    public static boolean contains(Context context, String name, String key) {

        return getSharedPreferences(context, name).contains(key);
    }

    /**
     * 移除Key
     */
    public static boolean remove(Context context, String key) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();

        editor.remove(key);

        return editor.commit();
    }

    /**
     * 移除Key
     */
    public static boolean remove(Context context, String name, String key) {
        SharedPreferences.Editor editor = getSharedPreferences(context, name).edit();

        editor.remove(key);

        return editor.commit();
    }

    public static boolean putInt(Context context, String key, int pValue) {

        SharedPreferences.Editor editor = getSharedPreferences(context).edit();

        editor.putInt(key, pValue);

        return editor.commit();
    }

    public static boolean putInt(Context context, String name, String key, int pValue) {

        SharedPreferences.Editor editor = getSharedPreferences(context, name).edit();

        editor.putInt(key, pValue);

        return editor.commit();
    }

    public static int getInt(Context context, String key, int defaultValue) {

        return getSharedPreferences(context).getInt(key, defaultValue);
    }

    public static int getInt(Context context, String key, String name, int defaultValue) {

        return getSharedPreferences(context, name).getInt(key, defaultValue);
    }

    public static boolean putLong(Context context, String key, long pValue) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();

        editor.putLong(key, pValue);

        return editor.commit();
    }

    public static boolean putLong(Context context, String name, String key, long pValue) {
        SharedPreferences.Editor editor = getSharedPreferences(context, name).edit();

        editor.putLong(key, pValue);

        return editor.commit();
    }

    public static long getLong(Context context, String key, long defaultValue) {
        return getSharedPreferences(context).getLong(key, defaultValue);
    }

    public static long getLong(Context context, String name, String key, long defaultValue) {
        return getSharedPreferences(context, name).getLong(key, defaultValue);
    }

    public static boolean putBoolean(Context context, String key, boolean pValue) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();

        editor.putBoolean(key, pValue);

        return editor.commit();
    }

    public static boolean putBoolean(Context context, String name, String key, boolean pValue) {
        SharedPreferences.Editor editor = getSharedPreferences(context, name).edit();

        editor.putBoolean(key, pValue);

        return editor.commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {

        return getSharedPreferences(context).getBoolean(key, defaultValue);
    }

    public static boolean getBoolean(Context context, String name, String key, boolean defaultValue) {

        return getSharedPreferences(context, name).getBoolean(key, defaultValue);
    }

    public static boolean putString(Context context, String key, String pValue) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();

        editor.putString(key, pValue);

        return editor.commit();
    }

    public static boolean putString(Context context, String name, String key, String pValue) {
        SharedPreferences.Editor editor = getSharedPreferences(context, name).edit();

        editor.putString(key, pValue);

        return editor.commit();
    }

    public static String getString(Context context, String key, String defaultValue) {

        return getSharedPreferences(context).getString(key, defaultValue);
    }

    public static String getString(Context context, String name, String key, String defaultValue) {

        return getSharedPreferences(context, name).getString(key, defaultValue);
    }

}
