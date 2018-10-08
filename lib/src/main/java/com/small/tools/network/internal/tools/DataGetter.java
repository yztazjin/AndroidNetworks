package com.small.tools.network.internal.tools;

import com.google.gson.Gson;
import com.small.tools.network.internal.interfaces.HTTPRequest;
import com.small.tools.network.internal.interfaces.StatusCode;

/**
 * Author: hjq
 * Date  : 2018/10/08 23:45
 * Name  : DataGetter
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class DataGetter {

    private static Gson gson = new Gson();

    private DataGetter() {
    }

    public static <T> T getData(HTTPRequest request, Class<T> type) {
        if (request == null
                || request.getResourceDataParser() == null
                || type == null
                || !StatusCode.isStatusCodeSuccessful(request.getResponse().getStatusCode())) {
            return null;
        } else {
            if (type.equals(request.getResourceDataParser().getParseType())) {
                return (T) request.getResourceDataParser().getData();
            } else if (String.class.equals(request.getResourceDataParser().getParseType())) {
                String str = (String) request.getResourceDataParser().getData();
                T value = gson.fromJson(str, type);
                return value;
            } else {
                return null;
            }
        }
    }

}
