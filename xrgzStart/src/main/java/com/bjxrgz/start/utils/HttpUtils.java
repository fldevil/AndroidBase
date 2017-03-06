package com.bjxrgz.start.utils;

import android.content.Context;

import com.bjxrgz.base.R;
import com.bjxrgz.base.utils.StringUtils;
import com.bjxrgz.start.base.MyApp;
import com.bjxrgz.start.domain.HttpError;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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

    private static final HashMap<String, APIUtils> callMap = new HashMap<>();

    static void clearToken() {
        callMap.clear();
    }

    public enum Head {
        empty, common, token
    }

    public enum Factory {
        empty, string, gson
    }

    public static APIUtils call(Head head, Factory factory) {
        String key = head.name() + factory.name();
        APIUtils call = callMap.get(key);
        if (call != null) {
            return call;
        }
        Interceptor hea;
        if (head == Head.common) {
            hea = getHeader("");
        } else if (head == Head.token) {
            String userToken = PreferencesUtils.getUser().getUserToken();
            hea = getHeader(userToken);
        } else {
            hea = null;
        }
        Converter.Factory fac;
        if (factory == Factory.string) {
            fac = getStringFactory();
        } else if (factory == Factory.gson) {
            fac = getGsonFactory();
        } else {
            fac = null;
        }
        APIUtils newCall = getService(hea, fac);
        callMap.put(key, newCall);
        return newCall;
    }

    public interface CallBack<T> {
        void onSuccess(T result);

        void onFailure(int httpCode, String errorMessage);
    }

    /**
     * 先调用call ,再调用此方法
     *
     * @param call     APIService接口调用
     * @param callBack 回调接口
     * @param <T>      返回实体类
     */
    public static <T> void enqueue(Call<T> call, final CallBack<T> callBack) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                // request
                Request request = response.raw().networkResponse().request();
                String method = request.method();
                HttpUrl url = request.url();
                Headers headers = request.headers();
                RequestBody body = request.body();
                // response
                int code = response.code();
                T result = response.body();
                ResponseBody error = response.errorBody();
                String headerString = ""; // 头部信息
                String bodyString = ""; // 请求信息
                String errorString = ""; // 错误信息
                try {
                    if (headers != null) {
                        headerString = headers.toString();
                    }
                    if (body != null) {
                        bodyString = body.toString();
                    }
                    if (error != null) {
                        errorString = error.string(); // error.string()不能执行两次
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 打印请求信息
                if (code == 200) {
                    LogUtils.d(code + " " + method + "\n" + url + "\n"
                            + headerString + "\n" + bodyString);
                } else {
                    LogUtils.e(code + " " + method + "\n" + url + "\n"
                            + headerString + "\n" + bodyString + "\n" + errorString);
                }
                // 响应处理
                if (code == 200) { // 200成功
                    if (result != null) {
                        String json;
                        if (result instanceof String) {
                            json = result.toString();
                        } else {
                            json = GsonUtils.get().toJson(result);
                        }
                        LogUtils.json(json); // 打印返回信息
                    }
                    if (callBack != null) {
                        callBack.onSuccess(result);
                    }
                } else { // 非200错误
                    if (callBack != null) {
                        callBack.onFailure(code, errorString);
                    }
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                Class<? extends Throwable> aClass = t.getClass();
                String errorMessage;
                if (aClass.equals(java.net.ConnectException.class)) { // 网络环境
                    errorMessage = MyApp.get().getString(R.string.http_error_connect);
                } else if (aClass.equals(java.net.SocketTimeoutException.class)) { // 超时错误
                    errorMessage = MyApp.get().getString(R.string.http_error_time);
                } else { // 其他网络错误
                    errorMessage = MyApp.get().getString(R.string.http_error_request);
                    LogUtils.e(t.toString());
                }
                if (callBack != null) {
                    HttpError httpError = new HttpError();
                    httpError.setErrorCode(-1);
                    httpError.setMessage(errorMessage);
                    String errorString = GsonUtils.get().toJson(httpError);
                    callBack.onFailure(417, errorString);
                }
            }
        });
    }

    /* 构建头信息 */
    private static Interceptor getHeader(String token) {
        HashMap<String, String> options = new HashMap<>();
        options.put("API_KEY", "330892d73e5f1171be4d8df7550bc2f3");
        options.put("Content-Type", "application/json;charset=utf-8");
        options.put("Accept", "application/json");
        if (!StringUtils.isEmpty(token)) {
            options.put("Authorization", token);
        }
        return getHeader(options);
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
