package ttyy.com.jinnetwork.core.work;

import java.io.File;
import java.util.Map;

import ttyy.com.jinnetwork.core.async.$HttpExecutorPool;
import ttyy.com.jinnetwork.core.callback.HttpCallback;
import ttyy.com.jinnetwork.core.http.ApacheHttpClientImpl;
import ttyy.com.jinnetwork.core.http.OKHttpClientImpl;
import ttyy.com.jinnetwork.core.http.URLConnectionImpl;
import ttyy.com.jinnetwork.core.http.base.Client;
import ttyy.com.jinnetwork.core.http.base.ClientType;
import ttyy.com.jinnetwork.core.work.inner.$HttpResponse;
import ttyy.com.jinnetwork.core.work.method_post.PostContentType;

/**
 * author: admin
 * date: 2017/02/28
 * version: 0
 * mail: secret
 * desc: HTTPRequest
 */

public class HTTPRequest {

    private HttpRequestBuilder builder;

    private boolean isRequestCanceled;

    protected HTTPRequest(HttpRequestBuilder builder) {
        this.builder = builder;
    }

    public Map<String, String> getHeaders() {

        return builder.getHeadersDict();
    }

    public Map<String, Object> getParams() {

        return builder.getParasmDict();
    }

    public <T> T getParam(String key){

        return (T) builder.getParasmDict().get(key);
    }

    public String getHeader(String key){

        return builder.getHeadersDict().get(key);
    }

    public String getRequestURL() {

        return builder.getRequestURL();
    }

    public HttpCallback getHttpCallback() {

        return builder.getHttpCallback();
    }

    public PostContentType getContentType() {
        return builder.getContentType();
    }

    /**
     * 取消
     *
     * @return
     */
    public HTTPRequest cancel() {
        isRequestCanceled = true;
        // 用户取消
        if (getHttpCallback() != null) {
            getHttpCallback().onCancel(this);
            getHttpCallback().onFinish(null);
        }
        return this;
    }

    public boolean isCanceled() {
        return isRequestCanceled;
    }

    public File getDownloadFile() {
        return builder.getDownloadFile();
    }

    public HTTPResponse request() {
        Client mClient;
        ClientType mClientType = builder.getClientType();
        if (mClientType == ClientType.APACHE_CLIENT) {

            mClient = ApacheHttpClientImpl.getInstance();
        } else if (mClientType == ClientType.OKHTTP_CLIENT) {

            mClient = OKHttpClientImpl.getInstance();
        } else {

            mClient = URLConnectionImpl.getInstance();
        }

        HttpCallback callback = getHttpCallback();
        HTTPResponse rsp = null;

        if (callback != null) {
            callback.onPreStart(this);
        }

        if(builder.mHttpMethod == HttpMethod.GET){
            rsp = mClient.get(this);
        }else if(builder.mHttpMethod == HttpMethod.POST){
            rsp = mClient.post(this);
        }else {
            // 没有HttpMethod
            $HttpResponse empty = new $HttpResponse(this);
            empty.setStatusCode(-1);
            empty.setErrorMessage("没有发现何时的HttpMethod");

            rsp = empty;
        }

        if (rsp != null) {
            if (callback != null) {
                if (rsp.getStatusCode() == 200
                        || rsp.getStatusCode() == 416
                        || rsp.getStatusCode() == 206) {
                    // 416 206 断点续传相关
                    callback.onSuccess(rsp);
                } else {
                    // 出错
                    callback.onFailure(rsp);
                }
            }

            // 回调终点
            getHttpCallback().onFinish(rsp);
        }

        return rsp;
    }

    public void requestNowAsync() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                request();
            }
        }).start();
    }

    public void requestAsync() {
        $HttpExecutorPool.get().getDefaultExecutor().addRequest(this).start();
    }
}
