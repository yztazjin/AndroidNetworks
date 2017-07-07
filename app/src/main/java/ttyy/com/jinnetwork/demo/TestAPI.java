package ttyy.com.jinnetwork.demo;

import ttyy.com.jinnetwork.core.callback.HTTPCallback;
import ttyy.com.jinnetwork.core.work.HTTPMethod;
import ttyy.com.jinnetwork.ext_reflect.anno.Callback;
import ttyy.com.jinnetwork.ext_reflect.anno.MethodType;
import ttyy.com.jinnetwork.ext_reflect.anno.Param;
import ttyy.com.jinnetwork.ext_reflect.anno.URLPath;

/**
 * Author: hjq
 * Date  : 2017/07/07 21:34
 * Name  : TestAPI
 * Intro : Edit By hjq
 * Version : 1.0
 */
public interface TestAPI {

    @URLPath("http://abc.efg.hi/test")
    @MethodType(HTTPMethod.SPECIAL)
    void test(@Param("param0") int param0,
              @Param("param1") String param1,
              @Callback HTTPCallback callback);

}
