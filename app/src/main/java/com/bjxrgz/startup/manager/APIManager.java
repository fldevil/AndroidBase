package com.bjxrgz.startup.manager;

import com.bjxrgz.startup.domain.User;
import com.bjxrgz.startup.domain.Version;

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
import retrofit2.http.Url;

/**
 * Created by JiangZhiGuo on 2016/10/14.
 * describe Retrofit接口
 */
public interface APIManager {

    /* BaseURL最好以/结尾 */
    String HOST_DEBUG = ""; // 测试
    String HOST_RELEASE = ""; // 正式

    String CHECK_UPDATE = "http://www.bjxrgz.com:808/bio/api/v1/";
    String DOWNLOAD_APK = "/getapp?file={name}";

    @Multipart // 上传文件
    @GET("demo/{path}")
    Call<List<User>> demo(@Url String url, @Path("path") String path,
                          @Header("key") String key, @HeaderMap Map<String, String> headers,
                          @Query("limit") String limit, @QueryMap Map<String, String> options,
                          @Part MultipartBody.Part file, @PartMap Map<String, RequestBody> params,
                          @Part("fileName") String description, @Part("file") RequestBody imgs,
                          @Body User user, @Body String requestBody);

    @GET(CHECK_UPDATE)
    Call<Version> checkUpdate();

    @GET(DOWNLOAD_APK)
    Call<ResponseBody> downloadAPK(@Path("name") String name);

}
