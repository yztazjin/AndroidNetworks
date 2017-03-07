package ttyy.com.jinnetwork.core.cache;

import ttyy.com.jinnetwork.core.work.HTTPRequest;

/**
 * author: admin
 * date: 2017/02/27
 * version: 0
 * mail: secret
 * desc: HTTPCache 暂不支持
 */

public class HTTPCache {

    protected HTTPCache(){

    }

    public boolean isCacheHit(HTTPRequest request){

        return false;
    }

}
