package com.bjxrgz.startup.manager;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.bjxrgz.startup.R;
import com.bjxrgz.startup.base.MyApp;
import com.bjxrgz.startup.utils.GsonUtils;
import com.bjxrgz.startup.utils.LogUtils;
import com.bjxrgz.startup.utils.WidgetUtils;

import org.json.JSONObject;
import org.xutils.BuildConfig;
import org.xutils.ex.HttpException;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * Created by JiangZhiGuo on 2016/8/5.
 * describe xUtils管理类
 */
public class XUtilsManager {

    /**
     * xUtils在App的初始化
     */
    public static void initApp(Application application, boolean isLog) {
        x.Ext.init(application); // xUtils 初始化
        if (isLog) {
            x.Ext.setDebug(BuildConfig.DEBUG); // xUtils log
        }
    }

    /**
     * xUtils在activity的初始化
     */
    public static void initBaseActivity(Activity activity) {
        x.view().inject(activity);
    }

    /**
     * xUtils在fragment的初始化
     */
    public static void initBaseFragment(Fragment fragment, View view) {
        x.view().inject(fragment, view);
    }

    /**
     * Http请求错误处理
     */
    public static void httpError(Throwable ex, final Context context) {
        try {
            if (ex.getClass().equals(java.net.ConnectException.class)) { // 网络环境
                WidgetUtils.showToast(context, R.string.http_error_connect);

            } else if (ex.getClass().equals(org.xutils.ex.HttpException.class)) {
                org.xutils.ex.HttpException hre = ((org.xutils.ex.HttpException) ex);
                int code = hre.getCode(); // 返回码
                LogUtils.log(Log.ERROR, MyApp.LOG_TAG, "HttpCode--->" + code);

                if (code == 404) {
                    WidgetUtils.showToast(context, R.string.http_error_404);

                } else if (code == 500) {
                    WidgetUtils.showToast(context, R.string.http_error_500);

                } else if (code == 401 || code == 409 || code == 417) { // 跳转登录
                    JSONObject object = new JSONObject(((HttpException) ex).getResult());
                    String message = (String) object.get("message"); // 错误信息
                    LogUtils.log(Log.ERROR, MyApp.LOG_TAG, "HttpMessage--->" + object.toString());

//                    LoginActivity.goActivity(context, message); // 跳转登录界面

                } else if (code == 410) { // 退出app
                    MyApp.instance.closeActivities();

                } else { // 弹出提示
                    JSONObject object = new JSONObject(((HttpException) ex).getResult());
                    String message = (String) object.get("message"); // 错误信息
                    LogUtils.log(Log.ERROR, MyApp.LOG_TAG, "HttpMessage--->" + object.toString());
                    WidgetUtils.showToast(context, message);
                }
            } else if (ex.getClass().equals(java.net.SocketTimeoutException.class)) {
                WidgetUtils.showToast(context, R.string.http_error_time);

            } else {
                LogUtils.log(Log.ERROR, MyApp.LOG_TAG, "httpError--->" + ex.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * XUtilsManager里的接口定义
     */
    interface Callback<T> {

        void onSuccess(T result);

        void onFinished();
    }

    /**
     * post下载
     */
    public static void getFile(final Context context, String url,
                               String savePath, final Callback<File> callback) {
        RequestParams params = new RequestParams(url);
        params.setAutoResume(true); // 断点续传
        params.setAutoRename(true); // 根据头信息自动命名文件
        params.setSaveFilePath(savePath);
        x.http().get(params, new org.xutils.common.Callback.CommonCallback<File>() {
            @Override
            public void onSuccess(File file) {
                if (callback != null) {
                    callback.onSuccess(file);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                httpError(ex, context);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.log(Log.ERROR, MyApp.LOG_TAG,
                        "getFile(onCancelled)--->" + cex.toString(), cex);
            }

            @Override
            public void onFinished() {
                if (callback != null) {
                    callback.onFinished();
                }
            }
        });
    }

    /**
     * get请求
     */
    public static void get(final Context context, RequestParams params,
                           final String log, final Callback<String> callback) {
        x.http().get(params, new org.xutils.common.Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.log(Log.DEBUG, MyApp.LOG_TAG, log + "(success)--->" + result);
                if (callback != null) {
                    callback.onSuccess(result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.log(Log.ERROR, MyApp.LOG_TAG, log + "(onError)--->");
                httpError(ex, context);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.log(Log.ERROR, MyApp.LOG_TAG,
                        log + "(onCancelled)--->" + cex.toString(), cex);
            }

            @Override
            public void onFinished() {
                if (callback != null) {
                    callback.onFinished();
                }
            }
        });
    }

    /**
     * post请求(无参)
     */
    public static void post(final Context context, RequestParams params,
                            final String log, final Callback<String> callback) {
        x.http().post(params, new org.xutils.common.Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.log(Log.DEBUG, MyApp.LOG_TAG, log + "(success)--->" + result);
                if (callback != null) {
                    callback.onSuccess(result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.log(Log.ERROR, MyApp.LOG_TAG, log + "(onError)--->");
                httpError(ex, context);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.log(Log.ERROR, MyApp.LOG_TAG,
                        log + "(onCancelled)--->" + cex.toString(), cex);
            }

            @Override
            public void onFinished() {
                if (callback != null) {
                    callback.onFinished();
                }
            }
        });
    }

    /**
     * post请求 json
     */
    public static void post(final Context context, RequestParams params,
                            String json, final String log, final Callback<String> callback) {
        LogUtils.log(Log.DEBUG, MyApp.LOG_TAG, log + "(params)--->" + json);
        params.setBodyContent(json);
        params.setAsJsonContent(true);
        x.http().post(params, new org.xutils.common.Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.log(Log.DEBUG, MyApp.LOG_TAG, log + "(success)--->" + result);
                if (callback != null) {
                    callback.onSuccess(result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.log(Log.ERROR, MyApp.LOG_TAG, log + "(onError)--->");
                httpError(ex, context);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.log(Log.ERROR, MyApp.LOG_TAG,
                        log + "(onCancelled)--->" + cex.toString(), cex);
            }

            @Override
            public void onFinished() {
                if (callback != null) {
                    callback.onFinished();
                }
            }
        });
    }

    /**
     * post请求 object
     */
    public static void post(final Context context, RequestParams params, Object domain,
                            final String log, final Callback<String> callback) {
        String json = GsonUtils.getInstance().toJson(domain);
        LogUtils.log(Log.DEBUG, MyApp.LOG_TAG, log + "(params)--->" + json);
        params.setBodyContent(json);
        params.setAsJsonContent(true);
        x.http().post(params, new org.xutils.common.Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.log(Log.DEBUG, MyApp.LOG_TAG, log + "(success)--->" + result);
                if (callback != null) {
                    callback.onSuccess(result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.log(Log.ERROR, MyApp.LOG_TAG, log + "(onError)--->");
                httpError(ex, context);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.log(Log.ERROR, MyApp.LOG_TAG,
                        log + "(onCancelled)--->" + cex.toString(), cex);
            }

            @Override
            public void onFinished() {
                if (callback != null) {
                    callback.onFinished();
                }
            }
        });
    }

    /**
     * put请求 (无参)
     */
    public static void put(final Context context, RequestParams params,
                           final String log, final Callback<String> callback) {
        x.http().request(HttpMethod.PUT, params, new org.xutils.common.Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.log(Log.DEBUG, MyApp.LOG_TAG, log + "(success)--->" + result);
                if (callback != null) {
                    callback.onSuccess(result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.log(Log.ERROR, MyApp.LOG_TAG, log + "(onError)--->");
                httpError(ex, context);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.log(Log.ERROR, MyApp.LOG_TAG,
                        log + "(onCancelled)--->" + cex.toString(), cex);
            }

            @Override
            public void onFinished() {
                if (callback != null) {
                    callback.onFinished();
                }
            }
        });
    }

    /**
     * put请求 json
     */
    public static void put(final Context context, RequestParams params, String json,
                           final String log, final Callback<String> callback) {
        LogUtils.log(Log.DEBUG, MyApp.LOG_TAG, log + "(params)--->" + json);
        params.setBodyContent(json);
        params.setAsJsonContent(true);

        x.http().request(HttpMethod.PUT, params, new org.xutils.common.Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.log(Log.DEBUG, MyApp.LOG_TAG, log + "(success)--->" + result);
                if (callback != null) {
                    callback.onSuccess(result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.log(Log.ERROR, MyApp.LOG_TAG, log + "(onError)--->");
                httpError(ex, context);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.log(Log.ERROR, MyApp.LOG_TAG,
                        log + "(onCancelled)--->" + cex.toString(), cex);
            }

            @Override
            public void onFinished() {
                if (callback != null) {
                    callback.onFinished();
                }
            }
        });
    }

    /**
     * put请求 object
     */
    public static void put(final Context context, RequestParams params, Object domain,
                           final String log, final Callback<String> callback) {
        String json = GsonUtils.getInstance().toJson(domain);
        LogUtils.log(Log.DEBUG, MyApp.LOG_TAG, log + "(params)--->" + json);
        params.setBodyContent(json);
        params.setAsJsonContent(true);

        x.http().request(HttpMethod.PUT, params, new org.xutils.common.Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.log(Log.DEBUG, MyApp.LOG_TAG, log + "(success)--->" + result);
                if (callback != null) {
                    callback.onSuccess(result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.log(Log.ERROR, MyApp.LOG_TAG, log + "(onError)--->");
                httpError(ex, context);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.log(Log.ERROR, MyApp.LOG_TAG,
                        log + "(onCancelled)--->" + cex.toString(), cex);
            }

            @Override
            public void onFinished() {
                if (callback != null) {
                    callback.onFinished();
                }
            }
        });
    }

    /**
     * delete请求
     */
    public static void delete(final Context context, RequestParams params,
                              final String log, final Callback<String> callback) {
        x.http().request(HttpMethod.DELETE, params, new org.xutils.common.Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.log(Log.DEBUG, MyApp.LOG_TAG, log + "(success)--->" + result);
                if (callback != null) {
                    callback.onSuccess(result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.log(Log.ERROR, MyApp.LOG_TAG, log + "(onError)--->");
                httpError(ex, context);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.log(Log.ERROR, MyApp.LOG_TAG,
                        log + "(onCancelled)--->" + cex.toString(), cex);
            }

            @Override
            public void onFinished() {
                if (callback != null) {
                    callback.onFinished();
                }
            }
        });
    }

}
