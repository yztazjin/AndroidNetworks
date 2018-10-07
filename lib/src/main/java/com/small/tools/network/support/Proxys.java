package com.small.tools.network.support;

import com.small.tools.network.support.proxy.SmallServiceProxy;

import java.lang.reflect.Proxy;

/**
 * Author: hjq
 * Date  : 2018/10/06 23:13
 * Name  : Proxys
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class Proxys {

    private Proxys() {
    }

    public static <T> T createService(Class<T> tClass) {
        if (!tClass.isInterface()) {
            throw new UnsupportedOperationException("proxys only support interface service class");
        }

        return (T) Proxy.newProxyInstance(Proxys.class.getClassLoader(),
                new Class[]{tClass}, new SmallServiceProxy());
    }

}
