package ttyy.com.jinnetwork.ext_imgloader;

import java.io.File;

import ttyy.com.jinnetwork.core.config.HttpConfig;
import ttyy.com.jinnetwork.core.http.OKHttpClientImpl;
import ttyy.com.jinnetwork.core.http.base.Client;

/**
 * Author: hjq
 * Date  : 2017/03/01 19:51
 * Name  : LoaderConfig
 * Intro : Edit By hjq
 * Modification  History:
 * Date          Author        	 Version          Description
 * ----------------------------------------------------------
 * 2017/03/01    hjq   1.0              1.0
 */
public class LoaderConfig extends HttpConfig {

    private int mMaxImageWidth;
    private int mMaxImageHeight;

    private Client mRequestClient;

    protected LoaderConfig(){
        super();
    }

    static class Holder{
        static LoaderConfig INSTANCE = new LoaderConfig();
    }

    public static LoaderConfig get(){
        return Holder.INSTANCE;
    }

    public static LoaderConfig create(){
        return new LoaderConfig();
    }

    @Override
    public File getCacheDir() {
        return super.getCacheDir();
    }

    public Client getRequestClient(){
        if(mRequestClient == null)
            mRequestClient = OKHttpClientImpl.create(this);
        return mRequestClient;
    }

    public LoaderConfig setRequestClient(Client client){
        mRequestClient = client;
        return this;
    }

}
