package com.bjxrgz.start.utils;

import android.content.Context;


import com.bjxrgz.base.utils.ActivityUtils;
import com.bjxrgz.base.utils.ToastUtils;
import com.bjxrgz.base.R;
import com.bjxrgz.start.base.MyApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by JiangZhiGuo on 2016/10/13.
 * describe Retrofit管理工具类
 */
public class HttpUtils {

    private static APIUtils callNullNull;
    private static APIUtils callNullGson;
    private static APIUtils callNullStr;
    private static APIUtils callHeaderGson;
    private static APIUtils callHeaderStr;
    private static APIUtils callTokenGson;
    private static APIUtils callTokenStr;

    /* 没登陆 */
    private static Interceptor getHeader() {
        HashMap<String, String> options = new HashMap<>();
        options.put("API_KEY", "330892d73e5f1171be4d8df7550bc2f3");
        options.put("Content-Type", "application/json;charset=utf-8");
        options.put("Accept", "application/json");
        return getHeader(options);
    }

    /* 已登陆 */
    private static Interceptor getHeaderToken() {
        HashMap<String, String> options = new HashMap<>();
        options.put("API_KEY", "330892d73e5f1171be4d8df7550bc2f3");
        options.put("Content-Type", "application/json;charset=utf-8");
        options.put("Accept", "application/json");
        String userToken = UserUtils.getUser().getUserToken();
        options.put("userToken", userToken);
        return getHeader(options);
    }

    public static APIUtils callTokenGson() {
        if (callTokenGson == null) {
            synchronized (HttpUtils.class) {
                if (callTokenGson == null) {
                    callTokenGson = getService(getHeaderToken(), getGsonFactory());
                }
            }
        }
        return callTokenGson;
    }

    public static APIUtils callTokenStr() {
        if (callTokenStr == null) {
            synchronized (HttpUtils.class) {
                if (callTokenStr == null) {
                    callTokenStr = getService(getHeaderToken(), getStringFactory());
                }
            }
        }
        return callTokenStr;
    }

    public static APIUtils callHeaderGson() {
        if (callHeaderGson == null) {
            synchronized (HttpUtils.class) {
                if (callHeaderGson == null) {
                    callHeaderGson = getService(getHeader(), getGsonFactory());
                }
            }
        }
        return callHeaderGson;
    }

    public static APIUtils callHeaderStr() {
        if (callHeaderStr == null) {
            synchronized (HttpUtils.class) {
                if (callHeaderStr == null) {
                    callHeaderStr = getService(getHeader(), getStringFactory());
                }
            }
        }
        return callHeaderStr;
    }

    public static APIUtils callNullGson() {
        if (callNullGson == null) {
            synchronized (HttpUtils.class) {
                if (callNullGson == null) {
                    callNullGson = getService(null, getGsonFactory());
                }
            }
        }
        return callNullGson;
    }

    public static APIUtils callNullStr() {
        if (callNullStr == null) {
            synchronized (HttpUtils.class) {
                if (callNullStr == null) {
                    callNullStr = getService(null, getStringFactory());
                }
            }
        }
        return callNullStr;
    }

    public static APIUtils callNullNull() {
        if (callNullNull == null) {
            synchronized (HttpUtils.class) {
                if (callNullNull == null) {
                    callNullNull = getService(null, null);
                }
            }
        }
        return callNullNull;
    }

    public interface CallBack<T> {
        void onSuccess(T result);

        void onFailure();
    }

