package com.bjxrgz.base.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.List;

/**
 * Created by gg on 2017/3/13.
 * 意图管理
 */
public class IntentUtil {

    /**
     * 拍照 ,不加保存路径，图片会被压缩
     */
    public static Intent getCameraIntent(Activity activity, File cameraFile) {
        PermissionUtil.requestCamera(activity, null);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        if (cameraFile == null) return intent;
        Uri uri = ConvertUtil.File2URI(cameraFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        return intent;
    }

    /**
     * 相册 ,可以自定义保存路径
     */
    public static Intent getPictureIntent(Context context) {
        Intent intent = new Intent();
        PackageManager packageManager = ManagerUtil.getPackageManager(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            if (intent.resolveActivity(packageManager) == null) {
                intent.setAction(Intent.ACTION_GET_CONTENT);
            }
        } else {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }
        if (intent.resolveActivity(packageManager) == null) {
            intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        intent.setType("image/*");
        return intent;
    }

    /**
     * 裁剪(通用) 1.启动拍照/相册 2.在onActivityForResult里调用此方法，启动裁剪功能
     */
    public static Intent getCropIntent(File from, File save) {
        return getCropIntent(from, save, 0, 0, 300, 300);
    }

    public static Intent getCropIntent(File from, File save, int aspectX, int aspectY,
                                       int outputX, int outputY) {
        if (FileUtil.isFileEmpty(from)) { // 源文件不存在
            FileUtil.deleteFile(from);
            FileUtil.deleteFile(save);
            return null;
        }
        Uri uri1 = ConvertUtil.File2URI(from);
        Uri uri2 = ConvertUtil.File2URI(save);
        return getCropIntent(uri1, uri2, aspectX, aspectY, outputX, outputY);
    }

    public static Intent getCropIntent(Uri from, Uri save, int aspectX, int aspectY,
                                       int outputX, int outputY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(from, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框比例
        if (aspectX != 0 && aspectY != 0) {
            intent.putExtra("aspectX", aspectX);
            intent.putExtra("aspectY", aspectY);
        }
        // 输出图片大小(太大会传输失败)
        if (outputX != 0 && outputY != 0) {
            intent.putExtra("outputX", outputX);
            intent.putExtra("outputY", outputY);
        }
        // 裁剪选项
        intent.putExtra("scale", true);
        intent.putExtra("noFaceDetection", true);
        // 数据返回
        intent.putExtra("return-data", false); // 不从intent里面拿
        intent.putExtra(MediaStore.EXTRA_OUTPUT, save);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        return intent;
    }

    /**
     * 获取安装App 支持7.0
     */
    public static Intent getInstallIntent(File file) {
        if (file == null) return null;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        String type = "application/vnd.android.package-archive";

        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = ConvertUtil.File2URI7(file);
        } else {
            uri = ConvertUtil.File2URI(file);
        }
        return intent.setDataAndType(uri, type);
    }

    /**
     * 获取打开当前App的意图
     */
    public static Intent getAppIntent(Context context) {
        return ManagerUtil.getPackageManager(context).getLaunchIntentForPackage(AppUtil.getPackageName(context));
    }

    /**
     * 获取分享文本的意图
     */
    public static Intent getShareIntent(String content) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, content); // 设置分享信息
        return intent;
    }

    public static Intent getShareIntent(String content, Uri uri) {
        if (uri == null) return getShareIntent(content);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("image/*");
        return intent;
    }

    public static Intent getShareIntent(String content, File image) {
        if (!FileUtil.isFileExists(image)) return null;
        return getShareIntent(content, Uri.fromFile(image));
    }

    /**
     * 打开手机联系人界面点击联系人后便获取该号码
     * 启动方式 startActivityForResult
     */
    public static Intent getContactsIntent() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.PICK");
        intent.setType("vnd.android.cursor.dir/phone_v2");
        return intent;
    }

    /**
     * 在onActivityResult中调用，获取选中的号码
     */
    public static String getSelectContact(Context context, Intent data) {
        String num = "";
        if (data == null) return num;
        Uri uri = data.getData();
        // 创建内容解析者
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor == null) return num;
        while (cursor.moveToNext()) {
            num = cursor.getString(cursor.getColumnIndex("data1"));
        }
        cursor.close();
        num = num.replaceAll("-", "");//替换的操作,555-6 -> 5556
        return num;
    }

    /**
     * 跳至填充好phoneNumber的拨号界面
     */
    public static Intent getDialIntent(String phoneNumber) {
        return new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
    }

    /**
     * 直接拨打phoneNumber
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.CALL_PHONE"/>}</p>
     */
    public static Intent getCallIntent(String phoneNumber) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        return intent;
    }

    /**
     * 短信发送界面
     */
    public static Intent getSMSIntent(String phoneNumber, String content) {
        Uri uri = Uri.parse("smsto:" + (TextUtils.isEmpty(phoneNumber) ? "" : phoneNumber));
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", TextUtils.isEmpty(content) ? "" : content);
        return intent;
    }

    /**
     * 彩信发送界面
     */
    public static Intent getSMSIntent(String phoneNumber, String content, File img) {
        Intent intent = getSMSIntent(phoneNumber, content);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(img));
        intent.setType("image/png");
        return intent;
    }

    /**
     * 直接发送短信
     * <uses-permission android:name="android.permission.SEND_SMS"/>
     */
    public static void sendSMS(Context context, String phoneNumber, String content) {
        if (StringUtil.isEmpty(content)) return;
        PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0, new Intent(), 0);
        SmsManager smsManager = SmsManager.getDefault();
        if (content.length() >= 70) {
            List<String> ms = smsManager.divideMessage(content);
            for (String str : ms) {
                smsManager.sendTextMessage(phoneNumber, null, str, sentIntent, null);
            }
        } else {
            smsManager.sendTextMessage(phoneNumber, null, content, sentIntent, null);
        }
    }

    /**
     * 回到Home
     */
    public static Intent getHomeIntent() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        return intent;
    }

    /**
     * 打开网络设置界面
     */
    public static Intent getNetSettingsIntent() {
        return new Intent(Settings.ACTION_SETTINGS);
    }

    /**
     * 打开app系统设置
     */
    public static Intent getSettingsIntent(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        String packageName = AppUtil.getPackageName(context);
        intent.setData(Uri.parse("package:" + packageName));
        return intent;
    }

    /**
     * 获取App信息的意图
     */
    public static Intent getInfoIntent(String packageName) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        return intent.setData(Uri.parse("package:" + packageName));
    }

    /**
     * 跳转应用市场的意图
     */
    public static Intent getMarketIntent(Context context) {
        String str = "market://details?id=" + context.getPackageName();
        return new Intent("android.intent.action.VIEW", Uri.parse(str));
    }

    /**
     * 获取卸载App的意图
     */
    public Intent getUninstallIntent(String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 获取打开浏览器的意图
     */
    public Intent getWebBrowseIntent(String url) {
        Uri address = Uri.parse(url);
        return new Intent(Intent.ACTION_VIEW, address);
    }

    /**
     * 打开Gps设置界面
     */
    public static void getGpsIntent(Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
