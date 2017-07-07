package ttyy.com.jinnetwork.ext_reflect;

import java.lang.reflect.Method;

import ttyy.com.jinnetwork.core.work.HTTPRequestBuilder;

/**
 * author: admin
 * date: 2017/03/01
 * version: 0
 * mail: secret
 * desc: APIRequestProxyInner
 */

public class APIRequestProxyInner extends BaseAPIRequestProxy{

    @Override
    public HTTPRequestBuilder richBuilder(HTTPRequestBuilder builder, Method method, Object[] args) {
        // Inner API Request Builder No Other Params To Rich Builder
        return builder;
    }
}
