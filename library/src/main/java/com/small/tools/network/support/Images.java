package com.small.tools.network.support;

import com.small.tools.network.support.images.SmallImages;
import com.small.tools.network.support.images.SmallImagesRequest;

/**
 * Author: hjq
 * Date  : 2018/10/02 16:12
 * Name  : Images
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class Images {

    private Images() {
    }

    public static SmallImages get() {
        return new SmallImagesRequest();
    }
}
