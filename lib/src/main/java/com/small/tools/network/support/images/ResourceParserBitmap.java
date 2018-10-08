package com.small.tools.network.support.images;

import android.graphics.drawable.BitmapDrawable;

import com.small.tools.network.internal.interfaces.HTTPCallback;
import com.small.tools.network.internal.interfaces.RemoteResource;
import com.small.tools.network.internal.interfaces.StatusCode;
import com.small.tools.network.internal.interfaces.HTTPRequest;
import com.small.tools.network.internal.interfaces.ResourceDataParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Author: hjq
 * Date  : 2018/10/04 11:08
 * Name  : ResourceParserBitmap
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class ResourceParserBitmap implements ResourceDataParser<BitmapDrawable> {

    int nWidth = 1024;
    int nHeight = 1024;
    BitmapDecoder mBitmapDecoder = new BitmapDecoderDefault();
    BitmapDrawable mParsedData;

    public ResourceParserBitmap setMaxSize(int width, int height) {
        nWidth = width;
        nHeight = height;
        return this;
    }

    public int[] getMaxSize() {
        int[] size = new int[]{nWidth, nHeight};
        return size;
    }

    @Override
    public int parse(HTTPRequest request, InputStream is) {

        int statusCode = StatusCode.PARSE_SUCCESS;

        if (request.getRemoteResource().getResourceType() == RemoteResource.Type.Network) {
            try {
                HTTPCallback callback = request.getHTTPCallback();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                long totalLength = request.getResponse().getContentLength();

                byte[] buffer = new byte[4096];
                int length = 0;
                while ((length = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, length);

                    if (request.isCanceled()
                            || request.isFinished()) {
                        statusCode = StatusCode.PARSE_ERROR_CANCEL_FINISH;
                        break;
                    }

                    if (callback != null) {
                        callback.onProgress(request, baos.size(), totalLength);
                    }
                }

                baos.flush();
                baos.close();

                if (statusCode == StatusCode.PARSE_SUCCESS) {
                    mParsedData = new BitmapDrawable(mBitmapDecoder.decode(baos.toByteArray(),
                            nWidth, nHeight));
                }
            } catch (IOException ex) {
                ex.printStackTrace();

                statusCode = StatusCode.PARSE_ERROR_IOEXCEPTION;
            }
        } else {
            mParsedData = new BitmapDrawable(mBitmapDecoder.decode(is, nWidth, nHeight));
        }

        return statusCode;
    }

    @Override
    public void parse(HTTPRequest request, BitmapDrawable data) {
        mParsedData = data;
    }

    @Override
    public BitmapDrawable getData() {
        return mParsedData;
    }

    @Override
    public Class<BitmapDrawable> getParseType() {
        return BitmapDrawable.class;
    }

    @Override
    public ResourceParserBitmap newInstance() {
        return new ResourceParserBitmap();
    }
}
