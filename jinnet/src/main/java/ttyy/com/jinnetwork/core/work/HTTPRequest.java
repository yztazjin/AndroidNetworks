package ttyy.com.jinnetwork.core.work;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import ttyy.com.jinnetwork.core.async.$HttpExecutorPool;
import ttyy.com.jinnetwork.core.async.HttpExecutor;
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

    /**
     * 获取请求URL
     * @return
     */
    public String getRequestURL() {

        return builder.getRequestURL();
    }

    public HttpCallback getHttpCallback() {

        return builder.getHttpCallback();
    }

    /**
     * 获取ContentType
     * get无效
     * 单独列出，post根据不同的type，有不同的提交数据行为
     * @return
     */
    public PostContentType getContentType() {
        return builder.getContentType();
    }

    /**
     * 取消
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

    /**
     * 获取下载存储的文件
     * @return
     */
    public File getDownloadFile() {
        return builder.getDownloadFile();
    }

    /**
     * 在当前线程中执行
     * @return
     */
    public HTTPResponse request() {

        HTTPResponse rsp = null;

        if(builder.getResponseStreamFile() != null){

            rsp = readDataFromCustomResponse(builder.getResponseStream());
        }else {

            rsp = readDataFromNetwork(getRequestClient());
        }

        return rsp;
    }

    /**
     * 获取网络请求客户端
     * @return
     */
    public Client getRequestClient(){
        Client mClient = builder.getRequestClient();
        if(mClient != null){
            return mClient;
        }

        ClientType mClientType = builder.getClientType();
        if (mClientType == ClientType.APACHE_CLIENT) {

            mClient = ApacheHttpClientImpl.getInstance();
        } else if (mClientType == ClientType.OKHTTP_CLIENT) {

            mClient = OKHttpClientImpl.getInstance();
        } else {

            mClient = URLConnectionImpl.getInstance();
        }

        return mClient;
    }

    /**
     * 从网络请求
     * @param mClient
     * @return
     */
    private HTTPResponse readDataFromNetwork(Client mClient){

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

    /**
     * 从自定义的Response返回流中获取数据
     * @return
     */
    private HTTPResponse readDataFromCustomResponse(InputStream is){
        HttpCallback callback = getHttpCallback();
        $HttpResponse rsp = new $HttpResponse(this);

        if (callback != null) {
            callback.onPreStart(this);
        }

        rsp.setStatusCode(200);
        rsp.setContentLength(builder.getResponseStreamFile().length());

        if(getDownloadFile() != null
                && getDownloadFile().equals(builder.getResponseStreamFile())){
            // 对同一个文件 不支持同时读写

        }else {
            rsp.readContentFromStream(is);
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

    /**
     * 异步执行
     * 立即执行
     */
    public void requestNowAsync() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                request();
            }
        }).start();
    }

    /**
     * 异步执行
     * 可能需要排队
     */
    public void requestAsync() {
        $HttpExecutorPool.get().getDefaultExecutor().addRequest(this).start();
    }

    /**
     * 异步执行
     * 可能需要排队
     * 指定Executor
     */
    public void $async(HttpExecutor executor) {
        executor.addRequest(this).start();
    }
}
