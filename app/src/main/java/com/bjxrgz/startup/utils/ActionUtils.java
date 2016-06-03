package com.bjxrgz.startup.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;

import com.bjxrgz.startup.R;

import java.io.File;

/**
 * Created by fd.meng on 2014/03/30
 * <p>
 * describe intent片段 , 包括多媒体，settings，系统应用
 */
public class ActionUtils {

    /**
     * ************************************多媒体************************************
     * <p>
     * 相册 , BitmapFactory.decodeStream(ResolverUtils.openInput(this, data.getData()));
     */
    public static void goPicture(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 相册 , 自带裁剪
     */
    public static void goPictureCrop(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra("crop", true);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 拍照 , (Bitmap) intent.getExtras().get("data");
     */
    public static void goCamera(Activity activity, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * ************************************Settings************************************
     * <p>
     * 打开app系统设置
     */
    public static void goAppSettings(Context context, String packageName) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:" + packageName));
        context.startActivity(intent);
    }

    /**
     * 跳转到无线网络设置界面
     */
    public static void goNetSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        context.startActivity(intent);
    }

    /**
     * 跳转到无线wifi网络设置界面
     */
    public static void goWifiSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        context.startActivity(intent);
    }

    /**
     * ************************************系统应用************************************
     * <p>
     * 直接打电话
     */
    public static void goCall(Context context, String phoneNumber) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        try {
            context.startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
            LogUtils.log(Log.DEBUG, "ActionUtils", "goCall--->权限拒绝");
        }
    }

    /**
     * 拨号器界面
     */
    public static void goDial(Context context, String phoneNumber) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        if (!TextUtils.isEmpty(phoneNumber))
            intent.setData(Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
    }

    /**
     * 短信发送界面, 不打开界面发送短信SmsManager.getDefault().sendTextMessage();
     */
    public static void goSms(Context context, String phoneNumber, String content) {
        Uri uri = Uri.parse("smsto:" + (TextUtils.isEmpty(phoneNumber) ? "" : phoneNumber));
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", TextUtils.isEmpty(content) ? "" : content);
        context.startActivity(intent);
    }

    /**
     * 发送 email
     */
    public static void goEmail(Context mContext, String to, String cc, String subject, String content) {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //email.setType("message/rfc822");
        email.setType("text/html");
        // 设置邮件发收人
        if (null != cc && !"".equals(cc.trim())) {
            String[] ccs = cc.split("\\;|\\,");
            email.putExtra(Intent.EXTRA_CC, ccs);
        }
        if (null != to && !"".equals(to.trim())) {
            String[] tos = to.split("\\;|\\,");
            email.putExtra(Intent.EXTRA_EMAIL, tos);
        }

        // 设置邮件标题
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        // 设置邮件内容
        email.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(content));

        // 调用系统的邮件系统
        //mContext.startActivity(Intent.createChooser(email, "请先设定一个默认的Email地址"));
        mContext.startActivity(Intent.createChooser(email, mContext.getString(R.string.select_email_sender)));
    }

    /**
     * ************************************其他************************************
     * <p>
     * 分享
     */
    public static void goShare(Context context, String content) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        context.startActivity(Intent.createChooser(intent, "分享"));
    }

    /**
     * 也是分享
     */
    private static void doShare(Context context, String title, String body, String url) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            intent.putExtra(Intent.EXTRA_HTML_TEXT, url);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, "分享"));
    }

    /**
     * 安装apk
     */
    public void goInstall(Context context, File file) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 主动回到Home，后台运行
     */
    public static void goHome(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        context.startActivity(intent);
    }

    /**
     * 退出app？
     */
    public static void goExist(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        //如果是服务里调用，必须加入new task标识
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        context.startActivity(intent);
    }

}
