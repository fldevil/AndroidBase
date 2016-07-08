package com.bjxrgz.startup.manager;

import android.content.Context;
import android.util.Log;

import com.bjxrgz.startup.base.BaseInterface;
import com.bjxrgz.startup.base.MyApp;
import com.bjxrgz.startup.utils.LogUtils;

import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Fan on 2016/6/22.
 * 请求网络数据封装
 */
public class APIManager {

    /**
     * demo
     */
    public static void demo(final Context context, final BaseInterface.Callback<String> callback) {

        RequestParams params = ParamsManager.getRequestParams("URI地址");
        RequestParams params1 = ParamsManager.getTokenRequestParams("URI地址");

        x.http().get(params, new org.xutils.common.Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                MyApp.processError(ex, context);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.log(Log.ERROR, MyApp.LOG_TAG, cex.toString(), cex);
            }

            @Override
            public void onFinished() {
                callback.onFinished();
            }
        });
    }
}
