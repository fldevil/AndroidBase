package com.bjxrgz.startup.manager;

import android.Manifest;
import android.content.Context;

import com.bjxrgz.startup.utils.LogUtils;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by Fan on 2016/11/18.
 * 权限验证框架管理
 */
public class PermManager {

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
        request(context, listener, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
    }

    /* 设备信息 */
    public static void requestDevice(Context context, PermissionListener listener) {
        request(context, listener, Manifest.permission.READ_PHONE_STATE);
    }

    /* 设备信息 */
    public static void requestCamera(Context context, PermissionListener listener) {
        request(context, listener, Manifest.permission.CAMERA);
    }

}
