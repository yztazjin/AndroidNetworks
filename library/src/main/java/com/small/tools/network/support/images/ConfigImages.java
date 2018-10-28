package com.small.tools.network.support.images;

import android.graphics.drawable.Drawable;

import com.small.tools.network.global.Configs;
import com.small.tools.network.global.SmallLogs;
import com.small.tools.network.internal.interfaces.ResourceDataParser;
import com.small.tools.network.internal.interfaces.SmallConfig;
import com.small.tools.network.internal.async.Scheduler;
import com.small.tools.network.internal.cache.CacheAction;
import com.small.tools.network.internal.cache.SmallCache;
import com.small.tools.network.internal.interfaces.HTTPClient;

/**
 * Author: hjq
 * Date  : 2018/10/06 19:05
 * Name  : ConfigImages
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class ConfigImages implements SmallConfig {

    Scheduler mScheduler;
    int mBitmapMaxWidth;
    int mBitmapMaxHeight;

    CacheAction mCacheAction;
    SmallCache mCacheManager;
    HTTPClient mHTTPClient;
    ResourceDataParser mDataParser;

    public ConfigImages() {
        mScheduler = new SchedulerImages();
        mBitmapMaxWidth = mBitmapMaxHeight = 512;

        mCacheAction = CacheAction.All;
        mCacheManager = Configs.getSingleton().getCacheManager();
        mHTTPClient = Configs.getSingleton().getHTTPClient();
        mDataParser = new ResourceParserBitmap();
    }

    public ConfigImages setScheduler(Scheduler scheduler) {
        mScheduler = scheduler;
        return this;
    }

    @Override
    public Scheduler getScheduler() {
        return mScheduler;
    }

    @Override
    public SmallConfig setDataParser(ResourceDataParser parser) {
        if (parser == null) {
            throw new NullPointerException("parser shouldn't set null");
        }

        if (!Drawable.class.isAssignableFrom(parser.getParseType())) {
            SmallLogs.w("data parse-type is not drawable!");
        }

        mDataParser = parser;
        return this;
    }

    @Override
    public ResourceDataParser getDataParser() {
        return mDataParser.newInstance();
    }

    public ConfigImages setBitmapMaxSize(int width, int height) {
        mBitmapMaxWidth = width;
        mBitmapMaxHeight = height;
        return this;
    }

    public int[] getBitmapMaxSize() {
        return new int[]{mBitmapMaxWidth, mBitmapMaxHeight};
    }

    @Override
    public SmallConfig setHTTPClient(HTTPClient client) {
        if (client == null) {
            throw new NullPointerException("request client shouldn't null!");
        }
        mHTTPClient = client;
        return this;
    }

    @Override
    public HTTPClient getHTTPClient() {
        return mHTTPClient;
    }

    @Override
    public ConfigImages setCacheManager(SmallCache cache) {
        mCacheManager = cache;
        return this;
    }

    @Override
    public SmallCache getCacheManager() {
        return mCacheManager;
    }

    public ConfigImages setCacheAction(CacheAction action) {
        mCacheAction = action;
        return this;
    }

    public CacheAction getCacheAction() {
        return mCacheAction;
    }
}
