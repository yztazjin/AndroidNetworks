package ttyy.com.jinnetwork.core.work;

import java.util.Map;

/**
 * author: admin
 * date: 2017/02/28
 * version: 0
 * mail: secret
 * desc: HTTPResponse
 */

public interface HTTPResponse {

    int getStatusCode();

    String getHeader(String key);

    Map<String, String> getHeaders();

    HTTPRequest getHttpRequest();

    long getContentLength();

    byte[] getContent();

    String getConentToString();

    String getErrorMessage();
}
