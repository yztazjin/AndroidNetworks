package ttyy.com.jinnetwork.ext_reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import ttyy.com.jinnetwork.core.work.HTTPRequestBuilder;
import ttyy.com.jinnetwork.ext_reflect.anno.$AnnoConverter;

/**
 * author: admin
 * date: 2017/07/07
 * version: 0
 * mail: secret
 * desc: BaseAPIRequestProxy
 */

public abstract class BaseAPIRequestProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        HTTPRequestBuilder requestBuilder = $AnnoConverter.convert(method, args);
        richBuilder(requestBuilder, method, args);
        requestBuilder.build().requestAsync();
        return null;
    }

    /**
     * 用户自定义
     * @param builder
     * @param method
     * @param args
     * @return
     */
    public abstract HTTPRequestBuilder richBuilder(HTTPRequestBuilder builder, Method method, Object[] args);
}
