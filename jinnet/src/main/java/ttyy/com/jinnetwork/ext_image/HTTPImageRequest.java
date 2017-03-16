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
            rsp = responseFromAllCacheTypes();
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

        getHttpCallback().onPreStart(this);
        // 是否启用缓存
        if (mImageBuilder.isUseCache()) {
            // 主线程中 从内存读取提高效率
            if(responseFromRuntimeCache() != null){
                return;
            }
        }

        $HttpExecutorPool.get().getImgExecutor().addRequest(this).start();
    }

    /**
     * 从内存中读取缓存
     * @return
     */
    private HTTPResponse responseFromRuntimeCache(){
        String uri = getRequestURL();
        boolean isRuntimeCacheHit = ImageCache.getInstance().isRuntimeCacheHit(uri);
        Log.i("Images", "isRuntimeCacheHit "+isRuntimeCacheHit);

        if(isRuntimeCacheHit){

            ViewTracker tracker = (ViewTracker) getHttpCallback();

            Bitmap bm = ImageCache.getInstance().getRuntimeCache(uri);
            ImageCache.getInstance().setIntoCache(uri, bm);

            $HttpResponse rsp = new $HttpResponse(this);
            rsp.setStatusCode(200);
            rsp.setContentLength(bm.getRowBytes() * bm.getHeight());

            tracker.setImageIntoView(bm);
            tracker.onFinish(rsp);

            return rsp;
        }
        return null;
    }

    /**
     * 从磁盘中读取缓存
     * @return
     */
    private HTTPResponse responseFromDiskCache(){
        String uri = getRequestURL();
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

        return null;
    }

    /**
     * 从所有缓存中获取响应
     * @return
     */
    private HTTPResponse responseFromAllCacheTypes(){

        HTTPResponse response = responseFromRuntimeCache();
        if(response == null){
            response = responseFromDiskCache();
        }

        return response;
    }

    @Override
    public Object getUniqueToken() {
        ViewTracker tracker = (ViewTracker) builder.getHttpCallback();
        if(tracker != null
                && tracker.getTargetView() != null){
            // 优先唯一标示符 当前要显示的View
            return tracker.getTargetView().hashCode();
        }else {
            return new Object();
        }
    }
}
