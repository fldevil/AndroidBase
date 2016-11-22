package com.bjxrgz.startup.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.bjxrgz.startup.base.MyApp;
import com.bjxrgz.startup.domain.User;
import com.bjxrgz.startup.utils.LogUtils;

import java.util.Map;

/**
 * Created by Fan on 2016/6/16.
 * Preference User管理类
 */
public class UserManager {

    private static final String SHARE_NAME = "userInfo";
    private static SharedPreferences preferences;

    /* 储存字段 */
    private static final String USER_USER_ID = "userId";
    private static final String USER_USER_TOKEN = "userToken";

    private synchronized static SharedPreferences getPreference() {
        if (preferences == null) {
            if (TextUtils.isEmpty(SHARE_NAME)) {
                preferences = PreferenceManager.getDefaultSharedPreferences(MyApp.getInstance());
            } else {
                preferences = MyApp.getInstance().getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
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
    public void clear() {
        getPreference().edit().clear().apply();
    }

    /* 获取所有存储的键值对 */
    public static Map<String, ?> getAllInfo() {
        return getPreference().getAll();
    }

    public static void setUser(User user) {
        LogUtils.json("setUser", GsonManager.getInstance().toJson(user));
        SharedPreferences.Editor editor = getPreference().edit();
        editor.putString(USER_USER_ID, user.getId());
        editor.putString(USER_USER_TOKEN, user.getUserToken());
        editor.apply();
    }

    public static User getUser() {
        User user = new User();
        user.setId(getPreference().getString(USER_USER_ID, ""));
        user.setUserToken(getPreference().getString(USER_USER_TOKEN, ""));
        LogUtils.json("getUser", GsonManager.getInstance().toJson(user));
        return user;
    }

}
