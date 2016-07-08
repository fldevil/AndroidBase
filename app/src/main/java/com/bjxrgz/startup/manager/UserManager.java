package com.bjxrgz.startup.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Fan on 2016/6/16.
 * Preference User管理类
 */
public class UserManager {

    /**
     * 保存Preference的name
     */
    public static final String PREFERENCE_NAME = "userInfo";
    private static UserManager userManager;
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    /**
     * 用户信息
     **/
    public static final String USER_USER_ID = "userID";
    public static final String USER_USER_TOKEN = "userToken";
    public static final String USER_NICK_NAME = "nickName";
    public static final String USER_USER_NO = "userNO";

    public static final String USER_IMG = "userImg"; // 用户头像
    public static final String USER_NAME = "userName"; // 用户名
    public static final String USER_SEX = "sex"; // 性别
    public static final String USER_BIRTHDAY = "birthday"; // 生日
    public static final String USER_ID_CARD = "idCard"; // 身份证号
    public static final String USER_TELE = "telephone"; // 手机号
    public static final String USER_EMAIL = "email"; // 电子邮箱

    private UserManager(Context context) {
        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    /**
     * 在MyApp里初始化
     */
    public static synchronized void init(Context context) {
        if (userManager == null) {
            userManager = new UserManager(context);
        }
    }

    /**
     * 单例模式，获取instance实例
     */
    public synchronized static UserManager getInstance() {
        if (userManager == null) {
            throw new RuntimeException("please init first!");
        }

        return userManager;
    }

//    public void setUser(User user) {
//        editor.putString(USER_USER_ID, user.getUserID());
//        editor.putString(USER_USER_NO, user.getUserNO());
//        editor.putString(USER_USER_TOKEN, user.getUserToken());
//        editor.putString(USER_NICK_NAME, user.getNickName());
//        editor.putString(USER_IMG, user.getUserImg());
//        editor.putString(USER_NAME, user.getUserName());
//        editor.putString(USER_SEX, user.getSex());
//        editor.putString(USER_BIRTHDAY, user.getBirthday());
//        editor.putString(USER_ID_CARD, user.getIdCard());
//        editor.putString(USER_TELE, user.getTelephone());
//        editor.putString(USER_EMAIL, user.getEmail());
//        editor.commit();
//    }

//    public User getUser() {
//        User user = new User();
//        user.setUserID(preferences.getString(USER_USER_ID, ""));
//        user.setUserNO(preferences.getString(USER_USER_NO, ""));
//        user.setUserToken(preferences.getString(USER_USER_TOKEN, ""));
//        user.setNickName(preferences.getString(USER_NICK_NAME, ""));
//        user.setUserImg(preferences.getString(USER_IMG, ""));
//        user.setUserName(preferences.getString(USER_NAME, ""));
//        user.setSex(preferences.getString(USER_SEX, ""));
//        user.setBirthday(preferences.getString(USER_BIRTHDAY, ""));
//        user.setIdCard(preferences.getString(USER_ID_CARD, ""));
//        user.setTelephone(preferences.getString(USER_TELE, ""));
//        user.setEmail(preferences.getString(USER_EMAIL, ""));
//        return user;
//    }

    /**
     * 清除用户信息
     */
    public void RemoveInfo() {
        editor.clear().apply();
    }
}
