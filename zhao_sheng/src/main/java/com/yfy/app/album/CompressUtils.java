package com.yfy.app.album;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;

import com.yfy.final_tag.FileTools;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * =====作者=====
 * 许英俊
 * =====时间=====
 * 2017/12/23.
 */

public class CompressUtils {

    static {
        System.loadLibrary("native-lib");
    }

    /**
     * LibJpeg压缩
     *
     * @param bitmap
     * @param quality
     * @param fileNameBytes
     * @param optimize
     * @return
     */
    public static native int compressBitmap(Bitmap bitmap, int quality, byte[] fileNameBytes, boolean optimize);


    /**
     *  得到单张图片压缩后的:file
     * @param filePath 图片路劲
     * @return
     */
    public static String compressFileSampleLibJpeg(Context context,String filePath) {
        String path = TagFinal.getAppFile() +  System.currentTimeMillis() + ".jpg";
        File saveFile= FileTools.getFile(path);
        Uri uri=Uri.parse(filePath);
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        int code = CompressUtils.compressBitmap(bitmap, 20, saveFile.getAbsolutePath().getBytes(), true);
        CompressUtils.compressBitmap(bitmap, 20, saveFile.getAbsolutePath().getBytes(), true);
        return path;
    }


    /**
     * 质量压缩
     *
     * @param bitmap 图
     * @param quality 比例100是原比例
     * @param file 保存文件
     */
    public static void compressQuality(Bitmap bitmap, int quality, File file) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 尺寸压缩
     *
     * @param bitmap
     * @param file
     */
    public static void compressSize(Bitmap bitmap, File file) {
        int ratio = 8;//尺寸压缩比例
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth() / ratio, bitmap.getHeight() / ratio, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, bitmap.getWidth() / ratio, bitmap.getHeight() / ratio);
        canvas.drawBitmap(bitmap, null, rect, null);

        compressQuality(result, 100, file);
    }

    /**
     * 采样率压缩
     *
     * @param filePath
     * @param file
     */
    public static void compressSample(String filePath, File file) {
        int inSampleSize = 8;//采样率设置
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        compressQuality(bitmap, 100, file);
    }



    /**
     *  得到单张图片压缩后的:file
     * @param filePath 图片路劲
     * @return
     */
    public static File compressFileSample(String filePath) {
        String path = TagFinal.getAppFile() +  System.currentTimeMillis() + ".jpg";
        File file= FileTools.getFile(path);
        compressSample(filePath, file);
        return file;
    }



    /**
     *  得到单张图片压缩后的:fileString
     * @param filePath 图片路劲
     * @return
     */
    public static String compressFileStringSample(String filePath) {
        String path = TagFinal.getAppFile() +  System.currentTimeMillis() + ".jpg";
        File file= FileTools.getFile(path);
        int inSampleSize = 8;//采样率设置
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        Bitmap bitmap_rita=rotaingImageView(readPictureDegree(filePath),bitmap);
        compressQuality(bitmap_rita, 100, file);
        return path;
    }
    /**
     * 得到单张图片压缩后的:base64code
     * @param filePath 图片路劲
     * @return
     */
    public static String compressBase64StringSample(String filePath) {

        int inSampleSize = 8;//采样率设置
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
//		byte[] encode = Base64.encode(bytes,Base64.DEFAULT);
        //加密后的字符串带有"\n"，导致在进行字符串比较的时候出现错误，解决办法是将

        return Base64.encodeToString(bytes, Base64.NO_WRAP);

    }


    /**
     * 读取照片旋转角度
     *
     * @param path 照片路径
     * @return 角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     * @param angle 被旋转角度
     * @param bitmap 图片对象
     * @return 旋转后的图片
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            Logger.i("zxx", "rotaingImageView: OutOfMemoryError");
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }


    /**
     * 像素压缩
     */

    public static String saveImage(String old){
        String path = Environment.getExternalStorageDirectory().toString() + "/yfy/" +  System.currentTimeMillis() + ".jpg";
        File file = new File(path);
        Bitmap bitMap=getSmallBitmap(old);
        FileOutputStream fos = null;
        ByteArrayOutputStream baos  = null;
        try {
            fos = new FileOutputStream(file);
            //图片允许最大空间 单位：KB
            double maxSize =500.00;
            //将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
            baos= new ByteArrayOutputStream();
            bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            //将字节换成KB
            double mid = b.length/1024;
            //判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
            if (mid > maxSize) {
                //获取bitmap大小 是允许最大大小的多少倍
                double i = mid / maxSize;
                //开始压缩 此处用到平方根 将宽带和高度压缩掉对应的平方根倍 （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
                bitMap = zoomImage(bitMap, bitMap.getWidth() / Math.sqrt(i), bitMap.getHeight() / Math.sqrt(i));
            }
            bitMap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File tmpFile = new File(old);
        if (tmpFile.exists())
            tmpFile.delete();//删除原文件


        if (FileTools.fileIsExists(path)) return path;
        return null;

    }
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth,scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        //避免出现内存溢出的情况，进行相应的属性设置。
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = true;

        return BitmapFactory.decodeFile(filePath, options);
    }
}
