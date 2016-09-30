package com.bjxrgz.startup.base;

/**
 * Created by mengfd on 5/26/16.
 * <p>
 * 常量类
 */
public class Constants {

    /**
     * ************************************API**********************************
     */
    public static final String API_TEST = ""; // 测试
    public static final String API_RELEASE = ""; // 正式
    public static String API_HOST;

    static {
        if (MyApp.IS_RELEASE) {
            Constants.API_HOST = Constants.API_RELEASE;
        } else {
            Constants.API_HOST = Constants.API_TEST;
        }
    }
}
