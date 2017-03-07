package ttyy.com.jinnetwork.ext_reflect.anno;

import org.json.JSONObject;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import ttyy.com.jinnetwork.Https;
import ttyy.com.jinnetwork.core.callback.HTTPCallback;
import ttyy.com.jinnetwork.core.work.HTTPRequest;
import ttyy.com.jinnetwork.core.work.HTTPMethod;
import ttyy.com.jinnetwork.core.work.HTTPRequestBuilder;
import ttyy.com.jinnetwork.core.work.method_get.HTTPRequestGetBuilder;
import ttyy.com.jinnetwork.core.work.method_post.HTTPRequestPostBuilder;

/**
 * author: admin
 * date: 2017/03/01
 * version: 0
 * mail: secret
 * desc: $$AnnoConverter
 */

public class $AnnoConverter {

    private $AnnoConverter() {

    }

    public static HTTPRequest convert(Method method, Object[] args) {

        String url = null;

        // Default Request MethodType
        HTTPMethod mHttpMethod = HTTPMethod.GET;
        // Http Request MethodType GET or POST
        MethodType mHttpMethodAnno = method.getAnnotation(MethodType.class);
        if (mHttpMethodAnno != null) {
            mHttpMethod = mHttpMethodAnno.value();
        }

        HTTPRequestBuilder builder = null;
        switch (mHttpMethod) {
            case GET:
                builder = Https.get(url);
                break;
            case POST:
                builder = Https.post(url, mHttpMethodAnno.content_type());
                break;
        }

        // Class URL
        HTTPUrl mClassRequestURLAnno = method.getDeclaringClass().getAnnotation(HTTPUrl.class);
        if (mClassRequestURLAnno != null) {
            builder.setRequestURL(mClassRequestURLAnno.value());
        }
        // MethodType URL
        HTTPUrl mMethodRequestURLAnno = method.getAnnotation(HTTPUrl.class);
        if (mMethodRequestURLAnno != null) {
            builder.setRequestURL(mMethodRequestURLAnno.value());
        }

        Annotation[][] panos = method.getParameterAnnotations();

        // 遍历该方法的所有参数的每个参数的所有注解
        for (int i = 0; i < args.length; i++) {
            // 遍历这个参数的所有注解
            for (int j = 0; j < panos[i].length; j++) {
                Annotation ano = panos[i][j];
                if (ano.annotationType().equals(Param.class)) {
                    // Request Params
                    Param param = (Param) ano;

                    if (args[i] == null
                            || param.value() == null
                            || param.value().trim().equals("")) {


                    } else {

                        String key = param.value();
                        addParam(mHttpMethod, builder, key, args[i]);
                    }
                    break;
                } else if (ano.annotationType().equals(Callback.class)) {
                    // Request Callback
                    if(args[i] instanceof HTTPCallback){
                        HTTPCallback callback = (HTTPCallback) args[i];
                        builder.setHttpCallback(callback);
                    }
                    break;
                } else if (ano.annotationType().equals(Header.class)) {
                    // Request Header
                    Header header = (Header) ano;
                    if (args[i] == null
                            || header.value() == null
                            || header.value().trim().equals("")) {


                    } else {

                        String key = header.value();
                        builder.addHeader(key, String.valueOf(args[i]));
                    }
                    break;
                } else if (ano.annotationType().equals(HTTPUrl.class)) {
                    // URL Path
                    if (args[i] != null) {
                        builder.setRequestURL(String.valueOf(args[i]));
                    }

                    break;
                } else if (ano.annotationType().equals(Path.class)) {
                    // URL Path Params
                    Path mPathParam = method.getDeclaringClass().getAnnotation(Path.class);
                    if (mPathParam != null) {
                        builder.addPathParam(mPathParam.key(), mPathParam.value());
                    }
                }
            }
        }

        return builder.build();
    }

    private static void addParam(HTTPMethod method, HTTPRequestBuilder builder, String key, Object param) {

        Class mParamType = param.getClass();
        if (mParamType.equals(File.class)) {
            // get专属

            if (method == HTTPMethod.GET) {
                ((HTTPRequestGetBuilder) builder).setDownloadMode((File) param);
            }
        } else if (mParamType.equals(JSONObject.class)) {
            // post专属

            if (method == HTTPMethod.POST) {
                ((HTTPRequestPostBuilder) builder).addParam(key, (JSONObject) param);
            }
        } else {
            // 其他参数

            builder.addParam(key, param);
        }

    }
}
