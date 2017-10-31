package com.user.project.http;

import com.bjxrgz.base.utils.LogUtil;
import com.bjxrgz.base.utils.StringUtil;

import java.io.File;
import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Retrofit管理工具类
 *
 * @author Fan
 */
public class RetrofitManager {

    /* 返回数据构造器 */
    private enum Factory {
        empty, string, gson
    }

    public static <T> T createGsonService(String baseUrl, Class<T> tClass) {
        Retrofit retrofit = getRetrofit(Factory.gson, baseUrl);
        return retrofit.create(tClass);
    }

    public static <T> T createStringService(String baseUrl, Class<T> tClass) {
        Retrofit retrofit = getRetrofit(Factory.string, baseUrl);
        return retrofit.create(tClass);
    }

    /**
     * 获取Retrofit
     */
    private static Retrofit getRetrofit(Factory factory, String baseUrl) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(baseUrl); // host
        switch (factory) {
            case empty:

                break;
            case string:
                builder.addConverterFactory(getStringFactory());
                break;
            case gson:
                builder.addConverterFactory(getGsonFactory());
                break;
        }
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        builder.client(getClient()); // client
        return builder.build();
    }

    /**
     * 获取OkHttp
     */
    private static OkHttpClient getClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(getHeaderInterceptor());
        builder.addInterceptor(getLogInterceptor());
        return builder.build();
    }

    /**
     * header拦截器
     */
    private static Interceptor getHeaderInterceptor() {
        return new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                builder.addHeader("API_KEY", "8557a557dbb0e66ce051106dd2fefe72");
                builder.addHeader("Content-Type", "application/json;charset=utf-8");
                builder.addHeader("Accept", "application/json");
                Request request = builder.build();
                return chain.proceed(request);
            }
        };
    }

    /**
     * 日志拦截器
     */
    private static Interceptor getLogInterceptor() {
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
                new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        String log = message.trim();
                        if (StringUtil.isEmpty(log)) return;
                        if (log.startsWith("{") || log.startsWith("[")) {
                            LogUtil.json(log);
                        } else {
                            LogUtil.d(log);
                        }
                    }
                });
        // BODY 请求/响应行 + 头 + 体
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }

    /* 数据解析构造器 */
    private static GsonConverterFactory getGsonFactory() {
        return GsonConverterFactory.create();
    }

    private static ScalarsConverterFactory getStringFactory() {
        return ScalarsConverterFactory.create();
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
