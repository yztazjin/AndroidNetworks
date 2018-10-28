package com.small.tools.network.internal.tools;

import android.text.TextUtils;

import com.small.tools.network.internal.interfaces.HTTPRequest;

import java.net.URLEncoder;
import java.util.Map;

/**
 * Author: hjq
 * Date  : 2018/10/03 14:38
 * Name  : URLGetterTransfer
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class URLGetterTransfer {

    public static String transform(HTTPRequest request) {

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : request.getParams().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value != null) {
                sb.append(key)
                        .append("=")
                        .append(URLEncoder.encode(value.toString()))
                        .append("&");
            }
        }

        String url = request.getRemoteResource().getResourceAddress();
        if (!TextUtils.isEmpty(sb)) {
            String query_params = sb.substring(0, sb.length() - 1);
            url = url + "?" + query_params;
        }

        return url;

    }

}
