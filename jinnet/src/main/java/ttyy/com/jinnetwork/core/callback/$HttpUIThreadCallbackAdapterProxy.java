package ttyy.com.jinnetwork.core.callback;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * author: admin
 * date: 2017/02/28
 * version: 0
 * mail: secret
 * desc: $HttpUIThreadCallbackAdapterProxy
 */

public class $HttpUIThreadCallbackAdapterProxy implements InvocationHandler {

    HttpCallback real;
    Handler mHandler;

    private $HttpUIThreadCallbackAdapterProxy(HttpCallback real){
        this.real = real;
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    public static HttpCallback get(HttpCallback adapter){
        $HttpUIThreadCallbackAdapterProxy proxy = new $HttpUIThreadCallbackAdapterProxy(adapter);
        return (HttpCallback)Proxy.newProxyInstance(HttpUIThreadCallbackAdapter.class.getClassLoader(),new Class[]{HttpCallback.class}, proxy);
    }

    /**
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {

        // UIThread中执行
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    method.invoke(real, args);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });

        return null;
    }
}
