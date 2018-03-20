package info.xiaomo.http.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于指定controller的方法对的path
 * @author 张力
 * @date 2017/12/22 16:33
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Path {
        String value() default "/";
}
