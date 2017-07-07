package ttyy.com.jinnetwork.core.work.method_special;

import java.io.File;
import java.io.InputStream;

import ttyy.com.jinnetwork.core.callback.HTTPCallback;
import ttyy.com.jinnetwork.core.http.base.Client;
import ttyy.com.jinnetwork.core.http.base.ClientType;
import ttyy.com.jinnetwork.core.work.HTTPMethod;
import ttyy.com.jinnetwork.core.work.HTTPRequestBuilder;

/**
 * Author: hjq
 * Date  : 2017/07/07 21:51
 * Name  : HTTPRequestSpecialBuilder
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class HTTPRequestSpecialBuilder extends HTTPRequestBuilder{

    public HTTPRequestSpecialBuilder(){
        super();
        mHttpMethod = HTTPMethod.SPECIAL;
    }

    @Override
    public HTTPRequestSpecialBuilder addParam(String key, Object value) {
        return (HTTPRequestSpecialBuilder) super.addParam(key, value);
    }

    @Override
    public HTTPRequestSpecialBuilder addHeader(String key, String value) {
        return (HTTPRequestSpecialBuilder) super.addHeader(key, value);
    }

    @Override
    public HTTPRequestSpecialBuilder removeHeader(String key){
        return (HTTPRequestSpecialBuilder) super.removeHeader(key);
    }

    @Override
    public HTTPRequestSpecialBuilder removeParam(String key){
        return (HTTPRequestSpecialBuilder) super.removeParam(key);
    }

    @Override
    public HTTPRequestSpecialBuilder setRequestURL(String url) {
        return (HTTPRequestSpecialBuilder)super.setRequestURL(url);
    }

    @Override
    public HTTPRequestSpecialBuilder addPathParam(String key, String value){
        return (HTTPRequestSpecialBuilder)super.addPathParam(key, value);
    }

    @Override
    public HTTPRequestSpecialBuilder setHttpCallback(HTTPCallback callback) {
        return (HTTPRequestSpecialBuilder)super.setHttpCallback(callback);
    }

    @Override
    public HTTPRequestSpecialBuilder setClientType(ClientType type) {
        return (HTTPRequestSpecialBuilder)super.setClientType(type);
    }

    @Override
    public HTTPRequestSpecialBuilder setRequestClient(Client client){
        return (HTTPRequestSpecialBuilder)super.setRequestClient(client);
    }

    @Override
    public HTTPRequestSpecialBuilder setResponseStream(InputStream is, File tmpFile){
        return (HTTPRequestSpecialBuilder)super.setResponseStream(is, tmpFile);
    }

    @Override
    public HTTPRequestSpecialBuilder setResponseFile(File file){
        return (HTTPRequestSpecialBuilder)super.setResponseFile(file);
    }

    /**
     * 设置此次请求的唯一标示符
     * @param token
     * @return
     */
    @Override
    public HTTPRequestSpecialBuilder setRequestUniqueToken(Object token){
        return (HTTPRequestSpecialBuilder)super.setRequestUniqueToken(token);
    }

    /**
     * 设置是否移除相同url的request 此url为包装过的url
     * @param value
     * @return
     */
    @Override
    public HTTPRequestSpecialBuilder setIsEnableRemoveSameRequest(boolean value){
        return (HTTPRequestSpecialBuilder)super.setIsEnableRemoveSameRequest(value);
    }

}
