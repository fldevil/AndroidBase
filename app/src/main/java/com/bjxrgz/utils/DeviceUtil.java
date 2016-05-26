package com.bjxrgz.utils;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by fd.meng on 2014/03/30
 *
 * DeviceUtil: 获取 deviceID, app verion,
 *
 */
public class DeviceUtil {

	public static String getDeviceId(Context context){
		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		String deviceId = tm.getDeviceId();
		if(deviceId == null || "".equals(deviceId)){
			deviceId = "000000000000000";
		}
		return deviceId;
	}

	/**
	 * 获取当前应用的版本号
	 * @param context
	 * @return
     */
	public static String getAppCurrentVersion(Context context)
	{
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		return "";
	}

	/**
	 *
	 * 判断是否是 Tablet
	 *
	 * @param context
	 * @return
	 */
	public static boolean isTablet(Context context) {
	    boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
	    boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
	    return (xlarge || large);
	}

	/**
	 * px 转 sp
	 * @param context
	 * @param px
     * @return
     */
	public static float pixelsToSp(Context context, Float px) {
		float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
		return px/scaledDensity;
	}

	/**
	 * sp 转 px
	 * @param context
	 * @param sp
     * @return
     */
	public static float spToPixels(Context context, Float sp) {
		float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
		return sp * scaledDensity;
	}

	/**
	 * 检查当前网络是否可用
	 *
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivityManager == null)
		{
			return false;
		}
		else
		{
			// 获取NetworkInfo对象
			NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

			if (networkInfo != null && networkInfo.length > 0)
			{
				for (int i = 0; i < networkInfo.length; i++)
				{
					// 判断当前网络状态是否为连接状态
					if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
					{
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 *
	 * 隐藏软键盘
	 */
	public static void hideSoftInputMode(Context context, View windowToken) {
		InputMethodManager imm = ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE));
		imm.hideSoftInputFromWindow(windowToken.getWindowToken(), 0);
	}

	/**
	 *
	 * 弹出软键盘
	 */
	public static void showSoftInputMode(Context context, View windowToken) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(windowToken, InputMethodManager.SHOW_FORCED);
	}

	/**
	 *
	 * 生成 GUID
	 *
	 * @param context
	 * @param objectId
	 * @return GUID
	 */
	public static String generateGUID(Context context, int objectId) {
		String guid = "";
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		guid = tm.getDeviceId() + "_" + DateUtil.getStrFromDateTime(Calendar.getInstance(), "yyyyMMddHHmmss") + "_" + String.valueOf(objectId);
		return guid;
	}
}
