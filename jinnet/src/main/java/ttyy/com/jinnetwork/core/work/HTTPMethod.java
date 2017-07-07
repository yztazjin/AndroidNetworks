package ttyy.com.jinnetwork.core.work;

/**
 * author: admin
 * date: 2017/02/28
 * version: 0
 * mail: secret
 * desc: MethodType
 */

public enum HTTPMethod {

    GET("GET"),
    POST("POST"),
    SPECIAL("USER_CUSTOM_METHOD");

    String desc;
    HTTPMethod(String value){
        desc = value;
    }

    String value(){
        return desc;
    }
}
