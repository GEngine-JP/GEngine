package info.xiaomo.core.http.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识注解，用于标识Http请求的Controller类
 *
 * @author 张力
 * @date 2017/12/22 16:30
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ControllerPackage {
    String value() default "";
}
