package com.bjxrgz.base.utils;

import android.content.Context;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * @author Fan
 * 硬盘缓存帮助类
 */

public class DiskLruCacheUtil {

    private static final String DIR_NAME = "diskCache";
    private static final int MAX_COUNT = 5 * 1024 * 1024;
    private static final int DEFAULT_APP_VERSION = 1;

    private Context mContext;
    private DiskLruCache mDiskLruCache;

    public DiskLruCacheUtil(Context context,File dir) throws IOException {
        mContext = context;
        mDiskLruCache = generateCache(dir, MAX_COUNT);
    }

    private DiskLruCache generateCache(File dir, int maxCount) throws IOException {
        if (!dir.exists() || !dir.isDirectory()) {
            throw new IllegalArgumentException(
                    dir + " is not a directory or does not exists. ");
        }

        int appVersion = AppUtil.getVersionCode(mContext);

        return DiskLruCache.open(dir, appVersion, DEFAULT_APP_VERSION, maxCount);
    }

    /**
     * editor base
     */
    private DiskLruCache.Editor editor(String key) {
        try {
            //wirte DIRTY
            DiskLruCache.Editor edit = mDiskLruCache.edit(EncryptUtil.encryptMD5ToString(key));
            //edit maybe null :the entry is editing
            if (edit == null) {
                LogUtil.e("the entry spcified key:" + key + " is editing by other . ");
            }
            return edit;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get base
     */
    private InputStream get(String key) {
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(EncryptUtil.encryptMD5ToString(key));
            if (snapshot == null) //not find entry , or entry.readable = false
            {
                LogUtil.e("not find entry , or entry.readable = false");
                return null;
            }
            //write READ
            return snapshot.getInputStream(0);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 存取string类型
     */
    public void put(String key, String value) {
        DiskLruCache.Editor edit = null;
        BufferedWriter bw = null;
        try {
            edit = editor(key);
            if (edit == null) return;
            OutputStream os = edit.newOutputStream(0);
            bw = new BufferedWriter(new OutputStreamWriter(os));
            bw.write(value);
            edit.commit();//write CLEAN
        } catch (IOException e) {
            e.printStackTrace();
            try {
                //s
                edit.abort();//write REMOVE
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                if (bw != null)
                    bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取string类型缓存
     */
    public String getAsString(String key) {
        InputStream inputStream = null;
        try {
            //write READ
            inputStream = get(key);
            if (inputStream == null) return null;
            StringBuilder sb = new StringBuilder();
            int len = 0;
            byte[] buf = new byte[128];
            while ((len = inputStream.read(buf)) != -1) {
                sb.append(new String(buf, 0, len));
            }
            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
            try {
                inputStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }
}
