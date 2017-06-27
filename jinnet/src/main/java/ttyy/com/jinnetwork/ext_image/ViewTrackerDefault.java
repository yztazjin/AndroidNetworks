package ttyy.com.jinnetwork.ext_image;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;

import ttyy.com.jinnetwork.core.config.__Log;
import ttyy.com.jinnetwork.core.work.HTTPRequest;
import ttyy.com.jinnetwork.core.work.HTTPResponse;

/**
 * author: admin
 * date: 2017/03/03
 * version: 0
 * mail: secret
 * desc: ViewTrackerDefault
 */

public class ViewTrackerDefault extends ViewTracker {

    public ViewTrackerDefault(View view) {
        super(view);
    }

    @Override
    public void onImageLoadPre(HTTPRequest request) {
        if (placeHolderId > 0) {
            setImageIntoView(placeHolderId);
        } else {
            __Log.w("Images", "Hasn't Set The PreStart ResourceId");
        }
    }

    @Override
    public void onImageLoadSuccess(HTTPResponse response, Bitmap bm) {
        setImageIntoView(bm);
    }

    @Override
    public void onImageLoadFailure(HTTPResponse response) {
        super.onImageLoadFailure(response);
        if (errorId > 0) {
            setImageIntoView(errorId);
        } else {
            __Log.w("Images", "Hasn't Set The Failure ResourceId");
        }
    }

    public void setImageIntoView(final int id) {
        if (view instanceof ImageView) {
            ((ImageView) view).setImageResource(id);
        } else {
            view.setBackgroundResource(id);
        }
    }

    public void setImageIntoView(final Bitmap bm) {
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


    }

}
