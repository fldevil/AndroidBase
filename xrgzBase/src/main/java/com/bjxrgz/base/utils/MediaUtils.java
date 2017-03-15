package com.bjxrgz.base.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

import com.bjxrgz.base.base.BaseApp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bjxrgz.base.utils.ImageUtils.getBitmap;

/**
 * Created by jiang on 2016/10/13
 * <p>
 * describe 多媒体管理工具类
 */
public class MediaUtils {

    /**
     * 获取拍照图片: 在onActivityResult中执行
     */
    public static Bitmap getCameraBitmap(File cameraFile) {
        if (FileUtils.isFileEmpty(cameraFile)) {
            FileUtils.deleteFile(cameraFile); // 删除垃圾文件
            return null;
        }
        Bitmap adjust = adjust(cameraFile);// 摆正角度
        FileUtils.deleteFile(cameraFile); // 删除源文件
        return adjust;
    }

    public static Bitmap getCameraBitmap(File cameraFile, double maxSize) {
        if (FileUtils.isFileEmpty(cameraFile)) {
            FileUtils.deleteFile(cameraFile); // 删除垃圾文件
            return null;
        }
        Bitmap small = getBitmap(cameraFile, maxSize); // File转Bitmap(压缩)
        FileUtils.createFileByDeleteOldFile(cameraFile); // 重置源文件
        ImageUtils.save(small, cameraFile.getAbsolutePath(), ImageUtils.FORMAT, true); // 保存图像
        if (small != null && !small.isRecycled()) {
            small.recycle();
        }
        return getCameraBitmap(cameraFile);
    }

    public static Bitmap getCameraBitmap(File cameraFile, int maxWidth, int maxHeight) {
        if (FileUtils.isFileEmpty(cameraFile)) {
            FileUtils.deleteFile(cameraFile); // 删除垃圾文件
            return null;
        }
        Bitmap small = getBitmap(cameraFile, maxWidth, maxHeight); // File转Bitmap(压缩)
        FileUtils.createFileByDeleteOldFile(cameraFile); // 重置源文件
        ImageUtils.save(small, cameraFile.getAbsolutePath(), ImageUtils.FORMAT, true); // 保存图像
        if (small != null && !small.isRecycled()) {
            small.recycle();
        }
        return getCameraBitmap(cameraFile);
    }

