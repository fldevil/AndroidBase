package com.user.project.http;

import android.content.Context;

import com.bjxrgz.base.utils.LogUtil;
import com.bjxrgz.base.utils.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

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
 * Created by JiangZhiGuo on 2016/10/13.
 * describe Retrofit管理工具类
 */
public class RetrofitFactory {

    /* 返回数据构造器 */
    public enum Factory {
        empty, string, gson
    }

    private static RetrofitFactory instance;

    private RetrofitFactory() {
    }

    public static RetrofitFactory getInstance() {
        if (instance == null) {
            synchronized (RetrofitFactory.class) {
                if (instance == null) {
                    instance = new RetrofitFactory();
                }
            }
        }
        return instance;
    }

    /**
     * header拦截器
     */
    private Interceptor getHeaderInterceptor() {
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
    private Interceptor getLogInterceptor() {
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

    /**
     * 获取OkHttp
     */
    private OkHttpClient getClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(getHeaderInterceptor());
        builder.addInterceptor(getLogInterceptor());
        return builder.build();
    }

    /**
     * 获取Retrofit
     */
    private Retrofit getRetrofit(Factory factory, String baseUrl) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(baseUrl); // host
        if (factory.ordinal() == Factory.empty.ordinal()) {

        } else if (factory.ordinal() == Factory.gson.ordinal()) {
            builder.addConverterFactory(getGsonFactory());
        } else if (factory.ordinal() == Factory.string.ordinal()) {
            builder.addConverterFactory(getStringFactory());
        }
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        builder.client(getClient()); // client
        return builder.build();
    }

    /* 数据解析构造器 */
    private GsonConverterFactory getGsonFactory() {
        return GsonConverterFactory.create();
    }

    private ScalarsConverterFactory getStringFactory() {
        return ScalarsConverterFactory.create();
    }

    /* 获取service 开始请求网络 */
    public <T> T getService(Factory factory, String baseUrl, Class<T> tClass) {
        Retrofit retrofit = getRetrofit(factory, baseUrl);
        return retrofit.create(tClass);
    }

    /* 语言环境 */
    private Locale getLocale(Context context) {
        return context.getResources().getConfiguration().locale;
    }

    /* 是否为英语环境 */
    private boolean isEN(Context context) {
        String language = getLocale(context).getLanguage();
        return language.endsWith("en");
    }

    /* 是否为中文环境 */
    private boolean isZH(Context context) {
        String language = getLocale(context).getLanguage();
        return language.endsWith("zh");
    }

    public RequestBody string2PartBody(String body) {
        return RequestBody.create(MediaType.parse("text/plain"), body);
    }

    public String file2PartKey(File file) {
        return "file\";filename=\"" + file.getName();
    }

    public RequestBody img2PartBody(File file) {
        return RequestBody.create(MediaType.parse("image/jpeg"), file);
    }

}
