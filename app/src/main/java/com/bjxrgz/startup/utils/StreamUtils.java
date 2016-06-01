package com.bjxrgz.startup.utils;

import android.graphics.Bitmap;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by fd.meng on 2014/03/30
 * <p>
 * InputStream，byte，bitmap，file，string之间的转换处理类
 */
public class StreamUtils {

    /**
     * ***********************************Bitmap***********************************
     */
    private void getBitmap() {
        // BitmapFactory.decode...已经封装好了
    }

    /**
     * ***********************************byte[]***********************************
     */
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

    public static byte[] getBytes(Bitmap bmp) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        bmp.compress(Bitmap.CompressFormat.PNG, 100, bos);// PNG 是无损压缩

        return bos.toByteArray();
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

    /**
     * ***********************************File***********************************
     */
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

    public static boolean getFile(Bitmap bmp, File target) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(target);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.flush();
                }
                if (fos != null) {
                    fos.close();
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

    /**
     * ***********************************InputStream***********************************
     */
    public static InputStream getInputStream(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InputStream getInputStream(Bitmap bitmap) {

        byte[] bytes = getBytes(bitmap);

        return new ByteArrayInputStream(bytes);
    }

    public static InputStream getInputStream(byte[] bytes) {

        return new ByteArrayInputStream(bytes);
    }

    /**
     * ***********************************String***********************************
     */
    public static String getString(InputStream is) {
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader in = new BufferedReader(isr);

        StringBuilder builder = new StringBuilder();
        String line;
        try {
            while ((line = in.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                isr.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }

    public static String getString(File file) {

        return getString(getInputStream(file));
    }

}
