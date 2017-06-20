package com.user.project.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.bjxrgz.base.BaseApp;
import com.bjxrgz.base.utils.GsonUtil;
import com.bjxrgz.base.utils.LogUtil;
import com.user.project.domain.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Fan on 2017/3/2.
 * Preferences 帮助类
 */

public class SPUtils {

    private static final String SHARE_USER = "user";

    /* 储存字段 */
    private static final String id = "id";
    private static final String userToken = "userToken";

    private static Map<String, SharedPreferences> map = new HashMap<>();

    private static SharedPreferences getSharedPreferences(String name) {
        if (TextUtils.isEmpty(name)) {
            throw new IllegalArgumentException("表名不能为空");
        }
        SharedPreferences sharedPreferences = map.get(name);
        if (sharedPreferences == null) {
            sharedPreferences = BaseApp.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE);
            map.put(name, sharedPreferences);
        }
        return sharedPreferences;
    }

    /**
     * 存取User
     */
    public static void setUser(User user) {
        LogUtil.json("setUser", GsonUtil.get().toJson(user));
        clearUser();
        SharedPreferences.Editor editor = getSharedPreferences(SHARE_USER).edit();
        editor.putString(id, user.getId());
        editor.putString(userToken, user.getUserToken());
        editor.apply();
    }

    /**
     * 获取缓存User
     */
    public static User getUser() {
        SharedPreferences preference = getSharedPreferences(SHARE_USER);
        User user = new User();
        user.setId(preference.getString(id, ""));
        user.setUserToken(preference.getString(userToken, ""));
        LogUtil.json("getUser", GsonUtil.get().toJson(user));
        return user;
    }

    /**
     * 清除用户信息
     */
    public static void clearUser() {
        SharedPreferences.Editor editor = getSharedPreferences(SHARE_USER).edit();
        editor.clear().apply();
    }
}
