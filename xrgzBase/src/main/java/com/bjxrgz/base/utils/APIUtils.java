package com.bjxrgz.base.utils;

import com.bjxrgz.base.domain.User;
import com.bjxrgz.base.domain.Version;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by JiangZhiGuo on 2016/10/14.
 * describe Retrofit接口
 */
public interface APIUtils {

    /* BaseURL最好以/结尾 */
//    String HOST = "http://192.168.0.8:8080/kljiyou-web/"; // 测试
    String HOST = "http://www.bjxrgz.com:808/kljiyou/"; // 正式
    String BASE_URL = HOST + "api/v1/zh-CN/";

    @Streaming // 下载大文件(请求需要放在子线程中)
    @Multipart // 上传文件
    @GET("demo/{path}")
    Call<List<User>> demo(@Url String url, @Path("path") String path, // {path}
                          @Header("key") String key, @HeaderMap Map<String, String> headers,
                          @Query("limit") String limit, @QueryMap Map<String, String> options,
                          @Part MultipartBody.Part file, @PartMap Map<String, RequestBody> params,
                          @Part("fileName") String description, @Part("file") RequestBody imgs,
                          @Body User user, @Body String requestBody);

    @Streaming
    @GET
    Call<ResponseBody> downloadLargeFile(@Url String url);

    @GET("checkUpdate/{version}")
    Call<Version> checkUpdate(@Path("version") int version);

}