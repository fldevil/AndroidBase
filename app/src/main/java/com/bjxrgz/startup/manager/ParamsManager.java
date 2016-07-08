package com.bjxrgz.startup.manager;

import org.xutils.http.RequestParams;

/**
 * Created by Fan on 2016/6/16.
 * RequestParams 帮助类
 */
public class ParamsManager {

    public static final String API_KEY_NAME = "API_KEY";
    public static final String API_KEY = "12d444380151cc0e9a6a3a5b94396912";

    public static final String REQUEST_FROM_NAME = "requestFrom";
    public static final String REQUEST_FROM = "0";

    public static final String USER_TOKEN_NAME = "userToken";

    /**
     * 获取 RequestParams 包括 header API_KEY requestFrom
     *
     * @param uri uri
     * @return RequestParams
     */
    public static RequestParams getRequestParams(String uri) {

        RequestParams params = new RequestParams(uri);
        params.addHeader(API_KEY_NAME, API_KEY);
        params.addHeader(REQUEST_FROM_NAME, REQUEST_FROM);

        return params;
    }

    /**
     * 获取 RequestParams 包括 header API_KEY requestFrom userToken
     *
     * @param uri uri
     * @return RequestParams
     */
    public static RequestParams getTokenRequestParams(String uri) {

//        String userToken = UserManager.getInstance().getUser().getUserToken();

        RequestParams params = new RequestParams(uri);
        params.addHeader(API_KEY_NAME, API_KEY);
        params.addHeader(REQUEST_FROM_NAME, REQUEST_FROM);
//        params.addHeader(USER_TOKEN_NAME, userToken);
//        Log.e("userToken", userToken);
//        params.addHeader(USER_TOKEN_NAME, "2f60c80d6d5fb4e3ef538ef91d0faad4");

        return params;
    }
}
