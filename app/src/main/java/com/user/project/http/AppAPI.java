package com.user.project.http;

import com.user.project.domain.User;
import com.user.project.domain.Version;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
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
public interface AppAPI {

    String HOST = "https://api.kljiyou.com/";
//    String HOST = "http://192.168.0.1/"; // 正式
    // BaseURL最好以/结尾
    String BASE_URL = HOST + "api/v1/zh-CN/";
    String IMG_FORE_URL = ""; // 图片前缀

    @Streaming // 下载大文件(请求需要放在子线程中)
    @Multipart // 上传文件
    @GET("demo/{path}")
    Call<List<User>> demo(@Url String url, @Path("path") String path, // {path}
                          @Header("key") String key, @HeaderMap Map<String, String> headers,
                          @Query("limit") String limit, @QueryMap Map<String, String> options,
                          @Part("name") String value, @PartMap Map<String, RequestBody> params,
                          @Body User user, @Body String requestBody);

    @Streaming
    @GET
    Observable<ResponseBody> downloadLargeFile(@Url String url);

    @GET("checkUpdate/{version}")
    Observable<Version> checkUpdate(@Path("version") int version);
}
