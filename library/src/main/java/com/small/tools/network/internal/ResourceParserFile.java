package com.small.tools.network.internal;

import com.small.tools.network.global.SmallLogs;
import com.small.tools.network.internal.interfaces.HTTPCallback;
import com.small.tools.network.internal.interfaces.HTTPRequest;
import com.small.tools.network.internal.interfaces.ResourceDataParser;
import com.small.tools.network.internal.interfaces.StatusCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Author: hjq
 * Date  : 2018/10/03 10:22
 * Name  : ResourceParserFile
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class ResourceParserFile implements ResourceDataParser<File> {

    File mReceivedFile;
    boolean mFromBreakPoint = true;

    public ResourceParserFile setFromBreakPoint(boolean value) {
        this.mFromBreakPoint = value;
        return this;
    }

    public boolean isFromBreakPoint() {
        return mFromBreakPoint;
    }

    public ResourceParserFile setReceivedFile(File file) {
        this.mReceivedFile = file;
        if (file != null && !file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
                this.mReceivedFile = file;
            } catch (IOException e) {
                SmallLogs.w(file.getAbsolutePath() + " create fail!");
                this.mReceivedFile = null;
            }
        }
        return this;
    }

    public File getReceivedFile() {
        return mReceivedFile;
    }

    @Override
    public int parse(HTTPRequest request, InputStream is) {
        File file = getReceivedFile();
        HTTPCallback callback = request.getHTTPCallback();

        if (file == null
                || !file.exists()) {

            return StatusCode.PARSE_ERROR_FILENOTFOUND;
        }

        int statusCode = StatusCode.PARSE_SUCCESS;
        long totalLength = file.length() + request.getResponse().getContentLength();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, mFromBreakPoint);
            byte[] buffer = new byte[4096];
            int length = 0;
            while ((length = is.read(buffer)) != -1) {
                fos.write(buffer, 0, length);

                if (request.isCanceled()
                        || request.isFinished()) {
                    statusCode = StatusCode.PARSE_ERROR_CANCEL_FINISH;
                    break;
                }

                if (callback != null) {
                    callback.onProgress(request, file.length(), totalLength);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();

            statusCode = StatusCode.PARSE_ERROR_FILENOTFOUND;
        } catch (IOException e) {
            e.printStackTrace();

            statusCode = StatusCode.PARSE_ERROR_IOEXCEPTION;
        } finally {

            try {
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();

                statusCode = StatusCode.PARSE_ERROR_IOEXCEPTION;
            }

        }

        return statusCode;
    }

    @Override
    public void parse(HTTPRequest request, File data) {
        mReceivedFile = data;
    }

    @Override
    public File getData() {
        return mReceivedFile;
    }

    @Override
    public Class<File> getParseType() {
        return File.class;
    }

    @Override
    public ResourceParserFile newInstance() {
        return new ResourceParserFile();
    }
}
