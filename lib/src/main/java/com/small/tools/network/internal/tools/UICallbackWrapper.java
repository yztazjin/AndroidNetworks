package com.small.tools.network.internal.tools;

import android.os.Handler;
import android.os.Looper;

import com.small.tools.network.internal.interfaces.HTTPCallback;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Author: hjq
 * Date  : 2018/10/06 12:18
 * Name  : UICallbackWrapper
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class UICallbackWrapper {

    private UICallbackWrapper() {

    }

    public static HTTPCallback wrap(HTTPCallback callback) {
        HTTPCallback wrapped = (HTTPCallback) Proxy.newProxyInstance(
                UICallbackWrapper.class.getClassLoader(),
                new Class[]{HTTPCallback.class}, new UIProxy(callback));

        return wrapped;
    }

    private static class UIProxy implements InvocationHandler {

        Handler mUIHandler = new Handler(Looper.getMainLooper());
        HTTPCallback mRealCallback;

        private UIProxy(HTTPCallback callback) {
            mRealCallback = callback;
        }

        @Override
        public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {

            mUIHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        method.invoke(mRealCallback, args);
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

}
