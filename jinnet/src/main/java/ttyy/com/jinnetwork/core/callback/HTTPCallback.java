package ttyy.com.jinnetwork.core.callback;

import ttyy.com.jinnetwork.core.work.HTTPRequest;
import ttyy.com.jinnetwork.core.work.HTTPResponse;

/**
 * author: admin
 * date: 2017/02/28
 * version: 0
 * mail: secret
 * desc: HTTPCallback
 */

public interface HTTPCallback {

    /**
     * 请求之前
     * @param request
     */
    void onPreStart(HTTPRequest request);

    /**
     * 进度
     * 下载进度
     * @param response
     * @param cur
     * @param total
     */
    void onProgress(HTTPResponse response, long cur, long total);

    /**
     * 获取到服务器响应
     * @param response
     */
    void onSuccess(HTTPResponse response);

    /**
     * 用户取消
     * @param response
     */
    void onCancel(HTTPRequest response);

    /**
     * 请求服务器失败
     * @param response
     */
    void onFailure(HTTPResponse response);

    /**
     * 终止
     */
    void onFinish(HTTPResponse response);
}
