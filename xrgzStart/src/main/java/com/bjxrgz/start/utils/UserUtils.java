package com.bjxrgz.start.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.bjxrgz.start.base.MyApp;
import com.bjxrgz.start.domain.User;

import java.util.Map;

/**
 * Created by Fan on 2016/6/16.
 * Preference User管理类
 */
public class UserUtils {

    private static final String SHARE_NAME = "userInfo";
    private static SharedPreferences preferences;

    /* 储存字段 */
    private static final String userToken = "userToken";

    private synchronized static SharedPreferences getPreference() {
        if (preferences == null) {
            if (TextUtils.isEmpty(SHARE_NAME)) {
                preferences = PreferenceManager.getDefaultSharedPreferences(MyApp.get());
            } else {
                preferences = MyApp.get().getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
            }
        }
        return preferences;
    }

    /* 是否包含Key */
    public static boolean contains(String key) {
        return getPreference().contains(key);
    }

    /* 移除Key */
    public static boolean remove(String key) {
        return getPreference().edit().remove(key).commit();
    }

    /* 清除信息 */
    public static void clear() {
        getPreference().edit().clear().apply();
        HttpUtils.clearToken();
    }

    /* 获取所有存储的键值对 */
    public static Map<String, ?> getAllInfo() {
        return getPreference().getAll();
    }

    public static void setUser(User user) {
        LogUtils.json("setUser", GsonUtils.get().toJson(user));
        SharedPreferences.Editor editor = getPreference().edit();
        editor.putString(userToken, user.getUserToken());
        editor.apply();
    }

    public static User getUser() {
        SharedPreferences preference = getPreference();
        User user = new User();
        user.setUserToken(preference.getString(userToken, ""));
        return user;
    }

}
