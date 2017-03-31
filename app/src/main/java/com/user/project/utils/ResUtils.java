package com.user.project.utils;

import com.bjxrgz.base.utils.AppUtils;
import com.bjxrgz.base.utils.FileUtils;
import com.bjxrgz.base.utils.StringUtils;

import java.io.File;

/**
 * Created by JiangZhiGuo on 2016-10-31.
 * describe 文件管理
 */
public class ResUtils {

    public static File createJPGInFiles() {
        String fileName = StringUtils.getUUID(8) + ".jpg";
        File jpgFile = new File(AppUtils.get().getFilesDir(), fileName);
        FileUtils.createFileByDeleteOldFile(jpgFile);
        return jpgFile;
    }

    public static File createJPGInRes() {
        String fileName = StringUtils.getUUID(8) + ".jpg";
        File jpgFile = new File(AppUtils.get().getResDir(), fileName);
        FileUtils.createFileByDeleteOldFile(jpgFile);
        return jpgFile;
    }

    public static File createAPKInRes(String versionName) {
        String fileName = versionName + ".apk";
        File apkFile = new File(AppUtils.get().getResDir(), fileName);
        FileUtils.createFileByDeleteOldFile(apkFile);
        return apkFile;
    }

}
