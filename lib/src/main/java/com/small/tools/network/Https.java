package com.small.tools.network;

import com.small.tools.network.internal.RemoteResources;
import com.small.tools.network.internal.SmallHTTPRequest;
import com.small.tools.network.internal.interfaces.HTTPRequest;
import com.small.tools.network.internal.interfaces.HTTPMethod;
import com.small.tools.network.internal.interfaces.SmallHeader;

/**
 * Author: hjq
 * Date  : 2018/10/02 16:12
 * Name  : Https
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class Https {

    private Https() {
    }

    public static HTTPRequest get(String url) {
        return new SmallHTTPRequest()
                .setRemoteResource(RemoteResources.create().setResourceAddress(url))
                .setHTTPMethod(HTTPMethod.GET);
    }

    public static HTTPRequest post(String url) {
        return post(url, SmallHeader.ContentType.ApplicationJson);
    }

    public static HTTPRequest post(String url, SmallHeader.ContentType contentType) {
        return new SmallHTTPRequest()
                .setRemoteResource(RemoteResources.net().setResourceAddress(url))
                .setHTTPMethod(HTTPMethod.POST)
                .setHTTPContent(contentType);
    }

}
