package com.small.tools.network.internal;

import com.small.tools.network.internal.interfaces.HTTPCallback;
import com.small.tools.network.internal.interfaces.HTTPRequest;

/**
 * Author: hjq
 * Date  : 2018/10/06 12:16
 * Name  : HTTPCallbackAdapter
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class HTTPCallbackAdapter implements HTTPCallback {

    @Override
    public void onStart(HTTPRequest request) {

    }

    @Override
    public void onProgress(HTTPRequest request, long cur, long total) {

    }

    @Override
    public void onSuccess(HTTPRequest request) {

    }

    @Override
    public void onFailure(HTTPRequest request) {

    }

    @Override
    public void onCancel(HTTPRequest request, boolean isUserCanceled) {

    }

    @Override
    public void onFinish(HTTPRequest request) {

    }
}
