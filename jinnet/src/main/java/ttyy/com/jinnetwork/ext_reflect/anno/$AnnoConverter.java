package ttyy.com.jinnetwork.ext_reflect.anno;

import org.json.JSONObject;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import ttyy.com.jinnetwork.Https;
import ttyy.com.jinnetwork.core.callback.HTTPCallback;
import ttyy.com.jinnetwork.core.work.HTTPMethod;
import ttyy.com.jinnetwork.core.work.HTTPRequestBuilder;
import ttyy.com.jinnetwork.core.work.method_get.HTTPRequestGetBuilder;
import ttyy.com.jinnetwork.core.work.method_post.HTTPRequestPostBuilder;
import ttyy.com.jinnetwork.core.work.method_special.HTTPRequestSpecialBuilder;

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

    public static HTTPRequestBuilder convert(Method method, Object[] args) {

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
            case SPECIAL:
                builder = new HTTPRequestSpecialBuilder();
                break;
        }

        // Class URL
        URLPath mClassRequestURLAnno = method.getDeclaringClass().getAnnotation(URLPath.class);
        if (mClassRequestURLAnno != null) {
            builder.setRequestURL(mClassRequestURLAnno.value());
        }
        // MethodType URL
        URLPath mMethodRequestURLAnno = method.getAnnotation(URLPath.class);
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
                    if (args[i] instanceof HTTPCallback) {
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
                } else if (ano.annotationType().equals(URLPath.class)) {
                    // URL
                    if (args[i] != null) {
                        builder.setRequestURL(String.valueOf(args[i]));
                    }

                    break;
                } else if (ano.annotationType().equals(PathParam.class)) {
                    // URL PathParam Params
                    PathParam mPathParam = (PathParam) ano;

                    if (args[i] == null
                            || mPathParam.value() == null
                            || mPathParam.value().trim().equals("")) {


                    } else {

                        String key = mPathParam.value();
                        builder.addPathParam(key, args[i].toString());
                    }
                }
            }
        }

        return builder;
    }

    private static void addParam(HTTPMethod method, HTTPRequestBuilder builder, String key, Object param) {

        Class mParamType = param.getClass();
        if (mParamType.equals(File.class)) {

            switch (method){
                case GET:
                    // get专属 下载
                    ((HTTPRequestGetBuilder) builder).setDownloadMode((File) param);
                    break;
                case POST:
                    // post专属 上传
                    ((HTTPRequestPostBuilder) builder).addParam(key, (File) param);
                    break;
                case SPECIAL:
                    // special专属 用户自定义
                    builder.addParam(key, param);
                    break;
                default:
                    break;
            }

        } else if (mParamType.equals(JSONObject.class)) {

            switch (method){
                case GET:
                    // get 没有对JSONObject的操作

                    break;
                case POST:
                    // post专属
                    ((HTTPRequestPostBuilder) builder).addParam(key, param);
                    break;
                case SPECIAL:
                    // special专属 用户自定义
                    builder.addParam(key, param);
                    break;
                default:
                    break;
            }

        } else {

            // 其他参数
            builder.addParam(key, param);
        }

    }
}
