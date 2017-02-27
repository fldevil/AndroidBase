package com.bjxrgz.startup.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.bjxrgz.startup.R;
import com.bjxrgz.startup.base.MyApp;

/**
 * Created by jiang on 2016/10/12
 * <p>
 * 网络相关工具类
 */
public class NetUtils {

    private static final int FALSE = -1;
    private static ConnectReceiver receiver;
    private static ConnectListener mListener;

    /**
     * 打开网络设置界面 <p>3.0以下打开设置界面</p>
     */
    public static Intent getSettingsIntent() {
        Intent intent;
        if (android.os.Build.VERSION.SDK_INT > 10) {
            intent = new Intent(Settings.ACTION_SETTINGS);
        } else {
            intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        }
        return intent;
    }

    /**
     * listener 先设置电量广播监听器
     */
    public static void setNetListener(ConnectListener listener) {
        mListener = listener;
    }

    /**
     * 广播,监听网络变化
     */
    public static class ConnectReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (mListener == null)
                return;
            if (isAvailable()) {
                int networkType = getNetworkType();

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
        if (receiver == null) {
            receiver = new ConnectReceiver();
        }
        context.registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    /**
     * context 注销
     */
    public static void unregisterReceiver(Context context) {
        if (receiver == null) {
            return;
        }
        context.unregisterReceiver(receiver);
        receiver = null;
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

    public static ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) MyApp.get().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static NetworkInfo getNetworkInfo() {
        return getConnectivityManager().getActiveNetworkInfo();
    }

    /**
     * 网络是否可用
     */
    public static boolean isAvailable() {
        final MyApp myApp = MyApp.get();
        NetworkInfo networkInfo = getNetworkInfo();
        boolean available = (networkInfo != null && getNetworkInfo().isAvailable());
        if (!available) {
            String show = myApp.getString(R.string.no_network_title);
            Toast.makeText(MyApp.get(), show, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取当前网络类型  {@link ConnectivityManager#TYPE_MOBILE}
     */
    public static int getNetworkType() {
        NetworkInfo networkInfo = getNetworkInfo();

        if (networkInfo == null)
            return FALSE;
        else
            return networkInfo.getType();
    }

    /**
     * 获取当前网络状态 {@link NetworkInfo.State State}
     */
    public static NetworkInfo.State getNetworkState() {
        NetworkInfo networkInfo = getNetworkInfo();

        if (networkInfo == null)
            return NetworkInfo.State.UNKNOWN;
        else
            return networkInfo.getState();
    }

    /**
     * 判断网络是否是4G
     *
     * @return {@code true}: 是<br>{@code false}: 不是
     */
    public static boolean is4G() {
        NetworkInfo info = getNetworkInfo();
        return info != null && info.isAvailable() && info.getSubtype() == TelephonyManager.NETWORK_TYPE_LTE;
    }

    /**
     * 判断wifi是否连接状态
     *
     * @return {@code true}: 连接<br>{@code false}: 未连接
     */
    public static boolean isWifiConnected() {
        ConnectivityManager cm = getConnectivityManager();
        return cm != null && cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 获取移动网络运营商名称
     * <p>如中国联通、中国移动、中国电信</p>
     *
     * @return 移动网络运营商名称
     */
    public static String getNetworkOperatorName() {
        TelephonyManager tm = (TelephonyManager) MyApp.get().getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getNetworkOperatorName() : null;
    }

    /**
     * 获取移动终端类型
     *
     * @return 手机制式
     * <ul>
     * <li>{@link TelephonyManager#PHONE_TYPE_NONE } : 0 手机制式未知</li>
     * <li>{@link TelephonyManager#PHONE_TYPE_GSM  } : 1 手机制式为GSM，移动和联通</li>
     * <li>{@link TelephonyManager#PHONE_TYPE_CDMA } : 2 手机制式为CDMA，电信</li>
     * <li>{@link TelephonyManager#PHONE_TYPE_SIP  } : 3</li>
     * </ul>
     */
    public static int getPhoneType() {
        TelephonyManager tm = (TelephonyManager) MyApp.get().getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getPhoneType() : -1;
    }

    public static final int NETWORK_WIFI = 1;    // wifi network
    public static final int NETWORK_4G = 4;    // "4G" networks
    public static final int NETWORK_3G = 3;    // "3G" networks
    public static final int NETWORK_2G = 2;    // "2G" networks
    public static final int NETWORK_UNKNOWN = 5;    // unknown network
    public static final int NETWORK_NO = -1;   // no network

    /**
     * 获取当前的网络类型(WIFI,2G,3G,4G)
     * <p>依赖上面的方法</p>
     *
     * @return 网络类型名称
     * <ul>
     * <li>NETWORK_WIFI   </li>
     * <li>NETWORK_4G     </li>
     * <li>NETWORK_3G     </li>
     * <li>NETWORK_2G     </li>
     * <li>NETWORK_UNKNOWN</li>
     * <li>NETWORK_NO     </li>
     * </ul>
     */
    public static String getNetWorkTypeName() {
        switch (getNetWorkType()) {
            case NETWORK_WIFI:
                return "WIFI";
            case NETWORK_4G:
                return "4G";
            case NETWORK_3G:
                return "3G";
            case NETWORK_2G:
                return "2G";
            case NETWORK_NO:
                return "NO";
            default:
                return "UNKNOWN";
        }
    }

    private static final int NETWORK_TYPE_GSM = 16;
    private static final int NETWORK_TYPE_TD_SCDMA = 17;
    private static final int NETWORK_TYPE_IWLAN = 18;

    /**
     * 获取当前的网络类型(WIFI,2G,3G,4G)
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}</p>
     *
     * @return 网络类型
     * <ul>
     * <li>{@link #NETWORK_WIFI   } = 1;</li>
     * <li>{@link #NETWORK_4G     } = 4;</li>
     * <li>{@link #NETWORK_3G     } = 3;</li>
     * <li>{@link #NETWORK_2G     } = 2;</li>
     * <li>{@link #NETWORK_UNKNOWN} = 5;</li>
     * <li>{@link #NETWORK_NO     } = -1;</li>
     * </ul>
     */
    public static int getNetWorkType() {
        int netType = NETWORK_NO;
        NetworkInfo info = getNetworkInfo();
        if (info != null && info.isAvailable()) {

            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                netType = NETWORK_WIFI;
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                switch (info.getSubtype()) {

                    case NETWORK_TYPE_GSM:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        netType = NETWORK_2G;
                        break;

                    case NETWORK_TYPE_TD_SCDMA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        netType = NETWORK_3G;
                        break;

                    case NETWORK_TYPE_IWLAN:
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        netType = NETWORK_4G;
                        break;
                    default:

                        String subtypeName = info.getSubtypeName();
                        if (subtypeName.equalsIgnoreCase("TD-SCDMA")
                                || subtypeName.equalsIgnoreCase("WCDMA")
                                || subtypeName.equalsIgnoreCase("CDMA2000")) {
                            netType = NETWORK_3G;
                        } else {
                            netType = NETWORK_UNKNOWN;
                        }
                        break;
                }
            } else {
                netType = NETWORK_UNKNOWN;
            }
        }
        return netType;
    }

}
