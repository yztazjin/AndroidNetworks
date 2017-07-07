package ttyy.com.jinnetwork.demo;

import android.util.Log;

import java.lang.reflect.Method;

import ttyy.com.jinnetwork.core.work.HTTPRequestBuilder;
import ttyy.com.jinnetwork.ext_reflect.BaseAPIRequestProxy;

/**
 * Author: hjq
 * Date  : 2017/07/07 22:07
 * Name  : TestAPIProxy
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class TestAPIProxy extends BaseAPIRequestProxy {

    @Override
    public HTTPRequestBuilder richBuilder(HTTPRequestBuilder builder, Method method, Object[] args) {

        Log.e("Test", "TestAPIProxy rich builder");
        builder.setRequestClient(TestHttpClient.INSTANCE);

        return builder;
    }
}
