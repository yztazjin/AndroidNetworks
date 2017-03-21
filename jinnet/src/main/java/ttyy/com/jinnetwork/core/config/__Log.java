package ttyy.com.jinnetwork.core.config;

import android.util.Log;

/**
 * Author: hjq
 * Date  : 2017/03/21 23:12
 * Name  : __Log
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class __Log {

    static boolean isDebugMode = false;

    public static void i(String tag, String msg){
        log(Log.INFO, tag, msg);
    }

    public static void w(String tag, String msg){
        log(Log.WARN, tag, msg);
    }

    public static void e(String tag, String msg){
        log(Log.ERROR, tag, msg);
    }

    public static void d(String tag, String msg){
        log(Log.DEBUG, tag, msg);
    }

    public static void log(int level, String tag, String msg){
        if(isDebugMode){
            Log.println(level, tag, msg);
        }
    }

}
