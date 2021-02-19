package info.xiaomo.gengine.script.zl.annotation;

/**
 * @author 张力
 * @date 2018/3/8 16:23
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Script {

    int order() default 0;
}
