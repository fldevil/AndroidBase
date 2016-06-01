package com.bjxrgz.startup.base;

/**
 * Created by mengfd on 5/26/16.
 * <p>
 * 常量类
 */
public class Constants {

    // umeng
    public static final String UMENG_KEY = "574ba9d7e0f55a6ef000229b";
    public static final String UMENG_MESSAGE_SECRET = "7ecb98cfd7c2ed28b2b55d61554dc129";
    public static final String UMENG_MASTER_SECRET = "zr7jry1pt6ibjdanc2fjap7ra30hgqgk";
    // aMaps
    public static final String LBS_KEY = "3477a7d3ac841c00e52777f655125d49";
    /**
     * *******************************SharePreference*******************************
     */
    public static final String USER = "User";

    /**
     * ************************************API**********************************
     */
    public static final String API_HOST = "http://192.168.0.117:8080/acquisition/";
    public static final String API_LOGIN = API_HOST + "User/Login";  //用户登录

    /**
     * ************************************Params**********************************
     */
    public static final String API_KEY_NAME = "APIKEY";
    public static final String API_KEY = "12d444380151cc0e9a6a3a5b94396912";
    // description="请求来源" type="Int" required="true" remark="0:andriod,1:IOS,2:PC"
    public static final String REQUEST_FROM = "requestFrom";
    // body
    // description="用户手机号(登录账号)" type="String" required="true" remark="用户手机号(登录账号)"
    public static final String USER_NO = "userNO";
    // description="密码" type="String" required="true" remark="设置登陆密码"
    public static final String PASSWORD = "password";
    // description="设备编号" type="String" required="true" remark=""
    public static final String DEVICE_ID = "deviceID";
    // description="推送标示" type="String" required="false" remark=""
    public static final String DEVICE_TOKEN = "deviceToken";

    // description="验证码" type="String" required="true" remark="手机接收到的验证码"
    // public static final String VerificationCode = "verificationCode";

}
