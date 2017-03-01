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

    /**
     * 状态码
     * @return
     */
    int getStatusCode();

    /**
     * 获取Header
     * @param key
     * @return
     */
    String getHeader(String key);

    /**
     * 获取header字典
     * @return
     */
    Map<String, String> getHeaders();

    HTTPRequest getHttpRequest();

    long getContentLength();

    /**
     * 相应内容二进制
     * @return
     */
    byte[] getContent();

    /**
     * 响应内容String
     * @return
     */
    String getConentToString();

    /**
     * 错误信息
     * @return
     */
    String getErrorMessage();
}
