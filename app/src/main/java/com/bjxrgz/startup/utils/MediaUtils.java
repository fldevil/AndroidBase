package com.bjxrgz.startup.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import com.bjxrgz.startup.base.MyApp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiang on 2016/10/13
 * <p>
 * describe 多媒体管理工具类
 */
public class MediaUtils {

    /**
     * ********************************意图*********************************
     * 获取分享文本的意图
     */
    public static Intent getShareIntent(String content) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, content); // 设置分享信息
        return intent;
    }

    public static Intent getShareIntent(String content, File image) {
        if (!FileUtils.isFileExists(image)) return null;
        return getShareIntent(content, Uri.fromFile(image));
    }

    public static Intent getShareIntent(String content, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("image/*");
        return intent;
    }

    /**
     * 拍照 ,不加保存路径，图片会被压缩
     */
    public static Intent getCameraIntent(File cameraFile) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = Uri.fromFile(cameraFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        return intent;
    }

    /**
     * 相册
     */
    public static Intent getPictureIntent() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        } else {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }
        if (intent.resolveActivity(MyApp.getInstance().getPackageManager()) == null) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }
        if (intent.resolveActivity(MyApp.getInstance().getPackageManager()) == null) {
            intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        intent.setType("image/*");
        return intent;
    }

    /**
     * 裁剪(通用)
     * 1.启动拍照
     * 2.在onActivityForResult里调用此方法，启动裁剪功能
     * 3.再次在onActivityForResult里先删除(File from) ,再调用getCameraBitmap(save)处理
     */
    public static Intent getCropIntent(File from, File save) {
        return getCropIntent(from, save, 1, 1, 300, 300);
    }

    public static Intent getCropIntent(File from, File save, int aspectX,
                                       int aspectY, int outputX, int outputY) {
        Intent intent = new Intent();
        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(from), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX); // 裁剪框比例
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", outputX); // 输出图片大小(太大会传输失败)
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true); // 缩放
        intent.putExtra("noFaceDetection", true); // 取消人脸识别功能
        intent.putExtra("return-data", true); // 返回数据
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(save)); // 返回数据路径
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString()); // 返回数据格式
        return intent;
    }

    /**
     * 裁剪(相册)
     * 1.直接调用此方法
     * 2.在onActivityForResult里调用getCameraBitmap(save)处理
     */
    public static Intent getPictureCropIntent(File save) {
        return getPictureCropIntent(save, 1, 1, 300, 300);
    }

    public static Intent getPictureCropIntent(File save, int aspectX, int aspectY,
                                              int outputX, int outputY) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX); // 裁剪框比例 1:1
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", outputX); // 输出图片大小(太大会传输失败)
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true); // 缩放
        intent.putExtra("noFaceDetection", true); // 取消人脸识别功能
        intent.putExtra("return-data", true); // 返回数据
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(save)); // 返回数据路径
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString()); // 返回数据格式
        return intent;
    }

    /**
     * 获取拍照图片: 在onActivityResult中执行,源文件会删除
     */
    public static Bitmap getCameraBitmap(File cameraFile) {
        if (cameraFile == null || cameraFile.length() == 0) {
            FileUtils.deleteFile(cameraFile); // 删除垃圾文件
            return null;
        } else {
            Bitmap adjust = adjust(cameraFile);// 摆正角度
            FileUtils.deleteFile(cameraFile); // 删除源文件
            return adjust;
        }
    }

    public static Bitmap getCameraBitmap(File cameraFile, double maxSize) {
        if (cameraFile == null || cameraFile.length() == 0) {
            FileUtils.deleteFile(cameraFile); // 删除垃圾文件
            return null;
        } else {
            Bitmap small = ImageUtils.getBitmap(cameraFile, maxSize); // File转Bitmap(压缩)
            FileUtils.createFileByDeleteOldFile(cameraFile); // 删除源文件
            ImageUtils.save(small, cameraFile.getAbsolutePath(), ImageUtils.FORMAT, true); // 保存图像
            if (small != null && !small.isRecycled()) {
                small.recycle();
            }
            return getCameraBitmap(cameraFile);
        }
    }

    public static Bitmap getCameraBitmap(File cameraFile, int maxWidth, int maxHeight) {
        if (cameraFile == null || cameraFile.length() == 0) {
            FileUtils.deleteFile(cameraFile); // 删除垃圾文件
            return null;
        } else {
            Bitmap small = ImageUtils.getBitmap(cameraFile, maxWidth, maxHeight); // File转Bitmap(压缩)
            FileUtils.createFileByDeleteOldFile(cameraFile); // 删除源文件
            ImageUtils.save(small, cameraFile.getAbsolutePath(), ImageUtils.FORMAT, true); // 保存图像
            if (small != null && !small.isRecycled()) {
                small.recycle();
            }
            return getCameraBitmap(cameraFile);
        }
    }

    /**
     * 获取相册图片: 在onActivityResult中执行 data.getData()
     */
    public static Bitmap getPictureBitmap(Context context, Intent picture) {
        InputStream stream = getPictureStream(context, picture);
        if (stream != null) {
            return BitmapFactory.decodeStream(stream);
        }
        return null;
    }

    public static Bitmap getPictureBitmap(Context context, Intent picture, double maxSize) {
        InputStream stream = getPictureStream(context, picture);
        if (stream != null) {
            return ImageUtils.getBitmap(stream, maxSize);
        }
        return null;
    }

    public static Bitmap getPictureBitmap(Context context, Intent picture, int maxWidth, int maxHeight) {
        InputStream stream = getPictureStream(context, picture);
        if (stream != null) {
            return ImageUtils.getBitmap(stream, maxWidth, maxHeight);
        }
        return null;
    }

    /*摆正图片旋转角度*/
    private static Bitmap adjust(File file) {
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        int degree = getRotateDegree(file.getAbsolutePath());
        if (degree != 0) {
            Matrix m = new Matrix();
            m.setRotate(degree);
            Bitmap adjust = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            return adjust;
        } else {
            return bitmap;
        }
    }

    /* 获取图片旋转角度 */
    private static int getRotateDegree(String filePath) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(filePath);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            if (orientation != -1) {
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /* 解决小米手机上获取图片路径为null的情况 */
    private static Uri getUri(Context context, Intent intent) {
        if (intent == null) {
            return null;
        }
        Uri uri = intent.getData();
        String type = intent.getType(); // 小米的type不是null 其他的是
        if (uri != null && uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuilder buff = new StringBuilder();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.ImageColumns._ID},
                        buff.toString(), null, null);
                int index = 0;
                if (cur != null) {
                    for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                        index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                        index = cur.getInt(index);
                    }
                    cur.close();
                }
                if (index > 0) {
                    Uri uri_temp = Uri.parse("content://media/external/images/media/" + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }

    /* 相册返回流 data.getData()*/
    private static InputStream getPictureStream(Context context, Intent picture) {
        if (picture != null) {
            Uri uri = getUri(context, picture);
            if (uri != null) {
                try {
                    return context.getContentResolver().openInputStream(uri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * ********************************数据*********************************
     *
     * @param uri        查询的uri
     * @param projection map里的key
     * @param orderBy    排序
     * @return 查询到的数据
     */
    private static List<Map<String, String>> getContentProvider(Context context, Uri uri,
                                                                String[] projection,
                                                                String orderBy) {
        List<Map<String, String>> list = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, orderBy);
        if (null == cursor) {
            return list;
        }
        while (cursor.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            for (int i = 0; i < projection.length; i++) {
                map.put(projection[i], cursor.getString(i));
                System.out.println(projection[i] + ":" + cursor.getString(i));
            }
            list.add(map);
        }
        cursor.close();
        return list;
    }

    /**
     * 获取设备里的所有图片信息
     */
    public static List<Map<String, String>> getImage(Context context) {
        String[] projection = {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.TITLE,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.SIZE};
        String orderBy = MediaStore.Images.Media.DISPLAY_NAME;
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        return getContentProvider(context, uri, projection, orderBy);
    }

    /**
     * 获取设备里的所有音频信息
     */
    public static List<Map<String, String>> getAudio(Context context) {
        String[] projection = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.MIME_TYPE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.SIZE};
        String orderBy = MediaStore.Audio.Media.DISPLAY_NAME;
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        return getContentProvider(context, uri, projection, orderBy);
    }

    /**
     * 获取设备里的所有视频信息
     */
    public static List<Map<String, String>> getVideo(Context context) {
        String[] projection = {MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.SIZE};
        String orderBy = MediaStore.Video.Media.DISPLAY_NAME;
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        return getContentProvider(context, uri, projection, orderBy);
    }

}
