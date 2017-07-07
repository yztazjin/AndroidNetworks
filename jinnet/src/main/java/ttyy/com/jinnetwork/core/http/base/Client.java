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

    /**
     * HTTP Post
     * @param worker
     * @return
     */
    HTTPResponse post(HTTPRequest worker);

    /**
     * HTTP Get
     * @param worker
     * @return
     */
    HTTPResponse get(HTTPRequest worker);

    /**
     * HTTP User Custom Operation HTTP Special
     * @param request
     * @return
     */
    HTTPResponse special(HTTPRequest request);

}
