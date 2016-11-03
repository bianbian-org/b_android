package com.techjumper.polyhomeb.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/4
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class UploadPicUtil {

    public Bitmap base64toBitmap(String base64Data) {
//        byte[] bitmapArray;
//        bitmapArray = Base64.decode(base64Data, 0);
//        return BitmapFactory
//                .decodeByteArray(bitmapArray, 0, bitmapArray.length);
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static String bitmapPath2Base64(String filePath) {
        Bitmap bitmap = loadSmallerBitmap(filePath);
        if (bitmap == null) return "";
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                baos.flush();
                baos.close();
                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String bitmap2Base64(Bitmap bitmap) {
        if (bitmap == null) return "";
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                baos.flush();
                baos.close();
                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private static Bitmap loadSmallerBitmap(String filePath) {
        //实例化一个图片参数
        BitmapFactory.Options options = new BitmapFactory.Options();
        //打开边界处理
        options.inJustDecodeBounds = true;
        //加载图片（此时图片并没有在内存中）
        BitmapFactory.decodeFile(filePath, options);
        //图片原始宽度
        int resWidth = options.outWidth;
        //图片目标宽度
        int targetWidth = options.outWidth;
        //计算线性压缩比例
        int scale = (int) (resWidth * 1.0f / (targetWidth * 0.5f));
        //设置线性压缩
        options.inSampleSize = scale;
        //关闭边界处理
        options.inJustDecodeBounds = false;
        //通过新参数加载图片到内存
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        return bitmap;
    }
}
