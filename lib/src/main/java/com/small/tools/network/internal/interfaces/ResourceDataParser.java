package com.small.tools.network.internal.interfaces;

import java.io.InputStream;

/**
 * Author: hjq
 * Date  : 2018/10/02 23:07
 * Name  : ResourceDataParser
 * Intro : Edit By hjq
 * Version : 1.0
 */
public interface ResourceDataParser<T> {

    int parse(HTTPRequest request, InputStream is);

    void parse(HTTPRequest request, T data);

    T getData();

    Class<T> getParseType();

    ResourceDataParser<T> copy();

}
