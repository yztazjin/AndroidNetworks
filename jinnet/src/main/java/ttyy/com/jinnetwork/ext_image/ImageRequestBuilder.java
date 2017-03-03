package ttyy.com.jinnetwork.ext_image;

import android.view.View;

import java.io.File;
import java.io.InputStream;

/**
 * author: admin
 * date: 2017/03/03
 * version: 0
 * mail: secret
 * desc: ImageRequestBuilder
 */

public interface ImageRequestBuilder {

    /**
     * 预置ID
     * @param id
     * @return
     */
    ImageRequestBuilder placeholder(int id);

    int getPlaceHolderResources();

    /**
     * 加载错误的id
     * @param id
     * @return
     */
    ImageRequestBuilder error(int id);

    int getErrorResources();

    /**
     * 启用缓存
     * @param cache
     * @return
     */
    ImageRequestBuilder useCache(boolean cache);

    boolean isUseCache();

    /**
     * Bitmap 转换
     * @param transition
     * @return
     */
    ImageRequestBuilder transition(ImageTransition transition);

    ImageTransition getTransition();

    /**
     * 图片加载完成后 显示的动画
     * @param id
     * @return
     */
    ImageRequestBuilder anim(int id);

    int getAnimResources();

    /**
     * 图片源 资源地址
     * @param uri
     * @return
     */
    ImageRequestBuilder source(String uri);

    /**
     * 图片源 文件
     * @param file
     * @return
     */
    ImageRequestBuilder source(File file);

    /**
     * 开始加载
     * @param view
     */
    void into(View view);

    /**
     * 加载
     * @param tracker
     */
    void into(ViewTracker tracker);

}
