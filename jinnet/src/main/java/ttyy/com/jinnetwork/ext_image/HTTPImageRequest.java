package ttyy.com.jinnetwork.ext_image;

import android.graphics.Bitmap;

import java.io.File;

import ttyy.com.jinnetwork.core.async.$HttpExecutorPool;
import ttyy.com.jinnetwork.core.config.__Log;
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

public class HTTPImageRequest extends HTTPRequest {

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
            switch (mImageBuilder.getImageCacheType()){
                case AllCache:
                    rsp = responseFromAllCacheTypes();
                    break;
                case RuntimeCache:
                    rsp = responseFromRuntimeCache();
                    break;
                case DiskCache:
                    rsp = responseFromDiskCache();
                    break;
            }
            if(rsp != null){
                return rsp;
            }
        }

        // 资源加载
        // 无论网络资源还是本地文件资源 若用户没有设置下载地址，设置默认的下载地址
        // 那么都会设置一个插件默认管理的下载地址
        if(getDownloadFile() == null){
            File origin_dir = new File(ImageCache.getInstance().getDiskCacheDir(), "origin");
            File file = null;
            if(mImageBuilder.getImageCacheType().useDiskCache()){
                // 使用磁盘缓存 以便下一次继续使用或者断点下载
                file = new File(origin_dir,  String.valueOf(mImageBuilder.getDecoratedRequestURL().hashCode()));
            }else {
                // 不适用磁盘缓存 随机生成一个文件用来下载 图片加载用完即删
                file = new File(origin_dir,  String.valueOf(System.currentTimeMillis()));
            }

            mImageBuilder.setDownloadMode(file);
        }

        // 加载资源
        if (uri.startsWith("file://")) {
            // 本地文件
            File mResponseFile = mImageBuilder.getResponseStreamFile();

            if (mResponseFile == null) {
                uri = uri.substring(7);
                mResponseFile = new File(uri);
                mImageBuilder.setResponseFile(mResponseFile);
            }

            rsp = readDataFromCustomResponse(mImageBuilder.getResponseStream());
        } else {

            rsp = readDataFromNetwork(getRequestClient());
            // 不启用缓存 文件下载完成后 直接删除 只针对网络请求有效
            if(!mImageBuilder.getImageCacheType().useDiskCache()){
                File mDownloadFile = mImageBuilder.getDownloadFile();
                if(mDownloadFile != null
                        && mDownloadFile.exists()){
                    mDownloadFile.getAbsoluteFile().delete();
                }
            }
        }

        return rsp;
    }

    @Override
    public void requestAsync() {

        getHttpCallback().onPreStart(this);
        // 是否启用缓存
        if (mImageBuilder.getImageCacheType().useRuntimeCache()) {
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
        __Log.i("Images", "isRuntimeCacheHit "+isRuntimeCacheHit);

        if(isRuntimeCacheHit){

            ViewTracker tracker = (ViewTracker) getHttpCallback();

            Bitmap bm = ImageCache.getInstance().getRuntimeCache(uri);
            ImageCache.getInstance().setIntoRuntimeCache(uri, bm);

            $HttpResponse rsp = new $HttpResponse(this);
            rsp.setStatusCode(101);//101 内存缓存加载状态码
            rsp.setContentLength(bm.getRowBytes() * bm.getHeight());

            getHttpCallback().onProgress(rsp, rsp.getContentLength(), rsp.getContentLength());
            getHttpCallback().onSuccess(rsp);
            // 回调设置ImageView图片资源
            if(tracker.isViewTracked()){
                tracker.onImageLoadSuccess(rsp, bm);
                tracker.onFinish(rsp);
            }

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
        __Log.i("Images", "isDiskCacheHit "+isDiskCacheHit);
        if(isDiskCacheHit){

            File sourceFile = ImageCache.getInstance().getDiskCache(uri);
            mImageBuilder.setResponseFile(sourceFile);
            mImageBuilder.setDownloadMode(sourceFile);
            $HttpResponse rsp = new $HttpResponse(this);
            rsp.setStatusCode(100);// 100磁盘thumb缓存专用状态码
            rsp.setContentLength(mImageBuilder.getResponseStreamFile().length());

            getHttpCallback().onProgress(rsp, rsp.getContentLength(), rsp.getContentLength());
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
        ViewTracker tracker = (ViewTracker) mImageBuilder.getHttpCallback();
        if(tracker != null
                && tracker.getTargetView() != null){
            // 优先唯一标示符 当前要显示的View
            return tracker.getTargetView().hashCode();
        }else {
            return new Object();
        }
    }
}
