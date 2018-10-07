package com.small.tools.network.internal;

import com.small.tools.network.internal.interfaces.RemoteResource;

import java.io.InputStream;

/**
 * Author: hjq
 * Date  : 2018/10/02 17:03
 * Name  : RemoteResources
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class RemoteResourceNetwork implements RemoteResource {

    String strUri;
    InputStream mResourceStream;

    protected RemoteResourceNetwork() {

    }

    @Override
    public RemoteResourceNetwork setResourceAddress(String uri) {
        strUri = uri;
        return this;
    }

    @Override
    public String getResourceAddress() {
        return strUri;
    }

    @Override
    public RemoteResourceNetwork setResourceStream(InputStream is) {
        mResourceStream = is;
        return this;
    }

    @Override
    public InputStream getResourceStream() {
        return mResourceStream;
    }

    @Override
    public final RemoteResourceNetwork setResourceType(Type type) {
        return this;
    }

    @Override
    public final Type getResourceType() {
        return Type.Network;
    }
}
