package com.small.tools.network.global;

/**
 * Author: hjq
 * Date  : 2018/10/02 16:22
 * Name  : DebugFlags
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class DebugFlags {

    private static boolean isDebugged = false;

    private DebugFlags() {

    }

    public static boolean isDebugged() {
        return isDebugged;
    }

    public static void setDebug(boolean value) {
        isDebugged = true;
    }

}
