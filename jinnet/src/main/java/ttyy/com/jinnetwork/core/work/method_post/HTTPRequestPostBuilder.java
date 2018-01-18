package ttyy.com.jinnetwork.core.work.method_post;

import java.io.File;
import java.io.InputStream;

import ttyy.com.jinnetwork.core.callback.HTTPCallback;
import ttyy.com.jinnetwork.core.http.base.Client;
import ttyy.com.jinnetwork.core.http.base.ClientType;
import ttyy.com.jinnetwork.core.work.HTTPMethod;
import ttyy.com.jinnetwork.core.work.HTTPRequestBuilder;

/**
 * author: admin
 * date: 2017/02/28
 * version: 0
 * mail: secret
 * desc: HTTPRequestPostBuilder
 */

public class HTTPRequestPostBuilder extends HTTPRequestBuilder {

    protected PostContentType mContentType;

    public HTTPRequestPostBuilder(PostContentType type){
        super();
        mContentType = type;
        mHttpMethod = HTTPMethod.POST;

        addHeader(type.key(), type.value());
    }

    @Override
    public HTTPRequestPostBuilder addParam(String key, Object value) {
        if(value instanceof File){
            // File 类型需要特殊处理
            return addParam(key, (File)value);
        }
        return (HTTPRequestPostBuilder)super.addParam(key, value);
    }

    public HTTPRequestPostBuilder addParam(String key, File file){
        if(file == null){
            return this;
        }
        addParam(key, new RequestFileBody(file));
        return this;
    }

    public HTTPRequestPostBuilder addParam(String key, RequestFileBody body){
        if(body.getFile() == null){
            return this;
        }
        return (HTTPRequestPostBuilder)super.addParam(key, body);
    }

    @Override
    public HTTPRequestPostBuilder addHeader(String key, String value) {
        return (HTTPRequestPostBuilder)super.addHeader(key, value);
    }

    @Override
    public HTTPRequestPostBuilder delHeader(String key){
        return (HTTPRequestPostBuilder)super.delHeader(key);
    }

    @Override
    public HTTPRequestPostBuilder delParam(String key){
        return (HTTPRequestPostBuilder)super.delParam(key);
    }

    @Override
    public HTTPRequestPostBuilder setRequestURL(String url) {
        return (HTTPRequestPostBuilder)super.setRequestURL(url);
    }

    @Override
    public HTTPRequestPostBuilder addPathParam(String key, String value){
        return (HTTPRequestPostBuilder)super.addPathParam(key, value);
    }

    @Override
    public HTTPRequestPostBuilder setHttpCallback(HTTPCallback callback) {
        return (HTTPRequestPostBuilder)super.setHttpCallback(callback);
    }

    @Override
    public HTTPRequestPostBuilder setClientType(ClientType type) {
        return (HTTPRequestPostBuilder)super.setClientType(type);
    }

    @Override
    public HTTPRequestPostBuilder setRequestClient(Client client){
        return (HTTPRequestPostBuilder)super.setRequestClient(client);
    }

    @Override
    public HTTPRequestPostBuilder setResponseStream(InputStream is, File tmpFile){
        return (HTTPRequestPostBuilder)super.setResponseStream(is, tmpFile);
    }

    @Override
    public HTTPRequestPostBuilder setResponseFile(File file){
        return (HTTPRequestPostBuilder)super.setResponseFile(file);
    }

    /**
     * 设置此次请求的唯一标示符
     * @param token
     * @return
     */
    @Override
    public HTTPRequestPostBuilder setRequestUniqueToken(Object token){
        return (HTTPRequestPostBuilder)super.setRequestUniqueToken(token);
    }

    /**
     * 设置是否移除相同url的request 此url为包装过的url
     * @param value
     * @return
     */
    @Override
    public HTTPRequestPostBuilder setIsEnableRemoveSameRequest(boolean value){
        return (HTTPRequestPostBuilder)super.setIsEnableRemoveSameRequest(value);
    }

    @Override
    public PostContentType getContentType() {
        return mContentType;
    }

}
