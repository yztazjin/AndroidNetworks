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
 * desc: $HTTPUIThreadCallbackAdapterProxy
 */

public class $HTTPUIThreadCallbackAdapterProxy implements InvocationHandler {

    HTTPCallback real;
    Handler mHandler;

    private $HTTPUIThreadCallbackAdapterProxy(HTTPCallback real){
        this.real = real;
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    public static HTTPCallback get(HTTPCallback adapter){
        $HTTPUIThreadCallbackAdapterProxy proxy = new $HTTPUIThreadCallbackAdapterProxy(adapter);
        return (HTTPCallback)Proxy.newProxyInstance(HTTPUIThreadCallbackAdapter.class.getClassLoader(),new Class[]{HTTPCallback.class}, proxy);
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

        // 确保在UIThread中执行
        if(Looper.myLooper() != Looper.getMainLooper()){
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
        }else {
            method.invoke(real, args);
        }

        return null;
    }
}
