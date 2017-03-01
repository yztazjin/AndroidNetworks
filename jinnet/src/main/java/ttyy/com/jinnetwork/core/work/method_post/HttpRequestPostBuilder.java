package ttyy.com.jinnetwork.core.work.method_post;

import org.json.JSONObject;

import java.io.File;

import ttyy.com.jinnetwork.core.callback.HttpCallback;
import ttyy.com.jinnetwork.core.http.base.ClientType;
import ttyy.com.jinnetwork.core.work.HttpMethod;
import ttyy.com.jinnetwork.core.work.HttpRequestBuilder;

/**
 * author: admin
 * date: 2017/02/28
 * version: 0
 * mail: secret
 * desc: HttpRequestPostBuilder
 */

public class HttpRequestPostBuilder extends HttpRequestBuilder {

    protected PostContentType mContentType;

    public HttpRequestPostBuilder(PostContentType type){
        super();
        mContentType = type;
        mHttpMethod = HttpMethod.POST;

        addHeader(type.key(), type.value());
    }

    @Override
    public HttpRequestPostBuilder addParam(String key, String value) {
        return (HttpRequestPostBuilder)super.addParam(key, value);
    }

    @Override
    public HttpRequestPostBuilder addParam(String key, int value) {
        return (HttpRequestPostBuilder)super.addParam(key, value);
    }

    @Override
    public HttpRequestPostBuilder addParam(String key, float value) {
        return (HttpRequestPostBuilder)super.addParam(key, value);
    }

    @Override
    public HttpRequestPostBuilder addParam(String key, long value) {
        return (HttpRequestPostBuilder)super.addParam(key, value);
    }

    @Override
    public HttpRequestPostBuilder addParam(String key, double value) {
        return (HttpRequestPostBuilder)super.addParam(key, value);
    }

    public HttpRequestPostBuilder addParam(String key, JSONObject value){
        getParasmDict().put(key, value);
        return this;
    }

    @Override
    public HttpRequestPostBuilder addHeader(String key, String value) {
        return (HttpRequestPostBuilder)super.addHeader(key, value);
    }

    @Override
    public HttpRequestPostBuilder removeHeader(String key){
        return (HttpRequestPostBuilder)super.removeHeader(key);
    }

    @Override
    public HttpRequestPostBuilder removeParam(String key){
        return (HttpRequestPostBuilder)super.removeParam(key);
    }

    @Override
    public HttpRequestPostBuilder setRequestURL(String url) {
        return (HttpRequestPostBuilder)super.setRequestURL(url);
    }

    @Override
    public HttpRequestPostBuilder setHttpCallback(HttpCallback callback) {
        return (HttpRequestPostBuilder)super.setHttpCallback(callback);
    }

    @Override
    public HttpRequestPostBuilder setClientType(ClientType type) {
        return (HttpRequestPostBuilder)super.setClientType(type);
    }

    @Override
    public PostContentType getContentType() {
        return mContentType;
    }
}
