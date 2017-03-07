package ttyy.com.jinnetwork.core.work.method_post;

import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;

import ttyy.com.jinnetwork.core.callback.HttpCallback;
import ttyy.com.jinnetwork.core.http.base.Client;
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
    public HttpRequestPostBuilder addPathParam(String key, String value){
        return (HttpRequestPostBuilder)super.addPathParam(key, value);
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
    public HttpRequestPostBuilder setRequestClient(Client client){
        return (HttpRequestPostBuilder)super.setRequestClient(client);
    }

    @Override
    public HttpRequestPostBuilder setResponseStream(InputStream is, File tmpFile){
        return (HttpRequestPostBuilder)super.setResponseStream(is, tmpFile);
    }

    @Override
    public HttpRequestPostBuilder setResponseFile(File file){
        return (HttpRequestPostBuilder)super.setResponseFile(file);
    }

    @Override
    public PostContentType getContentType() {
        return mContentType;
    }
}
