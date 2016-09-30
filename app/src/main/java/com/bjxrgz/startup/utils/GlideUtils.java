package com.bjxrgz.startup.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bjxrgz.startup.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by JiangZhiGuo on 2016/7/25.
 * describe Glide工具类
 */
public class GlideUtils {
    public static final String FORE_URL = "";

    public static final int ERROR_HEAD = R.mipmap.ic_launcher; // 错误头像
    public static final int ERROR_IMG = R.mipmap.ic_launcher; // 错误图片

    public static final int FADE_TIME = 200; // 渐变时间
    public static final DiskCacheStrategy CACHE_TYPE = DiskCacheStrategy.SOURCE; // 缓存类型


    private static void load(RequestManager requestManager, int Error, String url, ImageView view) {
        requestManager.load(FORE_URL + url)
//                .fitCenter()
//                .centerCrop()
//                .placeholder(R.mipmap.ic_launcher) //设置占位图,慎用
                .error(Error) //设置错误图片
                .crossFade(FADE_TIME) //设置淡入淡出效果，默认300ms，可以传参
                .diskCacheStrategy(CACHE_TYPE)
                .into(view);
    }

    private static void loadHead(Context context, String url, ImageView view) {
        RequestManager with = Glide.with(context);
        load(with, ERROR_HEAD, url, view);
    }

    private static void loadHead(Activity activity, String url, ImageView view) {
        RequestManager with = Glide.with(activity);
        load(with, ERROR_HEAD, url, view);
    }

    private static void loadHead(FragmentActivity fragmentActivity, String url, ImageView view) {
        RequestManager with = Glide.with(fragmentActivity);
        load(with, ERROR_HEAD, url, view);
    }

    private static void loadHead(Fragment fragment, String url, ImageView view) {
        RequestManager with = Glide.with(fragment);
        load(with, ERROR_HEAD, url, view);
    }

    private static void loadImg(Context context, String url, ImageView view) {
        RequestManager with = Glide.with(context);
        load(with, ERROR_IMG, url, view);
    }

    private static void loadImg(Activity activity, String url, ImageView view) {
        RequestManager with = Glide.with(activity);
        load(with, ERROR_IMG, url, view);
    }

    private static void loadImg(FragmentActivity fragmentActivity, String url, ImageView view) {
        RequestManager with = Glide.with(fragmentActivity);
        load(with, ERROR_IMG, url, view);
    }

    private static void loadImg(Fragment fragment, String url, ImageView view) {
        RequestManager with = Glide.with(fragment);
        load(with, ERROR_IMG, url, view);
    }

    private static void loadEmpty(Context context, String url, ImageView view) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        RequestManager with = Glide.with(context);
        load(with, ERROR_IMG, url, view);
    }

    private static void loadEmpty(Activity activity, String url, ImageView view) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        RequestManager with = Glide.with(activity);
        load(with, ERROR_IMG, url, view);
    }

    private static void loadEmpty(FragmentActivity fragmentActivity, String url, ImageView view) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        RequestManager with = Glide.with(fragmentActivity);
        load(with, ERROR_IMG, url, view);
    }

    private static void loadEmpty(Fragment fragment, String url, ImageView view) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        RequestManager with = Glide.with(fragment);
        load(with, ERROR_IMG, url, view);
    }

}
