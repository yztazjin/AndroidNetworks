package com.small.tools.network.support.images;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.small.tools.network.internal.async.Scheduler;
import com.small.tools.network.internal.cache.CacheAction;
import com.small.tools.network.internal.interfaces.ResourceDataParser;

import java.io.File;

/**
 * Author: hjq
 * Date  : 2018/10/04 12:56
 * Name  : SmallImages
 * Intro : Edit By hjq
 * Version : 1.0
 */
public interface SmallImages {

    SmallImages setResourceDataParser(ResourceDataParser parser);

    SmallImages setCacheAction(CacheAction action);

    SmallImages setScheduler(Scheduler scheduler);

    SmallImages loadFromDrawable(Context context, int id);

    SmallImages loadFromRaw(Context context, int id);

    SmallImages loadFromAssets(Context context, String name);

    SmallImages loadFromNetwork(String address);

    SmallImages loadFromDisk(File file);

    SmallImages placeholder(Drawable drawable);

    Drawable getPlaceholderDrawable();

    SmallImages fail(Drawable drawable);

    Drawable getFailDrawable();

    Drawable getSuccessDrawable();

    <T extends SmallImagesAction> T getAction();

    <T extends SmallImagesAction> void into(T view);

    void into(View view);

    void into(ImageView view);

}
