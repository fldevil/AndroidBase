package com.user.project.func;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.bjxrgz.base.BaseApp;
import com.bjxrgz.base.utils.AppUtil;
import com.bjxrgz.base.utils.DialogUtil;
import com.bjxrgz.base.utils.FileUtil;
import com.bjxrgz.base.utils.GsonUtil;
import com.bjxrgz.base.utils.IntentUtil;
import com.bjxrgz.base.utils.LogUtil;
import com.bjxrgz.base.utils.ToastUtil;
import com.user.project.domain.Version;
import com.user.project.http.Callback;
import com.user.project.http.HttpManager;
import com.user.project.utils.MyUtil;

import java.io.File;
import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * Created by Fan on 2017/1/13.
 * apk 更新
 */
public class UpdateApp {

    private Activity activity;
    private boolean isShowDialog;

    private Version version;

    public UpdateApp(Activity activity, boolean isShowDialog) {
        this.activity = activity;
        this.isShowDialog = isShowDialog;
        checkUpdate();
    }

    private void checkUpdate() {
        final int versionCode = AppUtil.getVersionCode(activity);
        HttpManager.getInstance().checkUpdate(versionCode, new Callback.CommonCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody result) {
                if (result.contentLength() != 0) {
                    String resultStr = null;
                    try {
                        resultStr = result.string();
                    } catch (IOException e) {
                        LogUtil.e(e.toString());
                        if (isShowDialog) {
                            ToastUtil.showShortToast("已经是最新版本");
                        }
                    }
                    if (!TextUtils.isEmpty(resultStr)) {
                        version = GsonUtil.get().fromJson(resultStr, Version.class);
                        if (versionCode < version.getVersionCode()) { // 小于 有新版本
                            showNoticeDialog(); //  提示对话框
                        } else {
                            if (isShowDialog) {
                                ToastUtil.showShortToast("已经是最新版本");
                            }
                        }
                    } else {
                        if (isShowDialog) {
                            ToastUtil.showShortToast("已经是最新版本");
                        }
                    }
                } else {
                    if (isShowDialog) {
                        ToastUtil.showShortToast("已经是最新版本");
                    }
                }
            }

            @Override
            public void onError(int httpCode, String errorMessage) {
                MyUtil.httpFailure(httpCode, errorMessage);
            }
        });
    }

    private void showNoticeDialog() {
        AlertDialog alertDialog = DialogUtil.createAlert(activity,
                "发现新版本:" + version.getVersionName(),
                version.getChangeLog(),
                "立即升级", "以后再说",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downloadApk(); //  确定开始下载
                        dialog.dismiss();
                    }
                });

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void downloadApk() {
        ToastUtil.showShortToast("开始下载最新版本");

        HttpManager.getInstance().downloadLargeFile(version.getUpdateUrl(), new Callback.CommonCallback<ResponseBody>() {
            @Override
            public void onSuccess(final ResponseBody result) {
                if (result == null || result.byteStream() != null) {
                    return;
                }
                BaseApp.getInstance().getThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        String fileName = version.getVersionName() + ".apk";
                        File apkFile = new File(AppUtil.getResDir(activity), fileName);
                        FileUtil.createFileByDeleteOldFile(apkFile);

                        FileUtil.writeFileFromIS(apkFile, result.byteStream(), false);
                        // 启动安装
                        Intent installIntent = IntentUtil.getInstallIntent(apkFile);
                        activity.startActivity(installIntent);
                    }
                });
            }

            @Override
            public void onError(int httpCode, String errorMessage) {
                MyUtil.httpFailure(httpCode, errorMessage);
            }
        });
    }
}
