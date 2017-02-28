package com.bjxrgz.project.service;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.view.WindowManager;

import com.bjxrgz.base.utils.AppUtils;
import com.bjxrgz.base.utils.DialogUtils;
import com.bjxrgz.base.utils.ToastUtils;
import com.bjxrgz.start.base.MyApp;
import com.bjxrgz.start.domain.Version;
import com.bjxrgz.start.utils.FilesUtils;
import com.bjxrgz.start.utils.HttpUtils;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class UpdateService extends Service {

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
        Call<Version> call = HttpUtils.callHeaderGson().checkUpdate();
        HttpUtils.enqueue(call, new HttpUtils.CallBack<Version>() {
            @Override
            public void onSuccess(Version result) {
                if (result != null) {
                    int versionCode = MyApp.get().getAppInfo().getVersionCode();
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
                        newThreadDown(version);
                    }
                });
        Window window = dialog.getWindow();
        if (window != null) {
            window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialog.show();
    }

    /* 子线程下载 */
    private void newThreadDown(final Version version) {
        MyApp.get().getThread().execute(new Runnable() {
            @Override
            public void run() {
                downloadApk(version);
            }
        });
    }

    /* 下载apk */
    private void downloadApk(final Version version) {
        Call<ResponseBody> call = HttpUtils.callNullNull().downloadLargeFile(version.getUpdateUrl());
        HttpUtils.enqueue(call, new HttpUtils.CallBack<ResponseBody>() {
            @Override
            public void onSuccess(final ResponseBody body) { // 回调也是子线程
                if (body != null && body.byteStream() != null) {
                    MyApp.get().getThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            File apkFile = FilesUtils.createAPKInRes(version.getVersionName());
                            com.bjxrgz.base.utils.FileUtils.writeFileFromIS(apkFile, body.byteStream(), false);
                            // 启动安装
                            Intent installIntent = AppUtils.getInstallIntent(apkFile);
                            UpdateService.this.startActivity(installIntent);
                        }
                    });
                }
            }

            @Override
            public void onFailure() {
                ToastUtils.toast("apk下载失败");
            }
        });
    }

}
