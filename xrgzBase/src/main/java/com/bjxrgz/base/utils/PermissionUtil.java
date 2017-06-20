package com.bjxrgz.base.utils;

import android.app.Activity;
import android.os.Build;

import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ADD_VOICEMAIL;
import static android.Manifest.permission.BODY_SENSORS;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.PROCESS_OUTGOING_CALLS;
import static android.Manifest.permission.READ_CALENDAR;
import static android.Manifest.permission.READ_CALL_LOG;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_MMS;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.RECEIVE_WAP_PUSH;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.SEND_SMS;
import static android.Manifest.permission.USE_SIP;
import static android.Manifest.permission.WRITE_CALENDAR;
import static android.Manifest.permission.WRITE_CALL_LOG;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by Fan on 2016/11/18.
 * 权限验证框架管理
 */
public class PermissionUtil {

    public interface PermissionListener {
        /**
         * 同意使用权限
         */
        void onAgree();

        /**
         * 拒绝使用权限
         */
        void onRefuse();
    }

    /**
     * 请求权限,返回结果一起处理
     */
    public static void request(Activity activity, final PermissionListener listener, final String... permissions) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        Observable<Boolean> request = rxPermissions.request(permissions);
        request.subscribe(new Observer<Boolean>() {

            private Disposable disposable;

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(@NonNull Boolean aBoolean) {
                if (aBoolean) { // 同意使用权限
                    if (listener != null) {
                        listener.onAgree();
                    }
                } else {
                    listener.onRefuse();
                    LogUtil.d("拒绝使用权限");
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                LogUtil.e("请求权限抛出异常" + e.toString());
                if (disposable.isDisposed()) {
                    disposable.dispose();
                }
            }

            @Override
            public void onComplete() {
                LogUtil.d("请求权限完成");
                if (disposable.isDisposed()) {
                    disposable.dispose();
                }
            }
        });
    }

    /**
     * CALENDAR 日历权限组
     */
    public static void requestCalendar(Activity activity,PermissionListener listener){
        request(activity,listener,READ_CALENDAR,WRITE_CALENDAR);
    }

    /**
     * CAMERA 相机权限组
     */
    public static void requestCamera(Activity activity,PermissionListener listener){
        request(activity,listener,CAMERA);
    }

    /**
     * CONTACTS 联系人权限组
     */
    public static void requestContacts(Activity activity,PermissionListener listener){
        request(activity,listener,READ_CONTACTS,WRITE_CONTACTS,GET_ACCOUNTS);
    }

    /**
     * LOCATION 定位信息权限组
     */
    public static void requestLocation(Activity activity,PermissionListener listener){
        request(activity,listener,ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION);
    }

    /**
     * MICROPHONE 录制视频权限组
     */
    public static void requestMicroPhone(Activity activity,PermissionListener listener){
        request(activity,listener,RECORD_AUDIO);
    }

    /**
     * PHONE 打电话权限组
     */
    public static void requestPhone(Activity activity,PermissionListener listener){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            request(activity,listener,READ_PHONE_STATE,CALL_PHONE,READ_CALL_LOG,WRITE_CALL_LOG,ADD_VOICEMAIL,USE_SIP,PROCESS_OUTGOING_CALLS);
        }else {
            request(activity,listener,READ_PHONE_STATE,CALL_PHONE,ADD_VOICEMAIL,USE_SIP,PROCESS_OUTGOING_CALLS);
        }
    }

    /**
     * SENSORS 传感器权限组
     */
    public static void requestSensors(Activity activity,PermissionListener listener){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            request(activity,listener,BODY_SENSORS);
        }
    }

    /**
     * SMS 短信权限组
     */
    public static void requestSms(Activity activity,PermissionListener listener){
        request(activity,listener,SEND_SMS,RECEIVE_SMS,READ_SMS,RECEIVE_WAP_PUSH,RECEIVE_MMS);
    }

    /**
     * STORAGE 内存卡权限组
     */
    public static void requestStorage(Activity activity,PermissionListener listener){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            request(activity,listener,READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE);
        }else {
            request(activity,listener,WRITE_EXTERNAL_STORAGE);
        }
    }
}
