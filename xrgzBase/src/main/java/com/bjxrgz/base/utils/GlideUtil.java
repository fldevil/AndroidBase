package com.bjxrgz.base.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

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
public class GlideUtil {

    /* 获取缓存目录 */
    public static File getCacheFile(Context context) {
        return new File(context.getCacheDir(), DiskCache.Factory.DEFAULT_DISK_CACHE_DIR);
    }

    /* 清除磁盘缓存 */
    public static void clearCache(Context context) {
        Glide.get(context.getApplicationContext()).clearDiskCache();
    }

    /* 清除内存缓存 */
    public static void clearMemory(Context context) {
        Glide.get(context).clearMemory();
    }

    /* 图片加载完毕监听 */
    public interface CompleteListener {
        void complete(Bitmap result);
    }

    private static void load(RequestManager requestManager, String url, int errorRes,
                             ImageView view, final CompleteListener completeListener) {
        DrawableTypeRequest<String> load = requestManager.load(url);
        if (errorRes != 0) { // 设置错误图片
            load.error(errorRes);
        }
        load.crossFade(100); // 设置淡入淡出效果，默认300ms(load.dontAnimate();取消效果)
        load.skipMemoryCache(false); // 不跳过内存缓存
        load.diskCacheStrategy(DiskCacheStrategy.SOURCE); // 磁盘缓存
        if (completeListener == null) {
            load.into(view);
        } else {
            load.into(new GlideDrawableImageViewTarget(view) {  //.listener() 也是监听异常和准备状态
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                    super.onResourceReady(resource, animation);
                }

                @Override
                protected void setResource(GlideDrawable resource) {
                    super.setResource(resource);
                    completeListener.complete(ConvertUtil.drawable2Bitmap(resource));
                }
            });
        }
    }

    public static void load(Context context, String url, ImageView view) {
        RequestManager requestManager = Glide.with(context);
        load(requestManager, url, 0, view, null);
    }

    public static void load(Context context, String url, int errorRes, ImageView view) {
        RequestManager requestManager = Glide.with(context);
        load(requestManager, url, errorRes, view, null);
    }
}
