package com.user.project.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bjxrgz.base.utils.DialogUtil;
import com.bjxrgz.base.utils.LogUtil;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.media.UMusic;
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

    public static void initApp(Context context, boolean debug) {
        Config.DEBUG = debug;
        umShareAPI = UMShareAPI.get(context);
        PlatformConfig.setWeixin("", "");
        PlatformConfig.setQQZone("", "");
    }

    /**
     * 检查是否支持分享和授权
     */
    private static boolean check(Activity activity, SHARE_MEDIA platform) {
        boolean install = umShareAPI.isInstall(activity, platform);
        boolean support = umShareAPI.isSupport(activity, platform);
        return install && support;
    }

    /**
     * QQ 和 sina 需要 (分享和授权都需要)
     */
    public static void initActivityResult(int requestCode, int resultCode, Intent data) {
        umShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    /***********************************登录************************************/

    /**
     * 先授权，再获取用户信息 这里获取的信息比授权的多
     */
    public static void toAuth(final Activity activity, final SHARE_MEDIA platform) {
        if (check(activity, platform)) {
            umShareAPI.doOauthVerify(activity, platform, new UMAuthListener() {
                @Override
                public void onStart(SHARE_MEDIA share_media) {

                }

                @Override
                public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                    LogUtil.d("auth_onComplete" + platform + "\n" + map.toString());
                    umShareAPI.getPlatformInfo(activity, platform, umInfoListener);
                }

                @Override
                public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                    LogUtil.e("auth_onError" + platform);
                    if (throwable != null) throwable.printStackTrace();
                }

                @Override
                public void onCancel(SHARE_MEDIA share_media, int i) {
                    LogUtil.d("auth_onCancel" + platform);
                }
            });
        }
    }

    private static UMAuthListener umInfoListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            LogUtil.d("info_onComplete" + platform + "\n" + data.toString());
            if (platform == SHARE_MEDIA.WEIXIN || platform == SHARE_MEDIA.QQ) {
                String uid = data.get("uid");
                int type = platform == SHARE_MEDIA.WEIXIN ? 1 : 2;
                String token = data.get("access_token");
                String name = data.get("name");
                String avator = data.get("iconurl");

                /*Map<String, Object> map = User.getLog3PartyRequest(uid, type, token,
                        name, avator, PushUtils.getDeviceToken());

                Call<User> call = HttpUtils.call(HttpUtils.Head.common, HttpUtils.Factory.gson)
                        .signin3Party(map);
                HttpUtils.enqueue(call, new HttpUtils.CallBack<User>() {
                    @Override
                    public void onSuccess(User result) {
                        SPUtils.setUser(result);
                        result.setToast(R.string.login_success);

                        // 环信登录
                        EaseuiUtils.getInstance().login(result.getId());

                        RxEvent<User> event = new RxEvent<>(RxEvent.ID.log, result);
                        RxUtils.get().post(event);
                    }

                    @Override
                    public void onFailure(int httpCode, String errorMessage) {
                        MyUtils.httpFailure(httpCode, errorMessage);
                    }
                });*/
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            LogUtil.e("info_onError" + platform);
            if (t != null) t.printStackTrace();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            LogUtil.d("info_onCancel" + platform);
        }
    };

    /***********************************分享************************************/

    /**
     * 自定义分享面板
     */
    public static void showShareUI(final Activity activity, final String title, final String desc, final String url, final String imgUrl) {
        View inflate = LayoutInflater.from(activity).inflate(R.layout.layout_share, null);
        final Dialog dialog = DialogUtil.createCustom(activity, R.style.DialogCustom, inflate, 1, 1);

        TextView tvWeixin = (TextView) inflate.findViewById(R.id.tvWeixin);
        TextView tvWeixinCircle = (TextView) inflate.findViewById(R.id.tvWeixinCircle);
        TextView tvQQ = (TextView) inflate.findViewById(R.id.tvQQ);
        TextView tvQZone = (TextView) inflate.findViewById(R.id.tvQZone);
        TextView tvCancel = (TextView) inflate.findViewById(R.id.tvCancel);

        // 微信
        tvWeixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                goShare(activity, SHARE_MEDIA.WEIXIN, title, desc, url, imgUrl);
            }
        });

        // 朋友圈
        tvWeixinCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                goShare(activity, SHARE_MEDIA.WEIXIN_CIRCLE, title, desc, url, imgUrl);
            }
        });

        // qq
        tvQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                goShare(activity, SHARE_MEDIA.QQ, title, desc, url, imgUrl);
            }
        });

        // qq空间
        tvQZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                goShare(activity, SHARE_MEDIA.QZONE, title, desc, url, imgUrl);
            }
        });

        // cancel
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 系统自带分享面板
     */
    private static void goShare(final Activity activity, final SHARE_MEDIA platform, String title,
                                String desc, String url, final String imgUrl) {
        UMImage image;
        if (TextUtils.isEmpty(imgUrl)) {
            image = getImage(activity, R.mipmap.ic_launcher);
        } else {
            image = getImage(activity, imgUrl);
        }

        UMWeb umWeb;
        if (TextUtils.isEmpty(url)) {
            return;
        } else {
            umWeb = getUrl(url, title, desc, image);
        }
        ShareAction action = getAction(activity, null, umWeb, null, null, null);
        if (platform == null) { // 打开分享面板
            open(action, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                    SHARE_MEDIA.QQ);
        } else { // 指定分享
            share(action, platform);
        }
    }

    /**
     * 网络图片(兼容性更高)
     */
    private static UMImage getImage(Context context, String url) {
        return new UMImage(context, url);
    }

    /**
     * 资源文件(兼容性更高)
     */
    private static UMImage getImage(Context context, int res) {
        return new UMImage(context, res);
    }

    /**
     * 音乐
     */
    private static UMusic getMusic(String url, String title, String desc, UMImage thumb) {
        UMusic music = new UMusic(url);
        music.setTitle(title); //音乐的标题
        music.setThumb(thumb); //音乐的缩略图
        music.setDescription(desc); //音乐的描述
        return music;
    }

    /**
     * 视频
     */
    private static UMVideo getVideo(String url, String title, String desc, UMImage thumb) {
        UMVideo video = new UMVideo(url);
        video.setTitle(title); // 音乐的标题
        video.setThumb(thumb); // 音乐的缩略图
        video.setDescription(desc); // 音乐的描述
        return video;
    }

    /**
     * 链接
     */
    private static UMWeb getUrl(String url, String title, String desc, UMImage thumb) {
        UMWeb umWeb = new UMWeb(url);
        umWeb.setTitle(title);
        umWeb.setDescription(desc);
        umWeb.setThumb(thumb);
        return umWeb;
    }

    /**
     * 获取分享信息
     */
    private static ShareAction getAction(Activity activity, String content,
                                         UMWeb url, UMImage image, UMusic music, UMVideo video) {
        ShareAction action = new ShareAction(activity);
        if (!TextUtils.isEmpty(content)) {
            action.withText(content);
        }
        if (url != null) {
            action.withMedia(url);
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

    /**
     * 打开自带分享面板
     */
    private static void open(ShareAction action, SHARE_MEDIA... list) {
        action.setDisplayList(list).open();
    }

    /**
     * 指定分享
     */
    private static void share(ShareAction action, SHARE_MEDIA platform) {
        action.setPlatform(platform).share();
    }

    /**
     * 分享回调
     */
    private static UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            LogUtil.d("umeng分享开始");
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            LogUtil.d("umeng分享返回结果" + platform);
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            LogUtil.e("umeng分享错误" + platform);
            if (t != null) t.printStackTrace();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            LogUtil.d("umeng分享取消" + platform);
        }
    };
}
