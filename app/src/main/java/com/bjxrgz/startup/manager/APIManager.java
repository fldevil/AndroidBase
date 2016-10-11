package com.bjxrgz.startup.manager;

import android.content.Context;

import com.bjxrgz.startup.base.Constants;

import org.xutils.http.RequestParams;

/**
 * Created by Fan on 2016/6/22.
 * 请求网络数据封装
 */
public class APIManager {

    /**
     * demo
     */
    public static void demo(Context context, XUtilsManager.Callback callback) {
        RequestParams params = HttpParamsManager.getRequestParams(Constants.API_HOME);
        XUtilsManager.get(context, params, "demo", callback);
    }

}
