package com.small.tools.network.internal;

import com.small.tools.network.internal.interfaces.RemoteResource;

/**
 * Author: hjq
 * Date  : 2018/10/02 17:03
 * Name  : RemoteResources
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class RemoteResources {

    private RemoteResources() {

    }

    public static RemoteResourceLocal local() {
        return new RemoteResourceLocal();
    }

    public static RemoteResourceNetwork net() {
        return new RemoteResourceNetwork();
    }

    public static RemoteResource create(){
        return new RemoteResourceAuto();
    }

}
