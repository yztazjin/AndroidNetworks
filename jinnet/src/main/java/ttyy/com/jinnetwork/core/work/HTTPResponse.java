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
     * -1 出现了异常
     * 100 图片加载 加载磁盘thumb缓存专用状态码
     * 101 图片加载 加载内存缓存专用状态码
     * 102 从磁盘文件中获取数据状态码
     * @return
     */
    int getStatusCode();

    /**
     * 状态码是不是成功的
     * @return
     */
    boolean isStatusCodeSuccessful();

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
