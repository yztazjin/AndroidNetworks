package ttyy.com.jinnetwork.core.work;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import ttyy.com.jinnetwork.core.async.$HttpExecutorPool;
import ttyy.com.jinnetwork.core.async.HttpExecutor;
import ttyy.com.jinnetwork.core.callback.HTTPCallback;
import ttyy.com.jinnetwork.core.config.__Log;
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

    protected HTTPRequestBuilder builder;

    protected boolean isRequestCanceled;

    protected HTTPRequest(HTTPRequestBuilder builder) {
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

        return builder.getDecoratedRequestURL();
    }

    public HTTPCallback getHttpCallback() {

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
     * @param isUserCancel  用户是否取消
     * @return
     */
    public HTTPRequest $cancel(boolean isUserCancel) {
        isRequestCanceled = true;
        // 用户取消
        if (getHttpCallback() != null && isUserCancel) {
            getHttpCallback().onCancel(this);
            getHttpCallback().onFinish(null);
        }else {
            __Log.w("Https", "AsyncExecutor Remove It !");
        }
        return this;
    }

    /**
     * 取消
     * @return
     */
    public HTTPRequest cancel() {
        // 默认为用户取消
        return $cancel(true);
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
    protected final HTTPResponse readDataFromNetwork(Client mClient){

        HTTPCallback callback = getHttpCallback();
        HTTPResponse rsp = null;

        if (callback != null) {
            callback.onPreStart(this);
        }

        if(builder.mHttpMethod == HTTPMethod.GET){
            // GET
            rsp = mClient.get(this);
        }else if(builder.mHttpMethod == HTTPMethod.POST){
            // POST
            rsp = mClient.post(this);
        }else if(builder.mHttpMethod == HTTPMethod.SPECIAL){
            // USER CUSTOM SPECIAL
            rsp = mClient.special(this);
        }else {
            // 没有HttpMethod
            $HttpResponse empty = new $HttpResponse(this);
            empty.setStatusCode(-1);
            empty.setErrorMessage("没有发现任何合适的HttpMethod");

            rsp = empty;
        }

        if (rsp != null) {
            if (callback != null) {
                if (rsp.isStatusCodeSuccessful()) {
                    // 416 206 断点续传相关
                    callback.onSuccess(rsp);
                } else {
                    // 出错
                    callback.onFailure(rsp);
                }

                // 回调终点
                callback.onFinish(rsp);
            }

        }

        return rsp;
    }

    /**
     * 从自定义的Response返回流中获取数据
     * @return
     */
    protected final HTTPResponse readDataFromCustomResponse(InputStream is){
        HTTPCallback callback = getHttpCallback();
        $HttpResponse rsp = new $HttpResponse(this);

        if (callback != null) {
            callback.onPreStart(this);
        }

        if(is == null){
            // InputStream Not Exists
            rsp.setStatusCode(-1);// 出错
            rsp.setErrorMessage("InputStream is null, maybe source file not exists");
        }else {
            // InputStream Exists
            // 从磁盘文件中读取数据状态码
            rsp.setStatusCode(802);
            rsp.setContentLength(builder.getResponseStreamFile().length());

            if(getDownloadFile() != null
                    && getDownloadFile().equals(builder.getResponseStreamFile())){
                // 对同一个文件 不支持同时读写 也没必要进行这样的IO操作
                try {
                    is.close();
                } catch (IOException e) {

                }
            }else {
                rsp.readContentFromStream(is);
            }
        }

        if (rsp != null) {
            if (callback != null) {
                if (rsp.isStatusCodeSuccessful()) {
                    // 416 206 断点续传相关
                    callback.onSuccess(rsp);
                } else {
                    // 出错
                    callback.onFailure(rsp);
                }

                // 回调终点
                callback.onFinish(rsp);
            }
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

    public Object getUniqueToken(){

        return builder.getRequestUniqueToken();
    }

    public boolean isEnableRemoveSameRequest(){

        return builder.isEnableRemoveSameRequest();
    }

    /**
     * 先判断hashcode获取到的地址是否相同
     * hashCode若相同那么说明equals相同
     * Object equals方法只判断地址,
     * String equals方法先判断地址是否相同，不同就比较具体的值
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        // obj null 肯定不相等
        if(obj == null){
            return false;
        }

        // 比较地址
        if(hashCode() == obj.hashCode()){
            return true;
        }

        // 比较值
        if(obj instanceof HTTPRequest){
            HTTPRequest t_obj = (HTTPRequest) obj;

            if(t_obj.isEnableRemoveSameRequest()
                    && isEnableRemoveSameRequest()){
                return getUniqueToken().equals(t_obj.getUniqueToken());
            }
        }
        return false;
    }

    /**
     * 默认情况下返回的是对象在JVM下的32位地址
     * @return
     */
    @Override
    public int hashCode() {
        if(isEnableRemoveSameRequest()){

            return getUniqueToken().hashCode();
        }
        return super.hashCode();
    }
}
