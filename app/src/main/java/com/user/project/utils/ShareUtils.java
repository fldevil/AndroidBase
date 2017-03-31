package com.user.project.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.bjxrgz.base.utils.LogUtils;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.utils.Log;
import com.user.project.R;

import java.util.Map;

/**
 * Created by JiangZhiGuo on 2016-11-22.
 * describe 分享/授权管理类 (用的是uemng的精简版，不用的话，
 * 1.删掉lib下的几个social(包括uemng_integrate_tool)相关的jar ,(总占100KB左右)
 * 2.删除lib下的so文件(总占100KB左右)
 * 3.删掉res下的social相关资源，(总占100KB左右)
 * 4.并去掉manifest下的配置)
 * 5.删除java里的微信activity
 */
public class ShareUtils {

    private static UMShareAPI umShareAPI;

    public static void initApp(Context context) {
        Log.LOG = true;
        Config.IsToastTip = true;
//        Config.dialogSwitch = true; // 是否使用默认dialog
//        Config.dialog = null;
//        Config.REDIRECT_URL = "您新浪后台的回调地址";
        umShareAPI = UMShareAPI.get(context);
        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
    }

    /* 检查是否支持分享和授权 */
    private static boolean check(Activity activity, SHARE_MEDIA platform) {
        boolean install = umShareAPI.isInstall(activity, platform);
        boolean support = umShareAPI.isSupport(activity, platform);
        return install && support;
    }

    /* QQ 和 sina 需要 (分享和授权都需要)*/
    public static void initActivityResult(int requestCode, int resultCode, Intent data) {
        umShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * ***********************************登录************************************
     * 先授权，再获取用户信息 这里获取的信息比授权的多
     */
    private static void toAuth(final Activity activity, final SHARE_MEDIA platform) {
        if (check(activity, platform)) {
            umShareAPI.doOauthVerify(activity, platform, new UMAuthListener() {
                @Override
                public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                    LogUtils.d("auth_onComplete" + platform + "\n" + map.toString());
                    umShareAPI.getPlatformInfo(activity, platform, umInfoListener);
                }

                @Override
                public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                    LogUtils.e("auth_onError" + platform);
                    if (throwable != null) throwable.printStackTrace();
                }

                @Override
                public void onCancel(SHARE_MEDIA share_media, int i) {
                    LogUtils.d("auth_onCancel" + platform);
                }
            });
        }
    }

    private static UMAuthListener umInfoListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            LogUtils.d("info_onComplete" + platform + "\n" + data.toString());
            if (platform == SHARE_MEDIA.WEIXIN || platform == SHARE_MEDIA.WEIXIN_CIRCLE
                    || platform == SHARE_MEDIA.QQ || platform == SHARE_MEDIA.QZONE
                    || platform == SHARE_MEDIA.SINA) {
                String token = data.get("access_token");
                String name = data.get("screen_name");
                String avator = data.get("profile_image_url");
                String gender = data.get("gender");

            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            LogUtils.e("info_onError" + platform);
            if (t != null) t.printStackTrace();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            LogUtils.d("info_onCancel" + platform);
        }
    };

    /**
     * ***********************************分享************************************
     */
    private static void goShare(final Activity activity, final SHARE_MEDIA platform, final String imgUrl) {
        String title = "";
        String content = "";
        String url = "";
        UMImage image;
        if (TextUtils.isEmpty(imgUrl)) {
            image = getImage(activity, R.mipmap.ic_launcher);
        } else {
            image = getImage(activity, imgUrl);
        }
        ShareAction action = getAction(activity, title, content, url, image, null, null);
        if (platform == null) { // 打开分享面板
            open(action, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                    SHARE_MEDIA.QQ, SHARE_MEDIA.SINA);
        } else { // 指定分享
            share(action, platform);
        }
    }

    /* 网络图片(兼容性更高) */
    private static UMImage getImage(Context context, String url) {
        return new UMImage(context, url);
    }

    /* 资源文件(兼容性更高) */
    private static UMImage getImage(Context context, int res) {
        return new UMImage(context, res);
    }

    /* 音乐 */
    private static UMusic getMusic(String url, String title, String desc, UMImage thumb) {
        UMusic music = new UMusic(url);
        music.setTitle(title); //音乐的标题
        music.setThumb(thumb); //音乐的缩略图
        music.setDescription(desc); //音乐的描述
        return music;
    }

    /* 视频 */
    private static UMVideo getVideo(String url, String title, String desc, UMImage thumb) {
        UMVideo video = new UMVideo(url);
        video.setTitle(title); // 音乐的标题
        video.setThumb(thumb); // 音乐的缩略图
        video.setDescription(desc); // 音乐的描述
        return video;
    }

    /* 获取分享信息 */
    private static ShareAction getAction(Activity activity, String title, String content,
                                         String url, UMImage image, UMusic music, UMVideo video) {
        ShareAction action = new ShareAction(activity);
        if (!TextUtils.isEmpty(title)) {
            action.withTitle(title);
        }
        if (!TextUtils.isEmpty(content)) {
            action.withText(content);
        }
        if (!TextUtils.isEmpty(url)) {
            action.withTargetUrl(url);
        }
        if (image != null) {
            action.withMedia(image);
        }
        if (music != null) {
            action.withMedia(music);
        }
        if (video != null) {
            action.withMedia(video);
        }
        action.setCallback(umShareListener);
        return action;
    }

    /* 打开自带分享面板 */
    private static void open(ShareAction action, SHARE_MEDIA... list) {
        action.setDisplayList(list).open();
    }

    /* 指定分享 */
    private static void share(ShareAction action, SHARE_MEDIA platform) {
        action.setPlatform(platform).share();
    }

    /* 分享回调 */
    private static UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            LogUtils.d("share_onResult" + platform);
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            LogUtils.e("share_onError" + platform);
            if (t != null) t.printStackTrace();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            LogUtils.d("share_onCancel" + platform);
        }
    };
}
