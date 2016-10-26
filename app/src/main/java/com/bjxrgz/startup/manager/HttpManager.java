package com.bjxrgz.startup.manager;

import android.content.Context;
import android.text.TextUtils;

import com.bjxrgz.startup.R;
import com.bjxrgz.startup.base.MyApp;
import com.bjxrgz.startup.utils.ActivityUtils;
import com.bjxrgz.startup.utils.LogUtils;
import com.bjxrgz.startup.utils.WidgetUtils;

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
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by JiangZhiGuo on 2016/10/13.
 * describe Retrofit管理工具类
 */
public class HttpManager {

    private static String BASE_URL; // 当前使用的HOST
    private static OkHttpClient tokenClient;
    private static OkHttpClient emptyClient;

    public static void initAPP() {
        if (MyApp.DEBUG) {
            BASE_URL = APIManager.HOST_DEBUG;
        } else {
            BASE_URL = APIManager.HOST_RELEASE;
        }
        tokenClient = getClient(getTokenHeader());
        emptyClient = getClient(getEmptyHeader());
    }

    /**
     * 获取OKHttp的client
     */
    private static OkHttpClient getClient(Interceptor header) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(header);
        return builder.build();
    }

    public static OkHttpClient getTokenClient() {
        return tokenClient;
    }

    public static OkHttpClient getEmptyClient() {
        return emptyClient;
    }

    /**
     * 构建头信息
     */
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

    private static Interceptor getEmptyHeader() {
        HashMap<String, String> options = new HashMap<>();
        options.put("API_KEY", "330892d73e5f1171be4d8df7550bc2f3");
        options.put("Content-Type", "application/json;charset=utf-8");
        options.put("Accept", "application/json");
        return getHeader(options);
    }

    private static Interceptor getTokenHeader() {
        HashMap<String, String> options = new HashMap<>();
        options.put("API_KEY", "330892d73e5f1171be4d8df7550bc2f3");
        options.put("Content-Type", "application/json;charset=utf-8");
        options.put("Accept", "application/json");
        String userToken = UserManager.getInstance().getUser().getUserToken();
        options.put("userToken", userToken);
        return getHeader(options);
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

    /**
     * 获取Retrofit实例
     */
    private static Retrofit getRetrofit(OkHttpClient client) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(BASE_URL); // host
        builder.addConverterFactory(GsonConverterFactory.create()); //gson构造器
        builder.client(client); // client
        return builder.build();
    }

    /**
     * 获取service 开始请求网络
     */
    public static APIManager getService(OkHttpClient client) {
        Retrofit retrofit = getRetrofit(client);
        return retrofit.create(APIManager.class);
    }

    /**
     * 异步执行
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
                Headers headers = response.headers();

                if (response.isSuccessful()) { // 成功
                    T result = response.body();
                    LogUtils.d(code + "\n" + headers.toString());

                    String json = GsonManager.getInstance().toJson(result);
                    LogUtils.json(json);

                    if (callBack != null) {
                        callBack.onSuccess(result);
                    }
                } else { // 错误
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
                if (callBack != null) {
                    callBack.onFailure();
                }
            }
        });
    }

    public interface CallBack<T> {
        void onSuccess(T result);

        void onFailure();
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
                WidgetUtils.showToast(MyApp.instance, message);
            } else { // 无错误信息
                if (code == 404) {
                    WidgetUtils.showToast(MyApp.instance, R.string.http_error_404);
                } else if (code == 500) {
                    WidgetUtils.showToast(MyApp.instance, R.string.http_error_500);
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
            WidgetUtils.showToast(MyApp.instance, R.string.http_error_connect);

        } else if (aClass.equals(java.net.SocketTimeoutException.class)) { // 超时错误
            WidgetUtils.showToast(MyApp.instance, R.string.http_error_time);

        } else {
            LogUtils.e(ex.toString());
        }
    }

}
