package com.small.tools.network.support.proxy;

import android.text.TextUtils;

import com.small.tools.network.Https;
import com.small.tools.network.global.Configs;
import com.small.tools.network.internal.interfaces.HTTPCallback;
import com.small.tools.network.internal.interfaces.HTTPMethod;
import com.small.tools.network.internal.interfaces.HTTPRequest;
import com.small.tools.network.internal.interfaces.SmallHeader;
import com.small.tools.network.support.proxy.anno.AnnoAddress;
import com.small.tools.network.support.proxy.anno.AnnoCallback;
import com.small.tools.network.support.proxy.anno.AnnoHeader;
import com.small.tools.network.support.proxy.anno.AnnoHeaders;
import com.small.tools.network.support.proxy.anno.AnnoMethod;
import com.small.tools.network.support.proxy.anno.AnnoParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Author: hjq
 * Date  : 2018/10/06 22:58
 * Name  : SmallServiceProxy
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class SmallServiceProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        HTTPRequest request = parseRequest(proxy, method, args);
        request.requestAsync();

        return null;
    }

    protected HTTPRequest parseRequest(Object proxy, Method method, Object[] args) {
        AnnoMethod annoMethod = method.getAnnotation(AnnoMethod.class);
        if (annoMethod == null) {
            throw new NullPointerException(method.getName() + " must be decorated by @HTTPMethod");
        }

        HTTPMethod httpMethod = annoMethod.value();
        if (httpMethod == null) {
            throw new NullPointerException(method.getName() + " HTTPMethod shouldn't null");
        }

        AnnoAddress annoAddress = method.getAnnotation(AnnoAddress.class);
        String httpAddress = annoAddress.value();

        HTTPRequest request = null;
        switch (httpMethod) {
            case GET:
                request = Https.get(httpAddress);
                break;
            case POST:
                request = Https.post(httpAddress);
                break;
        }

        AnnoHeaders annoHeaders = method.getAnnotation(AnnoHeaders.class);
        if (annoHeaders != null) {
            if (annoHeaders.names() == null
                    || annoHeaders.values() == null) {

            } else if (annoHeaders.names().length != annoHeaders.values().length) {

            } else {
                for (int i = 0; i < annoHeaders.names().length; i++) {
                    String name = annoHeaders.names()[i];
                    String value = annoHeaders.values()[i];
                    if (name.toLowerCase().equals("content-type")) {
                        if (SmallHeader.ContentType.ApplicationJson.getValue().toLowerCase()
                                .equals(value.toLowerCase())) {
                            request.setHTTPContent(SmallHeader.ContentType.ApplicationJson);
                        } else if (SmallHeader.ContentType.FormURLEncoded.getValue().toLowerCase()
                                .equals(value.toLowerCase())) {
                            request.setHTTPContent(SmallHeader.ContentType.FormURLEncoded);
                        } else if (SmallHeader.ContentType.MultipartFormdata.getValue().toLowerCase()
                                .equals(value.toLowerCase())) {
                            request.setHTTPContent(SmallHeader.ContentType.MultipartFormdata);
                        } else {
                            request.setHeader(name, value);
                        }
                    } else {
                        request.setHeader(name, value);
                    }
                }
            }
        }


        Annotation[][] panos = method.getParameterAnnotations();
        // 遍历该方法的所有参数的每个参数的所有注解
        for (int i = 0; i < args.length; i++) {
            // 遍历这个参数的所有注解
            for (int j = 0; j < panos[i].length; j++) {
                Annotation ano = panos[i][j];
                if (ano == null
                        || args[i] == null) {
                    continue;
                }

                if (AnnoParam.class.equals(ano.annotationType())) {
                    AnnoParam annoParam = (AnnoParam) ano;
                    request.setParam(annoParam.value(), String.valueOf(args[i]));

                } else if (AnnoHeader.class.equals(ano.annotationType())) {
                    AnnoHeader annoHeader = (AnnoHeader) ano;
                    String annoHeaderKey = annoHeader.value();
                    String value = String.valueOf(args[i]);
                    if (args[i] instanceof SmallHeader) {
                        SmallHeader header = (SmallHeader) args[i];
                        request.setHeader(header);
                    } else if (annoHeaderKey.toLowerCase().equals("content-type")) {
                        if (SmallHeader.ContentType.ApplicationJson.getValue().toLowerCase()
                                .equals(value.toLowerCase())) {
                            request.setHTTPContent(SmallHeader.ContentType.ApplicationJson);
                        } else if (SmallHeader.ContentType.FormURLEncoded.getValue().toLowerCase()
                                .equals(value.toLowerCase())) {
                            request.setHTTPContent(SmallHeader.ContentType.FormURLEncoded);
                        } else if (SmallHeader.ContentType.MultipartFormdata.getValue().toLowerCase()
                                .equals(value.toLowerCase())) {
                            request.setHTTPContent(SmallHeader.ContentType.MultipartFormdata);
                        } else {
                            request.setHeader(annoHeaderKey, value);
                        }
                    } else {
                        request.setHeader(annoHeaderKey, value);
                    }

                } else if (AnnoCallback.class.equals(ano.annotationType())) {
                    HTTPCallback callback = (HTTPCallback) args[i];
                    request.setHTTPCallback(callback);
                }

            }
        }

        return request;
    }


}
