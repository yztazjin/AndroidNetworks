package com.small.tools.network.internal.interfaces;

import java.util.Map;

/**
 * Author: hjq
 * Date  : 2018/10/02 16:16
 * Name  : SmallHTTPResponse
 * Intro : Edit By hjq
 * Version : 1.0
 */
public interface HTTPResponse {

    int getStatusCode();

    boolean isStatusSuccessful();

    long getContentLength();

    Map<String, String> getHeaders();

    String getHeader(String key);

    HTTPRequest getRequest();

}
