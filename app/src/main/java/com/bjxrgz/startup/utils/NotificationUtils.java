package com.bjxrgz.startup.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.NotificationCompat;

/**
 * author cipherGG
 * Created by Administrator on 2016/5/25.
 * describe 通知管理类
 */
public class NotificationUtils {

    /**
     * @return 通知
     */
    public static NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /**
     * @param id 显示通知，唯一标识
     */
    public static void showNotification(Context context, int id, Notification notification) {
        getNotificationManager(context).notify(id, notification);
    }

    /**
     * @param id 撤销通知，这么用
     */
    public static void cancelNotification(Context context, int id) {
        getNotificationManager(context).cancel(id);
    }

    /**
     * context 撤销所有通知
     */
    public static void cancelAll(Context context) {
        getNotificationManager(context).cancelAll();
    }

    /**
     * android 3.0之后的新notification, style底下有获取
     */
    public static Notification getNotification(Context context, int iconID,
                                               NotificationCompat.Style style, PendingIntent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(iconID).setTicker("新通知");
        if (style != null){
            builder.setStyle(style);
        }
        builder.setWhen(System.currentTimeMillis());
        builder.setContentIntent(intent);
        Notification notification = builder.build();
        notification.flags = Notification.DEFAULT_ALL;
        return notification;
    }

    /**
     * 文本样式
     */
    public static NotificationCompat.BigTextStyle getTextStyle(String bigTitle, String text,
                                                               String summery) {
        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.setBigContentTitle(bigTitle);
        style.bigText(text);
        style.setSummaryText(summery);
        return style;
    }

    /**
     * 图片样式
     */
    public static NotificationCompat.BigPictureStyle getPictureStyle(String bigTitle, Bitmap content) {
        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
        style.setBigContentTitle(bigTitle);
        style.bigPicture(content);
        return style;
    }

    /**
     * 弹出进度条样式的通知, Notification notification = builder.build();
     */
    public static NotificationCompat.Builder getProgressBuild(Context context, int iconID,
                                                              int max, int progress,
                                                              PendingIntent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(iconID).setTicker("新通知");
        builder.setProgress(max, progress, false);
        builder.setWhen(System.currentTimeMillis());
        builder.setOngoing(true);
        builder.setContentIntent(intent);
        return builder;
    }

    /**
     * builder 通过builder来更新进度
     */
    public static void updateProgress(Context context, NotificationCompat.Builder builder,
                                      int max, int progress, int id) {
        builder.setProgress(max, progress, false);
        getNotificationManager(context).notify(id, builder.build());
    }

}