package com.bjxrgz.startup.manager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.bjxrgz.startup.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
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

    private static final int FADE_TIME = 200; // 渐变时间
    private static final DiskCacheStrategy CACHE_TYPE = DiskCacheStrategy.SOURCE; // 缓存类型

    /**
     * 清除内存缓存
     */
    public static void clearMemory(Context context) {
        Glide.get(context).clearMemory();
    }

    /**
     * 清除磁盘缓存
     */
    public static void clearCache(Context context) {
        Glide.get(context.getApplicationContext()).clearDiskCache();
    }

    /**
     * 获取缓存目录
     */
    public static File getCacheFile(Context context) {
        return new File(context.getCacheDir(), DiskCache.Factory.DEFAULT_DISK_CACHE_DIR);
    }

    /**
     * 获取图片
     */
    private static void getDrawable(Context context, String url, ImageViewTarget<GlideDrawable> target) {
        Glide.with(context)
                .load(FORE_URL + url)
                .diskCacheStrategy(CACHE_TYPE)
                .into(target);
    }

    private static void load(RequestManager requestManager, int Error, String url,
                             ImageView view) {
        requestManager.load(FORE_URL + url)
//                .fitCenter()
//                .centerCrop()
//                .placeholder(R.mipmap.ic_launcher) //设置占位图,慎用
//                .thumbnail(0.3f) // 先加载缩略图，再加载完整的
//                .override(1, 1) // 设置宽高
//                .listener() // 监听异常和准备状态
//                .animate() // 在异步加载资源完成时会执行该动画
//                .priority() // 指定加载的优先级
                .error(Error) //设置错误图片
                .crossFade(FADE_TIME) //设置淡入淡出效果，默认300ms，可以传参
                .diskCacheStrategy(CACHE_TYPE)
                .into(view);
    }

    /**
     * 加载头像
     */
    private static void loadHead(Context context, String url, ImageView view) {
        RequestManager with = Glide.with(context);
        load(with, ERROR_HEAD, url, view);
    }

    private static void loadHead(FragmentActivity activity, String url, ImageView view) {
        RequestManager with = Glide.with(activity);
        load(with, ERROR_HEAD, url, view);
    }

    private static void loadHead(Fragment fragment, String url, ImageView view) {
        RequestManager with = Glide.with(fragment);
        load(with, ERROR_HEAD, url, view);
    }

    /**
     * 加载图片
     */
    private static void loadImg(Context context, String url, ImageView view) {
        RequestManager with = Glide.with(context);
        load(with, ERROR_IMG, url, view);
    }

    private static void loadImg(FragmentActivity activity, String url, ImageView view) {
        RequestManager with = Glide.with(activity);
        load(with, ERROR_IMG, url, view);
    }

    private static void loadImg(Fragment fragment, String url, ImageView view) {
        RequestManager with = Glide.with(fragment);
        load(with, ERROR_IMG, url, view);
    }

}
