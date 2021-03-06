package com.bjxrgz.base.utils;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

import com.bjxrgz.base.BaseApp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jiang on 2016/10/13
 * <p>
 * describe 多媒体管理工具类
 */
public class MediaUtil {

    /**
     * 获取拍照图片: 在onActivityResult中执行
     */
    public static Bitmap getCameraBitmap(File cameraFile) {
        if (FileUtil.isFileEmpty(cameraFile)) {
            FileUtil.deleteFile(cameraFile); // 删除垃圾文件
            return null;
        }
        Bitmap adjust = adjust(cameraFile);// 摆正角度
        FileUtil.deleteFile(cameraFile); // 删除源文件
        return adjust;
    }

    public static Bitmap getCameraBitmap(File cameraFile, long maxByteSize) {
        if (FileUtil.isFileEmpty(cameraFile)) {
            FileUtil.deleteFile(cameraFile); // 删除垃圾文件
            return null;
        }
        Bitmap small = ImageUtil.compressFile(cameraFile, maxByteSize); // File转Bitmap(压缩)
        FileUtil.createFileByDeleteOldFile(cameraFile); // 重置源文件
        ImageUtil.save(small, cameraFile.getAbsolutePath(), ImageUtil.FORMAT, true); // 保存图像
        if (small != null && !small.isRecycled()) {
            small.recycle();
        }
        return getCameraBitmap(cameraFile);
    }

    public static Bitmap getCameraBitmap(File cameraFile, int maxWidth, int maxHeight) {
        if (FileUtil.isFileEmpty(cameraFile)) {
            FileUtil.deleteFile(cameraFile); // 删除垃圾文件
            return null;
        }
        Bitmap small = ImageUtil.getBitmap(cameraFile, maxWidth, maxHeight); // File转Bitmap(压缩)
        FileUtil.createFileByDeleteOldFile(cameraFile); // 重置源文件
        ImageUtil.save(small, cameraFile.getAbsolutePath(), ImageUtil.FORMAT, true); // 保存图像
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
                ContentResolver cr = BaseApp.getInstance().getContentResolver();
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

    public static Bitmap getPictureBitmap(Intent data) {
        if (data == null) return null;
        Bitmap picture = null;
        Uri uri = getPictureUri(data);
        if (uri != null) {
            try {
                InputStream stream = BaseApp.getInstance()
                        .getContentResolver().openInputStream(uri);
                picture = BitmapFactory.decodeStream(stream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            picture = data.getParcelableExtra("data");
        }
        return picture;
    }

    public static Bitmap getPictureBitmap(Intent data, long maxByteSize) {
        if (data == null) return null;
        Bitmap picture;
        Uri uri = getPictureUri(data);
        if (uri != null) {
            File file = ConvertUtil.URI2File(uri);
            picture = ImageUtil.compressFile(file, maxByteSize);
        } else {
            picture = data.getParcelableExtra("data");
        }
        return picture;
    }

    public static Bitmap getPictureBitmap(Intent data, int maxWidth, int maxHeight) {
        if (data == null) return null;
        Bitmap picture;
        Uri uri = getPictureUri(data);
        if (uri != null) {
            File file = ConvertUtil.URI2File(uri);
            picture = ImageUtil.getBitmap(file, maxWidth, maxHeight);
        } else {
            picture = data.getParcelableExtra("data");
        }
        return picture;
    }

    /**
     * 获取裁剪图片: 在onActivityResult中执行,记得删除源文件!!
     */
    public static Bitmap getCropBitmap(File cropFile) {
        if (FileUtil.isFileEmpty(cropFile)) {
            FileUtil.deleteFile(cropFile); // 删除垃圾文件
            return null;
        } else {
            return BitmapFactory.decodeFile(cropFile.getAbsolutePath());
        }
    }

    public static Bitmap getCropBitmap(File cropFile, long maxByteSize) {
        if (FileUtil.isFileEmpty(cropFile)) {
            FileUtil.deleteFile(cropFile); // 删除垃圾文件
            return null;
        }
        Bitmap small = ImageUtil.compressFile(cropFile, maxByteSize); // File转Bitmap(压缩)
        FileUtil.createFileByDeleteOldFile(cropFile); // 重置源文件
        ImageUtil.save(small, cropFile.getAbsolutePath(), ImageUtil.FORMAT, true); // 保存图像
        if (small != null && !small.isRecycled()) {
            small.recycle();
        }
        return getCropBitmap(cropFile);
    }

    public static Bitmap getCropBitmap(File cropFile, int maxWidth, int maxHeight) {
        if (FileUtil.isFileEmpty(cropFile)) {
            FileUtil.deleteFile(cropFile); // 重置源文件
            return null;
        }
        Bitmap small = ImageUtil.getBitmap(cropFile, maxWidth, maxHeight); // File转Bitmap(压缩)
        FileUtil.createFileByDeleteOldFile(cropFile); // 删除源文件
        ImageUtil.save(small, cropFile.getAbsolutePath(), ImageUtil.FORMAT, true); // 保存图像
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

}
