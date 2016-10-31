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
     *
     * @param content 分享信息
     * @return 意图
     */
    public static Intent getShareIntent(String content) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        return intent;
    }

    /**
     * 获取分享图片的意图
     *
     * @param content 文本
     * @param image   图片文件
     * @return intent
     */
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
     * 相册意图
     */
    public static Intent getPictureIntent() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        } else {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        return intent;
    }

    /**
     * 照片: 在onActivityResult中执行
     */
    public static Bitmap getPictureBitmap(Context context, Intent data) {
        if (data == null) {
            return null;
        } else {
            Uri uri = getUri(context, data);
            InputStream stream = openInput(context, uri);
            return BitmapFactory.decodeStream(stream);
        }
    }

    /**
     * 压缩后的照片: 在onActivityResult中执行
     */
    public static Bitmap getPictureBitmap(Context context, Intent data, double maxSize) {
        if (data == null) {
            return null;
        } else {
            Uri uri = getUri(context, data);
            InputStream stream = openInput(context, uri);
            return ImageUtils.getBitmap(stream, maxSize);
        }
    }

    /**
     * 压缩后的照片: 在onActivityResult中执行
     */
    public static Bitmap getPictureBitmap(Context context, Intent data, int maxWidth, int maxHeight) {
        if (data == null) {
            return null;
        } else {
            Uri uri = getUri(context, data);
            InputStream stream = openInput(context, uri);
            return ImageUtils.getBitmap(stream, maxWidth, maxHeight);
        }
    }

    /**
     * 拍照 , 不加保存路径的话，图片会被压缩保存
     */
    public static Intent getCameraIntent(File cameraFile) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = Uri.fromFile(cameraFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        return intent;
    }

//    public static Intent buildImageGetIntent(Uri saveTo, int outputX, int outputY, boolean returnData) {
//        return buildImageGetIntent(saveTo, 1, 1, outputX, outputY, returnData);
//    }
//
//    public static Intent buildImageGetIntent(Uri saveTo, int aspectX, int aspectY,
//                                             int outputX, int outputY, boolean returnData) {
//        Intent intent = new Intent();
//        if (Build.VERSION.SDK_INT < 19) {
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//        } else {
//            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//        }
//        intent.setType("image*//*");
//        intent.putExtra("output", saveTo);
//        intent.putExtra("aspectX", aspectX);
//        intent.putExtra("aspectY", aspectY);
//        intent.putExtra("outputX", outputX);
//        intent.putExtra("outputY", outputY);
//        intent.putExtra("scale", true);
//        intent.putExtra("return-data", returnData);
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
//        return intent;
//    }
//
//    public static Intent buildImageCropIntent(Uri uriFrom, Uri uriTo, int outputX, int outputY, boolean returnData) {
//        return buildImageCropIntent(uriFrom, uriTo, 1, 1, outputX, outputY, returnData);
//    }
//
//    public static Intent buildImageCropIntent(Uri uriFrom, Uri uriTo, int aspectX, int aspectY,
//                                              int outputX, int outputY, boolean returnData) {
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uriFrom, "image*//*");
//        intent.putExtra("crop", "true");
//        intent.putExtra("output", uriTo);
//        intent.putExtra("aspectX", aspectX);
//        intent.putExtra("aspectY", aspectY);
//        intent.putExtra("outputX", outputX);
//        intent.putExtra("outputY", outputY);
//        intent.putExtra("scale", true);
//        intent.putExtra("return-data", returnData);
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
//        return intent;
//    }

    /**
     * 照片: 在onActivityResult中执行
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

    /**
     * 压缩后的照片: 在onActivityResult中执行
     */
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
            Bitmap adjust = adjust(cameraFile); // 摆正角度
            FileUtils.deleteFile(cameraFile);
            return adjust;
        }
    }

    /**
     * 压缩后的照片: 在onActivityResult中执行
     */
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
            Bitmap adjust = adjust(cameraFile); // 摆正角度
            FileUtils.deleteFile(cameraFile);
            return adjust;
        }
    }

    /*获取Uri的输入流, 相册选取图片时可读取 data.getData()*/
    private static InputStream openInput(Context context, Uri uri) {
        try {
            return context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*解决小米手机上获取图片路径为null的情况*/
    private static Uri getUri(Context context, Intent intent) {
        Uri uri = intent.getData();
        String type = intent.getType(); // 小米的type不是null 其他的是
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
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

    /*获取图片旋转角度*/
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
