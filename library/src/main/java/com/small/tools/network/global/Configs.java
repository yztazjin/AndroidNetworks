package com.small.tools.network.global;

import com.small.tools.network.internal.ResourceParserString;
import com.small.tools.network.internal.async.Scheduler;
import com.small.tools.network.internal.async.SchedulerFIFO;
import com.small.tools.network.internal.cache.SmallCache;
import com.small.tools.network.internal.cache.SmallCacheDefault;
import com.small.tools.network.internal.interfaces.HTTPClient;
import com.small.tools.network.internal.client.ClientOKHttpIml;
import com.small.tools.network.internal.interfaces.ResourceDataParser;
import com.small.tools.network.internal.interfaces.SmallConfig;
import com.small.tools.network.support.images.ConfigImages;

/**
 * Author: hjq
 * Date  : 2018/10/05 23:11
 * Name  : Configs
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class Configs implements SmallConfig {

    static class Holder {
        static final Configs INSTANCE = new Configs();
    }

    HTTPClient mClient;
    SmallCache mCacheManager;
    Scheduler mScheduler;
    ResourceDataParser mDataParser;

    volatile ConfigImages mConfigImages;

    private Configs() {
        mClient = new ClientOKHttpIml();
        mCacheManager = new SmallCacheDefault();
        mScheduler = new SchedulerFIFO();
        mDataParser = new ResourceParserString();
    }

    public static Configs getSingleton() {
        return Holder.INSTANCE;
    }

    public static Configs create() {
        return new Configs();
    }

    @Override
    public Configs setHTTPClient(HTTPClient client) {
        if (client == null) {
            throw new NullPointerException("request client shouldn't be null");
        }
        this.mClient = client;
        return this;
    }

    @Override
    public HTTPClient getHTTPClient() {
        return this.mClient;
    }

    @Override
    public Configs setCacheManager(SmallCache cache) {
        this.mCacheManager = cache;
        return this;
    }

    @Override
    public SmallCache getCacheManager() {
        return mCacheManager;
    }

    @Override
    public Scheduler getScheduler() {
        return mScheduler;
    }

    @Override
    public SmallConfig setDataParser(ResourceDataParser parser) {
        if (parser == null) {
            throw new NullPointerException("dataparser shouldn't set null");
        }
        mDataParser = parser;
        return this;
    }

    @Override
    public ResourceDataParser getDataParser() {
        return mDataParser.newInstance();
    }

    @Override
    public Configs setScheduler(Scheduler scheduler) {
        mScheduler = scheduler;
        return this;
    }

    public synchronized ConfigImages images() {
        ConfigImages inst = mConfigImages;
        if (inst == null) {
            synchronized (Configs.class) {
                inst = mConfigImages;
                if (inst == null) {
                    inst = new ConfigImages();
                    mConfigImages = inst;
                }
            }
        }

        return inst;
    }

}
