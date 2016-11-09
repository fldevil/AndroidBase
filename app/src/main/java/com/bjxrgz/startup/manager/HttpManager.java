package com.bjxrgz.startup.manager;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;

import com.bjxrgz.startup.R;
import com.bjxrgz.startup.base.MyApp;
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

    private static String BASE_URL; // 当前使用的HOST
    private static APIManager APITokenGson;
    private static APIManager APITokenString;
    private static APIManager APIEmptyGson;
    private static APIManager APIEmptyString;
    private static APIManager APINullGson;

    public static void initAPP() {
        if (MyApp.DEBUG) {
            BASE_URL = APIManager.HOST_DEBUG;
        } else {
            BASE_URL = APIManager.HOST_RELEASE;
        }
    }

    /*构建头信息*/
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

    private static Interceptor getHeaderEmpty() {
        HashMap<String, String> options = new HashMap<>();
        options.put("API_KEY", "330892d73e5f1171be4d8df7550bc2f3");
        options.put("Content-Type", "application/json;charset=utf-8");
        options.put("Accept", "application/json");
        return getHeader(options);
    }

    private static Interceptor getHeaderToken() {
        HashMap<String, String> options = new HashMap<>();
        options.put("API_KEY", "330892d73e5f1171be4d8df7550bc2f3");
        options.put("Content-Type", "application/json;charset=utf-8");
        options.put("Accept", "application/json");
        String userToken = UserManager.getInstance().getUser().getUserToken();
        options.put("userToken", userToken);
        return getHeader(options);
    }

    /*数据解析构造器*/
    private static GsonConverterFactory getGsonFactory() {
        return GsonConverterFactory.create();
    }

    private static ScalarsConverterFactory getStringFactory() {
        return ScalarsConverterFactory.create();
    }

    /*获取OKHttp的client*/
    private static OkHttpClient getClient(Interceptor header) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (header != null) {
            builder.addInterceptor(header);
        }
        return builder.build();
    }

    /*获取Retrofit实例*/
    private static Retrofit getRetrofit(Interceptor header, Converter.Factory factory) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(BASE_URL); // host
        builder.addConverterFactory(factory); //解析构造器
        builder.client(getClient(header)); // client
        return builder.build();
    }

    /*获取service 开始请求网络*/
    private static APIManager getService(Interceptor header, Converter.Factory factory) {
        Retrofit retrofit = getRetrofit(header, factory);
        return retrofit.create(APIManager.class);
    }

    public static APIManager getAPITokenGson() {
        if (APITokenGson == null) {
            synchronized (HttpManager.class) {
                if (APITokenGson == null) {
                    APITokenGson = getService(getHeaderToken(), getGsonFactory());
                }
            }
        }
        return APITokenGson;
    }

    public static APIManager getAPITokenString() {
        if (APITokenString == null) {
            synchronized (HttpManager.class) {
                if (APITokenString == null) {
                    APITokenString = getService(getHeaderToken(), getStringFactory());
                }
            }
        }
        return APITokenString;
    }

    public static APIManager getAPIEmptyGson() {
        if (APIEmptyGson == null) {
            synchronized (HttpManager.class) {
                if (APIEmptyGson == null) {
                    APIEmptyGson = getService(getHeaderEmpty(), getGsonFactory());
                }
            }
        }
        return APIEmptyGson;
    }

    public static APIManager getAPIEmptyString() {
        if (APIEmptyString == null) {
            synchronized (HttpManager.class) {
                if (APIEmptyString == null) {
                    APIEmptyString = getService(getHeaderEmpty(), getStringFactory());
                }
            }
        }
        return APIEmptyString;
    }

    public static APIManager getAPINullGson() {
        if (APINullGson == null) {
            synchronized (HttpManager.class) {
                if (APINullGson == null) {
                    APINullGson = getService(null, getGsonFactory());
                }
            }
        }
        return APINullGson;
    }

    public interface CallBack<T> {
        void onSuccess(T result);

        void onFailure();
    }

    public static <T> void enqueue(Call<T> call, final CallBack<T> callBack) {
        enqueue(null, call, callBack);
    }

    /**
     * @param dialog   等待loading（内部自动show/dismiss）
     * @param call     APIService接口调用
     * @param callBack 回调接口
     * @param <T>      返回实体类
     */
    public static <T> void enqueue(final Dialog dialog, Call<T> call, final CallBack<T> callBack) {
        if (dialog != null) {
            dialog.show();
        }
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                int code = response.code();
                Headers headers = response.headers();
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
                    ResponseBody error = response.errorBody();
                    if (error != null) {
                        try {
                            String message = error.string();
                            LogUtils.e(code + "\n"
                                    + headers.toString() + "\n"
                                    + message);
                            responseError(code, message);
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
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
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
            if (!TextUtils.isEmpty(message)) { // 有错误信息
                ViewManager.showToast(message);
            } else { // 无错误信息
                if (code == 404) {
                    ViewManager.showToast(R.string.http_error_404);
                } else if (code == 500) {
                    ViewManager.showToast(R.string.http_error_500);
                }
            }
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

    /*语言环境*/
    private static Locale getLocale(Context context) {
        return context.getResources().getConfiguration().locale;
    }

    /*是否为英语环境*/
    private static boolean isEN(Context context) {
        String language = getLocale(context).getLanguage();
        return language.endsWith("en");
    }

    /*是否为中文环境*/
    private static boolean isZH(Context context) {
        String language = getLocale(context).getLanguage();
        return language.endsWith("zh");
    }

}
