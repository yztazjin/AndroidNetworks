package ttyy.com.jinnetwork.core.config;

import java.io.File;

import ttyy.com.jinnetwork.core.http.base.ClientType;
import ttyy.com.jinnetwork.core.work.method_post.PostContentType;

/**
 * author: admin
 * date: 2017/02/27
 * version: 0
 * mail: secret
 * desc: HTTPConfig
 */

public class HTTPConfig {

    /**
     * 连接超时时间
     */
    private long mConnectTimeOut;
    /**
     * 读取超时时间
     */
    private long mReadTimeOut;
    /**
     * 是否忽略证书
     */
    private boolean mIgnoreCertificate;
    /**
     * 缓存文件目录
     */
    private File mCacheDirFile;
    /**
     * 是否是Debug模式
     */
    private boolean isDebugMode;
    /**
     * post 参数模式
     */
    private PostContentType mPostContentType;
    /**
     * 客户端类型
     */
    private ClientType mClientType;

    static class Holder{
        static HTTPConfig INSTANCE = new HTTPConfig();
    }

    protected HTTPConfig(){
        // 连接超时15s
        mConnectTimeOut = 15 * 1000;
        // 读取超时60s
        mReadTimeOut = 60 * 1000;
        // 忽略证书
        mIgnoreCertificate = true;
        // debug模式
        isDebugMode = true;

        // post content-type
        mPostContentType = PostContentType.ApplicationJson;
        // 网络客户端
        mClientType = ClientType.OKHTTP_CLIENT;
    }

    public static HTTPConfig get(){
        return Holder.INSTANCE;
    }

    public static HTTPConfig create(){
        return new HTTPConfig();
    }

    public HTTPConfig setConnectTimeOut(long millions){
        mConnectTimeOut = millions;
        return this;
    }

    public long getConnectTimeOut(){
        return mConnectTimeOut;
    }

    public HTTPConfig setReadTimeOut(long millions){
        mReadTimeOut = millions;
        return this;
    }

    public long getReadTimeOut(){
        return mReadTimeOut;
    }

    public HTTPConfig setIgnoreCertificate(boolean isIgnore){
        mIgnoreCertificate = isIgnore;
        return this;
    }

    public boolean isIgnoreCertificate(){
        return mIgnoreCertificate;
    }

    public HTTPConfig setCacheDir(File file){
        mCacheDirFile = file;
        return this;
    }

    public File getCacheDir(){
        return mCacheDirFile;
    }

    public HTTPConfig setDebugMode(boolean value){
        this.isDebugMode = value;
        return this;
    }

    public boolean isDebugMode(){
        return this.isDebugMode;
    }

    public PostContentType getPostContentType(){
        return mPostContentType;
    }

    public HTTPConfig setPostConentType(PostContentType type){
        mPostContentType = type;
        return this;
    }

    public HTTPConfig setClientType(ClientType type){
        mClientType = type;
        return this;
    }

    public ClientType getClientType(){
        return mClientType;
    }

}
