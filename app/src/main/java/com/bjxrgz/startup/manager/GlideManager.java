package com.bjxrgz.startup.manager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.bjxrgz.startup.R;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.ImageViewTarget;

import java.io.File;

/**
 * Created by JiangZhiGuo on 2016/7/25.
 * describe Glide工具类
 */
public class GlideManager {

    private static final String FORE_URL = "";

    private static final int ERROR_HEAD = R.mipmap.ic_launcher; // 错误头像
    private static final int ERROR_IMG = R.mipmap.ic_launcher; // 错误图片

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

    /* 获取图片 */
    public static void getDrawable(Context context, String url, ImageViewTarget<GlideDrawable> target) {
        Glide.with(context)
                .load(FORE_URL + url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(target);
    }

    /* 图片加载完毕监听 */
    public interface Listener {
        void complete(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation);
    }

    private static void load(RequestManager requestManager, int error, String url,
                             ImageView view, final Listener listener) {
        DrawableTypeRequest<String> load = requestManager.load(FORE_URL + url);
        if (error != 0) { //设置错误图片
            load.error(error);
        }
//        load.fitCenter();
//        load.centerCrop();
//        load.placeholder(R.mipmap.ic_launcher); //设置占位图,慎用
//        load.animate(); // 在异步加载资源完成时会执行该动画
//        load.override(1, 1); // 设置宽高
        load.thumbnail(0.1f); // 先加载缩略图，再加载完整的
        load.crossFade(100); // 设置淡入淡出效果，默认300ms
//        load.dontAnimate(); // 取消淡入淡出
        load.skipMemoryCache(false); // 内存缓存
        load.diskCacheStrategy(DiskCacheStrategy.SOURCE); // 磁盘缓存
        load.into(new GlideDrawableImageViewTarget(view) {  //.listener() 也是监听异常和准备状态
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                super.onResourceReady(resource, animation);
                if (listener != null) {
                    listener.complete(resource, animation);
                }
            }
        });
    }

    public static void load(Context context, String url, int errorImg, ImageView view, Listener listener) {
        RequestManager with = Glide.with(context);
        load(with, errorImg, url, view, listener);
    }

    public static void load(FragmentActivity activity, String url, int errorImg, ImageView view, Listener listener) {
        RequestManager with = Glide.with(activity);
        load(with, errorImg, url, view, listener);
    }

    public static void load(Fragment fragment, String url, int errorImg, ImageView view, Listener listener) {
        RequestManager with = Glide.with(fragment);
        load(with, errorImg, url, view, listener);
    }

    public static void load(Context context, String url, int errorImg, ImageView view) {
        RequestManager with = Glide.with(context);
        load(with, errorImg, url, view, null);
    }

    public static void load(FragmentActivity activity, String url, int errorImg, ImageView view) {
        RequestManager with = Glide.with(activity);
        load(with, errorImg, url, view, null);
    }

    public static void load(Fragment fragment, String url, int errorImg, ImageView view) {
        RequestManager with = Glide.with(fragment);
        load(with, errorImg, url, view, null);
    }

    public static void load(Context context, String url, ImageView view) {
        RequestManager with = Glide.with(context);
        load(with, 0, url, view, null);
    }

    public static void load(FragmentActivity activity, String url, ImageView view) {
        RequestManager with = Glide.with(activity);
        load(with, 0, url, view, null);
    }

    public static void load(Fragment fragment, String url, ImageView view) {
        RequestManager with = Glide.with(fragment);
        load(with, 0, url, view, null);
    }

    public static void load(Context context, String url, ImageView view, Listener listener) {
        RequestManager with = Glide.with(context);
        load(with, 0, url, view, listener);
    }

    public static void load(FragmentActivity activity, String url, ImageView view, Listener listener) {
        RequestManager with = Glide.with(activity);
        load(with, 0, url, view, listener);
    }

    public static void load(Fragment fragment, String url, ImageView view, Listener listener) {
        RequestManager with = Glide.with(fragment);
        load(with, 0, url, view, listener);
    }
}