    /**
     * 先调用getCall ,再调用此方法
     *
     * @param call     APIService接口调用
     * @param callBack 回调接口
     * @param <T>      返回实体类
     */
    public static <T> void enqueue(Call<T> call, final CallBack<T> callBack) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                int code = response.code();
                if (code == 200) { // 200成功
                    T result = response.body();
                    String json;
                    if (result instanceof String) {
                        json = result.toString();
                    } else {
                        json = GsonUtils.get().toJson(result);
                    }
                    LogUtils.json(json);
                    if (callBack != null) {
                        callBack.onSuccess(result);
                    }
                } else { // 非200错误
                    Headers headers = response.headers();
                    ResponseBody error = response.errorBody();
                    if (null != error) {
                        try {
                            String headerString = headers.toString();
                            String errorString = error.string(); // 不能执行两次
                            LogUtils.e(code + "\n" + headerString + "\n" + errorString);
                            responseError(code, errorString);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (callBack != null) {
                        callBack.onFailure();
                    }
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                responseError(t);
                if (callBack != null) {
                    callBack.onFailure();
                }
            }
        });
    }

    /**
     * http响应错误处理
     */
    private static void responseError(int code, String errorMessage) {
        switch (code) {
            case 401: // 用户验证失败
//                LoginActivity.goActivity(MyApp.get());
                break;
            case 403: // API Key 不正确 或者没给
                ToastUtils.toast("Key错误");
                break;
            case 404:
                ToastUtils.toast("资源未找到");
                break;
            case 409: // 用户版本过低, 应该禁止用户登录，并提示用户升级
//                UpdateService.goService(MyApp.get());
                break;
            case 410: // 用户被禁用,请求数据的时候得到该 ErrorCode, 应该退出应用
                ActivityUtils.closeActivities();
                break;
            case 417: // 逻辑错误，必须返回错误信息 errorCode: 1001: 用户被锁定
//                HttpError httpError = GsonUtils.get().fromJson(errorMessage, HttpError.class);
//                if (httpError != null) {
//                    String message = httpError.getMessage();
//                    ToastUtils.toast(message);
//                }
                break;
            case 500:
                ToastUtils.toast("服务器异常");
                break;
        }
    }

    /**
     * Http请求错误处理
     */
    private static void responseError(Throwable ex) {
        Class<? extends Throwable> aClass = ex.getClass();
        if (aClass.equals(java.net.ConnectException.class)) { // 网络环境
            ToastUtils.toast(R.string.http_error_connect);

        } else if (aClass.equals(java.net.SocketTimeoutException.class)) { // 超时错误
            ToastUtils.toast(R.string.http_error_time);

        } else {
            LogUtils.e(ex.toString());
        }
    }

    /* 构建头信息 */
    private static Interceptor getHeader(final Map<String, String> options) {
        return new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                for (String key : options.keySet()) {
                    builder.addHeader(key, options.get(key));
                }
                Request request = builder.build();
                return chain.proceed(request);
            }
        };
    }

    /* 数据解析构造器 */
    private static GsonConverterFactory getGsonFactory() {
        return GsonConverterFactory.create();
    }

    private static ScalarsConverterFactory getStringFactory() {
        return ScalarsConverterFactory.create();
    }

    /* 获取OKHttp的client */
    private static OkHttpClient getClient(Interceptor header) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (header != null) {
            builder.addInterceptor(header);
        }
        return builder.build();
    }

    /* 获取Retrofit实例 */
    private static Retrofit getRetrofit(Interceptor header, Converter.Factory factory) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(APIUtils.BASE_URL); // host
        if (factory != null) {
            builder.addConverterFactory(factory); //解析构造器
        }
        builder.client(getClient(header)); // client
        return builder.build();
    }

    /* 获取service 开始请求网络 */
    private static APIUtils getService(Interceptor header, Converter.Factory factory) {
        Retrofit retrofit = getRetrofit(header, factory);
        return retrofit.create(APIUtils.class);
    }

    /* 语言环境 */
    private static Locale getLocale(Context context) {
        return context.getResources().getConfiguration().locale;
    }

    /* 是否为英语环境 */
    private static boolean isEN(Context context) {
        String language = getLocale(context).getLanguage();
        return language.endsWith("en");
    }

    /* 是否为中文环境 */
    private static boolean isZH(Context context) {
        String language = getLocale(context).getLanguage();
        return language.endsWith("zh");
    }

}