    /**
     * 获取相册图片: 在onActivityResult中执行 data.getData()
     */
    public static Uri getPictureUri(Intent data) {
        if (data == null) return null;
        Uri uri = data.getData();
        if (uri == null) return null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_FILE) &&
                (data.getType().contains("image/"))) { // 小米的type不是null 其他的是
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = BaseApp.get().getContentResolver();
                String buff = "(" + MediaStore.Images.ImageColumns.DATA + "=" +
                        "'" + path + "'" + ")";
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.ImageColumns._ID},
                        buff, null, null);
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
                        return uri_temp;
                    }
                }
            }
        }
        return uri;
    }

    public static Bitmap getPictureBitmap(Context context, Intent data) {
        if (data == null) return null;
        Bitmap picture = null;
        Uri uri = getPictureUri(data);
        if (uri != null) {
            try {
                InputStream stream = context.getContentResolver().openInputStream(uri);
                picture = BitmapFactory.decodeStream(stream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            picture = data.getParcelableExtra("data");
        }
        return picture;
    }

    public static Bitmap getPictureBitmap(Context context, Intent data, double maxSize) {
        if (data == null) return null;
        Bitmap picture = null;
        Uri uri = getPictureUri(data);
        if (uri != null) {
            try {
                InputStream stream = context.getContentResolver().openInputStream(uri);
                picture = getBitmap(stream, maxSize);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            picture = data.getParcelableExtra("data");
        }
        return picture;
    }

    public static Bitmap getPictureBitmap(Context context, Intent data, int maxWidth, int maxHeight) {
        if (data == null) return null;
        Bitmap picture = null;
        Uri uri = getPictureUri(data);
        if (uri != null) {
            try {
                InputStream stream = context.getContentResolver().openInputStream(uri);
                picture = getBitmap(stream, maxWidth, maxHeight);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            picture = data.getParcelableExtra("data");
        }
        return picture;
    }

    /**
     * 获取裁剪图片: 在onActivityResult中执行,记得删除源文件!!
     */
    public static Bitmap getCropBitmap(File cropFile) {
        if (FileUtils.isFileEmpty(cropFile)) {
            FileUtils.deleteFile(cropFile); // 删除垃圾文件
            return null;
        } else {
            return BitmapFactory.decodeFile(cropFile.getAbsolutePath());
        }
    }

    public static Bitmap getCropBitmap(File cropFile, double maxSize) {
        if (FileUtils.isFileEmpty(cropFile)) {
            FileUtils.deleteFile(cropFile); // 删除垃圾文件
            return null;
        }
        Bitmap small = getBitmap(cropFile, maxSize); // File转Bitmap(压缩)
        FileUtils.createFileByDeleteOldFile(cropFile); // 重置源文件
        ImageUtils.save(small, cropFile.getAbsolutePath(), ImageUtils.FORMAT, true); // 保存图像
        if (small != null && !small.isRecycled()) {
            small.recycle();
        }
        return getCropBitmap(cropFile);
    }

    public static Bitmap getCropBitmap(File cropFile, int maxWidth, int maxHeight) {
        if (FileUtils.isFileEmpty(cropFile)) {
            FileUtils.deleteFile(cropFile); // 重置源文件
            return null;
        }
        Bitmap small = getBitmap(cropFile, maxWidth, maxHeight); // File转Bitmap(压缩)
        FileUtils.createFileByDeleteOldFile(cropFile); // 删除源文件
        ImageUtils.save(small, cropFile.getAbsolutePath(), ImageUtils.FORMAT, true); // 保存图像
        if (small != null && !small.isRecycled()) {
            small.recycle();
        }
        return getCropBitmap(cropFile);
    }

    /* 摆正图片旋转角度 */
    private static Bitmap adjust(File file) {
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        int degree = getRotateDegree(file.getAbsolutePath());
        if (degree != 0) {
            Matrix m = new Matrix();
            m.setRotate(degree);
            Bitmap adjust = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(), bitmap.getHeight(), m, true);
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
                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
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

    /**
     * ********************************数据*********************************
     *
     * @param uri        查询的uri
     * @param projection map里的key
     * @param orderBy    排序
     * @return 查询到的数据
     */
    public static List<Map<String, String>> getProviderColumn(Uri uri, String[] projection,
                                                              String selection,
                                                              String[] selectionArgs,
                                                              String orderBy) {
        List<Map<String, String>> list = new ArrayList<>();
        Cursor cursor = BaseApp.get().getContentResolver()
                .query(uri, projection, selection, selectionArgs, orderBy);
        if (null == cursor) return list;
        while (cursor.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            for (int i = 0; i < projection.length; i++) {
                map.put(projection[i], cursor.getString(i));
            }
            list.add(map);
        }
        cursor.close();
        return list;
    }

    public static String getProviderColumnTop(Uri uri, String[] projection, String selection,
                                              String[] selectionArgs, String orderBy) {
        Cursor cursor = BaseApp.get().getContentResolver()
                .query(uri, projection, selection, selectionArgs, orderBy);

        if (cursor != null && cursor.moveToFirst()) {
            int index = cursor.getColumnIndex(projection[0]);
            if (index > -1) return cursor.getString(index);
            cursor.close();
        }
        return null;
    }

    /**
     * 获取设备里的所有图片信息
     */
    public static List<Map<String, String>> getImage() {
        String[] projection = {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.TITLE,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.SIZE};
        String orderBy = MediaStore.Images.Media.DISPLAY_NAME;
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        return getProviderColumn(uri, projection, null, null, orderBy);
    }

    /**
     * 获取设备里的所有音频信息
     */
    public static List<Map<String, String>> getAudio() {
        String[] projection = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.MIME_TYPE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.SIZE};
        String orderBy = MediaStore.Audio.Media.DISPLAY_NAME;
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        return getProviderColumn(uri, projection, null, null, orderBy);
    }

    /**
     * 获取设备里的所有视频信息
     */
    public static List<Map<String, String>> getVideo() {
        String[] projection = {MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.SIZE};
        String orderBy = MediaStore.Video.Media.DISPLAY_NAME;
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        return getProviderColumn(uri, projection, null, null, orderBy);
    }

}
