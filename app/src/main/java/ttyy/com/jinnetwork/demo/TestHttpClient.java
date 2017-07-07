package ttyy.com.jinnetwork.demo;

import android.util.Log;

import ttyy.com.jinnetwork.core.http.base.Client;
import ttyy.com.jinnetwork.core.work.HTTPRequest;
import ttyy.com.jinnetwork.core.work.HTTPResponse;
import ttyy.com.jinnetwork.core.work.inner.$HttpResponse;

/**
 * Author: hjq
 * Date  : 2017/07/07 22:08
 * Name  : TestHttpClient
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class TestHttpClient implements Client {

    public static TestHttpClient INSTANCE = new TestHttpClient();

    @Override
    public HTTPResponse post(HTTPRequest worker) {
        $HttpResponse err = new $HttpResponse(worker);
        err.setStatusCode(-1);
        err.setErrorMessage("不支持HTTP POST 请求方式");
        return err;
    }

    @Override
    public HTTPResponse get(HTTPRequest worker) {
        $HttpResponse err = new $HttpResponse(worker);
        err.setStatusCode(-1);
        err.setErrorMessage("不支持HTTP GET 请求方式");
        return err;
    }

    @Override
    public HTTPResponse special(HTTPRequest request) {

        Log.e("Test", "use user special method ");
        $HttpResponse err = new $HttpResponse(request);
        err.setStatusCode(200);
        err.setErrorMessage("SUCCESS");

        return err;
    }
}
