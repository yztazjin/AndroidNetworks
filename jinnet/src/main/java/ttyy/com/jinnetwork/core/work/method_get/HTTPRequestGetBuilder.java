package ttyy.com.jinnetwork.core.work.method_get;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import ttyy.com.jinnetwork.core.callback.HTTPCallback;
import ttyy.com.jinnetwork.core.config.__Log;
import ttyy.com.jinnetwork.core.http.base.Client;
import ttyy.com.jinnetwork.core.http.base.ClientType;
import ttyy.com.jinnetwork.core.work.HTTPMethod;
import ttyy.com.jinnetwork.core.work.HTTPRequestBuilder;

/**
 * author: admin
 * date: 2017/03/01
 * version: 0
 * mail: secret
 * desc: HTTPRequestGetBuilder
 */

public class HTTPRequestGetBuilder extends HTTPRequestBuilder {

    private File mDownloadFile;

    public HTTPRequestGetBuilder(){
        super();
        mHttpMethod = HTTPMethod.GET;
    }

    @Override
    public HTTPRequestGetBuilder addParam(String key, Object value) {
        return (HTTPRequestGetBuilder)super.addParam(key, value);
    }

    @Override
    public HTTPRequestGetBuilder addHeader(String key, String value) {
        return (HTTPRequestGetBuilder)super.addHeader(key, value);
    }

    @Override
    public HTTPRequestGetBuilder removeHeader(String key){
        return (HTTPRequestGetBuilder)super.removeHeader(key);
    }

    @Override
    public HTTPRequestGetBuilder removeParam(String key){
        return (HTTPRequestGetBuilder)super.removeParam(key);
    }

    @Override
    public HTTPRequestGetBuilder setRequestURL(String url) {
        return (HTTPRequestGetBuilder)super.setRequestURL(url);
    }

    @Override
    public HTTPRequestGetBuilder addPathParam(String key, String value){
        return (HTTPRequestGetBuilder)super.addPathParam(key, value);
    }

    @Override
    public HTTPRequestGetBuilder setHttpCallback(HTTPCallback callback) {
        return (HTTPRequestGetBuilder)super.setHttpCallback(callback);
    }

    @Override
    public HTTPRequestGetBuilder setClientType(ClientType type) {
        return (HTTPRequestGetBuilder)super.setClientType(type);
    }

    @Override
    public HTTPRequestGetBuilder setRequestClient(Client client){
        return (HTTPRequestGetBuilder)super.setRequestClient(client);
    }

    @Override
    public HTTPRequestGetBuilder setResponseStream(InputStream is, File tmpFile){
        return (HTTPRequestGetBuilder)super.setResponseStream(is, tmpFile);
    }

    @Override
    public HTTPRequestGetBuilder setResponseFile(File file){
        return (HTTPRequestGetBuilder)super.setResponseFile(file);
    }

    public HTTPRequestGetBuilder setDownloadMode(File file){
        if(file == null)
            return this;

        if(!file.exists()){
            boolean value = file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                __Log.w("Https", "setDownloadMode can't create file");
            }
        }else {
            if(file.length() > 0){
                addHeader("Range", "bytes=" + file.length() + "-");
            }
        }
        mDownloadFile = file;
        return this;
    }

    @Override
    public File getDownloadFile() {
        return mDownloadFile;
    }

    /**
     * 设置此次请求的唯一标示符
     * @param token
     * @return
     */
    @Override
    public HTTPRequestGetBuilder setRequestUniqueToken(Object token){
        return (HTTPRequestGetBuilder)super.setRequestUniqueToken(token);
    }

    /**
     * 设置是否移除相同url的request 此url为包装过的url
     * @param value
     * @return
     */
    @Override
    public HTTPRequestGetBuilder setIsEnableRemoveSameRequest(boolean value){
        return (HTTPRequestGetBuilder)super.setIsEnableRemoveSameRequest(value);
    }
}
