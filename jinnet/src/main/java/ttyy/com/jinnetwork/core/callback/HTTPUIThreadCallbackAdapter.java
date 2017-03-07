package ttyy.com.jinnetwork.core.callback;

import ttyy.com.jinnetwork.core.work.HTTPRequest;
import ttyy.com.jinnetwork.core.work.HTTPResponse;

/**
 * author: admin
 * date: 2017/02/28
 * version: 0
 * mail: secret
 * desc: HTTPUIThreadCallbackAdapter
 */

public class HTTPUIThreadCallbackAdapter implements HTTPCallback {

    @Override
    public void onPreStart(HTTPRequest request) {

    }

    @Override
    public void onProgress(HTTPResponse request, long cur, long total) {

    }

    @Override
    public void onSuccess(HTTPResponse response) {

    }

    @Override
    public void onCancel(HTTPRequest response) {

    }

    @Override
    public void onFailure(HTTPResponse response) {

    }

    @Override
    public void onFinish(HTTPResponse response) {

    }
}
