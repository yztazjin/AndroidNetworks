package ttyy.com.jinnetwork.core.work;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import ttyy.com.jinnetwork.core.callback.$HttpUIThreadCallbackAdapterProxy;
import ttyy.com.jinnetwork.core.callback.HttpCallback;
import ttyy.com.jinnetwork.core.callback.HttpUIThreadCallbackAdapter;
import ttyy.com.jinnetwork.core.config.HttpConfig;
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

    private Map<String, Object> params = new TreeMap<String, Object>();
    private Map<String, String> headers = new TreeMap<String, String>();

    private String mRequestURL;

    private HttpCallback mCallback;

    protected HttpMethod mHttpMethod;
    protected ClientType mClientType;

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

    public ClientType getClientType(){
        return mClientType;
    }

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
