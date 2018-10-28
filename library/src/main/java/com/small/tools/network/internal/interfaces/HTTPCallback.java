package com.small.tools.network.internal.interfaces;

/**
 * Author: hjq
 * Date  : 2018/10/02 22:04
 * Name  : HTTPCallback
 * Intro : Edit By hjq
 * Version : 1.0
 */
public interface HTTPCallback {

    void onStart(HTTPRequest request);

    void onProgress(HTTPRequest request, long cur, long total);

    void onSuccess(HTTPRequest request);

    void onFailure(HTTPRequest request);

    void onCancel(HTTPRequest request, boolean isUserCanceled);

    void onFinish(HTTPRequest request);

}
