package ttyy.com.jinnetwork.core.work.method_post;

/**
 * author: admin
 * date: 2017/02/28
 * version: 0
 * mail: secret
 * desc: PostContentType
 */

public enum PostContentType {

    ApplicationJson("application/json"),

    FormURLEncoded("application/x-www-form-urlencoded"),

    MultipartFormdata("multipart/form-data");

    String content;

    PostContentType(String value){
        content = value;
    }

    public String key(){

        return "Content-Type";
    }

    public String value(){
        return content;
    }

    public void setValue(String content){
        this.content = content;
    }

}
