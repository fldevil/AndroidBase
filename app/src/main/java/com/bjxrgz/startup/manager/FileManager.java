package com.bjxrgz.startup.manager;

import com.bjxrgz.startup.base.MyApp;
import com.bjxrgz.startup.utils.FileUtils;
import com.bjxrgz.startup.utils.StringUtils;

import java.io.File;

/**
 * Created by JiangZhiGuo on 2016-10-31.
 * describe 文件管理
 */

public class FileManager {

    public static File createJPGInRes() {
        String fileName = StringUtils.getRandom(8) + ".jpg";
        File jpgFile = new File(MyApp.get().getAppInfo().getResDir(), fileName);
        FileUtils.createOrExistsFile(jpgFile);
        return jpgFile;
    }

    public static File createJPGInFiles() {
        String fileName = StringUtils.getRandom(8) + ".jpg";
        File jpgFile = new File(MyApp.get().getAppInfo().getFilesDir(), fileName);
        FileUtils.createOrExistsFile(jpgFile);
        return jpgFile;
    }

    public static File createAPKInRes(String versionName) {
        String fileName = versionName + ".apk";
        File apkFile = new File(MyApp.get().getAppInfo().getResDir(), fileName);
        FileUtils.createOrExistsFile(apkFile);
        return apkFile;
    }

}
