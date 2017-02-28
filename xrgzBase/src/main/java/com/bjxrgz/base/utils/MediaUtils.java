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
import android.os.Build;
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
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile));
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
            if (intent.resolveActivity(BaseApp.get().getPackageManager()) == null) {
                intent.setAction(Intent.ACTION_GET_CONTENT);
            }
        } else {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }
        if (intent.resolveActivity(BaseApp.get().getPackageManager()) == null) {
            intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        intent.setType("image/*");
        return intent;
    }

    /**
     * 裁剪(通用)
     * 1.启动拍照/相册
     * 2.在onActivityForResult里调用此方法，启动裁剪功能
     * 3.再次在onActivityForResult里先删除(File from) ,再调用getCropBitmap(save)处理
     */
    public static Intent getCropIntent(File from, File save) {
        if (from != null && from.length() != 0) {
            return getCropIntent(Uri.fromFile(from), Uri.fromFile(save), 1, 1, 300, 300);
        } else {
            FileUtils.deleteFile(from);
            FileUtils.deleteFile(save);
            return null; // 拍照/相册失败,则不跳转裁剪
        }
    }

    public static Intent getCropIntent(Uri from, Uri save, int aspectX,
                                       int aspectY, int outputX, int outputY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(from, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框比例
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        // 输出图片大小(太大会传输失败)
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        // 裁剪选项
        intent.putExtra("scale", true);
        intent.putExtra("noFaceDetection", true);
        // 数据返回
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, save);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        return intent;
    }

    /**
     * 获取拍照图片: 在onActivityResult中执行,会删除源文件
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
            Bitmap small = getBitmap(cameraFile, maxSize); // File转Bitmap(压缩)
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
            Bitmap small = getBitmap(cameraFile, maxWidth, maxHeight); // File转Bitmap(压缩)
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
    public static Bitmap getPictureBitmap(Context context, Intent data) {
        Bitmap picture = null;
        if (data != null) {
            Uri uri = getUri(context, data);
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
        }
        return picture;
    }

    public static Bitmap getPictureBitmap(Context context, Intent data, double maxSize) {
        Bitmap picture = null;
        if (data != null) {
            Uri uri = getUri(context, data);
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
        }
        return picture;
    }

    public static Bitmap getPictureBitmap(Context context, Intent data, int maxWidth, int maxHeight) {
        Bitmap picture = null;
        if (data != null) {
            Uri uri = getUri(context, data);
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
        }
        return picture;
    }

    /**
     * 获取裁剪图片: 在onActivityResult中执行,不会删除源文件!!
     */
    public static Bitmap getCropBitmap(File cropFile) {
        if (cropFile == null || cropFile.length() == 0) {
            FileUtils.deleteFile(cropFile); // 删除垃圾文件
            return null;
        } else {
            return BitmapFactory.decodeFile(cropFile.getAbsolutePath());
        }
    }

    public static Bitmap getCropBitmap(File cropFile, double maxSize) {
        if (cropFile == null || cropFile.length() == 0) {
            FileUtils.deleteFile(cropFile); // 删除垃圾文件
            return null;
        } else {
            Bitmap small = getBitmap(cropFile, maxSize); // File转Bitmap(压缩)
            FileUtils.createFileByDeleteOldFile(cropFile); // 删除源文件
            ImageUtils.save(small, cropFile.getAbsolutePath(), ImageUtils.FORMAT, true); // 保存图像
            if (small != null && !small.isRecycled()) {
                small.recycle();
            }
            return getCropBitmap(cropFile);
        }
    }

    public static Bitmap getCropBitmap(File cropFile, int maxWidth, int maxHeight) {
        if (cropFile == null || cropFile.length() == 0) {
            FileUtils.deleteFile(cropFile); // 删除垃圾文件
            return null;
        } else {
            Bitmap small = getBitmap(cropFile, maxWidth, maxHeight); // File转Bitmap(压缩)
            FileUtils.createFileByDeleteOldFile(cropFile); // 删除源文件
            ImageUtils.save(small, cropFile.getAbsolutePath(), ImageUtils.FORMAT, true); // 保存图像
            if (small != null && !small.isRecycled()) {
                small.recycle();
            }
            return getCropBitmap(cropFile);
        }
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
        Uri uri = intent.getData();
        if (uri != null && uri.getScheme().equals("file")) {
            String type = intent.getType(); // 小米的type不是null 其他的是
            if ((type.contains("image/"))) {
                String path = uri.getEncodedPath();
                if (path != null) {
                    path = Uri.decode(path);
                    ContentResolver cr = context.getContentResolver();
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
                            uri = uri_temp;
                        }
                    }
                }
            }
        }
        return uri;
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
