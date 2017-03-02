package ttyy.com.jinnetwork.ext_imgloader.luban_compress;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static ttyy.com.jinnetwork.ext_imgloader.luban_compress.Preconditions.checkNotNull;

/**
 * Author: hjq from luban
 * Date  : 2016/12/26 11:08
 * Name  : LBC
 * Intro : 从Luban项目中抽出的图片压缩
 *        Luban:https://github.com/Curzibn/Luban
 *        Luban介绍: 最接近微信的图片压缩算法
 *        这是一个耗时操作，最好放到线程中操作
 * Modification  History:
 * Date          Author        	 Version          Description
 * ----------------------------------------------------------
 * 2016/12/26    hjq from luban  1.0              1.0
 */
public class LBC {


    private LBC(){
    }

    static class Holder{
        static LBC INSTANCE = new LBC();
    }

    public static LBC getInstance(){
        return Holder.INSTANCE;
    }

    /**
     * 将目标File 压缩为符合大小的Bitmap 再讲Bitmap转存到文件中
     * @param file
     * @param thumbFile
     * @return
     */
    public Bitmap compressToFile(File file, File thumbFile) {

        checkNotNull(file, "Target File Is Null");

        double size;
        String filePath = file.getAbsolutePath();

        int[] imageSize = getImageSize(filePath);
        int width = imageSize[0];
        int height = imageSize[1];
        int thumbW = width % 2 == 1 ? width + 1 : width;
        int thumbH = height % 2 == 1 ? height + 1 : height;

        width = thumbW > thumbH ? thumbH : thumbW;
        height = thumbW > thumbH ? thumbW : thumbH;

        double scale = ((double) width / height);

        if (scale <= 1 && scale > 0.5625) {
            if (height < 1664) {
                if (file.length() / 1024 < 150){
                    return compressToBitmap(file);
                }

                size = (width * height) / Math.pow(1664, 2) * 150;
                size = size < 60 ? 60 : size;
            } else if (height >= 1664 && height < 4990) {
                thumbW = width / 2;
                thumbH = height / 2;
                size = (thumbW * thumbH) / Math.pow(2495, 2) * 300;
                size = size < 60 ? 60 : size;
            } else if (height >= 4990 && height < 10240) {
                thumbW = width / 4;
                thumbH = height / 4;
                size = (thumbW * thumbH) / Math.pow(2560, 2) * 300;
                size = size < 100 ? 100 : size;
            } else {
                int multiple = height / 1280 == 0 ? 1 : height / 1280;
                thumbW = width / multiple;
                thumbH = height / multiple;
                size = (thumbW * thumbH) / Math.pow(2560, 2) * 300;
                size = size < 100 ? 100 : size;
            }
        } else if (scale <= 0.5625 && scale > 0.5) {
            if (height < 1280 && file.length() / 1024 < 200){
                return compressToBitmap(file);
            }

            int multiple = height / 1280 == 0 ? 1 : height / 1280;
            thumbW = width / multiple;
            thumbH = height / multiple;
            size = (thumbW * thumbH) / (1440.0 * 2560.0) * 400;
            size = size < 100 ? 100 : size;
        } else {
            int multiple = (int) Math.ceil(height / (1280.0 / scale));
            thumbW = width / multiple;
            thumbH = height / multiple;
            size = ((thumbW * thumbH) / (1280.0 * (1280 / scale))) * 500;
            size = size < 100 ? 100 : size;
        }

        return lubanCompressToFile(filePath, thumbFile.getAbsolutePath(), thumbW, thumbH, (long) size);
    }

