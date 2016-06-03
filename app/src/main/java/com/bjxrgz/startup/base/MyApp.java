package com.bjxrgz.startup.base;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.bjxrgz.startup.utils.LogUtils;

import org.json.JSONObject;
import org.xutils.ex.HttpException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * application
 */
public class MyApp extends Application {

    // 当前实例
    public static MyApp instance;
    // 默认线程池
    public final static ExecutorService threadPool = Executors.newCachedThreadPool();
    // 主线程handler
    public final static Handler mainHandler = new Handler(Looper.getMainLooper());
    // 传输数据的List，用完之后clear
    public static List<Map<String, Object>> DATA = new ArrayList<>();
    // 所有已启动的Activity
    private List<Activity> activities = new LinkedList<>();
    // 所有已启动的Service
    private List<Service> services = new LinkedList<>();
    // LogTag
    public static final String LOG_TAG = "new_energy";
    // log开关
    public static final boolean PRINT_LOG = true;
    // sd卡目录
    public static String APP_SDCARD_NAME = "";
    //设备id
    public static String DEVICE_ID = "345ujhgfds";
    // umeng device token
    public static String DEVICE_TOKEN = "dsfaghhghfdfh";
    //设备类型
    public static final String DEVICE = "android";
    //操作系统
    public static final String SYSTEM_VERSION = android.os.Build.VERSION.RELEASE;
    //
    public static final String APPLICAITON_EXIT_ACTION = "com.package.ACTION_LOGOUT";

    /********************
     * api
     *************************/
    public static String API_HOST = "http://www.xxx.com/";
    public static String API_XXX = API_HOST + "xxx";

    /********************
     * login info
     *************************/
    public static final String LOGIN_INFO = "startup_login_info";
    public static final String LOGIN_INFO_USER_NAME = "startup_login_user_name";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            APP_SDCARD_NAME = Environment.getExternalStorageDirectory() + "/new_energy/";
        } else {
            APP_SDCARD_NAME = Environment.getRootDirectory() + "/new_energy/";
        }

        // 监听所有activity的生命周期 ,可撤销
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                LogUtils.log(Log.DEBUG, activity.getClass().getSimpleName(), "--->onCreate");
                addActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                LogUtils.log(Log.DEBUG, activity.getClass().getSimpleName(), "--->onStarted");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                LogUtils.log(Log.DEBUG, activity.getClass().getSimpleName(), "--->onResumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                LogUtils.log(Log.DEBUG, activity.getClass().getSimpleName(), "--->onPaused");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                LogUtils.log(Log.DEBUG, activity.getClass().getSimpleName(), "--->onStopped");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                LogUtils.log(Log.DEBUG, activity.getClass().getSimpleName(), "--->onCreated");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                LogUtils.log(Log.DEBUG, activity.getClass().getSimpleName(), "--->onDestroyed");
                removeActivity(activity);
            }
        });

        // 监听当前app的内存 ,可撤销
        registerComponentCallbacks(new ComponentCallbacks2() {
            // 触发条件：当系统决定要杀死一个进程以求更多内存
            @Override
            public void onTrimMemory(int level) {
                LogUtils.log(Log.DEBUG, "MyApp", "onTrimMemory--->level == " + level);
            }

            // 配置发生变化，如横竖屏切换
            @Override
            public void onConfigurationChanged(Configuration newConfig) {
                LogUtils.log(Log.DEBUG, "MyApp", "onTrimMemory--->");
            }

            // 触发条件：内存不足, 并且系统想要清理内存以获取更多内存时
            @Override
            public void onLowMemory() {
                LogUtils.log(Log.DEBUG, "MyApp", "onLowMemory--->");
            }
        });
    }

    /**
     * 获取日志文件
     */
    public static String getLogPath() {
        return APP_SDCARD_NAME + "log/new_energy_log.txt";
    }

    /**
     * 获取资源路径
     */
    public static String getResourcePath() {
        return APP_SDCARD_NAME + "resource/%s/%d/";
    }

    /*
    public static void SaveUser(User user) {
        SharedPreferences preferences = instance
                .getSharedPreferences(Constants.USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.commit();
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
     * 检测用户账户是否在该设备上有效
     */
    public static void processError(Throwable ex, final Context context) {
        try {
            if (ex.getClass().equals(java.net.ConnectException.class)) {
                Toast.makeText(context, "网络连接错误", Toast.LENGTH_SHORT).show();

            } else if (ex.getClass().equals(org.xutils.ex.HttpException.class)) {
                JSONObject object = new JSONObject(((HttpException) ex).getResult());
                String message = (String) object.get("message"); // 错误信息
                org.xutils.ex.HttpException hre = ((org.xutils.ex.HttpException) ex);
                int code = hre.getCode(); // 返回码

                if (code == 401 || code == 409 || code == 410) { // 跳转登录界面
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//                     removeInfo();
//                    if (context instanceof LoginActivity) {
//                        return;
//                    }
//                    Intent intent = new Intent(context, SignUpActivity.class);
//                    context.startActivity(intent);
                } else if (code == 500) {
                    Toast.makeText(context, "系统错误", Toast.LENGTH_SHORT).show();
                } else { // 弹出提示
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            } else if (ex.getClass().equals(java.net.SocketTimeoutException.class)) {
                Toast.makeText(context, "连接超时", Toast.LENGTH_SHORT).show();

            } else {
                // Toast.makeText(context, "数据异常", Toast.LENGTH_SHORT).show();
                LogUtils.log(Log.ERROR, LOG_TAG, ex.toString(), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * activity在onCreate是调用此方法
     */
    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * activity在onDestroy是调用此方法
     */
    public void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    /**
     * start service在onCreate是调用此方法
     */
    public void addService(Service service) {
        services.add(service);
    }

    /**
     * start service在onDestroy是调用此方法
     */
    public void removeService(Service service) {
        services.remove(service);
    }

    /**
     * 关闭所有activity，应用与一键退出
     */
    public void closeActivities() {
        for (Activity activity : activities) {
            if (activity != null)
                activity.finish();
        }
    }

    /**
     * 关闭所有start service，应用与一键退出
     */
    public void closeServices() {
        for (Service service : services) {
            if (service != null)
                stopService(new Intent(this, service.getClass()));
        }
    }

    /**
     * 严核模式,允许在主线程进行网络操作,打印存在的隐患
     */
    private void initStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .penaltyDialog()
                .detectNetwork()
                .penaltyLog()
                .build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
    }

}
