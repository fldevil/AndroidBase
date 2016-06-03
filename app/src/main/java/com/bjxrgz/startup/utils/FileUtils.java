package com.bjxrgz.startup.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

import com.bjxrgz.startup.base.MyApp;

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
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
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
     * 格式化文件大小
     */
    public static String getSize(Context context, long fileLength) {

        return Formatter.formatFileSize(context, fileLength);
    }

    /**
     * ************************************日志******************************
     * <p>
     * 记录日志
     */
    public static void writeLogFile(boolean printLog, String content, boolean isEncrypt) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        if (printLog) {
            writeFile(MyApp.getLogPath(), content, isEncrypt);
        }
    }

    /**
     * 写日志文件
     */
    private static void writeFile(String name, String content, boolean isEncrypt) {

        OutputStreamWriter osw = null;
        try {
            File file = new File(name);
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            } else if (file.length() > 1024 * 60) {// 60k
                file.delete();
                file.createNewFile();
            }
            osw = new OutputStreamWriter(new FileOutputStream(file, true));
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss|SSS");
            String time = "---" + (sdf.format(new Date()));
            String logStr = content + time;
            if (isEncrypt) {
                logStr = Base64.encode(logStr.getBytes("utf-8"));
            }
            osw.write(logStr + "\n");
        } catch (Exception ex) {
            Log.e(MyApp.LOG_TAG, ((ex.getMessage() == null) ? "" : ex.getMessage()));
        } finally {
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e) {
                    Log.e(MyApp.LOG_TAG, ((e.getMessage() == null) ? "" : e.getMessage()));
                }
            }
        }
    }

    /**
     * 写日志
     */
    public static void writeLogFile(boolean printLog, Exception e) {
        if (printLog) {
            try {
                e.printStackTrace(new PrintStream(MyApp.getLogPath()));
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (Exception ex) {
                Log.e(MyApp.LOG_TAG, ((ex.getMessage() == null) ? "" : ex.getMessage()));
            }
        }
    }

    /**
     * 读 文本文件
     */
    public static String readTxtFile(File fileName) throws Exception {
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
        //System.out.println("读取出来的文件内容是�?" + "\r\n" + result);
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
            Log.e(MyApp.LOG_TAG, "FileUtils->hashFile:" + e.toString());
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
            // Log.e(this.getClass().getSimpleName(), "IOException", e);
            // cancel(true);
        } catch (Exception e) {
            // Log.e(this.getClass().getSimpleName(), "Exception", e);
            // cancel(true);
        }
    }

    /**
     * ************************************系统文件流******************************
     * <p>
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
     * context 关闭Asset资源
     */
    public static void closeAsset(Context context) {
        AssetManager assets = context.getAssets();

        assets.close();
    }

    /**
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

        return deleteFile(getExternalFilesDir(context), context);
    }

    /**
     * SDCard/Android/data/你的应用包名/cache/目录
     */
    public static File getExternalCacheDir(Context context) {

        return context.getExternalCacheDir();
    }

    public static int cleanExternalCacheDir(Context context) {

        return deleteFile(getExternalCacheDir(context), context);
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

        return deleteFile(getFilesDir(context), context);
    }

    /**
     * /data/data/<application package>/cache/目录
     */
    public static File getCacheDir(Context context) {

        return context.getCacheDir();
    }

    public static int cleanCacheDir(Context context) {

        return deleteFile(getCacheDir(context), context);
    }

    /**
     * /data/data/<application package>/shared_prefs/目录
     */
    public static File getSharePreferenceDir(Context context) {

        return new File("/data/data/" + context.getPackageName() + "/shared_prefs");
    }

    public static int cleanSharePreferenceDir(Context context) {

        return deleteFile(getSharePreferenceDir(context), context);
    }

    /**
     * /data/data/<application package>/databases/目录
     */
    public static File getDataBasesDir(Context context) {

        return new File("/data/data/" + context.getPackageName() + "/databases");
    }

    public static int cleanDataBasesDir(Context context) {

        return deleteFile(getDataBasesDir(context), context);
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

        return getSize(context, freeSpace);
    }


    /**
     * 内存最大空间
     */
    public static String getMemoryMax(Context context) {
        long maxMemory = Runtime.getRuntime().maxMemory();

        return getSize(context, maxMemory);
    }

    /**
     * 内存总共空间
     */
    public static String getMemoryTotal(Context context) {
        long totalMemory = Runtime.getRuntime().totalMemory();

        return getSize(context, totalMemory);
    }

    /**
     * 内存剩余空间
     */
    public static String getMemoryFree(Context context) {
        long freeSpace = Runtime.getRuntime().freeMemory();

        return getSize(context, freeSpace);
    }

    /**
     * 内存总共空间
     */
    public static String getInterTotal(Context context) {
        long totalSpace = Environment.getDataDirectory().getTotalSpace();

        return getSize(context, totalSpace);
    }

    /**
     * 内存剩余空间
     */
    public static String getInterFree(Context context) {
        long freeSpace = Environment.getDataDirectory().getFreeSpace();

        return getSize(context, freeSpace);
    }

    /**
     * 内存使用空间
     */
    public static String getInterUsable(Context context) {
        long usableSpace = Environment.getDataDirectory().getUsableSpace();

        return getSize(context, usableSpace);
    }

    /**
     * ************************************外存空间************************************
     * <p>
     * 外存总共空间
     */
    public static String getExternalTotal(Context context) {
        long totalSpace = Environment.getExternalStorageDirectory().getTotalSpace();

        return getSize(context, totalSpace);
    }

    /**
     * 外存剩余空间
     */
    public static String getExternalFree(Context context) {
        long freeSpace = Environment.getExternalStorageDirectory().getFreeSpace();

        return getSize(context, freeSpace);
    }

    /**
     * 外存使用空间
     */
    public static String getExternalUsable(Context context) {
        long usableSpace = Environment.getExternalStorageDirectory().getUsableSpace();

        return getSize(context, usableSpace);
    }

    /**
     * ************************************文件管理************************************
     * <p>
     * 创建文件或者文件夹
     */
    public static boolean createFile(File file, Context context) {
        if (file.exists()) {
            Toast.makeText(context, "文件已存在", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (file.mkdirs()) {
            Toast.makeText(context, "文件创建成功", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(context, "文件创建失败", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * 删除文件或文件夹
     */
    public static int deleteFile(File file, Context context) {
        int number = 0;
        if (file == null || !file.exists()) {
            Toast.makeText(context, "文件不存在", Toast.LENGTH_SHORT).show();
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
            number += deleteD(file);
        }
        return number;
    }

    /**
     * 删除文件夹
     */
    private static int deleteD(File file) {
        int number = 0;
        File[] files = file.listFiles();

        if (files != null && files.length != 0) {
            for (File file1 : files) {
                if (file1.isFile()) {
                    if (file1.delete()) {
                        number += 1;
                    }
                } else if (file1.isDirectory()) {
                    number += deleteD(file1);
                }
            }
        }
        file.delete(); // 最后别忘了把目录给删除
        return number;
    }

    /**
     * 复制文件或者文件夹
     */
    public static int copyFile(File source, File target, Context context) {
        int number = 0;
        if (source.isFile()) {
            if (copyF(source, target, context)) {
                number = 1;
            }
        } else if (source.isDirectory()) {
            number = copyD(source, target, context);
        }
        return number;
    }

    /**
     * 复制文件夹
     */
    private static int copyD(File source, File target, Context context) {
        int number = 0;
        if (!beforeCopy(source, target, context)) {
            return number;
        }
        File[] files = source.listFiles();
        if (files.length == 0) {
            return number;
        }
        for (File file : files) {
            if (file.isFile()) {
                File targetFile = new File(target.getAbsolutePath() + File.separator + file.getName());

                if (copyF(file, targetFile, context)) {
                    number += 1;
                }
            } else if (file.isDirectory()) {
                File targetDir = new File(target.getAbsolutePath() + File.separator + file.getName());
                number += copyD(file, targetDir, context);
            }
        }
        return number;
    }

    /**
     * 复制文件
     */
    private static boolean copyF(File source, File target, Context context) {
        if (!beforeCopy(source, target, context)) {
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
    private static boolean beforeCopy(File source, File target, Context context) {
        if (!source.exists()) {
            Toast.makeText(context, "源文件不存在", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!source.canRead()) {
            Toast.makeText(context, "源文件不可读", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (target.exists()) {
            Toast.makeText(context, "目标文件已存在", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (!createFile(target, context)) {
                return false;
            }
        }
        if (!target.canWrite()) {
            Toast.makeText(context, "目标文件不可写", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
