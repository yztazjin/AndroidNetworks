package com.small.tools.network.global;

import android.util.Log;

/**
 * Author: hjq
 * Date  : 2018/10/02 16:17
 * Name  : SmallLogs
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class SmallLogs {

    private static final String TAG = "SmallHttp";
    private static final String THROWABLE_TAG = "SmallHttpException";

    public static void i(Throwable tr) {
        if (DebugFlags.isDebugged())
            Log.i(TAG, THROWABLE_TAG, tr);
    }

    public static void i(String msg) {
        if (DebugFlags.isDebugged())
            Log.i(TAG, msg);
    }

    public static void d(Throwable tr) {
        if (DebugFlags.isDebugged())
            Log.d(TAG, THROWABLE_TAG, tr);
    }

    public static void d(String msg) {
        if (DebugFlags.isDebugged())
            Log.d(TAG, msg);
    }

    public static void e(Throwable tr) {
        Log.e(TAG, THROWABLE_TAG, tr);
    }

    public static void e(String msg) {
        Log.e(TAG, msg);
    }

    public static void w(Throwable tr) {
        Log.w(TAG, THROWABLE_TAG, tr);
    }

    public static void w(String msg) {
        Log.w(TAG, msg);
    }

    public static void v(Throwable tr) {
        if (DebugFlags.isDebugged())
            Log.v(TAG, THROWABLE_TAG, tr);
    }

    public static void v(String msg) {
        if (DebugFlags.isDebugged())
            Log.v(TAG, msg);
    }

}
