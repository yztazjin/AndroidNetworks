package com.small.tools.network.internal;

import com.small.tools.network.internal.interfaces.StatusCode;
import com.small.tools.network.internal.interfaces.HTTPRequest;
import com.small.tools.network.internal.interfaces.HTTPResponse;

import java.util.Map;
import java.util.TreeMap;

/**
 * Author: hjq
 * Date  : 2018/10/02 21:30
 * Name  : SmallHTTPResponse
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class SmallHTTPResponse implements HTTPResponse {

    int nStatusCode;
    long mContentLength;
    TreeMap<String, String> mHeaders;
    HTTPRequest mRequest;

    SmallHTTPResponse() {
        nStatusCode = StatusCode.NOT_INITIALIZE;
        mHeaders = new TreeMap<String, String>();
    }

    public SmallHTTPResponse setStatusCode(int code) {
        nStatusCode = code;
        return this;
    }

    @Override
    public int getStatusCode() {
        return nStatusCode;
    }

    @Override
    public boolean isStatusSuccessful() {
        return StatusCode.isStatusCodeSuccessful(nStatusCode);
    }

    public SmallHTTPResponse setContentLength(long length){
        mContentLength = length;
        return this;
    }

    @Override
    public long getContentLength() {
        return mContentLength;
    }

    public SmallHTTPResponse setHeader(String key, String value) {
        if (key != null) {
            mHeaders.put(key, value);
        }
        return this;
    }

    @Override
    public Map<String, String> getHeaders() {
        return new TreeMap<>(mHeaders);
    }

    @Override
    public String getHeader(String key) {
        if (key == null) {
            return null;
        }
        return mHeaders.get(key);
    }

    public SmallHTTPResponse setRequest(HTTPRequest request) {
        mRequest = request;
        return this;
    }

    @Override
    public HTTPRequest getRequest() {
        return mRequest;
    }
}
