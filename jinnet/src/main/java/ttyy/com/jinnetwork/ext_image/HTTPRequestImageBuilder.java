package ttyy.com.jinnetwork.ext_image;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import java.io.File;

import ttyy.com.jinnetwork.core.work.HTTPRequest;
import ttyy.com.jinnetwork.core.work.method_get.HTTPRequestGetBuilder;
import ttyy.com.jinnetwork.ext_image.cache.ImageCache;

/**
 * author: admin
 * date: 2017/03/03
 * version: 0
 * mail: secret
 * desc: HTTPRequestImageBuilder
 */

public class HTTPRequestImageBuilder extends HTTPRequestGetBuilder implements ImageRequestBuilder {

    protected boolean mUseCache;

    protected int mPlaceHolderId;

    protected int mErrorId;

    protected ImageTransition mBitmapTransition;

    protected int mAnimId;

    @Override
    public ImageRequestBuilder placeholder(int id) {
        mPlaceHolderId = id;
        return this;
    }

    @Override
    public int getPlaceHolderResources() {
        return mPlaceHolderId;
    }

    @Override
    public ImageRequestBuilder error(int id) {
        mErrorId = id;
        return this;
    }

    @Override
    public int getErrorResources() {
        return mErrorId;
    }

    @Override
    public ImageRequestBuilder useCache(boolean cache) {
        mUseCache = cache;
        return this;
    }

    @Override
    public boolean isUseCache() {
        return mUseCache;
    }

    @Override
    public ImageRequestBuilder transition(ImageTransition transition) {
        mBitmapTransition = transition;
        return this;
    }

    @Override
    public ImageTransition getTransition() {
        return mBitmapTransition;
    }

    @Override
    public ImageRequestBuilder anim(int id) {
        mAnimId = id;
        return this;
    }

    @Override
    public int getAnimResources() {
        return mAnimId;
    }

    @Override
    public ImageRequestBuilder source(String uri) {
        setRequestURL(uri);
        return this;
    }

    @Override
    public ImageRequestBuilder source(File file) {
        String uri = "file://"+file.getAbsolutePath();
        setResponseFile(file);
        return source(uri);
    }

    @Override
    public void into(View view) {
        if(view == null
                || TextUtils.isEmpty(getDecoratedRequestURL())){
            return;
        }

        // 缓存未初始化,初始化
        if(ImageCache.getInstance().getDiskCacheDir() == null){
            Context context = view.getContext().getApplicationContext();
            ImageCache.getInstance().setDiskCacheDir(context);
        }

        // 没有设置下载地址，设置默认的下载地址
        if(getDownloadFile() == null){
            File origin_dir = new File(ImageCache.getInstance().getDiskCacheDir(), "origin");
            File file = new File(origin_dir,  String.valueOf(getDecoratedRequestURL().hashCode()));
            setDownloadMode(file);
        }

        into(new ViewTracker(view));
    }

    @Override
    public void into(ViewTracker tracker) {
        tracker.loadResouces(this);
        setHttpCallback(tracker);

        build().requestAsync();
    }

    @Override
    public HTTPRequest build() {
        return new HTTPImageRequest(this);
    }
}
