package com.small.tools.network.internal.interfaces;

import java.io.InputStream;

/**
 * Author: hjq
 * Date  : 2018/10/02 17:22
 * Name  : RemoteResources
 * Intro : Edit By hjq
 * Version : 1.0
 */
public interface RemoteResource {

    enum Type {
        Network,
        Local,
        Other;
    }

    RemoteResource setResourceAddress(String uri);

    String getResourceAddress();

    RemoteResource setResourceStream(InputStream is);

    InputStream getResourceStream();

    RemoteResource setResourceType(Type type);

    Type getResourceType();

}
