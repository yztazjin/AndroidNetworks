package com.small.tools.network.internal.interfaces;

/**
 * Author: hjq
 * Date  : 2018/10/03 11:12
 * Name  : StatusCode
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class StatusCode {

    public static final int NOT_INITIALIZE = -1;

    public static final int PARSE_SUCCESS = 0;

    public static final int PARSE_ERROR_CANCEL_FINISH = -1001;
    public static final int PARSE_ERROR_FILENOTFOUND = -1002;
    public static final int PARSE_ERROR_IOEXCEPTION = -1003;

    public static final int REQUEST_ERROR_USER_CANCEL = -2000;
    public static final int REQUEST_ERROR_TOOL_CANCEL = -2001;
    public static final int REQUEST_ERROR_UNSUPPORTENCODE = -2002;
    public static final int REQUEST_ERROR_NOT_PREPARED = -2003;
    public static final int REQUEST_ERROR_OTHERRES = -2004;

    public static final int REQUEST_SUCCESS_FROM_CACHE = 1000;
    public static final int REQUEST_SUCCESS_FROM_FILE = 1001;
    public static final int REQUEST_SUCCESS_FROM_STREAM = 1002;
    public static final int REQUEST_SUCCESS_FROM_PARSEDDATA = 1003;

    public static boolean isStatusCodeSuccessful(int code){
        return code == PARSE_SUCCESS
                || code == REQUEST_SUCCESS_FROM_CACHE
                || code == REQUEST_SUCCESS_FROM_FILE
                || code == REQUEST_SUCCESS_FROM_STREAM
                || code == 200
                || code == 416
                || code == 206;
    }
}
