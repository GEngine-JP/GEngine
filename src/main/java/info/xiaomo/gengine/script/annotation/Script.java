package info.xiaomo.gengine.script.annotation;

/**
 * @author 张力
 * @date 2018/3/8 16:23
 */
public @interface Script {

    int order() default 0;
    boolean bootstrap() default false;
}
