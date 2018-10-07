package com.small.tools.network.internal.interfaces;

import com.small.tools.network.internal.async.Scheduler;
import com.small.tools.network.internal.cache.SmallCache;

/**
 * Author: hjq
 * Date  : 2018/10/06 19:31
 * Name  : SmallConfig
 * Intro : Edit By hjq
 * Version : 1.0
 */
public interface SmallConfig {

    SmallConfig setHTTPClient(HTTPClient client);

    HTTPClient getHTTPClient();

    SmallConfig setCacheManager(SmallCache cache);

    SmallCache getCacheManager();

    SmallConfig setScheduler(Scheduler scheduler);

    Scheduler getScheduler();

}
