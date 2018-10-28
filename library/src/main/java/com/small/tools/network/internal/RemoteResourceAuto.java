package com.small.tools.network.internal;

import android.text.TextUtils;

import com.small.tools.network.internal.interfaces.RemoteResource;

import java.io.InputStream;

/**
 * Author: hjq
 * Date  : 2018/10/02 17:03
 * Name  : RemoteResources
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class RemoteResourceAuto implements RemoteResource {

    String strUri;
    Type mType = Type.Other;
    InputStream mResourceStream;

    protected RemoteResourceAuto() {

    }

    @Override
    public RemoteResourceAuto setResourceAddress(String uri) {
        strUri = uri;
        if (!TextUtils.isEmpty(uri)) {

            if (strUri.startsWith("file://")) {
                strUri = strUri.substring(7);
                setResourceType(Type.Local);
            } else {
                setResourceType(Type.Network);
            }
        } else {
            mType = Type.Other;
        }
        return this;
    }

    @Override
    public String getResourceAddress() {
        return strUri;
    }

    @Override
    public RemoteResourceAuto setResourceStream(InputStream is) {
        mResourceStream = is;
        return this;
    }

    @Override
    public InputStream getResourceStream() {
        return mResourceStream;
    }

    @Override
    public final RemoteResourceAuto setResourceType(Type type) {
        mType = type;
        return this;
    }

    @Override
    public final Type getResourceType() {
        return mType;
    }
}
