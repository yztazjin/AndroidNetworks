package ttyy.com.jinnetwork.core.work;

/**
 * author: admin
 * date: 2017/02/28
 * version: 0
 * mail: secret
 * desc: MethodType
 */

public enum HTTPMethod {

    /**
     * Client.get
     */
    GET("GET"),
    /**
     * Client.post
     */
    POST("POST"),
    /**
     * 此Method会回调到Client中特殊标出的方法
     */
    SPECIAL("USER_CUSTOM_METHOD");

    String desc;
    HTTPMethod(String value){
        desc = value;
    }

    String value(){
        return desc;
    }
}
