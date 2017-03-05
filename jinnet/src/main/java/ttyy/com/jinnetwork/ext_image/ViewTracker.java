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

import ttyy.com.jinnetwork.core.callback.HttpCallback;
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

public class ViewTracker implements HttpCallback {

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

    public void loadResouces(HttpRequestImageBuilder builder) {
        placeHolderId = builder.getPlaceHolderResources();
        errorId = builder.getErrorResources();
        mAnimId = builder.getAnimResources();
        mTransition = builder.getTransition();
        mUseCache = builder.isUseCache();

        mSourceTokenURL = builder.getRequestURL();
        if (!TextUtils.isEmpty(mSourceTokenURL)) {
            mSourceToken = mSourceTokenURL.hashCode();
            view.setTag(mSourceToken);
        } else {
            mSourceToken = -1;
        }
    }

    @Override
    public final void onPreStart(final HTTPRequest request) {
        Log.i("Images", "url -> "+request.getRequestURL());
        if (placeHolderId > 0) {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (isViewTracked()) {
                        setImageIntoView(placeHolderId);
                    }
                }
            });
        }
    }

    @Override
    public void onProgress(HTTPResponse request, long cur, long total) {
    }

    @Override
    public final void onSuccess(HTTPResponse response) {
        File file = response.getHttpRequest().getDownloadFile();
        Log.i("Images", "onSuccess "+file.getAbsolutePath());

        // 处理图片
        final Bitmap bm = decodeFileToBitmap(file);

        // 是否需要缓存
        if(mUseCache){
            ImageCache.getInstance().setIntoCache(response.getHttpRequest().getRequestURL(), bm);
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (isViewTracked()) {
                    // Btimap处理
                    Bitmap future_bm = bm;
                    preSetBitmapIntoView(future_bm);
                    setImageIntoView(future_bm);
                    onImageLoadSuccessAnimation(mAnimId);
                    afterSetBitmapIntoView(future_bm);
                }
            }
        });

    }

    @Override
    public final void onCancel(HTTPRequest response) {

    }

    @Override
    public final void onFailure(final HTTPResponse response) {
        Log.i("Images", "onFailure "+response.getErrorMessage());
        if (errorId > 0) {
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

    public final View getTargetView(){
        return view;
    }

    /**
     * 解析File
     * @param file
     * @return
     */
    public Bitmap decodeFileToBitmap(File file){
        return Compressor.get().compressToBitmap(file);
    }

    public void setImageIntoView(int id) {
        if (view instanceof ImageView) {
            ((ImageView) view).setImageResource(id);
        } else {
            view.setBackgroundResource(id);
        }
    }

    public void setImageIntoView(Bitmap bm) {
        if(mTransition != null){
            bm = mTransition.translate(bm);
        }
        if (view instanceof ImageView) {
            ((ImageView) view).setImageBitmap(bm);
        } else {
            view.setBackgroundDrawable(new BitmapDrawable(bm));
        }
    }

    /**
     * 图片加载成功后的动画
     * @param id
     */
    public void onImageLoadSuccessAnimation(int id){
        Animation anim = AnimationUtils.loadAnimation(view.getContext(), id);
        view.startAnimation(anim);
    }

    /**
     * 加载成功设置图片之前
     * @param bm
     */
    public void preSetBitmapIntoView(Bitmap bm){

    }

    /**
     * 加载成功设置图片之后
     * @param bm
     */
    public void afterSetBitmapIntoView(Bitmap bm){

    }

    public boolean isViewTracked(){
        if (view.getTag() != null
                && view.getTag().equals(mSourceToken)) {
            return true;
        }

        return false;
    }

}
