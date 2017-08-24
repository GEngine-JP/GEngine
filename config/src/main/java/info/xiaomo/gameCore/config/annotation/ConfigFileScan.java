/**
 * 创建日期:  2017年08月24日 16:18
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.gameCore.config.annotation;

import java.lang.annotation.*;

/**
 * 配置文件信息注解
 *
 * @author YangQiang
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConfigFileScan {
    /**
     * 配置文件地址
     *
     * @return
     */
    String value();

    /**
     * 配置文件后缀
     *
     * @return
     */
    String suffix();
}
