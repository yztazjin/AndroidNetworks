package ttyy.com.jinnetwork.demo;

import com.small.tools.network.internal.interfaces.HTTPClient;
import com.small.tools.network.internal.interfaces.HTTPRequest;
import com.small.tools.network.internal.interfaces.HTTPResponse;

/**
 * Author: hjq
 * Date  : 2017/07/07 22:08
 * Name  : TestHttpClient
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class TestHttpClient implements HTTPClient {

    public static TestHttpClient INSTANCE = new TestHttpClient();

    @Override
    public HTTPResponse get(HTTPRequest request) {
        return null;
    }

    @Override
    public HTTPResponse post(HTTPRequest request) {
        return null;
    }

//    @Override
//    public  post(HTTPRequest worker) {
//        $HttpResponse err = new $HttpResponse(worker);
//        err.setStatusCode(-1);
//        err.setErrorMessage("不支持HTTP POST 请求方式");
//        return err;
//    }
//
//    @Override
//    public HTTPResponse get(HTTPRequest worker) {
//        $HttpResponse err = new $HttpResponse(worker);
//        err.setStatusCode(-1);
//        err.setErrorMessage("不支持HTTP GET 请求方式");
//        return err;
//    }
//
//    @Override
//    public HTTPResponse special(HTTPRequest request) {
//
//        Log.e("Test", "use user special method ");
//        $HttpResponse err = new $HttpResponse(request);
//        err.setStatusCode(200);
//        err.setErrorMessage("SUCCESS");
//
//        return err;
//    }
}
