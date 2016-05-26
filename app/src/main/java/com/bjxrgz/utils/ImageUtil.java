package com.bjxrgz.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;

/**
 * Created by fd.meng on 2014/03/30
 *
 * 图像处理类
 *
 */
public class ImageUtil {

    /**
     * 添加水印
     *
     * @param src
     * @param watermark
     * @param title
     * @return
     */
	public static Bitmap watermarkBitmap(Bitmap src, Bitmap watermark,String title) {
        if (src == null) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight(); 
        //需要处理图片太大造成的内存超过的问题,这里我的图片很小所以不写相应代码了        
        Bitmap newb= Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src    
        Paint paint=new Paint();
        //加入图片
        if (watermark != null) {
            int ww = watermark.getWidth();
            int wh = watermark.getHeight();
            paint.setAlpha(50);
            cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, paint);// 在src的右下角画入水印            
        }
        //加入文字
        if(title!=null)
        {
            //String familyName ="宋体";
            //Typeface font = Typeface.create(familyName,Typeface.BOLD);
        	TextPaint textPaint = new TextPaint();
            textPaint.setAntiAlias(true);
            textPaint.setTextSize(40.0f);
            textPaint.setColor(Color.RED);
            
//            TextPaint textPaint=new TextPaint();
//            textPaint.setColor(Color.RED);
//            textPaint.setTypeface(font);
//            textPaint.setTextSize(90);
            //这里是自动换行的
            StaticLayout layout = new StaticLayout(title,textPaint,w,Alignment.ALIGN_NORMAL,1.0F,0.0F,true);
            layout.draw(cv);
            //文字就加左上角算了
            //cv.drawText(title,0,40,paint); 
        }
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        cv.restore();// 存储
        return newb;
    }
	
}
