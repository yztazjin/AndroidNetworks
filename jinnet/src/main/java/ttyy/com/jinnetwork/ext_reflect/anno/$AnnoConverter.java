package ttyy.com.jinnetwork.ext_reflect.anno;

import org.json.JSONObject;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import ttyy.com.jinnetwork.Https;
import ttyy.com.jinnetwork.core.callback.HttpCallback;
import ttyy.com.jinnetwork.core.work.HTTPRequest;
import ttyy.com.jinnetwork.core.work.HttpMethod;
import ttyy.com.jinnetwork.core.work.HttpRequestBuilder;
import ttyy.com.jinnetwork.core.work.method_get.HttpRequestGetBuilder;
import ttyy.com.jinnetwork.core.work.method_post.HttpRequestPostBuilder;

/**
 * author: admin
 * date: 2017/03/01
 * version: 0
 * mail: secret
 * desc: $$AnnoConverter
 */

public class $AnnoConverter {

    private $AnnoConverter(){

    }

    public static HTTPRequest convert(Method method, Object[] args){

        String url = null;
        HttpMethod mHttpMethod = HttpMethod.GET;

        HTTPMethod mHttpMethodAnno = method.getAnnotation(HTTPMethod.class);
        if(mHttpMethodAnno != null){
            mHttpMethod = mHttpMethodAnno.value();
        }

        HttpRequestBuilder builder = null;
        switch (mHttpMethod){
            case GET:
                builder = Https.get(url);
                break;
            case POST:
                builder = Https.post(url, mHttpMethodAnno.content_type());
                break;
        }

        HTTPUrl mClassRequestURLAnno = method.getDeclaringClass().getAnnotation(HTTPUrl.class);
        if(mClassRequestURLAnno != null){
            builder.setRequestURL(mClassRequestURLAnno.value());
        }else {
            HTTPUrl mMethodRequestURLAnno = method.getAnnotation(HTTPUrl.class);
            if(mMethodRequestURLAnno != null){
                builder.setRequestURL(mMethodRequestURLAnno.value());
            }
        }


        Annotation[][] panos = method.getParameterAnnotations();

        // 遍历该方法的所有参数的每个参数的所有注解
        for (int i = 0; i < args.length; i++) {
            // 遍历这个参数的所有注解
            for (int j = 0; j < panos[i].length; j++) {
                Annotation ano = panos[i][j];
                if (ano.annotationType().equals(Param.class)) {
                    Param param = (Param) ano;

                    if(args[i] == null
                            || param.value() == null
                            || param.value().trim().equals("")){


                    }else {

                        String key = param.value();
                        addParam(mHttpMethod, builder, key, args[i]);
                    }
                    break;
                } else if (ano.annotationType().equals(Callback.class)) {
                    HttpCallback callback = (HttpCallback) args[i];
                    builder.setHttpCallback(callback);
                    break;
                }else if(ano.annotationType().equals(Header.class)){
                    Header header = (Header) ano;
                    if(args[i] == null
                            ||header.value() == null
                            || header.value().trim().equals("")){


                    }else {

                        String key = header.value();
                        builder.addHeader(key, String.valueOf(args[i]));
                    }
                    break;
                }else if(ano.annotationType().equals(HTTPUrl.class)){

                    if(args[i] != null){
                        builder.setRequestURL(String.valueOf(args[i]));
                    }

                    break;
                }
            }
        }

        return builder.build();
    }

    private static void addParam(HttpMethod method, HttpRequestBuilder builder, String key, Object param){

        Class mParamType = param.getClass();
        if(mParamType.equals(Integer.class)
                || mParamType.equals(int.class)){

            builder.addParam(key, (int)param);
        }else if(mParamType.equals(Float.class)
                || mParamType.equals(float.class)){

            builder.addParam(key, (float)param);
        }else if(mParamType.equals(Double.class)
                || mParamType.equals(double.class)){

            builder.addParam(key, (double)param);
        }else if(mParamType.equals(Long.class)
                || mParamType.equals(long.class)){

            builder.addParam(key, (long)param);
        }else if(mParamType.equals(String.class)){

            builder.addParam(key, (String)param);
        }else if(mParamType.equals(File.class)){
            // get专属

            if(method == HttpMethod.GET){
                ((HttpRequestGetBuilder)builder).setDownloadMode((File)param);
            }
        }else if(mParamType.equals(JSONObject.class)){
            // post专属

            if(method == HttpMethod.POST){
                ((HttpRequestPostBuilder)builder).addParam(key, (JSONObject)param);
            }
        }else {
            // 其他类型不处理

        }

    }
}
