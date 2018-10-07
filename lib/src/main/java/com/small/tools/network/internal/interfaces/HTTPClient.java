package com.small.tools.network.internal.interfaces;

/**
 * Author: hjq
 * Date  : 2018/10/02 16:16
 * Name  : HTTPClientType
 * Intro : Edit By hjq
 * Version : 1.0
 */
public interface HTTPClient {

    HTTPResponse get(HTTPRequest request);

    HTTPResponse post(HTTPRequest request);

}
