package com.bjxrgz.base.utils;

import android.content.Context;

import com.bjxrgz.base.R;
import com.bjxrgz.base.base.BaseApp;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
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

    /* api集合（不同的header参数） */
    private static final HashMap<String, APIUtils> callMap = new HashMap<>();

    /* 清除缓存的api集合 */
    static void clearToken() {
        callMap.clear();
    }

    /* Authorization参数（自定义） */
    public enum Head {
        empty, common, token
    }

    /* 返回数据构造器 */
    public enum Factory {
        empty, string, gson
    }

    /* 获取api */
    public static APIUtils call(Head head, Factory factory) {
        String key = head.name() + factory.name();
        APIUtils call = callMap.get(key);
        if (call != null) return call;
        Interceptor hea;
        if (head == Head.common) {
            hea = getHeader("");
        } else if (head == Head.token) {
            String userToken = SPUtils.getUser().getUserToken();
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

    /* http请求回调 */
    public interface CallBack<T> {
        void onSuccess(T result);

        void onFailure(int httpCode, String errorMessage);
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
                T result = response.body();
                // 响应处理
                if (callBack == null) return;
                if (code == 200) { // 200成功
                    callBack.onSuccess(result);
                } else { // 响应非200
                    String errorString = "";
                    try {
                        ResponseBody error = response.errorBody();
                        errorString = error.string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        callBack.onFailure(code, errorString);
                    }
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                Class<? extends Throwable> aClass = t.getClass();
                String errorMessage;
                if (aClass.equals(java.net.ConnectException.class)) { // 网络环境
                    errorMessage = BaseApp.get().getString(R.string.http_error_connect);
                } else if (aClass.equals(java.net.SocketTimeoutException.class)) { // 超时错误
                    errorMessage = BaseApp.get().getString(R.string.http_error_time);
                } else { // 其他网络错误
                    errorMessage = BaseApp.get().getString(R.string.http_error_request);
                    LogUtils.e(t.toString());
                }
                ToastUtils.get().show(errorMessage);
            }
        });
    }

    /* 构建头Map */
    public static HashMap<String, String> getHeaderMap(String token) {
        HashMap<String, String> options = new HashMap<>();
        options.put("API_KEY", "");
        options.put("Content-Type", "application/json;charset=utf-8");
        options.put("Accept", "application/json");
        if (!StringUtils.isEmpty(token)) {
            options.put("Authorization", token);
        }
        return options;
    }

    /* 构建头client */
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

    /* 构建头信息 */
    private static Interceptor getHeader(String token) {
        HashMap<String, String> options = getHeaderMap(token);
        return getHeader(options);
    }

    /* 获取OKHttp的client */
    private static OkHttpClient getClient(Interceptor header) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(getLogInterceptor());
        if (header != null) {
            builder.addInterceptor(header);
        }
        return builder.build();
    }

    /* 获取日志拦截器 */
    private static Interceptor getLogInterceptor() {
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
                new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        if (StringUtils.isEmpty(message)) return;
                        LogUtils.d(message);
                    }
                });
        // BODY 请求/响应行 + 头 + 体
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }

    /* 获取Retrofit实例 */
    private static Retrofit getRetrofit(Interceptor header, Converter.Factory factory) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(APIUtils.BASE_URL); // host
        if (factory != null) {
            builder.addConverterFactory(factory); // 解析构造器
        }
        builder.client(getClient(header)); // client
        return builder.build();
    }

    /* 数据解析构造器 */
    private static GsonConverterFactory getGsonFactory() {
        return GsonConverterFactory.create();
    }

    private static ScalarsConverterFactory getStringFactory() {
        return ScalarsConverterFactory.create();
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

    public static RequestBody string2PartBody(String body) {
        return RequestBody.create(MediaType.parse("text/plain"), body);
    }

    public static String file2PartKey(File file) {
        return "file\";filename=\"" + file.getName();
    }

    public static RequestBody img2PartBody(File file) {
        return RequestBody.create(MediaType.parse("image/jpeg"), file);
    }

}
