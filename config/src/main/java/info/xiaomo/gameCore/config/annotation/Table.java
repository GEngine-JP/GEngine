/**
 * 创建日期:  2017年08月12日 15:23
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.gameCore.config.annotation;

import java.lang.annotation.*;

/**
 * 表注解
 *
 * @author YangQiang
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table{
    /**
     * 名字
     *
     * @return
     */
    String name() default "";

    /**
     * 主键
     *
     * @return
     */
    String[] primaryKey();
}
