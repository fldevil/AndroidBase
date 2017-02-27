package com.bjxrgz.startup.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.bjxrgz.startup.utils.ConvertUtils;
import com.bjxrgz.startup.utils.StringUtils;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.io.File;

/**
 * Created by JiangZhiGuo on 2016/7/25.
 * describe Glide工具类
 */
public class GlideUtils {

    /* 清除内存缓存 */
    public static void clearMemory(Context context) {
        Glide.get(context).clearMemory();
    }

    /* 清除磁盘缓存 */
    public static void clearCache(Context context) {
        Glide.get(context.getApplicationContext()).clearDiskCache();
    }

    /* 获取缓存目录 */
    public static File getCacheFile(Context context) {
        return new File(context.getCacheDir(), DiskCache.Factory.DEFAULT_DISK_CACHE_DIR);
    }

    /* 图片加载完毕监听 */
    public interface CompleteListener {
        void complete(Bitmap result);
    }

    private static void load(RequestManager requestManager,
                             String foreUrl, String url, int errorRes,
                             ImageView view, final CompleteListener completeListener) {
        String imgUrl;
        if (StringUtils.isEmpty(foreUrl))
            imgUrl = url;
        else
            imgUrl = foreUrl + url;
        DrawableTypeRequest<String> load = requestManager.load(imgUrl);
        if (errorRes != 0) //设置错误图片
            load.error(errorRes);
        load.thumbnail(0.1f); // 先加载缩略图，再加载完整的
        load.crossFade(100); // 设置淡入淡出效果，默认300ms(load.dontAnimate();取消效果)
        load.skipMemoryCache(false); // 不跳过内存缓存
        load.diskCacheStrategy(DiskCacheStrategy.SOURCE); // 磁盘缓存
        load.into(new GlideDrawableImageViewTarget(view) {  //.listener() 也是监听异常和准备状态
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                super.onResourceReady(resource, animation);
            }

            @Override
            protected void setResource(GlideDrawable resource) {
                super.setResource(resource);
                if (completeListener != null) {
                    completeListener.complete(ConvertUtils.drawable2Bitmap(resource));
                }
            }
        });
    }

    /* 前置url,错误图片,回调监听 */
    public static void load(Context context, String foreUrl, String url, int errorRes,
                            ImageView view, CompleteListener completeListener) {
        RequestManager with = Glide.with(context);
        load(with, foreUrl, url, errorRes, view, completeListener);
    }

    public static void load(FragmentActivity activity, String foreUrl, String url, int errorRes,
                            ImageView view, CompleteListener completeListener) {
        RequestManager with = Glide.with(activity);
        load(with, foreUrl, url, errorRes, view, completeListener);
    }

    public static void load(Fragment fragment, String foreUrl, String url, int errorRes,
                            ImageView view, CompleteListener completeListener) {
        RequestManager with = Glide.with(fragment);
        load(with, foreUrl, url, errorRes, view, completeListener);
    }

    /* 前置url */
    public static void load(Context context, String foreUrl, String url, ImageView view) {
        load(context, foreUrl, url, 0, view, null);
    }

    public static void load(FragmentActivity activity, String foreUrl, String url, ImageView view) {
        load(activity, foreUrl, url, 0, view, null);
    }

    public static void load(Fragment fragment, String foreUrl, String url, ImageView view) {
        load(fragment, foreUrl, url, 0, view, null);
    }

    /* 错误图片 */
    public static void load(Context context, String url, int errorRes, ImageView view) {
        load(context, "", url, errorRes, view, null);
    }

    public static void load(FragmentActivity activity, String url, int errorRes, ImageView view) {
        load(activity, "", url, errorRes, view, null);
    }

    public static void load(Fragment fragment, String url, int errorRes, ImageView view) {
        load(fragment, "", url, errorRes, view, null);
    }

    /* 回调监听 */
    public static void load(Context context, String url, ImageView view,
                            CompleteListener completeListener) {
        RequestManager with = Glide.with(context);
        load(with, "", url, 0, view, completeListener);
    }

    public static void load(FragmentActivity activity, String url, ImageView view,
                            CompleteListener completeListener) {
        RequestManager with = Glide.with(activity);
        load(with, "", url, 0, view, completeListener);
    }

    public static void load(Fragment fragment, String url, ImageView view,
                            CompleteListener completeListener) {
        RequestManager with = Glide.with(fragment);
        load(with, "", url, 0, view, completeListener);
    }

    /* 加载图片 */
    public static void load(Context context, String url, ImageView view) {
        load(context, url, view, null);
    }

    public static void load(FragmentActivity activity, String url, ImageView view) {
        load(activity, url, view, null);
    }

    public static void load(Fragment fragment, String url, ImageView view) {
        load(fragment, url, view, null);
    }
}
