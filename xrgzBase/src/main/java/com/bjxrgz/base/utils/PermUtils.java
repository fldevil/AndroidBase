package com.bjxrgz.base.utils;

import android.Manifest;
import android.content.Context;
import android.os.Build;

import com.tbruyelle.rxpermissions.RxPermissions;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by Fan on 2016/11/18.
 * 权限验证框架管理
 */
public class PermUtils {

    public interface PermissionListener {
        /* 同意使用权限 */
        void onAgree();
    }

    /**
     * 请求权限,返回结果一起处理
     */
    public static void request(Context context, final PermissionListener listener, final String... permissions) {
        RxPermissions rxPermissions = RxPermissions.getInstance(context);
        Observable<Boolean> request = rxPermissions.request(permissions);
        request.subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean) { // 同意使用权限
                    if (listener != null) {
                        listener.onAgree();
                    }
                } else {
                    LogUtils.d("拒绝使用权限");
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                LogUtils.e("请求权限抛出异常");
            }
        }, new Action0() {
            @Override
            public void call() {
                LogUtils.d("请求权限完成");
            }
        });
    }

    /* app信息 */
    public static void requestApp(Context context, PermissionListener listener) {
        String[] permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            permission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};
        } else {
            permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};
        }
        request(context, listener, permission);
    }

    /* 设备信息 */
    public static void requestDevice(Context context, PermissionListener listener) {
        request(context, listener, Manifest.permission.READ_PHONE_STATE);
    }

    /* 拍照 */
    public static void requestCamera(Context context, PermissionListener listener) {
        request(context, listener, Manifest.permission.CAMERA);
    }

    /* 权限 */
    public static void requestMap(Context context, PermUtils.PermissionListener listener) {
        PermUtils.request(context, listener, Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    /* 分享 */
    public static void requestShare(Context context, PermUtils.PermissionListener listener) {
        String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP,
                Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.WRITE_APN_SETTINGS};
        PermUtils.request(context, listener, mPermissionList);
    }

}
