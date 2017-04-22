package ttyy.com.jinnetwork.ext_image;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.io.File;

import ttyy.com.jinnetwork.core.callback.HTTPCallback;
import ttyy.com.jinnetwork.core.config.__Log;
import ttyy.com.jinnetwork.core.work.HTTPRequest;
import ttyy.com.jinnetwork.core.work.HTTPResponse;
import ttyy.com.jinnetwork.ext_image.cache.ImageCache;
import ttyy.com.jinnetwork.ext_image.cache.ImageCacheType;
import ttyy.com.jinnetwork.ext_image.compressor.Compressor;
import ttyy.com.jinnetwork.ext_image.compressor.JinCompressor;

/**
 * author: admin
 * date: 2017/03/03
 * version: 0
 * mail: secret
 * desc: ViewTracker
 */

public class ViewTracker implements HTTPCallback {

    Handler mHandler = new Handler(Looper.getMainLooper());

    private int placeHolderId;

    private int errorId;

    private ImageTransition mTransition;

    private int mAnimId;

    private View view;

    private String mSourceTokenURL;
    private int mSourceToken;

    private ImageCacheType mImageCacheType;

    public ViewTracker(View view) {
        this.view = view;
    }

    public void loadResouces(HTTPRequestImageBuilder builder) {
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
    public void onPreStart(HTTPRequest request) {
        __Log.i("Images", "onPreStart " + mSourceTokenURL);

        if (placeHolderId > 0) {
            setImageIntoView(placeHolderId);
        } else {
            __Log.w("Images", "Hasn't Set The PreStart ResourceId");
        }
    }

    @Override
    public void onProgress(HTTPResponse request, long cur, long total) {
    }

    @Override
    public void onSuccess(HTTPResponse response) {
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

        __Log.i("Images", "onSuccess " + mSourceTokenURL);

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

        if (isViewTracked()) {
            // Btimap处理 在线程中处理Bitmap
            bm = preSetBitmapIntoView(bm);
            setImageIntoView(bm);
        } else {
            __Log.w("Images", "View Not Tracked Ignored It");
        }

    }

    @Override
    public void onCancel(HTTPRequest response) {
        __Log.i("Images", "onCancel " + mSourceTokenURL);
    }

    @Override
    public void onFailure(HTTPResponse response) {
        __Log.i("Images", "onFailure " + mSourceTokenURL);
        if (errorId > 0) {
            setImageIntoView(errorId);
        } else {
            __Log.w("Images", "Hasn't Set The Failure ResourceId");
        }
    }

    @Override
    public void onFinish(HTTPResponse response) {
    }

    public final View getTargetView() {
        return view;
    }

    /**
     * 解析File
     *
     * @param file
     * @return
     */
    public Bitmap decodeFileToBitmap(File file) {
        return JinCompressor.get().compress(file);
    }

    public void setImageIntoView(final int id) {
        if (!isViewTracked()) {
            __Log.w("Images", "View Not Tracked Ignored It");
            return;
        }

        if (Looper.myLooper() != Looper.getMainLooper()) {
            __Log.w("Images", "Not In UILooper Post To UILooper");
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    setImageIntoView(id);
                }
            });
        } else {
            if (view instanceof ImageView) {
                ((ImageView) view).setImageResource(id);
            } else {
                view.setBackgroundResource(id);
            }
        }
    }

    public void setImageIntoView(final Bitmap bm) {
        if (!isViewTracked()) {
            __Log.w("Images", "View Not Tracked Ignored It");
            return;
        }

        if (Looper.myLooper() != Looper.getMainLooper()) {
            __Log.w("Images", "Not In UILooper Post To UILooper");
            // 不在UI主线程
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    setImageIntoView(bm);
                }
            });
        } else {
            // 正在排队的任务忽略掉
            mHandler.removeCallbacksAndMessages(null);
            // 在UI主线程
            Bitmap mSuccessBitmap = bm;
            if (mTransition != null) {
                mSuccessBitmap = mTransition.translate(bm);
            }

            if (view instanceof ImageView) {
                ((ImageView) view).setImageBitmap(mSuccessBitmap);
            } else {
                view.setBackgroundDrawable(new BitmapDrawable(mSuccessBitmap));
            }

            // 加载动画
            onImageLoadSuccessAnimation(mAnimId);
            // 加载完成后
            afterSetBitmapIntoView(mSuccessBitmap);
        }

    }

    /**
     * 图片加载成功后的动画
     *
     * @param id
     */
    public void onImageLoadSuccessAnimation(int id) {
        if (id > 0) {
            Animation anim = AnimationUtils.loadAnimation(view.getContext(), id);
            view.startAnimation(anim);
        }
    }

    /**
     * 加载成功设置图片之前
     *
     * @param bm
     */
    public Bitmap preSetBitmapIntoView(Bitmap bm) {

        return bm;
    }

    /**
     * 加载成功设置图片之后
     *
     * @param bm
     */
    public void afterSetBitmapIntoView(Bitmap bm) {

    }

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

}
