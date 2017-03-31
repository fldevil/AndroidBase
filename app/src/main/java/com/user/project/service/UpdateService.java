package com.user.project.service;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.view.WindowManager;

import com.bjxrgz.base.domain.Version;
import com.bjxrgz.base.utils.ActivityUtils;
import com.bjxrgz.base.utils.AppUtils;
import com.bjxrgz.base.utils.DialogUtils;
import com.bjxrgz.base.utils.FileUtils;
import com.bjxrgz.base.utils.HttpUtils;
import com.bjxrgz.base.utils.IntentUtils;
import com.user.project.MyApp;
import com.user.project.R;
import com.user.project.utils.MyUtils;
import com.user.project.utils.ResUtils;

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
        final int code = AppUtils.get().getVersionCode();
        Call<Version> call = HttpUtils.call(HttpUtils.Head.common, HttpUtils.Factory.gson)
                .checkUpdate(code);
        HttpUtils.enqueue(call, new HttpUtils.CallBack<Version>() {
            @Override
            public void onSuccess(Version result) {
                if (result == null) {
                    stopSelf(); // 停止服务
                    return;
                }
                if (code < result.getVersionCode()) { // 小于 有新版本
                    showNoticeDialog(result); //  提示对话框
                } else {
                    stopSelf(); // 停止服务
                }
            }

            @Override
            public void onFailure(int httpCode, String errorMessage) {
                MyUtils.httpFailure(httpCode, errorMessage);
            }
        });
    }

    /* 提示更新 */
    private void showNoticeDialog(final Version version) {
        String title = String.format(getString(R.string.find_new_version), version.getVersionName());
        String message = version.getChangeLog();
        String positive = getString(R.string.update_now);
        String negative = getString(R.string.update_delay);
        AlertDialog dialog = DialogUtils.createAlert(this, title, message, positive, negative,
                new DialogInterface.OnClickListener() {
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
        Call<ResponseBody> call = HttpUtils.call(HttpUtils.Head.empty, HttpUtils.Factory.empty)
                .downloadLargeFile(version.getUpdateUrl());
        HttpUtils.enqueue(call, new HttpUtils.CallBack<ResponseBody>() {
            @Override
            public void onSuccess(final ResponseBody body) { // 回调也是子线程
                if (body == null || body.byteStream() == null) return;
                MyApp.get().getThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        File apkFile = ResUtils.createAPKInRes(version.getVersionName());
                        FileUtils.writeFileFromIS(apkFile, body.byteStream(), false);
                        // 启动安装
                        Intent installIntent = IntentUtils.getInstallIntent(apkFile);
                        ActivityUtils.startActivity(UpdateService.this, installIntent);
                    }
                });
            }

            @Override
            public void onFailure(int httpCode, String errorMessage) {
                MyUtils.httpFailure(httpCode, errorMessage);
            }
        });
    }

}