    /**
     * 将目标文件压缩为Bitmap
     * @param file
     * @return
     */
    public Bitmap compressToBitmap(File file){

        checkNotNull(file, "Target File Is Null");

        String filePath = file.getAbsolutePath();

        int[] imageSize = getImageSize(filePath);
        int width = imageSize[0];
        int height = imageSize[1];
        int thumbW = width % 2 == 1 ? width + 1 : width;
        int thumbH = height % 2 == 1 ? height + 1 : height;

        width = thumbW > thumbH ? thumbH : thumbW;
        height = thumbW > thumbH ? thumbW : thumbH;

        double scale = ((double) width / height);

        if (scale <= 1 && scale > 0.5625) {
            if (height < 1664) {
                if (file.length() / 1024 < 150){
                    return lubanCompressToBitmap(filePath, width, height);
                }

            } else if (height >= 1664 && height < 4990) {
                thumbW = width / 2;
                thumbH = height / 2;
            } else if (height >= 4990 && height < 10240) {
                thumbW = width / 4;
                thumbH = height / 4;
            } else {
                int multiple = height / 1280 == 0 ? 1 : height / 1280;
                thumbW = width / multiple;
                thumbH = height / multiple;
            }
        } else if (scale <= 0.5625 && scale > 0.5) {
            if (height < 1280 && file.length() / 1024 < 200){
                return lubanCompressToBitmap(filePath, width, height);
            }

            int multiple = height / 1280 == 0 ? 1 : height / 1280;
            thumbW = width / multiple;
            thumbH = height / multiple;
        } else {
            int multiple = (int) Math.ceil(height / (1280.0 / scale));
            thumbW = width / multiple;
            thumbH = height / multiple;
        }

        return lubanCompressToBitmap(filePath, thumbW, thumbH);
    }

    /**
     * 文件压缩为Bitmap
     *
     * @param imagePath the target image path
     * @param width     the width
     * @param height    the height
     * @return {@link Bitmap}
     */
    private Bitmap lubanCompressToBitmap(String imagePath, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);

        int outH = options.outHeight;
        int outW = options.outWidth;
        int inSampleSize = 1;

        if (outH > height || outW > width) {
            int halfH = outH / 2;
            int halfW = outW / 2;

            while ((halfH / inSampleSize) > height && (halfW / inSampleSize) > width) {
                inSampleSize *= 2;
            }
        }

        options.inSampleSize = inSampleSize;

        options.inJustDecodeBounds = false;

        int heightRatio = (int) Math.ceil(options.outHeight / (float) height);
        int widthRatio = (int) Math.ceil(options.outWidth / (float) width);

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                options.inSampleSize = heightRatio;
            } else {
                options.inSampleSize = widthRatio;
            }
        }
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(imagePath, options);
    }

    /**
     * 文件压缩为指定图片后 转存为单独的另一个文件
     * @param largeImagePath
     * @param thumbFilePath
     * @param width
     * @param height
     * @param size
     * @return
     */
    private Bitmap lubanCompressToFile(String largeImagePath, String thumbFilePath, int width, int height, long size){
        Bitmap thbBitmap = lubanCompressToBitmap(largeImagePath, width, height);
        saveImage(thumbFilePath, thbBitmap, size);
        return thbBitmap;
    }

    /**
     * 获取图片宽高
     *
     * @param imagePath the path of image
     */
    private int[] getImageSize(String imagePath) {
        int[] res = new int[2];

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;
        BitmapFactory.decodeFile(imagePath, options);

        res[0] = options.outWidth;
        res[1] = options.outHeight;

        return res;
    }

    /**
     * 保存图片到指定路径
     * Save image with specified size
     *
     * @param filePath the image file save path 储存路径
     * @param bitmap   the image what be save   目标图片
     * @param size     the file size of image   期望大小
     */
    private File saveImage(String filePath, Bitmap bitmap, long size) {
        checkNotNull(bitmap, "Bitmap Is Null");

        File result = new File(filePath.substring(0, filePath.lastIndexOf("/")));

        if (!result.exists() && !result.mkdirs()) return null;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int options = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, stream);

        while (stream.toByteArray().length / 1024 > size && options > 6) {
            stream.reset();
            options -= 6;
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, stream);
        }
        bitmap.recycle();

        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(stream.toByteArray());
            fos.flush();
            fos.close();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new File(filePath);
    }

}
