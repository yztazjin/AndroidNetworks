package com.small.tools.network.support.images;

import android.graphics.Bitmap;

import java.io.File;
import java.io.InputStream;

/**
 * Author: hjq
 * Date  : 2018/10/04 11:26
 * Name  : BitmapDecoder
 * Intro : Edit By hjq
 * Version : 1.0
 */
public interface BitmapDecoder {

    Bitmap decode(Bitmap bitmap, int rWidth, int rHeight);

    Bitmap decode(InputStream is, int rWidth, int rHeight);

    Bitmap decode(File file, int rWidth, int rHeight);

    Bitmap decode(byte[] byteArray, int rWidth, int rHeight);

}
