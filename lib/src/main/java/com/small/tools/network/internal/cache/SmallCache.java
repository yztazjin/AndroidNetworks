package com.small.tools.network.internal.cache;

import com.small.tools.network.internal.interfaces.HTTPRequest;

import java.io.File;

/**
 * Author: hjq
 * Date  : 2018/10/04 20:32
 * Name  : SmallCache
 * Intro : Edit By hjq
 * Version : 1.0
 */
public interface SmallCache {

    void cache(HTTPRequest request, Object parsedData);

    SmallCache setDiskCacheDirectory(File file);

    File getDiskCacheDirectory();

    <T> T getCachedData(HTTPRequest request);

    boolean isCacheHit(HTTPRequest request);
}
