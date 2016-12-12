package com.bjxrgz.startup.manager;

import android.content.Context;

import com.bjxrgz.startup.R;
import com.bjxrgz.startup.utils.ActivityUtils;
import com.bjxrgz.startup.utils.LogUtils;

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
public class HttpManager {

    private static String HOST;

    private static APIManager callGson;
    private static APIManager callGsonEmpty;
    private static APIManager callGsonToken;
    private static APIManager callStr;
    private static APIManager callStrEmpty;
    private static APIManager callStrToken;

    public static void initApp(boolean isDebug) {
        if (isDebug) {
            HOST = APIManager.HOST_DEBUG;
        } else {
            HOST = APIManager.HOST_RELEASE;
        }
    }

    /* 没登陆 */
    private static Interceptor getHeaderEmpty() {
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
        String userToken = UserManager.getUser().getUserToken();
        options.put("userToken", userToken);
        return getHeader(options);
    }

    public static APIManager getCallGsonToken() {
        if (callGsonToken == null) {
            synchronized (HttpManager.class) {
                if (callGsonToken == null) {
                    callGsonToken = getService(getHeaderToken(), getGsonFactory());
                }
            }
        }
        return callGsonToken;
    }

    public static APIManager getCallStrToken() {
        if (callStrToken == null) {
            synchronized (HttpManager.class) {
                if (callStrToken == null) {
                    callStrToken = getService(getHeaderToken(), getStringFactory());
                }
            }
        }
        return callStrToken;
    }

    public static APIManager getCallGsonEmpty() {
        if (callGsonEmpty == null) {
            synchronized (HttpManager.class) {
                if (callGsonEmpty == null) {
                    callGsonEmpty = getService(getHeaderEmpty(), getGsonFactory());
                }
            }
        }
        return callGsonEmpty;
    }

    public static APIManager getCallStrEmpty() {
        if (callStrEmpty == null) {
            synchronized (HttpManager.class) {
                if (callStrEmpty == null) {
                    callStrEmpty = getService(getHeaderEmpty(), getStringFactory());
                }
            }
        }
        return callStrEmpty;
    }

    public static APIManager getCallGson() {
        if (callGson == null) {
            synchronized (HttpManager.class) {
                if (callGson == null) {
                    callGson = getService(null, getGsonFactory());
                }
            }
        }
        return callGson;
    }

    public static APIManager getCallStr() {
        if (callStr == null) {
            synchronized (HttpManager.class) {
                if (callStr == null) {
                    callStr = getService(null, getStringFactory());
                }
            }
        }
        return callStr;
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
                        json = GsonManager.getInstance().toJson(result);
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
                            LogUtils.e(code + "\n" + headers.toString() + "\n" + error.string());
                            responseError(code, error.string());
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
        String message = "";
        try {
            JSONObject object = new JSONObject(errorMessage);
            message = (String) object.get("message"); // 错误信息
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (code == 401 || code == 409 || code == 417) { // 跳转登录
//            LoginActivity.goActivity(context, message); // 跳转登录界面

        } else if (code == 410) { // 退出app
            ActivityUtils.closeActivities();

        } else { // 弹出提示
            ViewManager.showToast(message);
        }
    }

    /**
     * Http请求错误处理
     */
    private static void responseError(Throwable ex) {
        Class<? extends Throwable> aClass = ex.getClass();
        if (aClass.equals(java.net.ConnectException.class)) { // 网络环境
            ViewManager.showToast(R.string.http_error_connect);

        } else if (aClass.equals(java.net.SocketTimeoutException.class)) { // 超时错误
            ViewManager.showToast(R.string.http_error_time);

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
        builder.baseUrl(HOST); // host
        builder.addConverterFactory(factory); //解析构造器
        builder.client(getClient(header)); // client
        return builder.build();
    }

    /* 获取service 开始请求网络 */
    private static APIManager getService(Interceptor header, Converter.Factory factory) {
        Retrofit retrofit = getRetrofit(header, factory);
        return retrofit.create(APIManager.class);
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
