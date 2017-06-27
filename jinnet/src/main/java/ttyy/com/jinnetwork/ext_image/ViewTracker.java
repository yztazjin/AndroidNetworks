package ttyy.com.jinnetwork.ext_image;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.io.File;

import ttyy.com.jinnetwork.core.callback.HTTPCallback;
import ttyy.com.jinnetwork.core.config.__Log;
import ttyy.com.jinnetwork.core.work.HTTPRequest;
import ttyy.com.jinnetwork.core.work.HTTPResponse;
import ttyy.com.jinnetwork.ext_image.cache.ImageCache;
import ttyy.com.jinnetwork.ext_image.cache.ImageCacheType;
import ttyy.com.jinnetwork.ext_image.compressor.JinCompressor;

/**
 * author: admin
 * date: 2017/06/27
 * version: 0
 * mail: secret
 * desc: ViewTracker
 */

public abstract class ViewTracker implements HTTPCallback {

    protected Handler mHandler = new Handler(Looper.getMainLooper());

    protected int placeHolderId;

    protected int errorId;

    protected ImageTransition mTransition;

    protected int mAnimId;

    protected View view;

    protected String mSourceTokenURL;
    protected int mSourceToken;

    protected ImageCacheType mImageCacheType;

    public ViewTracker(View view) {
        this.view = view;
    }

    public final void loadResouces(HTTPRequestImageBuilder builder) {
        placeHolderId = builder.getPlaceHolderResources();
        errorId = builder.getErrorResources();
        mAnimId = builder.getAnimResources();
        mTransition = builder.getTransition();
        mImageCacheType = builder.getImageCacheType();

        mSourceTokenURL = builder.getDecoratedRequestURL();
        if (!TextUtils.isEmpty(mSourceTokenURL)) {
            mSourceToken = mSourceTokenURL.hashCode();
            view.setTag(mSourceToken);
        } else {
            mSourceToken = -1;
        }
    }

    @Override
    public final void onPreStart(HTTPRequest request) {
        __Log.i("Images", "onLoadPreStart " + mSourceTokenURL);
        __ImageLoadPreProxy(request);
    }

    @Override
    public final void onProgress(HTTPResponse response, long cur, long total) {

    }

    @Override
    public final void onSuccess(HTTPResponse response) {
        if (isRespFromRuntimCache(response)) {
            // 从磁盘缓存成功 不需要设置解析文件 外层直接通过setBitmap
            return;
        }

        File file = response.getHttpRequest().getDownloadFile();

        // 处理图片
        Bitmap bm = decodeFileToBitmap(file);

        // 解析失败
        if (bm == null) {
            onFailure(response);
            return;
        }

        __Log.i("Images", "onLoadSuccess " + mSourceTokenURL);

        // 是否需要磁盘缓存
        if (mImageCacheType.useDiskCache()
                && !isRespFromDiskCache(response)) {
            // 不是从磁盘thumb缓存而来 就存进thumb缓存
            ImageCache.getInstance().setIntoDiskCache(mSourceTokenURL, bm);
        }
        // 是否需要内存缓存
        if (mImageCacheType.useRuntimeCache()) {
            ImageCache.getInstance().setIntoRuntimeCache(mSourceTokenURL, bm);
        }

        __ImageLoadSuccessProxy(response, bm);
    }

    @Override
    public final void onCancel(HTTPRequest response) {
        __Log.i("Images", "Sys CancelLoad " + mSourceTokenURL);
    }

    @Override
    public final void onFailure(final HTTPResponse response) {
        __Log.i("Images", "onLoadFailure " + mSourceTokenURL);
        if(isViewTracked()){
            if(Looper.myLooper() == Looper.getMainLooper()){
                onImageLoadFailure(response);
            }else {
                __Log.w("Images", "Not In UILooper Post To UILooper");
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onFailure(response);
                    }
                });
            }
        }
    }

    @Override
    public final void onFinish(final HTTPResponse response) {
        __Log.i("Images", "onLoadFinish " + mSourceTokenURL);
        if(isViewTracked()){
            if(Looper.myLooper() == Looper.getMainLooper()){
                onImageLoadFinish(response);
            }else {
                __Log.w("Images", "Not In UILooper Post To UILooper");
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onFinish(response);
                    }
                });
            }
        }
    }

    /**
     * 解析File
     * @param file
     * @return
     */
    public Bitmap decodeFileToBitmap(File file) {
        return JinCompressor.get().compress(file);
    }

    /**
     * 该次回调需要设置图片的View
     * @return
     */
    public final View getTargetView() {
        return view;
    }

    /**
     * View加载的图url与View相适配
     * 避免ListView View乱加载图片
     * @return
     */
    public boolean isViewTracked() {
        if (view.getTag() != null
                && view.getTag().equals(mSourceToken)) {
            return true;
        }

        return false;
    }

    /**
     * response状态码判断
     * -1 出现了异常
     * 100 图片加载 加载磁盘thumb缓存专用状态码
     * 101 图片加载 加载内存缓存专用状态码
     * 102 从磁盘文件中获取数据状态码
     *
     * @param response
     * @return
     */
    public final boolean isRespFromRuntimCache(HTTPResponse response) {
        return response.getStatusCode() == 101;
    }

    public final boolean isRespFromDiskCache(HTTPResponse response) {
        return response.getStatusCode() == 100;
    }

    public final boolean isRespFromDiskSource(HTTPResponse response) {
        return response.getStatusCode() == 102;
    }

    /**
     * UI操作应该在主线程
     * @param request
     */
    public final void __ImageLoadPreProxy(final HTTPRequest request){
        if(isViewTracked()){
            if(Looper.myLooper() == Looper.getMainLooper()){
                onImageLoadPre(request);
            }else {
                __Log.w("Images", "Not In UILooper Post To UILooper");
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        __ImageLoadPreProxy(request);
                    }
                });
            }
        }else {
            __Log.w("Images", "View Not Tracked Ignored It");
        }
    }

    /**
     * UI操作应该在主线程
     * @param response
     * @param bm
     */
    public final void __ImageLoadSuccessProxy(final HTTPResponse response, final Bitmap bm){
        if (isViewTracked()) {

            if(Looper.myLooper() == Looper.getMainLooper()){
                // Btimap处理 在线程中处理Bitmap
                onImageLoadSuccess(response, bm);

                // 加载成功动画
                if (mAnimId > 0) {
                    Animation anim = AnimationUtils.loadAnimation(view.getContext(), mAnimId);
                    view.startAnimation(anim);
                }
            }else {
                __Log.w("Images", "Not In UILooper Post To UILooper");
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // 正在排队的任务忽略掉
//                        mHandler.removeCallbacksAndMessages(null);
                        __ImageLoadSuccessProxy(response, bm);
                    }
                });
            }

        } else {
            __Log.w("Images", "View Not Tracked Ignored It");
        }
    }

    /**
     * 图片加载之前
     * @param request
     */
    public abstract void onImageLoadPre(HTTPRequest request);

    /**
     * 图片加载成功
     * @param response
     * @param bm
     */
    public abstract void onImageLoadSuccess(HTTPResponse response, Bitmap bm);

    /**
     * 图片加载失败
     * @param response
     */
    public void onImageLoadFailure(HTTPResponse response){

    }

    /**
     * 图片加载结束
     * @param response
     */
    public void onImageLoadFinish(HTTPResponse response){

    }

}
