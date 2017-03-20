package ttyy.com.jinnetwork.ext_image;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.io.File;

import ttyy.com.jinnetwork.core.callback.HTTPCallback;
import ttyy.com.jinnetwork.core.work.HTTPRequest;
import ttyy.com.jinnetwork.core.work.HTTPResponse;
import ttyy.com.jinnetwork.ext_image.cache.ImageCache;
import ttyy.com.jinnetwork.ext_image.processor.Compressor;

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

    private boolean mUseCache;

    public ViewTracker(View view) {
        this.view = view;
    }

    public void loadResouces(HTTPRequestImageBuilder builder) {
        placeHolderId = builder.getPlaceHolderResources();
        errorId = builder.getErrorResources();
        mAnimId = builder.getAnimResources();
        mTransition = builder.getTransition();
        mUseCache = builder.isUseCache();

        mSourceTokenURL = builder.getDecoratedRequestURL();
        if (!TextUtils.isEmpty(mSourceTokenURL)) {
            mSourceToken = mSourceTokenURL.hashCode();
            view.setTag(mSourceToken);
        } else {
            mSourceToken = -1;
        }
    }

    @Override
    public final void onPreStart(final HTTPRequest request) {
        Log.i("Images", "url -> " + request.getRequestURL());

        if (placeHolderId > 0) {
            if (!isViewTracked()) {
                return;
            }

            if (Looper.myLooper() == Looper.getMainLooper()) {
                setImageIntoView(placeHolderId);
            } else {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setImageIntoView(placeHolderId);
                    }
                });
            }
        }
    }

    @Override
    public void onProgress(HTTPResponse request, long cur, long total) {
    }

    @Override
    public final void onSuccess(HTTPResponse response) {
        File file = response.getHttpRequest().getDownloadFile();

        // 处理图片
        Bitmap bm = decodeFileToBitmap(file);

        // 解析失败
        if (bm == null) {
            onFailure(response);
            return;
        }

        Log.i("Images", "onSuccess " + file.getAbsolutePath());

        // 是否需要缓存
        if (mUseCache) {
            ImageCache.getInstance().setIntoCache(response.getHttpRequest().getRequestURL(), bm);
        }

        if (isViewTracked()) {
            // Btimap处理 在线程中处理Bitmap
            bm = preSetBitmapIntoView(bm);
            final Bitmap preSettedBitmap = bm;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (isViewTracked()) {
                        setImageIntoView(preSettedBitmap);
                    }
                }
            });
        }

    }

    @Override
    public final void onCancel(HTTPRequest response) {
        Log.i("Images", "onCancel " + mSourceTokenURL);
    }

    @Override
    public final void onFailure(HTTPResponse response) {
        Log.i("Images", "onFailure " + mSourceTokenURL);
        if (errorId > 0) {
            if (!isViewTracked()) {
                return;
            }

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (isViewTracked()) {
                        setImageIntoView(errorId);
                    }
                }
            });
        }
    }

    @Override
    public final void onFinish(HTTPResponse response) {

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
        return Compressor.get().compressToBitmap(file);
    }

    public void setImageIntoView(int id) {
        if (view instanceof ImageView) {
            ((ImageView) view).setImageResource(id);
        } else {
            view.setBackgroundResource(id);
        }
    }

    public void setImageIntoView(final Bitmap bm) {
        if (!isViewTracked()) {
            Log.w("Images", "View Not Tracked Ignored It");
            return;
        }

        if (Looper.myLooper() != Looper.getMainLooper()) {
            Log.w("Images", "Not In UILooper Post To UILooper");
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
        Animation anim = AnimationUtils.loadAnimation(view.getContext(), id);
        view.startAnimation(anim);
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

}
