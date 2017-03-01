package ttyy.com.jinnetwork.ext_reflect.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ttyy.com.jinnetwork.core.work.HttpMethod;
import ttyy.com.jinnetwork.core.work.method_post.PostContentType;

/**
 * author: admin
 * date: 2017/03/01
 * version: 0
 * mail: secret
 * desc: HTTPMethod
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HTTPMethod {

    /**
     * GET POST
     * @return
     */
    HttpMethod value();

    PostContentType content_type() default PostContentType.ApplicationJson;

}
