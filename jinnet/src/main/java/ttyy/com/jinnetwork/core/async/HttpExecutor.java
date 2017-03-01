package ttyy.com.jinnetwork.core.async;

import ttyy.com.jinnetwork.core.work.HTTPRequest;

/**
 * Author: hjq
 * Date  : 2017/03/01 21:05
 * Name  : HttpExecutor
 * Intro : Edit By hjq
 * Modification  History:
 * Date          Author        	 Version          Description
 * ----------------------------------------------------------
 * 2017/03/01    hjq   1.0              1.0
 */
public interface HttpExecutor {

    HttpExecutor addRequest(HTTPRequest request);

    void start();
}
