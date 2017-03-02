package ttyy.com.jinnetwork.ext_imgloader;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.LruCache;

import java.io.File;
import java.io.IOException;

import ttyy.com.jinnetwork.core.cache.HttpCache;
import ttyy.com.jinnetwork.core.work.HTTPRequest;

/**
 * Author: hjq
 * Date  : 2017/03/01 20:25
 * Name  : ImageCache
 * Intro : Edit By hjq
 * Modification  History:
 * Date          Author        	 Version          Description
 * ----------------------------------------------------------
 * 2017/03/01    hjq   1.0              1.0
 */
public class ImageCache extends HttpCache {

    LruCache<Integer, Bitmap> mRuntimeCache;

    private ImageCache(){
        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        mRuntimeCache = new LruCache<>(maxSize);
    }

    static class Holder{
        static ImageCache INSTANCE = new ImageCache();
    }

    public static ImageCache get(){
        return Holder.INSTANCE;
    }

    public void setToRuntimCache(int token, Bitmap bm){
        mRuntimeCache.put(token, bm);
    }

    public Bitmap getRuntimeCache(int token){
        return mRuntimeCache.get(token);
    }

    public boolean isRuntimeCacheHit(HTTPRequest request){
        String url = request.getRequestURL();

        if(url == null
                || TextUtils.isEmpty(url)
                || LoaderConfig.get().getCacheDir() == null){
            return false;
        }

        int token = url.hashCode();
        return mRuntimeCache.get(token) != null;
    }

    @Override
    public boolean isCacheHit(HTTPRequest request) {

        String url = request.getRequestURL();

        if(url == null
                || TextUtils.isEmpty(url)
                || LoaderConfig.get().getCacheDir() == null){
            return false;
        }

        int token = url.hashCode();

        return getCachedFile(token).exists();
    }

    private File getCachedFile(int token){
        File thumbCacheDir = new File(LoaderConfig.get().getCacheDir(), "thumb");
        File cachedFile = new File(thumbCacheDir, String.valueOf(token));
        return cachedFile;
    }

    public boolean isThumbFile(File file){
        if(LoaderConfig.get().getCacheDir() == null){
            return false;
        }

        File thumbCacheDir = new File(LoaderConfig.get().getCacheDir(), "thumb");
        return file.getParentFile().equals(thumbCacheDir);
    }
}
