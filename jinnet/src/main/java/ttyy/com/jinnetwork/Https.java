package ttyy.com.jinnetwork;

import java.lang.reflect.Proxy;

import ttyy.com.jinnetwork.core.config.HttpConfig;
import ttyy.com.jinnetwork.core.work.method_get.HttpRequestGetBuilder;
import ttyy.com.jinnetwork.core.work.method_post.HttpRequestPostBuilder;
import ttyy.com.jinnetwork.core.work.method_post.PostContentType;
import ttyy.com.jinnetwork.ext_reflect.APIRequestProxy;

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
    public static HttpRequestGetBuilder get(String url) {

        return new HttpRequestGetBuilder().setRequestURL(url);
    }

    /**
     * Post请求
     *
     * @param url
     * @param type
     * @return
     */
    public static HttpRequestPostBuilder post(String url, PostContentType type) {
        if (type == null) {
            type = HttpConfig.get().getPostContentType();
        }

        return new HttpRequestPostBuilder(type).setRequestURL(url);
    }

    /**
     * Post请求
     *
     * @param url
     * @return
     */
    public static HttpRequestPostBuilder post(String url) {

        return post(url, HttpConfig.get().getPostContentType());
    }

    /**
     * 动态代理 + 注解模式
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T createService(Class<T> clazz) {

        APIRequestProxy proxy = new APIRequestProxy();

        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, proxy);
    }

}
