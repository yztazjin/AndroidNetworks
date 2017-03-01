package ttyy.com.jinnetwork.core.work.method_get;

import java.io.File;
import java.io.IOException;

import ttyy.com.jinnetwork.core.callback.HttpCallback;
import ttyy.com.jinnetwork.core.http.base.ClientType;
import ttyy.com.jinnetwork.core.work.HttpMethod;
import ttyy.com.jinnetwork.core.work.HttpRequestBuilder;

/**
 * author: admin
 * date: 2017/03/01
 * version: 0
 * mail: secret
 * desc: HttpRequestGetBuilder
 */

public class HttpRequestGetBuilder extends HttpRequestBuilder {

    private File mDownloadFile;

    public HttpRequestGetBuilder(){
        super();
        mHttpMethod = HttpMethod.GET;
    }

    @Override
    public HttpRequestGetBuilder addParam(String key, String value) {
        return (HttpRequestGetBuilder)super.addParam(key, value);
    }

    @Override
    public HttpRequestGetBuilder addParam(String key, int value) {
        return (HttpRequestGetBuilder)super.addParam(key, value);
    }

    @Override
    public HttpRequestGetBuilder addParam(String key, float value) {
        return (HttpRequestGetBuilder)super.addParam(key, value);
    }

    @Override
    public HttpRequestGetBuilder addParam(String key, long value) {
        return (HttpRequestGetBuilder)super.addParam(key, value);
    }

    @Override
    public HttpRequestGetBuilder addParam(String key, double value) {
        return (HttpRequestGetBuilder)super.addParam(key, value);
    }

    @Override
    public HttpRequestGetBuilder addHeader(String key, String value) {
        return (HttpRequestGetBuilder)super.addHeader(key, value);
    }

    @Override
    public HttpRequestGetBuilder removeHeader(String key){
        return (HttpRequestGetBuilder)super.removeHeader(key);
    }

    @Override
    public HttpRequestGetBuilder removeParam(String key){
        return (HttpRequestGetBuilder)super.removeParam(key);
    }

    @Override
    public HttpRequestGetBuilder setRequestURL(String url) {
        return (HttpRequestGetBuilder)super.setRequestURL(url);
    }

    @Override
    public HttpRequestGetBuilder setHttpCallback(HttpCallback callback) {
        return (HttpRequestGetBuilder)super.setHttpCallback(callback);
    }

    @Override
    public HttpRequestGetBuilder setClientType(ClientType type) {
        return (HttpRequestGetBuilder)super.setClientType(type);
    }

    public HttpRequestGetBuilder setDownloadMode(File file){
        if(file == null)
            return this;

        if(!file.exists()){
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
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
}
