package com.bjxrgz.startup.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import com.bjxrgz.startup.base.MyApp;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by fd.meng on 2014/03/30
 * <p>
 * 图像处理类
 */
public class BitmapUtils {

    /**
     * 添加水印
     */
    public static Bitmap watermarkBitmap(Bitmap src, Bitmap watermark, String title) {
        if (src == null) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight();
        //需要处理图片太大造成的内存超过的问题,这里我的图片很小所以不写相应代码了
        Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
        Paint paint = new Paint();
        //加入图片
        if (watermark != null) {
            int ww = watermark.getWidth();
            int wh = watermark.getHeight();
            paint.setAlpha(50);
            cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, paint);// 在src的右下角画入水印
        }
        //加入文字
        if (title != null) {
            TextPaint textPaint = new TextPaint();
            textPaint.setAntiAlias(true);
            textPaint.setTextSize(40.0f);
            textPaint.setColor(Color.RED);

//            String familyName ="宋体";
//            Typeface font = Typeface.create(familyName,Typeface.BOLD);
//            TextPaint textPaint=new TextPaint();
//            textPaint.setColor(Color.RED);
//            textPaint.setTypeface(font);
//            textPaint.setTextSize(90);

            //这里是自动换行的
            StaticLayout layout = new StaticLayout(title, textPaint, w, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
            layout.draw(cv);
            //文字就加左上角算了
            //cv.drawText(title,0,40,paint);
        }
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        cv.restore();// 存储
        return newb;
    }

    /**
     * Bitmap转换成Drawable
     */
    public Drawable bitmap2Drawable(Context context, Bitmap bitmap) {

        return new BitmapDrawable(context.getResources(), bitmap);
    }

    /**
     * Drawable转换成Bitmap
     */
    public Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * @param source 需要裁剪的Bitmap
     * @param x      裁剪起始点
     * @param y      左上角
     * @param width  裁剪长宽
     * @param height 左上角
     */
    public static Bitmap cutBitmap(Bitmap source, int x, int y, int width, int height) {

        return Bitmap.createBitmap(source, x, y, width, height);
    }

    /**
     * @param source 透明，旋转，移动，缩放
     * @param matrix 可以set各种属性，导入包要看好啊，android.graphics.Matrix
     */
    public static Bitmap matrixBitmap(Bitmap source, Matrix matrix) {

        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    /**
     * ***********************************压缩***************************************
     */
    public static final Bitmap.CompressFormat PNG = Bitmap.CompressFormat.PNG; // 无损
    public static final Bitmap.CompressFormat JPEG = Bitmap.CompressFormat.JPEG; // 有损

    /**
     * 大小压缩上限,超过就压缩 (0不压缩)
     */
    public static Bitmap compressBitmapSize(Bitmap bmp, Bitmap.CompressFormat type, int size) {
        Bitmap result;
        if (size > 0) { // 1024*1024 = 1M
            // 字节流
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // 这里压缩options%，把压缩后的数据存放到baos中
            bmp.compress(type, 100, baos);
            // 压缩比率
            int options = 100;
            // 循环判断如果压缩后图片是否大于maxSize,大于继续压缩
            while (baos.toByteArray().length > size) {
                // 重置baos即清空baos
                baos.reset();
                // 每次都减少10
                options -= 10;
                LogUtils.log(Log.DEBUG, MyApp.LOG_TAG, "compressBmpSize(options)--->" + options);
                // 这里压缩options%，把压缩后的数据存放到baos中
                bmp.compress(type, options, baos);
            }
            // 把压缩后的数据baos存放到ByteArrayInputStream中
            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
            // 把ByteArrayInputStream数据生成图片
            result = BitmapFactory.decodeStream(isBm);
        } else {
            result = bmp;
        }
        return result;
    }

    /**
     * 尺寸倍数,值越大，图片尺寸越小 (1不压缩)
     */
    public static boolean compressBitmapRatio(Bitmap bmp, File resultFile,
                                              Bitmap.CompressFormat type, int ratio) {
        if (ratio != 1) {
            // 压缩Bitmap到对应尺寸
            Bitmap result = Bitmap.createBitmap(bmp.getWidth() / ratio, bmp.getHeight() / ratio, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            Rect rect = new Rect(0, 0, bmp.getWidth() / ratio, bmp.getHeight() / ratio);
            canvas.drawBitmap(bmp, null, rect, null);
        }
        return compressBitmapQuality(bmp, resultFile, type, 100);
    }

    /**
     * 质量压缩 值越大，图片质量越大 (100不压缩)
     */
    public static boolean compressBitmapQuality(Bitmap bmp, File resultFile,
                                                Bitmap.CompressFormat type, int quality) {
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(resultFile));
            bmp.compress(type, quality, bos);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.flush();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 压缩图片(android自带方法压缩)
     */
    public static Bitmap compressBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            int maxSize = 1024 * 1024 * 10; // 1M (我去 这是怎么算的啊)

            int byteCount = bitmap.getByteCount(); // 为什么不对应啊

            LogUtils.log(Log.DEBUG, MyApp.LOG_TAG, "compressBitmap/byteCount(start)--->" + byteCount);

            while (byteCount > maxSize) {
                int options = byteCount / maxSize; // 和参数没关系啊
                int width = bitmap.getWidth() / options;
                int height = bitmap.getHeight() / options;
                bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                byteCount = bitmap.getByteCount();
            }
            LogUtils.log(Log.DEBUG, MyApp.LOG_TAG, "compressBitmap/byteCount(end)--->" + byteCount);
        }
        return bitmap;
    }

    /**
     * 压缩图片
     */
    public static Bitmap compressFile(File file) {
        int maxSize = 1024 * 1024; // 1M

        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        long length = file.length();
        LogUtils.log(Log.DEBUG, MyApp.LOG_TAG, "compressFile(length)--->" + length);

        if (length > maxSize) {
            return compressBitmap(bitmap);
        } else {
            return bitmap;
        }
    }

    /**
     * ***********************************加载内存***************************************
     * <p>
     * 以下是加载图片的内存优化，如bitmap已存在app当中，请参考createBitmap
     * 二次采样，不会申请内存空间
     */
    public static BitmapFactory.Options getOptionsTrue() {
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;

        return options;
    }

    /**
     * @param options 参数里的options只有宽高属性
     * @param ratio   缩放至 1 / ratio
     * @return 会获取图片的options，带缩放属性
     */
    public static BitmapFactory.Options getOptionsFalse(BitmapFactory.Options options, int ratio) {
        options.inSampleSize = ratio;

        options.inJustDecodeBounds = false;

        return options;
    }

    /**
     * 同上 , 只不过  options.inSampleSize 要自己算
     */
    public static BitmapFactory.Options getOptionsFalse(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;

        return options;
    }

    /**
     * 注意：放大时不会影响，缩小时才会影响inSampleSize，避免申请过多内存
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round(height / reqHeight);
            final int widthRatio = Math.round(width / reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

}
