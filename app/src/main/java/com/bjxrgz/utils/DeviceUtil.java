package com.bjxrgz.utils;

import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.telephony.TelephonyManager;

public class DeviceUtil {

	public static String getDeviceId(Context context){
		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		String deviceId = tm.getDeviceId();
		if(deviceId == null || "".equals(deviceId)){
			deviceId = "000000000000000";
		}
		return deviceId;
	}
	
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
	
	public static boolean isTablet(Context context) {
	    boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
	    boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
	    return (xlarge || large);
	}
	
	private static final String GooglePlayStorePackageNameOld = "com.google.market";
	private static final String GooglePlayStorePackageNameNew = "com.android.vending";

	public static boolean isGooglePlayStoreInstalled(Context context) {	    
	    PackageManager pm = context.getPackageManager();
	    List<PackageInfo> packages = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
	    for (PackageInfo packageInfo : packages) {
	        if (packageInfo.packageName.equals(GooglePlayStorePackageNameOld) ||
	            packageInfo.packageName.equals(GooglePlayStorePackageNameNew)) {
	            return true;
	        }
	    }
	    return false;
	}
}
