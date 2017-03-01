package ttyy.com.jinnetwork.core.http.base;

import ttyy.com.jinnetwork.core.work.HTTPResponse;
import ttyy.com.jinnetwork.core.work.HTTPRequest;

/**
 * author: admin
 * date: 2017/02/27
 * version: 0
 * mail: secret
 * desc: Client
 */

public interface Client {

    HTTPResponse post(HTTPRequest worker);

    HTTPResponse get(HTTPRequest worker);

}
