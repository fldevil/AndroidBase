package com.bjxrgz.base.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.bjxrgz.base.BaseApp;
import com.bjxrgz.base.R;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by jiang on 2016/10/12
 * <p>
 * 网络相关工具类
 */
public class NetUtil {

    private static ConnectListener mListener;

    /**
     * 网络是否可用
     */
    public static boolean isAvailable(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);
        boolean available = (networkInfo != null && getNetworkInfo(context).isAvailable());
        if (!available) {
            String show = BaseApp.getInstance().getString(R.string.no_network_title);
            ToastUtil.showShortToast(show);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断wifi是否连接状态
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = ManagerUtil.getConnectivityManager(context);
        return cm != null && cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 注册网络监听,listener监听回调（网络状态变化）
     */
    public static void registerReceiver(Context context, ConnectListener listener) {
        mListener = listener;
        context.registerReceiver(receiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    /**
     * 注销网络监听
     */
    public static void unregisterReceiver(Context context) {
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
    private static BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mListener == null) return;
            int type = getNetworkType(context);
            String name = getNetworkName(context);
            NetworkInfo.State state = getNetworkState(context);
            String operator = getNetworkOperator(context);
            mListener.onStateChange(type, name, state, operator);
        }
    };

    /**
     * 获取代表联网状态的NetWorkInfo对象
     */
    private static NetworkInfo getNetworkInfo(Context context) {
        return ManagerUtil.getConnectivityManager(context).getActiveNetworkInfo();
    }

    /**
     * 获取当前网络类型
     */
    public static int getNetworkType(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);
        if (networkInfo == null) {
            return -1;
        }
        return networkInfo.getType();
    }

    /**
     * 获取当前网络类型名称
     */
    public static String getNetworkName(Context context) {
        int networkType = getNetworkType(context);
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
    public static NetworkInfo.State getNetworkState(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);
        if (networkInfo == null) {
            return NetworkInfo.State.UNKNOWN;
        }
        return networkInfo.getState();
    }

    /**
     * 获取移动网络运营商名称
     *
     * @return 如中国联通、中国移动、中国电信
     */
    public static String getNetworkOperator(Context context) {
        TelephonyManager tm = ManagerUtil.getTelephonyManager(context);
        return tm != null ? tm.getNetworkOperatorName() : null;
    }

    /**
     * 获取IP地址 eg:127.168.x.x
     */
    public static String getIpAddress() {
        String ipAddress = "";
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    InetAddress ia = ias.nextElement();
                    if (ia instanceof Inet6Address) continue; // skip ipv6
                    String ip = ia.getHostAddress();
                    String host = "127.0.0.1";
                    if (!host.equals(ip)) {
                        ipAddress = ip;
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ipAddress;
    }

}
