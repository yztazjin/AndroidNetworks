package com.small.tools.network.internal;

import com.small.tools.network.internal.interfaces.HTTPCallback;
import com.small.tools.network.internal.interfaces.HTTPRequest;
import com.small.tools.network.internal.interfaces.ResourceDataParser;
import com.small.tools.network.internal.interfaces.StatusCode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Author: hjq
 * Date  : 2018/10/03 10:20
 * Name  : ResourceParserString
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class ResourceParserString implements ResourceDataParser<String> {

    String strData;

    @Override
    public int parse(HTTPRequest request, InputStream is) {

        HTTPCallback callback = request.getHTTPCallback();
        long totalLength = request.getResponse().getContentLength();
        int statusCode = StatusCode.PARSE_SUCCESS;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
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
                strData = baos.toString();
            }
        } catch (IOException ex) {
            ex.printStackTrace();

            statusCode = StatusCode.PARSE_ERROR_IOEXCEPTION;
        }

        return statusCode;
    }

    @Override
    public void parse(HTTPRequest request, String data) {
        strData = data;
    }

    @Override
    public String getData() {
        return strData;
    }

    @Override
    public Class<String> getParseType() {
        return String.class;
    }

    @Override
    public ResourceParserString copy() {
        return new ResourceParserString();
    }
}
