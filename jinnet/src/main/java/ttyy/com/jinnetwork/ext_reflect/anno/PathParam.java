package ttyy.com.jinnetwork.ext_reflect.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author: admin
 * date: 2017/03/07
 * version: 0
 * mail: secret
 * desc: PathParam
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PathParam {

    String value();

}
