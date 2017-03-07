package ttyy.com.jinnetwork.ext_image;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;

import ttyy.com.jinnetwork.core.async.$HttpExecutorPool;
import ttyy.com.jinnetwork.core.work.HTTPRequest;
import ttyy.com.jinnetwork.core.work.HTTPResponse;
import ttyy.com.jinnetwork.core.work.inner.$HttpResponse;
import ttyy.com.jinnetwork.ext_image.cache.ImageCache;

/**
 * author: admin
 * date: 2017/03/03
 * version: 0
 * mail: secret
 * desc: HTTPImageRequest
 */

class HTTPImageRequest extends HTTPRequest {

    private HTTPRequestImageBuilder mImageBuilder;

    protected HTTPImageRequest(HTTPRequestImageBuilder builder) {
        super(builder);
        mImageBuilder = builder;
    }

    @Override
    public HTTPResponse request() {
        HTTPResponse rsp = null;
        String uri = getRequestURL();

        // 是否启用缓存
        if (mImageBuilder.isUseCache()
                && ((ViewTracker)getHttpCallback()).isViewTracked()) {
            rsp = responseFromCache();
            if(rsp != null){
                return rsp;
            }
        }

        if (uri.startsWith("file://")) {
            // 本地文件
            File mResponseFile = builder.getResponseStreamFile();

            if (mResponseFile == null) {
                uri = uri.substring(7);
                mResponseFile = new File(uri);
                builder.setResponseFile(mResponseFile);
            }

            rsp = readDataFromCustomResponse(builder.getResponseStream());
        } else {

            rsp = readDataFromNetwork(getRequestClient());
        }

        return rsp;
    }

    @Override
    public void requestAsync() {

        // 是否启用缓存
        if (mImageBuilder.isUseCache()) {
            if(responseFromCache() != null){
                return;
            }
        }

        $HttpExecutorPool.get().getImgExecutor().addRequest(this).start();
    }

    /**
     * 从缓存中获取响应
     * @return
     */
    private HTTPResponse responseFromCache(){

        String uri = getRequestURL();
        boolean isRuntimeCacheHit = ImageCache.getInstance().isRuntimeCacheHit(uri);
        Log.i("Images", "isRuntimeCacheHit "+isRuntimeCacheHit);
        if(isRuntimeCacheHit){
            getHttpCallback().onPreStart(this);

            ViewTracker tracker = (ViewTracker) getHttpCallback();

            Bitmap bm = ImageCache.getInstance().getRuntimeCache(uri);
            ImageCache.getInstance().setIntoCache(uri, bm);

            $HttpResponse rsp = new $HttpResponse(this);
            rsp.setStatusCode(200);
            rsp.setContentLength(bm.getRowBytes() * bm.getHeight());

            tracker.setImageIntoView(bm);
            tracker.onFinish(rsp);

            return rsp;
        }else {
            boolean isDiskCacheHit = ImageCache.getInstance().isDiskCacheHit(uri);
            Log.i("Images", "isDiskCacheHit "+isDiskCacheHit);
            if(isDiskCacheHit){

                File sourceFile = ImageCache.getInstance().getDiskCache(uri);
                builder.setResponseFile(sourceFile);
                $HttpResponse rsp = new $HttpResponse(this);
                rsp.setStatusCode(200);
                rsp.setContentLength(builder.getResponseStreamFile().length());

                getHttpCallback().onSuccess(rsp);
                getHttpCallback().onFinish(rsp);

                return rsp;
            }
        }

        return null;
    }
}
