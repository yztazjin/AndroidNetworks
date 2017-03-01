package ttyy.com.jinnetwork.ext_reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import ttyy.com.jinnetwork.core.work.HTTPRequest;
import ttyy.com.jinnetwork.ext_reflect.anno.$AnnoConverter;

/**
 * author: admin
 * date: 2017/03/01
 * version: 0
 * mail: secret
 * desc: APIRequestProxy
 */

public class APIRequestProxy implements InvocationHandler{

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        HTTPRequest request = $AnnoConverter.convert(method, args);
        request.requestAsync();
        return null;
    }

}
