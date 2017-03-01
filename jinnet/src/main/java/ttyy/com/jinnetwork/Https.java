package ttyy.com.jinnetwork;

import android.util.Log;

import java.io.File;
import java.lang.reflect.Proxy;

import ttyy.com.jinnetwork.core.async.$HttpExecutorPool;
import ttyy.com.jinnetwork.core.config.HttpConfig;
import ttyy.com.jinnetwork.core.work.method_get.HttpRequestGetBuilder;
import ttyy.com.jinnetwork.core.work.method_post.HttpRequestPostBuilder;
import ttyy.com.jinnetwork.core.work.method_post.PostContentType;
import ttyy.com.jinnetwork.ext_imgloader.TargetWrapper;
import ttyy.com.jinnetwork.ext_imgloader.LoaderConfig;
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

    /**
     * 加载图片
     * @param url
     * @param target
     */
    public static void loadImg(String url, TargetWrapper target) {

        if (url == null) {
            Log.d("Https", "url is empty");
            return;
        }

        if (target != null) {
            target.getTargetView().setTag(url.hashCode());
        }

        HttpRequestGetBuilder builder = get(url)
                .setRequestClient(LoaderConfig.get().getRequestClient())
                .setHttpCallback(target);

        if (url.startsWith("file://")) {
            url = url.substring(7);
            File mResponseFile = new File(url);
            builder.setResponseFile(mResponseFile);
        }

        File cacheDir = LoaderConfig.get().getCacheDir();
        if (cacheDir != null) {
            builder.setDownloadMode(new File(cacheDir, String.valueOf(url.hashCode())));
        }

        builder.build().$async($HttpExecutorPool.get().getImgExecutor());

    }

}
