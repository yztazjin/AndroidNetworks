package com.small.tools.network.internal;

import com.small.tools.network.internal.interfaces.HTTPRequest;
import com.small.tools.network.internal.tools.DataGetter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Author: hjq
 * Date  : 2018/10/06 12:16
 * Name  : HTTPCallbackAdapter
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class HTTPJsonCallback<T> extends HTTPCallbackAdapter {

    Class<T> mJsonType;

    public HTTPJsonCallback() {
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            mJsonType = (Class<T>) pt.getActualTypeArguments()[0];
        }
    }

    @Override
    public final void onSuccess(HTTPRequest request) {

        onSuccess(request, DataGetter.getData(request, mJsonType));
    }

    public void onSuccess(HTTPRequest request, T data) {

    }

}
