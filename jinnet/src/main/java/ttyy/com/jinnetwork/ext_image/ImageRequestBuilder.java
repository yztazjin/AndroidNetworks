package ttyy.com.jinnetwork.ext_image;

import android.view.View;

import java.io.File;

import ttyy.com.jinnetwork.ext_image.cache.ImageCacheType;
import ttyy.com.jinnetwork.ext_image.compressor.JinCompressor;

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
     * 设置缓存类型
     * @param type
     * @return
     */
    ImageRequestBuilder useCache(ImageCacheType type);

    /**
     * 是否启用缓存
     * @return
     */
    boolean isUseCache();

    /**
     * 获取图片缓存类型
     * @return
     */
    ImageCacheType getImageCacheType();

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
     * 图片压缩参数配置
     * @param config
     * @return
     */
    ImageRequestBuilder compressConfig(JinCompressor.Config config);

    JinCompressor.Config getCompressConfig();

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
