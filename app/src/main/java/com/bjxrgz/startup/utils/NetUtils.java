package com.bjxrgz.startup.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.bjxrgz.startup.R;
import com.bjxrgz.startup.base.MyApp;

/**
 * Created by fd.meng on 2014/03/30
 * <p>
 * 网络状态的管理和监听
 */
public class NetUtils {

    private static final int FALSE = -1;
    private static ConnectReceiver receiver;
    private static ConnectListener mListener;

    /**
     * 广播,监听网络变化
     */
    public static class ConnectReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.log(Log.DEBUG, MyApp.LOG_TAG, "NetUtils(ConnectReceiver)--->onReceive");
            if (mListener == null)
                return;
            if (isAvailable(context)) {
                int networkType = getNetworkType(context);

                if (networkType == FALSE) {
                    mListener.typeNull();

                } else if (networkType == ConnectivityManager.TYPE_MOBILE) {
                    mListener.typeMobile();

                } else if (networkType == ConnectivityManager.TYPE_WIFI) {
                    mListener.typeWifi();

                } else if (networkType == ConnectivityManager.TYPE_BLUETOOTH) {
                    mListener.typeBlueTooth();

                } else {
                    mListener.typeNoKnow();
                }
            } else {
                mListener.noConnect();
            }
        }
    }

    /**
     * context 注册
     */
    public static void registerReceiver(Context context) {
        if (receiver == null)
            receiver = new ConnectReceiver();

        context.registerReceiver(receiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    /**
     * context 注销
     */
    public static void unregisterReceiver(Context context) {
        if (receiver == null)
            return;

        context.unregisterReceiver(receiver);
        receiver = null;
    }

    /**
     * listener 设置电量广播监听器
     */
    public static void setConnectListener(ConnectListener listener) {

        mListener = listener;
    }

    /**
     * 监听器
     */
    public interface ConnectListener {

        void noConnect();

        void typeNull();

        void typeMobile();

        void typeWifi();

        void typeBlueTooth();

        void typeNoKnow();
    }

    /**
     * 网络连接
     */
    public static ConnectivityManager getConnectivityManager(Context context) {

        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * 网络信息
     */
    public static NetworkInfo getNetworkInfo(Context context) {

        return getConnectivityManager(context).getActiveNetworkInfo();
    }

    /**
     * 主动--->网络是否可用
     */
    public static boolean isAvailable(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);
        boolean available = (networkInfo != null && getNetworkInfo(context).isAvailable());
        if (!available) {
            Toast.makeText(context, context.getString(R.string.no_network_title), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    /**
     * 主动--->获取当前网络类型  {@link ConnectivityManager#TYPE_MOBILE}
     */
    public static int getNetworkType(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);

        if (networkInfo == null)
            return FALSE;
        else
            return networkInfo.getType();
    }

    /**
     * 主动--->获取当前网络状态 {@link NetworkInfo.State State}
     */
    public static NetworkInfo.State getNetworkState(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);

        if (networkInfo == null)
            return NetworkInfo.State.UNKNOWN;
        else
            return networkInfo.getState();
    }

}
