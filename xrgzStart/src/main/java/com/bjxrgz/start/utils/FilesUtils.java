package com.bjxrgz.start.utils;

import com.bjxrgz.base.utils.FileUtils;
import com.bjxrgz.base.utils.StringUtils;
import com.bjxrgz.start.base.MyApp;

import java.io.File;

/**
 * Created by JiangZhiGuo on 2016-10-31.
 * describe 文件管理
 */
public class FilesUtils {

    public static File createJPGInRes() {
        String fileName = StringUtils.getRandom(8) + ".jpg";
        File jpgFile = new File(MyApp.get().getAppInfo().getResDir(), fileName);
        FileUtils.createFileByDeleteOldFile(jpgFile);
        return jpgFile;
    }

    public static File createJPGInFiles() {
        String fileName = StringUtils.getRandom(8) + ".jpg";
        File jpgFile = new File(MyApp.get().getAppInfo().getFilesDir(), fileName);
        FileUtils.createFileByDeleteOldFile(jpgFile);
        return jpgFile;
    }

    public static File createAPKInRes(String versionName) {
        String fileName = versionName + ".apk";
        File apkFile = new File(MyApp.get().getAppInfo().getResDir(), fileName);
        FileUtils.createFileByDeleteOldFile(apkFile);
        return apkFile;
    }

}
