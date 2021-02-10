package info.xiaomo.core.http.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * json注解，如果controller的方法上有此注解，那么会对返回值进行json编码
 * @author 张力
 * @date 2017/12/23 14:48
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Json {
}
