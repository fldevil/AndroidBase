package com.bjxrgz.startup;

import android.app.Application;
import android.os.Environment;

/**
 * Created by fd.meng on 2014/03/30
 *
 * 继承 application
 *
 */
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
	 *
	 * 获取日志文件
	 *
	 * @return 日志文件
     */
	public static String getLogFile() {
		return APP_SDCARD_NAME + "log/startup_log.txt";
	}

	/**
	 *
	 * 获取资源路径
	 *
	 * @return 资源文件路径
     */
	public static String getResourcePath() {
		return APP_SDCARD_NAME + "recource/%s/%d/";
	}

}
