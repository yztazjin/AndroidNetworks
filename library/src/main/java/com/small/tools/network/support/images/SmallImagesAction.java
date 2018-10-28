package com.small.tools.network.support.images;

import android.view.View;

/**
 * Author: hjq
 * Date  : 2018/10/04 13:01
 * Name  : SmallImagesAction
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class SmallImagesAction<T extends View> {

    T mTarget;

    public SmallImagesAction(T view) {
        mTarget = view;
    }

    public final T getTarget() {
        return mTarget;
    }

    public void placeholder(SmallImages images) {
    }

    public void onProgress(SmallImages images, long cur, long total) {
    }

    public void onSuccess(SmallImages images) {
    }

    public void onFailure(SmallImages images) {
    }

    public void onCancel(SmallImages images, boolean isUserCanceled) {
    }

    public void onFinish(SmallImages images) {
    }

}
