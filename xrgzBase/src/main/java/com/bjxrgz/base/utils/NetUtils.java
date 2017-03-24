package com.bjxrgz.base.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.bjxrgz.base.R;
import com.bjxrgz.base.base.BaseApp;

/**
 * Created by jiang on 2016/10/12
 * <p>
 * 网络相关工具类
 */
public class NetUtils {
    private static NetUtils instance;
    private static ConnectListener mListener;

    public static NetUtils get() {
        if (instance == null) {
            synchronized (NetUtils.class) {
                if (instance == null) {
                    instance = new NetUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 网络是否可用
     */
    public boolean isAvailable() {
        final BaseApp baseApp = BaseApp.get();
        NetworkInfo networkInfo = getNetworkInfo();
        boolean available = (networkInfo != null && getNetworkInfo().isAvailable());
        if (!available) {
            String show = baseApp.getString(R.string.no_network_title);
            ToastUtils.toast(show);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断wifi是否连接状态
     */
    public boolean isWifi() {
        ConnectivityManager cm = getConnectivityManager();
        return cm != null && cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 注册网络监听,listener监听回调（网络状态变化）
     */
    public void registerReceiver(Context context, ConnectListener listener) {
        mListener = listener;
        context.registerReceiver(receiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    /**
     * 注销网络监听
     */
    public void unregisterReceiver(Context context) {
        context.unregisterReceiver(receiver);
    }

    /**
     * 监听器
     */
    public interface ConnectListener {
        void onStateChange(int type, String name,
                           NetworkInfo.State state, String operator); // 无连接
    }

    /**
     * 广播,监听网络变化
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mListener == null) return;
            int type = getNetworkType();
            String name = getNetworkName();
            NetworkInfo.State state = getNetworkState();
            String operator = getNetworkOperator();
            mListener.onStateChange(type, name, state, operator);
        }
    };

    private ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) BaseApp.get()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    private NetworkInfo getNetworkInfo() {
        return getConnectivityManager().getActiveNetworkInfo();
    }

    /**
     * 获取当前网络类型
     *
     * @return {@link ConnectivityManager#TYPE_MOBILE}
     */
    public int getNetworkType() {
        NetworkInfo networkInfo = getNetworkInfo();
        if (networkInfo == null) return -1;
        else return networkInfo.getType();
    }

    /**
     * 获取当前网络类型名称
     */
    public String getNetworkName() {
        int networkType = getNetworkType();
        String name;
        if (networkType == ConnectivityManager.TYPE_MOBILE) {
            name = "移动";
        } else if (networkType == ConnectivityManager.TYPE_WIFI) {
            name = "WIFI";
        } else if (networkType == ConnectivityManager.TYPE_BLUETOOTH) {
            name = "蓝牙";
        } else {
            name = "未知";
        }
        return name;
    }

    /**
     * 获取当前网络状态
     *
     * @return {@link NetworkInfo.State State}
     */
    public NetworkInfo.State getNetworkState() {
        NetworkInfo networkInfo = getNetworkInfo();
        if (networkInfo == null) return NetworkInfo.State.UNKNOWN;
        else return networkInfo.getState();
    }

    /**
     * 获取移动网络运营商名称
     *
     * @return 如中国联通、中国移动、中国电信
     */
    public String getNetworkOperator() {
        TelephonyManager tm = (TelephonyManager) BaseApp.get()
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getNetworkOperatorName() : null;
    }

}
