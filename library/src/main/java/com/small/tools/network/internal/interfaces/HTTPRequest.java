package com.small.tools.network.internal.interfaces;

import com.small.tools.network.internal.async.Scheduler;
import com.small.tools.network.internal.cache.CacheAction;
import com.small.tools.network.internal.cache.SmallCache;
import com.small.tools.network.internal.SmallFileBody;

import java.io.File;
import java.util.Map;

/**
 * Author: hjq
 * Date  : 2018/10/02 16:14
 * Name  : SmallHTTPRequest
 * Intro : Edit By hjq
 * Version : 1.0
 */
public interface HTTPRequest {

    /* ********************** headers api begin ******************* */

    HTTPRequest setHeader(String key, String value);

    HTTPRequest setHeaders(Map<String, String> headers, boolean isForce);

    HTTPRequest clearHeader(String key);

    HTTPRequest clearHeaders();

    String getHeader(String key);

    Map<String, String> getHeaders();

    HTTPRequest setHTTPContent(SmallHeader.ContentType content);

    HTTPRequest setHeader(SmallHeader header);

    SmallHeader.ContentType getHTTPContent();

    /* ********************** headers api end ******************* */


    /* ********************** params api begin ******************* */

    HTTPRequest setParam(String key, String value);

    HTTPRequest setParam(String key, int value);

    HTTPRequest setParam(String key, long value);

    HTTPRequest setParam(String key, boolean value);

    HTTPRequest setParam(String key, float value);

    HTTPRequest setParam(String key, double value);

    HTTPRequest setParam(String key, File file);

    HTTPRequest setParam(String key, SmallFileBody file);

    HTTPRequest setParams(SmallParamsParser parser, boolean isForce);

    HTTPRequest setParams(Map<String, Object> params, boolean isForce);

    HTTPRequest clearParam(String key);

    HTTPRequest clearParams();

    <T> T getParam(String key);

    Map<String, Object> getParams();

    /* ********************** params api end ******************* */


    /* ********************** ssl api begin ******************* */

    // HTTPRequest setIgnoreSslCertificate();

    // HTTPRequest setTrustedSslCertificate(SSLSocketFactory factory);

    boolean isIgnoreSslCertificate();

    // SSLSocketFactory getTrustedSslCertificate();

    /* ********************** ssl api end ******************* */


    /* ********************** cache api begin ******************* */

    HTTPRequest setCacheAction(CacheAction action);

    CacheAction getCacheAction();

    HTTPRequest setCacheManager(SmallCache cache);

    SmallCache getCacheManager();

    /* ********************** cache api end ******************* */


    /* ********************** remote-resource api begin ******************* */

    HTTPRequest setRequestAddress(String address);

    HTTPRequest setRemoteResource(RemoteResource resources);

    RemoteResource getRemoteResource();

    /**
     * Remote-Resource that will be saved to local file
     * @param data
     * @return
     */
    HTTPRequest setResourceDataParser(ResourceDataParser data);

    <T extends ResourceDataParser> T getResourceDataParser();

    /* ********************** remote-resource api end ******************* */


    /* ********************** request api begin ******************* */

    HTTPRequest setHTTPClient(HTTPClient client);

    HTTPClient getHTTPClient();

    HTTPMethod getHTTPMethod();

    HTTPRequest setHTTPMethod(HTTPMethod method);

    HTTPResponse getResponse();

    HTTPRequest setHTTPCallback(HTTPCallback callback);

    HTTPCallback getHTTPCallback();

    HTTPRequest setScheduler(Scheduler scheduler);

    Scheduler getScheduler();

    HTTPResponse requestSync();

    void requestAsync();

    /* ********************** request api end ******************* */


    /* ********************** status api begin ******************* */

    boolean isExecuted();

    HTTPRequest cancel(boolean isUserCanceled);

    boolean isCanceled();

    HTTPRequest finish();

    boolean isFinished();

    /* ********************** status api end ******************* */

    Object getToken();

}
