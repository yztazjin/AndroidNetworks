package com.small.tools.network.internal;

import android.text.TextUtils;

import com.small.tools.network.global.SmallLogs;
import com.small.tools.network.internal.interfaces.RemoteResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Author: hjq
 * Date  : 2018/10/02 17:03
 * Name  : RemoteResources
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class RemoteResourceLocal implements RemoteResource {

    String strUri;
    File mFile;
    InputStream mResourceStream;

    protected RemoteResourceLocal() {

    }

    public RemoteResourceLocal setResourceAddress(File file) {
        mFile = file;
        strUri = mFile == null ? strUri : mFile.getAbsolutePath();
        return this;
    }

    @Override
    public RemoteResourceLocal setResourceAddress(String uri) {
        strUri = uri;
        return this;
    }

    @Override
    public String getResourceAddress() {
        return strUri;
    }

    @Override
    public RemoteResourceLocal setResourceStream(InputStream is) {
        mResourceStream = is;
        return this;
    }

    @Override
    public InputStream getResourceStream() {
        return mResourceStream;
    }

    @Override
    public final RemoteResourceLocal setResourceType(Type type) {
        return this;
    }

    @Override
    public final Type getResourceType() {
        return Type.Local;
    }
}
