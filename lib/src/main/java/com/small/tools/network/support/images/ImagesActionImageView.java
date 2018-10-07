package com.small.tools.network.support.images;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Author: hjq
 * Date  : 2018/10/04 14:18
 * Name  : ImagesActionImageView
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class ImagesActionImageView extends SmallImagesAction<ImageView> {

    public ImagesActionImageView(ImageView view) {
        super(view);
    }

    @Override
    public void placeholder(final SmallImages images) {
        super.placeholder(images);
        if (images != null
                && images.getPlaceholderDrawable() != null) {
            getTarget().setImageDrawable(images.getPlaceholderDrawable());
        }
    }

    @Override
    public void onSuccess(final SmallImages images) {
        super.onSuccess(images);
        if (images != null
                && images.getSuccessDrawable() != null) {

            getTarget().setImageDrawable(images.getSuccessDrawable());

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
            getTarget().setImageDrawable(images.getFailDrawable());
        }
    }
}
