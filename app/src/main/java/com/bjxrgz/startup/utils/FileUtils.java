package com.bjxrgz.startup.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by fd.meng on 2014/03/30
 * <p>
 * 文件处理类
 */
public class FileUtils {

    /**
     * 访问Asset资源
     */
    public static InputStream openAsset(Context context, String fileName) {
        AssetManager assets = context.getAssets();
        try {
            return assets.open(fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭Asset资源
     */
    public static void closeAsset(Context context) {
        AssetManager assets = context.getAssets();

        assets.close();
    }

    /**
     * ************************************文件流******************************
     * 打开内部文件输出流
     */
    public static FileOutputStream openFileOutput(Context context, String file) {
        FileOutputStream outputStream = null;
        try {
            outputStream = context.openFileOutput(file, Context.MODE_PRIVATE);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return outputStream;
    }

    /**
     * 打开内部文件输入流
     */
    public static FileInputStream openFileInput(Context context, String file) {
        FileInputStream inputStream = null;
        try {
            inputStream = context.openFileInput(file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    public static boolean getFile(InputStream is, File target) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(target);
            byte[] buffer = new byte[1024];
            int len;

            while ((len = is.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
            fileOutputStream.flush();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean getFile(byte[] content, File target) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(target);
            fos.write(content);
            return true;

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static byte[] getBytes(File file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            return getBytes(fis);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static byte[] getBytes(InputStream is) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int len;
        try {
            while ((len = is.read(bytes)) != -1) {
                bos.write(bytes, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读 文本文件
     */
    public static String readFile(File fileName) throws Exception {
        String result = "";
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);
            try {
                String read = null;
                while ((read = bufferedReader.readLine()) != null) {
                    result = result + read;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (fileReader != null) {
                fileReader.close();
            }
        }
        LogUtils.d("读取出来的文件内容是--->\\r\\n" + result);
        return result;
    }

    /**
     * get file's CRC32 hash code
     */
    public static String hashFile(String fileName) {
        File f = new File(fileName);
        f.hashCode();
        try { // 对文件进行crc校验
            // long begin = System.currentTimeMillis();
            // FileInputStream in = new
            // FileInputStream("e:\\developer_tools\\SnowOSX3.6.iso");//
            // 指定目标文件
            FileInputStream in = new FileInputStream(fileName);// 指定目标文件
            FileChannel channel = in.getChannel(); // 从文件中获取�?个�?�道
            CRC32 crc = new CRC32();
            int length = (int) channel.size();
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, length); // 用只读模式从该�?�道获取字节缓冲，实现文件到内存的映�?
            for (int i = 0; i < length; i++) {
                int c = buffer.get(i);
                crc.update(c);// 按字节做crc
            }

            return (Long.toHexString(crc.getValue())).toUpperCase();

            // /System.out.println("crc code�?" +
            // (Long.toHexString(crc.getValue())).toUpperCase());
            // long end = System.currentTimeMillis();
            // System.out.println("运行" + (end - begin)/1000 + "s");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e);
        }
        return "";
    }

    /**
     * 解压
     */
    public static void unZip(String zipFile, String outPath) {
        InputStream is;
        ZipInputStream zis;
        try {
            // InputStream from File.
            // is = new FileInputStream(FILEPATHIN);

            // InputStream from URL
            is = new FileInputStream(zipFile);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry zipEntry;

            // INTERNAL STORAGE
            // baseFolder = new File(c.getFilesDir(),
            // Constants.FOLDER_ZIP_OUTPUT);

            // EXTERNAL STORAGE
            File baseFolder = new File(outPath);
            if (!baseFolder.exists() && !baseFolder.isDirectory()) {
                baseFolder.mkdirs();
            }

            while ((zipEntry = zis.getNextEntry()) != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int count;

                String zipEntryName = zipEntry.getName();

                if (zipEntry.isDirectory()) {
                    // FOLDER
                    File zipEntryFolder = new File(outPath + File.separator + zipEntryName);
                    if (!zipEntryFolder.isDirectory()) {
                        zipEntryFolder.mkdirs();
                    }
                } else {
                    // FILE
                    FileOutputStream fout = new FileOutputStream(outPath + File.separator + zipEntryName);
                    // reading and writing
                    while ((count = zis.read(buffer)) != -1) {
                        baos.write(buffer, 0, count);
                        byte[] bytes = baos.toByteArray();
                        fout.write(bytes);
                        baos.reset();
                    }

                    fout.close();
                    zis.closeEntry();
                }
            }

            zis.close();
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e(e);
            // Log.e(this.getClass().getSimpleName(), "IOException", e);
            // cancel(true);
        }
    }

    /**
     * ************************************外存************************************
     * <p>
     * SDCard是否存在
     */
    public static boolean isExternalExist() {

        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * SDCard
     */
    public static File getExternalDir() {

        return Environment.getExternalStorageDirectory();
    }

    /**
     * SDCard/Android/data/你的应用的包名/files/目录
     */
    public static File getExternalFilesDir(Context context) {

        return context.getExternalFilesDir("");
    }

    public static int cleanExternalFilesDir(Context context) {

        return deleteFile(getExternalFilesDir(context));
    }

    /**
     * SDCard/Android/data/你的应用包名/cache/目录
     */
    public static File getExternalCacheDir(Context context) {

        return context.getExternalCacheDir();
    }

    public static int cleanExternalCacheDir(Context context) {

        return deleteFile(getExternalCacheDir(context));
    }

    /**
     * ************************************内存************************************
     * <p>
     * 内部存储文件
     */
    public static File getInternalFile(Context context, String name, int mode) {

        return context.getDir(name, mode);
    }

    public static boolean cleanInternalDir(Context context, String name) {

        return context.deleteFile(name);
    }

    /**
     * /data/data/<application package>/files/目录
     */
    public static File getFilesDir(Context context) {

        return context.getFilesDir();
    }

    public static int cleanFilesDir(Context context) {

        return deleteFile(getFilesDir(context));
    }

    /**
     * /data/data/<application package>/cache/目录
     */
    public static File getCacheDir(Context context) {

        return context.getCacheDir();
    }

    public static int cleanCacheDir(Context context) {

        return deleteFile(getCacheDir(context));
    }

    /**
     * /data/data/<application package>/shared_prefs/目录
     */
    public static File getSharePreferenceDir(Context context) {

        return new File("/data/data/" + context.getPackageName() + "/shared_prefs");
    }

    public static int cleanSharePreferenceDir(Context context) {

        return deleteFile(getSharePreferenceDir(context));
    }

    /**
     * /data/data/<application package>/databases/目录
     */
    public static File getDataBasesDir(Context context) {

        return new File("/data/data/" + context.getPackageName() + "/databases");
    }

    public static int cleanDataBasesDir(Context context) {

        return deleteFile(getDataBasesDir(context));
    }

    /**
     * /data/data/<application package>/databases/dbName
     */
    public static File getDataBase(Context context, String dataBaseName) {

        return context.getDatabasePath(dataBaseName);
    }

    public static boolean cleanDatabase(Context context, String dbName) {

        return context.deleteDatabase(dbName);
    }

    /**
     * ************************************内存空间************************************
     * <p>
     * 获取内存总共空间
     */
    public static String getIntermaxMemory(Context context) {
        long freeSpace = Runtime.getRuntime().maxMemory();

        return StringUtils.getFileSize(context, freeSpace);
    }


    /**
     * 内存最大空间
     */
    public static String getMemoryMax(Context context) {
        long maxMemory = Runtime.getRuntime().maxMemory();

        return StringUtils.getFileSize(context, maxMemory);
    }

    /**
     * 内存总共空间
     */
    public static String getMemoryTotal(Context context) {
        long totalMemory = Runtime.getRuntime().totalMemory();

        return StringUtils.getFileSize(context, totalMemory);
    }

    /**
     * 内存剩余空间
     */
    public static String getMemoryFree(Context context) {
        long freeSpace = Runtime.getRuntime().freeMemory();

        return StringUtils.getFileSize(context, freeSpace);
    }

    /**
     * 内存总共空间
     */
    public static String getInterTotal(Context context) {
        long totalSpace = Environment.getDataDirectory().getTotalSpace();

        return StringUtils.getFileSize(context, totalSpace);
    }

    /**
     * 内存剩余空间
     */
    public static String getInterFree(Context context) {
        long freeSpace = Environment.getDataDirectory().getFreeSpace();

        return StringUtils.getFileSize(context, freeSpace);
    }

    /**
     * 内存使用空间
     */
    public static String getInterUsable(Context context) {
        long usableSpace = Environment.getDataDirectory().getUsableSpace();

        return StringUtils.getFileSize(context, usableSpace);
    }

    /**
     * ************************************外存空间************************************
     * <p>
     * 外存总共空间
     */
    public static String getExternalTotal(Context context) {
        long totalSpace = Environment.getExternalStorageDirectory().getTotalSpace();

        return StringUtils.getFileSize(context, totalSpace);
    }

    /**
     * 外存剩余空间
     */
    public static String getExternalFree(Context context) {
        long freeSpace = Environment.getExternalStorageDirectory().getFreeSpace();

        return StringUtils.getFileSize(context, freeSpace);
    }

    /**
     * 外存使用空间
     */
    public static String getExternalUsable(Context context) {
        long usableSpace = Environment.getExternalStorageDirectory().getUsableSpace();

        return StringUtils.getFileSize(context, usableSpace);
    }

    /**
     * ************************************文件管理************************************
     * <p>
     * 创建文件或者文件夹
     */
    public static boolean createFile(File file) {
        if (file.exists()) {
            LogUtils.d("文件已存在");
            return false;
        }
        if (file.mkdirs()) {
            LogUtils.d("文件创建成功");
            return true;
        } else {
            LogUtils.d("文件创建失败");
            return false;
        }
    }

    /**
     * 删除文件或文件夹
     */
    public static int deleteFile(File file) {
        int number = 0;
        if (file == null || !file.exists()) {
            LogUtils.d("文件不存在");
            return number;
        }
        if (file.isFile()) {
            if (!file.exists()) {
                return number;
            }
            if (file.delete()) {
                number += 1;
            }
        } else if (file.isDirectory()) {
            number += deleteDir(file);
        }
        return number;
    }

    /**
     * 删除文件夹
     */
    private static int deleteDir(File file) {
        int number = 0;
        File[] files = file.listFiles();

        if (files != null && files.length != 0) {
            for (File file1 : files) {
                if (file1.isFile()) {
                    if (file1.delete()) {
                        number += 1;
                    }
                } else if (file1.isDirectory()) {
                    number += deleteDir(file1);
                }
            }
        }
        file.delete(); // 最后别忘了把目录给删除
        return number;
    }

    /**
     * 复制文件或者文件夹
     */
    public static int copyFile(File source, File target) {
        int number = 0;
        if (source.isFile()) {
            if (copyStream(source, target)) {
                number = 1;
            }
        } else if (source.isDirectory()) {
            number = copyDir(source, target);
        }
        return number;
    }

    /**
     * 复制文件夹
     */
    private static int copyDir(File source, File target) {
        int number = 0;
        if (!beforeCopy(source, target)) {
            return number;
        }
        File[] files = source.listFiles();
        if (files.length == 0) {
            return number;
        }
        for (File file : files) {
            if (file.isFile()) {
                File targetFile = new File(target.getAbsolutePath() + File.separator + file.getName());

                if (copyStream(file, targetFile)) {
                    number += 1;
                }
            } else if (file.isDirectory()) {
                File targetDir = new File(target.getAbsolutePath() + File.separator + file.getName());
                number += copyDir(file, targetDir);
            }
        }
        return number;
    }

    /**
     * 复制文件
     */
    private static boolean copyStream(File source, File target) {
        if (!beforeCopy(source, target)) {
            return false;
        }
        BufferedInputStream is = null;
        BufferedOutputStream os = null;
        try {
            is = new BufferedInputStream(new FileInputStream(source));
            os = new BufferedOutputStream(new FileOutputStream(target));

            int len;
            byte[] buf = new byte[2048];

            while ((len = is.read(buf)) != -1) {
                os.write(buf, 0, len);
            }
            os.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 复制前的准备工作
     */
    private static boolean beforeCopy(File source, File target) {
        if (!source.exists()) {
            LogUtils.d("源文件不存在");
            return false;
        }
        if (!source.canRead()) {
            LogUtils.d("源文件不可读");
            return false;
        }
        if (target.exists()) {
            LogUtils.d("目标文件已存在");
            return false;
        } else {
            if (!createFile(target)) {
                return false;
            }
        }
        if (!target.canWrite()) {
            LogUtils.d("目标文件不可写");
            return false;
        }
        return true;
    }

}
