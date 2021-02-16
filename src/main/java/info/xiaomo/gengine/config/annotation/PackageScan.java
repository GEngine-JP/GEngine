/**
 * 创建日期:  2017年08月24日 16:12
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.gengine.config.annotation;

import java.lang.annotation.*;

/**
 * 包名注解
 *
 * @author YangQiang
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PackageScan {
    /**
     * 包名地址
     *
     * @return
     */
    String value();
}
