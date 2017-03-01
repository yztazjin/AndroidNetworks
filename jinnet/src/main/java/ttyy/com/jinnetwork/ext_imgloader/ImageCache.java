package ttyy.com.jinnetwork.ext_imgloader;

import android.text.TextUtils;

import java.io.File;

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
        File file = new File(LoaderConfig.get().getCacheDir(), String.valueOf(token));
        return file;
    }
}
