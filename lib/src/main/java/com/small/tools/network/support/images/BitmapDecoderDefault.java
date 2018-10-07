package com.small.tools.network.support.images;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.File;
import java.io.InputStream;

/**
 * Author: hjq
 * Date  : 2018/10/04 11:26
 * Name  : BitmapDecoder
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class BitmapDecoderDefault implements BitmapDecoder {

    @Override
    public Bitmap decode(Bitmap bitmap, int rWidth, int rHeight) {
        Matrix matrix = new Matrix();

        float sx = ((float) rWidth) / bitmap.getWidth();
        sx = sx < 1 ? sx : 1;
        float sy = ((float) rHeight) / bitmap.getHeight();
        sy = sy < 1 ? sy : 1;
        matrix.postScale(sx, sy);
        Bitmap bm = Bitmap.createBitmap(bitmap,
                0, 0, bitmap.getWidth(), bitmap.getHeight(),
                matrix, false);

        return bm;
    }

    @Override
    public Bitmap decode(InputStream is, int rWidth, int rHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = true;

        Bitmap bitmap = null;

        BitmapFactory.decodeStream(is, null, options);

        int i = 0;
        while ((options.outWidth >> i) > rWidth
                || (options.outHeight >> i) > rHeight) {
            i++;
        }

        options.inJustDecodeBounds = false;
        options.inSampleSize = i;

        bitmap = BitmapFactory.decodeStream(is, null, options);

        return bitmap;
    }

    @Override
    public Bitmap decode(File file, int rWidth, int rHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = true;

        Bitmap bitmap = null;

        BitmapFactory.decodeFile(file.getAbsolutePath(), options);

        int i = 0;
        while ((options.outWidth >> i) > rWidth
                || (options.outHeight >> i) > rHeight) {
            i++;
        }

        options.inJustDecodeBounds = false;
        options.inSampleSize = i;

        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

        return bitmap;
    }

    @Override
    public Bitmap decode(byte[] byteArray, int rWidth, int rHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = true;

        Bitmap bitmap = null;

        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);

        int i = 0;
        while ((options.outWidth >> i) > rWidth
                || (options.outHeight >> i) > rHeight) {
            i++;
        }

        options.inJustDecodeBounds = false;
        options.inSampleSize = i;

        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);

        return bitmap;
    }
}
