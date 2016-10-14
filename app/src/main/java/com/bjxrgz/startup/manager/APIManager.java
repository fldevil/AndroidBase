package com.bjxrgz.startup.manager;

import com.bjxrgz.startup.domain.User;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Jiang on 2016/10/14.
 * Retrofit接口
 */
public interface APIManager {

    @GET("home")
    Call<ResponseBody> getUser(); // 从ResponseBody里获取string

    @GET("home")
    Call<User> getHome2(); // 直接返回bean

    @GET("home?limit=100")
    Call<User> getUser3(); // 可直接写死参数

    @GET("home")
    Call<User> getUser4(@Query("limit") String limit); // 也可传参控制参数

    @GET("home")
    Call<User> getUser5(@QueryMap Map<String, String> options); // 也可传参控制多参数

    @POST("home/{list}/{limit}/start")
    Call<List<User>> getUser6(@Path("list") String user, @Path("limit") String limit); //URL中的占位填充

    @GET("users/{user}/list")
    Call<List<User>> getUser7(@Path("user") String user, @Query("limit") String limit);

    @POST("users/new")
    Call<List<User>> postUser(@Body User user); // body

    @GET("user")
    Call<User> getUser8(@Header("Authorization") String authorization, @Header("API_KET") String API_KET); // 不用这个，每次都要写 太麻烦

}
