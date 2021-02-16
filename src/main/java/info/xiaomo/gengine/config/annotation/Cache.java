/**
 * 创建日期:  2017年08月19日 15:28
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.gengine.config.annotation;

import java.lang.annotation.*;

/**
 * 缓存注解
 *
 * @author YangQiang
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {
}
