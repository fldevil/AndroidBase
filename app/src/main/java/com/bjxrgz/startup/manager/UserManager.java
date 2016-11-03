package com.bjxrgz.startup.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.bjxrgz.startup.domain.User;
import com.bjxrgz.startup.utils.LogUtils;

/**
 * Created by Fan on 2016/6/16.
 * Preference User管理类
 */
public class UserManager {

    /**
     * 保存Preference的name
     */
    private static UserManager userManager;
    private static SharedPreferences preferences;

    /**
     * 用户信息
     **/
    private static final String USER_USER_ID = "userID";
    private static final String USER_USER_TOKEN = "userToken";


    private UserManager(Context context) {
        preferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }

    /**
     * 在MyApp里初始化
     */
    public static synchronized void initApp(Context context) {
        if (userManager == null) {
            userManager = new UserManager(context);
        }
    }

    /**
     * 单例模式，获取instance实例
     */
    public synchronized static UserManager getInstance() {
        if (userManager == null) {
            throw new RuntimeException("please initApp first!");
        }
        return userManager;
    }

    public void setUser(User user) {
        LogUtils.json("setUser", GsonManager.getInstance().toJson(user));
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_USER_ID, user.getId());
        editor.putString(USER_USER_TOKEN, user.getUserToken());
//        editor.commit();
        editor.apply();
    }

    public User getUser() {
        User user = new User();
        user.setId(preferences.getString(USER_USER_ID, ""));
        user.setUserToken(preferences.getString(USER_USER_TOKEN, ""));
        LogUtils.json("getUser", GsonManager.getInstance().toJson(user));
        return user;
    }

    /**
     * 清除用户信息
     */
    public void RemoveInfo() {
        preferences.edit().clear().apply();
    }
}
