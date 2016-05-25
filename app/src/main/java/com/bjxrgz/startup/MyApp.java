package com.bjxrgz.startup;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap.Config;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.bjxrgz.utils.DateUtil;

public class MyApp extends Application {
	
	public static Application app = null;
	public static String APP_SDCARD_NAME = "";
	public static final boolean PRINTLOG = true;
	public static final String LOG_TAG = "startup";

	//设备类型
	public static final String DEVICE = "android";
	//操作系统
	public static final String SYSTEM_VERSION = android.os.Build.VERSION.RELEASE;

	public static final String APPLICAITON_EXIT_ACTION = "com.package.ACTION_LOGOUT";

	/******************** date format *************************/		
	public static final String DATE_DISPLAY_FORMAT = "yyyy-MM-dd";
	public static final String DATE_TIME_DISPLAY_FORMAT = "yyyy-MM-dd HH:mm";
	
	/******************** api *************************/
	
	public static String API_HOST = "http://www.xxx.com/";
	public static String API_XXX = API_HOST + "xxx";

	/******************** login info *************************/	
	public static final String LOGIN_INFO = "startup_login_info";
	public static final String LOGIN_INFO_USER_NAME = "startup_login_user_name";
	
	@Override
	public void onCreate() {
        super.onCreate();
        app = this; 

		boolean isSDcardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		if(isSDcardExist) {
			APP_SDCARD_NAME = Environment.getExternalStorageDirectory() + "/startup/";
		} else {
			APP_SDCARD_NAME = Environment.getRootDirectory() + "/startup/";
		}
	}

	/*public static void SaveUser(User user) {
		SharedPreferences loginSessionPref = app.getSharedPreferences(MyApp.LOGIN_INFO, 0);
		SharedPreferences.Editor loginSessionEdit = loginSessionPref.edit();
		loginSessionEdit.putString(MyApp.LOGIN_INFO_USER_NAME, user.getuName());
		loginSessionEdit.putString(MyApp.USER_INFO_USER_CARD, user.getCardId());
		loginSessionEdit.commit();
	}
	
	public static User GetLoginInfo() {
		User user = new User();
		SharedPreferences SessionPref = app.getSharedPreferences(MyApp.LOGIN_INFO, 0);
		user.setuName(SessionPref.getString(MyApp.LOGIN_INFO_USER_NAME, ""));
		user.setCardId(SessionPref.getString(MyApp.USER_INFO_USER_CARD, ""));
		return user;
	}
	
	public static void RemoveInfo() {
		SharedPreferences RemoveSessionPref = app.getSharedPreferences(MyApp.LOGIN_INFO, 0);
		SharedPreferences.Editor RemoveSessionEdit = RemoveSessionPref.edit();
		RemoveSessionEdit.remove(MyApp.LOGIN_INFO_USER_NAME);
		RemoveSessionEdit.remove(MyApp.USER_INFO_USER_CARD);
		RemoveSessionEdit.commit();
	}
	*/

	/**
	 * 获取版本号
	 * @return 当前应用的版本号
	 */
	public static String getVersion() {
		String version = "";
		try {
			PackageInfo info = app.getPackageManager().getPackageInfo(app.getPackageName(), 0);
			version = info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return version;
	}

	public static String getLogFile() {
		return APP_SDCARD_NAME + "log/startup_log.txt";
	}

	public static String getResourcePath() {
		return APP_SDCARD_NAME + "recource/%s/%d/";
	}

	public static String generateGUID(Context context, int objectId) {
		String guid = "";
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		guid = tm.getDeviceId() + "_" + DateUtil.getStrFromDateTime(Calendar.getInstance(), "yyyyMMddHHmmss") + "_" + String.valueOf(objectId);
		return guid;
	}

	public static boolean isTablet(Context context) {
		boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
		boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
		return (xlarge || large);
	}

	/**
	 * 检查当前网络是否可用
	 *
	 * @return
	 */
	public static boolean isNetworkAvailable() {
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		ConnectivityManager connectivityManager = (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE);

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
	 * 验证密码 6-16位，数字和字母组合
	 * @return 匹配成功
	 */
	public static boolean verifyPassword(String password){
		String reg = "^(?=.*?[a-zA-Z])(?=.*?[0-9])[a-zA-Z0-9]{6,16}$";
		return password.matches(reg);
	}
}
