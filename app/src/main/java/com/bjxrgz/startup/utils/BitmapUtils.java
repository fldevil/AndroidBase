package com.bjxrgz.startup.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.io.InputStream;

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
     * ***********************************加载内存***************************************
     * <p>
     * 以下是加载图片的内存优化，如bitmap已存在app当中，请参考createBitmap
     * 二次采样，不会申请内存空间
     */
    private static BitmapFactory.Options getOptionsTrue() {
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;

        return options;
    }

    /**
     * @param options 参数里的options只有宽高属性
     * @param ratio   缩放至 1 / ratio
     * @return 会获取图片的options，带缩放属性
     */
    private static BitmapFactory.Options getOptionsFalse(BitmapFactory.Options options, int ratio) {
        options.inSampleSize = ratio;

        options.inJustDecodeBounds = false;

        return options;
    }

    /**
     * 同上 , 只不过  options.inSampleSize 要自己算
     */
    private static BitmapFactory.Options getOptionsFalse(BitmapFactory.Options options, int reqWidth, int reqHeight) {
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

    /**
     * 图片文件
     */
    public static Bitmap getOptionsBitmap(String filePath, int ratio) {

        BitmapFactory.Options optionsTrue = getOptionsTrue();

        BitmapFactory.decodeFile(filePath, optionsTrue);

        BitmapFactory.Options optionsFalse = getOptionsFalse(optionsTrue, ratio);

        return BitmapFactory.decodeFile(filePath, optionsFalse);
    }

    public static Bitmap getOptionsBitmap(String filePath, int reqWidth, int reqHeight) {

        BitmapFactory.Options optionsTrue = getOptionsTrue();

        BitmapFactory.decodeFile(filePath, optionsTrue);

        BitmapFactory.Options optionsFalse = getOptionsFalse(optionsTrue, reqWidth, reqHeight);

        return BitmapFactory.decodeFile(filePath, optionsFalse);
    }

    /**
     * 字节流
     */
    public static Bitmap getOptionsBitmap(byte[] data, int ratio) {

        BitmapFactory.Options optionsTrue = getOptionsTrue();

        BitmapFactory.decodeByteArray(data, 0, data.length, optionsTrue);

        BitmapFactory.Options optionsFalse = getOptionsFalse(optionsTrue, ratio);

        return BitmapFactory.decodeByteArray(data, 0, data.length, optionsFalse);
    }

    public static Bitmap getOptionsBitmap(byte[] data, int reqWidth, int reqHeight) {

        BitmapFactory.Options optionsTrue = getOptionsTrue();

        BitmapFactory.decodeByteArray(data, 0, data.length, optionsTrue);

        BitmapFactory.Options optionsFalse = getOptionsFalse(optionsTrue, reqWidth, reqHeight);

        return BitmapFactory.decodeByteArray(data, 0, data.length, optionsFalse);
    }

    /**
     * resource
     */
    public static Bitmap getOptionsBitmap(Context context, int resID, int ratio) {

        BitmapFactory.Options optionsTrue = getOptionsTrue();

        BitmapFactory.decodeResource(context.getResources(), resID, optionsTrue);

        BitmapFactory.Options optionsFalse = getOptionsFalse(optionsTrue, ratio);

        return BitmapFactory.decodeResource(context.getResources(), resID, optionsFalse);
    }

    public static Bitmap getOptionsBitmap(Context context, int resID, int reqWidth, int reqHeight) {

        BitmapFactory.Options optionsTrue = getOptionsTrue();

        BitmapFactory.decodeResource(context.getResources(), resID, optionsTrue);

        BitmapFactory.Options optionsFalse = getOptionsFalse(optionsTrue, reqWidth, reqHeight);

        return BitmapFactory.decodeResource(context.getResources(), resID, optionsFalse);
    }

    /**
     * 输入流
     */
    public static Bitmap getOptionsBitmap(InputStream is, int ratio) {

        BitmapFactory.Options optionsTrue = getOptionsTrue();

        BitmapFactory.decodeStream(is, null, optionsTrue);

        BitmapFactory.Options optionsFalse = getOptionsFalse(optionsTrue, ratio);

        return BitmapFactory.decodeStream(is, null, optionsFalse);
    }

    public static Bitmap getOptionsBitmap(InputStream is, int reqWidth, int reqHeight) {

        BitmapFactory.Options optionsTrue = getOptionsTrue();

        BitmapFactory.decodeStream(is, null, optionsTrue);

        BitmapFactory.Options optionsFalse = getOptionsFalse(optionsTrue, reqWidth, reqHeight);

        return BitmapFactory.decodeStream(is, null, optionsFalse);
    }

}