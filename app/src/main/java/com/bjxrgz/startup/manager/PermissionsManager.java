package com.bjxrgz.startup.manager;

import android.content.Context;

import com.bjxrgz.startup.utils.LogUtils;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by Fan on 2016/11/18.
 * 权限验证框架管理
 */

public class PermissionsManager {

    public interface PermissionListener {
        /**
         * 同意使用权限
         */
        void onAgree();

        /**
         * 拒绝使用权限
         */
        void onRefuse();

        /**
         * 拒绝使用权限，且不再询问
         */
        void onRefuseAndNotAsk();
    }

    /**
     * 请求权限
     */
    public static void request(Context context, final PermissionListener listener, final String... permissions) {
        RxPermissions.getInstance(context)
                .requestEach(permissions)
                .subscribe(new Action1<Permission>() {
                    @Override
                    public void call(Permission permission) {
                        if (permission.granted) {
                            LogUtils.d("同意使用" + permission.name + "权限");
                            if (listener != null) {
                                listener.onAgree();
                            }
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            LogUtils.d("拒绝使用" + permission.name + "权限");
                            if (listener != null) {
                                listener.onRefuse();
                            }
                        } else {
                            LogUtils.d("拒绝使用" + permission.name + "权限，且不再询问");
                            if (listener != null) {
                                listener.onRefuseAndNotAsk();
                            }
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
                        LogUtils.e("请求权限完成");
                    }
                });
    }
}
