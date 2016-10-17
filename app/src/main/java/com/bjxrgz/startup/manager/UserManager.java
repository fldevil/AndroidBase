package com.bjxrgz.startup.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.bjxrgz.startup.domain.User;

/**
 * Created by Fan on 2016/6/16.
 * Preference User管理类
 */
public class UserManager {

    /**
     * 保存Preference的name
     */
    private static final String PREFERENCE_NAME = "userInfo";
    private static UserManager userManager;
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    /**
     * 用户信息
     **/
    public static final String USER_USER_ID = "userID";
    public static final String USER_USER_TOKEN = "userToken";


    private UserManager(Context context) {
        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
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
        editor.putString(USER_USER_ID, user.getId());
        editor.putString(USER_USER_TOKEN, user.getUserToken());
        editor.commit();
    }

    public User getUser() {
        User user = new User();
        user.setId(preferences.getString(USER_USER_ID, ""));
        user.setUserToken(preferences.getString(USER_USER_TOKEN, ""));
        return user;
    }

    /**
     * 清除用户信息
     */
    public void RemoveInfo() {
        editor.clear().apply();
    }
}
