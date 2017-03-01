package ttyy.com.jinnetwork.core.work;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

import ttyy.com.jinnetwork.core.callback.$HttpUIThreadCallbackAdapterProxy;
import ttyy.com.jinnetwork.core.callback.HttpCallback;
import ttyy.com.jinnetwork.core.callback.HttpUIThreadCallbackAdapter;
import ttyy.com.jinnetwork.core.config.HttpConfig;
import ttyy.com.jinnetwork.core.http.base.Client;
import ttyy.com.jinnetwork.core.http.base.ClientType;
import ttyy.com.jinnetwork.core.work.method_post.PostContentType;

/**
 * author: admin
 * date: 2017/02/27
 * version: 0
 * mail: secret
 * desc: HttpRequestBuilder
 */

public class HttpRequestBuilder {

    protected Map<String, Object> params = new TreeMap<String, Object>();
    protected Map<String, String> headers = new TreeMap<String, String>();

    protected String mRequestURL;

    protected HttpCallback mCallback;

    protected HttpMethod mHttpMethod;
    protected ClientType mClientType;
    protected Client mRequestClient;

    /**
     * 响应文件
     * 如果响应文件不为空，那么就不会从网络请求数据，直接从该文件获取数据
     */
    protected byte[] mResponseStreamBytes;

    protected HttpRequestBuilder(){
        mClientType = HttpConfig.get().getClientType();
    }

    public HttpRequestBuilder addParam(String key, String value){
        params.put(key, value);
        return this;
    }

    public HttpRequestBuilder addParam(String key, int value){
        params.put(key, value);
        return this;
    }

    public HttpRequestBuilder addParam(String key, float value){
        params.put(key, value);
        return this;
    }

    public HttpRequestBuilder addParam(String key, long value){
        params.put(key, value);
        return this;
    }

    public HttpRequestBuilder addParam(String key, double value){
        params.put(key, value);
        return this;
    }

    public HttpRequestBuilder addHeader(String key, String value){
        headers.put(key, value);
        return this;
    }

    public HttpRequestBuilder removeHeader(String key){
        headers.remove(key);
        return this;
    }

    public HttpRequestBuilder removeParam(String key){
        params.remove(key);
        return this;
    }

    public Map<String, String> getHeadersDict(){
        return headers;
    }

    public Map<String, Object> getParasmDict(){
        return params;
    }

    public HttpRequestBuilder setRequestURL(String url){
        mRequestURL = url;
        return this;
    }

    /**
     * 设置自定义的网络请求客户端
     * @param client
     * @return
     */
    public HttpRequestBuilder setRequestClient(Client client){
        mRequestClient = client;
        return this;
    }

    /**
     * 获取自定义的网络客户端
     * @return
     */
    public Client getRequestClient(){
        return mRequestClient;
    }

    public String getRequestURL(){
        return mRequestURL;
    }

    public HttpRequestBuilder setHttpCallback(HttpCallback callback){
        if(callback instanceof HttpUIThreadCallbackAdapter){
            mCallback = $HttpUIThreadCallbackAdapterProxy.get(callback);
        }else {
            mCallback = callback;
        }
        return this;
    }

    public HttpCallback getHttpCallback(){
        return mCallback;
    }

    public File getDownloadFile(){
        return null;
    }

    public HttpRequestBuilder setResponseStream(InputStream is){

        byte[] buffer = new byte[4096];
        int length = 0;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            while ((length = is.read(buffer))!=-1){
                baos.write(buffer, 0, length);
            }
            is.close();
            mResponseStreamBytes = baos.toByteArray();
        } catch (IOException e) {
            Log.w("Https", "io exception");
        }

        return this;
    }

    /**
     * 设置自定义的响应文件
     * 设置了该字段，数据不会走网络请求
     * @param file
     * @return
     */
    public HttpRequestBuilder setResponseFile(File file){
        try {
            return setResponseStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            Log.w("Https", "response file not exists!");
        }
        return this;
    }

    /**
     * 包装自定义的响应流
     * @return
     */
    public InputStream getResponseStream(){
        if(mResponseStreamBytes == null)
            return null;
        return new ByteArrayInputStream(mResponseStreamBytes);
    }

    /**
     * 获取响应流的数据 二进制
     * @return
     */
    public byte[] getResponseStreamBytes(){
        return mResponseStreamBytes;
    }

    /**
     * 获取网络请求客户端类型
     * @return
     */
    public ClientType getClientType(){
        return mClientType;
    }

    /**
     * 获取post的contenttype
     * @return
     */
    public PostContentType getContentType(){
        return null;
    }

    public HttpRequestBuilder setClientType(ClientType type){
        mClientType = type;
        return this;
    }

    public HTTPRequest build(){
        return new HTTPRequest(this);
    }
}
