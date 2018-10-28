package com.small.tools.network.support.images;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Author: hjq
 * Date  : 2018/10/04 14:18
 * Name  : ImagesActionImageView
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class ImagesActionView extends SmallImagesAction<View> {

    public ImagesActionView(View view) {
        super(view);
    }

    @Override
    public void placeholder(final SmallImages images) {
        super.placeholder(images);
        if (images != null
                && images.getPlaceholderDrawable() != null) {
            getTarget().setBackgroundDrawable(images.getPlaceholderDrawable());
        }
    }

    @Override
    public void onSuccess(final SmallImages images) {
        super.onSuccess(images);
        if (images != null
                && images.getSuccessDrawable() != null) {
            getTarget().setBackgroundDrawable(images.getSuccessDrawable());

            Animation anim = AnimationUtils.loadAnimation(getTarget().getContext(),
                    android.R.anim.fade_in);

            getTarget().startAnimation(anim);
        }
    }

    @Override
    public void onFailure(final SmallImages images) {
        super.onFailure(images);
        if (images != null
                && images.getFailDrawable() != null) {
            getTarget().setBackgroundDrawable(images.getSuccessDrawable());
        }
    }
}
