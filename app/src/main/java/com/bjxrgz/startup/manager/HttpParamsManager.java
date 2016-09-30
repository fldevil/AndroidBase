package com.bjxrgz.startup.manager;

import android.util.Log;

import com.bjxrgz.startup.base.MyApp;
import com.bjxrgz.startup.utils.LogUtils;

import org.xutils.http.RequestParams;

/**
 * Created by Fan on 2016/6/16.
 * RequestParams 帮助类
 */
public class HttpParamsManager {

    public static final String API_KEY_NAME = "API_KEY";
    public static final String API_KEY = "330892d73e5f1171be4d8df7550bc2f3";

    public static final String USER_TOKEN_NAME = "Authorization";

    public static final String REQUEST_ACCEPT = "Accept";
    public static final String REQUEST_JSON = "application/json";

    /**
     * 获取 RequestParams 包括 header API_KEY requestFrom
     */
    public static RequestParams getRequestParams(String uri) {
        RequestParams params = new RequestParams(uri);
        params.addHeader(API_KEY_NAME, API_KEY);
        params.addHeader(REQUEST_ACCEPT, REQUEST_JSON);

        return params;
    }

    /**
     * 获取 RequestParams 包括 header API_KEY requestFrom userToken
     */
    public static RequestParams getTokenRequestParams(String uri) {
        String userToken = UserManager.getInstance().getUser().getUserToken();
        LogUtils.log(Log.DEBUG, MyApp.LOG_TAG, "userToken--->" + userToken);

        RequestParams params = new RequestParams(uri);
        params.addHeader(API_KEY_NAME, API_KEY);
        params.addHeader(REQUEST_ACCEPT, REQUEST_JSON);
        params.addHeader(USER_TOKEN_NAME, userToken);

        return params;
    }
}
