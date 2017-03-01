package ttyy.com.jinnetwork.core.work;

/**
 * author: admin
 * date: 2017/02/28
 * version: 0
 * mail: secret
 * desc: HTTPMethod
 */

public enum HttpMethod {

    GET("GET"),
    POST("POST");

    String desc;
    HttpMethod(String value){
        desc = value;
    }

    String value(){
        return desc;
    }
}
