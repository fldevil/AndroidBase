package com.bjxrgz.startup.service;

import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
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
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UpdateService extends Service {

    private Version version;

    public UpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw null;
    }

    @Override
    public void onCreate() {
//        checkUpdate();
    }

    private void checkUpdate() {
        APIManager apiEmptyGson = HttpManager.getAPIEmptyGson();
        Call<Version> responseBodyCall = apiEmptyGson.checkUpdate();
        HttpManager.enqueue(responseBodyCall, new HttpManager.CallBack<Version>() {
            @Override
            public void onSuccess(Version result) {
                int versionCode = MyApp.appInfo.getVersionCode();
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
                        downloadApk(); //  确定开始下载
                    }
                });
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }

    private void downloadApk() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HttpManager.BASE_URL)
                .build();
        APIManager service = retrofit.create(APIManager.class);
        Call<ResponseBody> call = service.downloadAPK(version.getUpdateUrl());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody body = response.body();
                if (body != null) {
                    InputStream inputStream = body.byteStream();
                    if (null != inputStream) {
                        File apkFile = FileManager.createAPKFile(version.getVersionName());
                        FileUtils.writeFileFromIS(apkFile, inputStream, false);
                        Intent installIntent = AppUtils.getInstallIntent(apkFile);
                        UpdateService.this.startService(installIntent);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ViewManager.showToast("下载失败");
            }
        });
    }

}
