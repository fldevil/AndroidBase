package com.bjxrgz.startup.service;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.view.WindowManager;

import com.bjxrgz.startup.base.MyApp;
import com.bjxrgz.startup.domain.Version;
import com.bjxrgz.startup.manager.APIManager;
import com.bjxrgz.startup.manager.FileManager;
import com.bjxrgz.startup.manager.HttpManager;
import com.bjxrgz.startup.manager.ViewManager;
import com.bjxrgz.startup.utils.AppUtils;
import com.bjxrgz.startup.utils.DialogUtils;
import com.bjxrgz.startup.utils.FileUtils;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class UpdateService extends Service {

    private Version version;

    public static void goService(Context from) {
        Intent intent = new Intent(from, UpdateService.class);
        from.startService(intent);
    }

    public UpdateService() {
    }

    @Override
    public void onCreate() {
        checkUpdate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // startService才走这个 不走下面的
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // bindService不走上面的 走这个
        throw null;
    }

    private void checkUpdate() {
        APIManager apiEmptyGson = HttpManager.getCallGson();
        Call<Version> responseBodyCall = apiEmptyGson.checkUpdate();
        HttpManager.enqueue(responseBodyCall, new HttpManager.CallBack<Version>() {
            @Override
            public void onSuccess(Version result) {
                int versionCode = MyApp.get().getAppInfo().getVersionCode();
                if (result != null) {
                    version = result;
                    if (versionCode < result.getVersionCode()) { // 小于 有新版本
                        showNoticeDialog(result); //  提示对话框
                    } else {
                        stopSelf(); // 停止服务
                    }
                } else {
                    stopSelf(); // 停止服务
                }
            }

            @Override
            public void onFailure() {

            }
        });
    }

    /* 提示更新 */
    private void showNoticeDialog(final Version version) {
        String title = "有新版本:" + version.getVersionName();
        String message = version.getChangeLog();
        AlertDialog dialog = DialogUtils.createAlert(this, title, message, "立即更新",
                "以后再说", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyApp.get().getThread().execute(new Runnable() {
                            @Override
                            public void run() { // 子线程下载
                                downloadApk();
                            }
                        });
                    }
                });
        Window window = dialog.getWindow();
        if (window != null) {
            window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialog.show();
    }

    private void downloadApk() {
        APIManager apiNullGson = HttpManager.getCallGson();
        Call<ResponseBody> downCall = apiNullGson.downloadAPK(version.getUpdateUrl());
        HttpManager.enqueue(downCall, new HttpManager.CallBack<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody body) { // 回调也是子线程
                if (body != null && body.byteStream() != null) {
                    File apkFile = FileManager.createAPKInRes(version.getVersionName());
                    FileUtils.writeFileFromIS(apkFile, body.byteStream(), false);
                    Intent installIntent = AppUtils.getInstallIntent(apkFile);
                    UpdateService.this.startActivity(installIntent);
                }
            }

            @Override
            public void onFailure() {
                ViewManager.toast("下载失败");
            }
        });
    }

}
