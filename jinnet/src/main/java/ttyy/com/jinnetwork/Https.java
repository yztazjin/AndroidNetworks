package ttyy.com.jinnetwork;

import java.lang.reflect.Proxy;

import ttyy.com.jinnetwork.core.config.HTTPConfig;
import ttyy.com.jinnetwork.core.work.method_get.HTTPRequestGetBuilder;
import ttyy.com.jinnetwork.core.work.method_post.HTTPRequestPostBuilder;
import ttyy.com.jinnetwork.core.work.method_post.PostContentType;
import ttyy.com.jinnetwork.ext_reflect.APIRequestProxyInner;
import ttyy.com.jinnetwork.ext_reflect.BaseAPIRequestProxy;

/**
 * author: admin
 * date: 2017/02/27
 * version: 0
 * mail: secret
 * desc: Https
 */

public class Https {

    private Https() {

    }

    /**
     * Get请求
     *
     * @param url
     * @return
     */
    public static HTTPRequestGetBuilder get(String url) {

        return new HTTPRequestGetBuilder().setRequestURL(url);
    }

    /**
     * Post请求
     *
     * @param url
     * @param type
     * @return
     */
    public static HTTPRequestPostBuilder post(String url, PostContentType type) {
        if (type == null) {
            type = HTTPConfig.get().getPostContentType();
        }

        return new HTTPRequestPostBuilder(type).setRequestURL(url);
    }

    /**
     * Post请求
     *
     * @param url
     * @return
     */
    public static HTTPRequestPostBuilder post(String url) {

        return post(url, HTTPConfig.get().getPostContentType());
    }

    /**
     * 动态代理 + 注解模式
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T createService(Class<T> clazz) {

        APIRequestProxyInner proxy = new APIRequestProxyInner();

        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, proxy);
    }

    /**
     * 动态代理 + 注解模式 + 用户自定义
     *
     * @param proxyClass
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T createService(Class<? extends BaseAPIRequestProxy> proxyClass, Class<T> clazz) {
        BaseAPIRequestProxy proxy = null;
        try {
            proxy = proxyClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, proxy);
    }

}
