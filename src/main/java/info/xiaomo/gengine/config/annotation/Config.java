/** 创建日期: 2017年08月21日 18:35 创建作者: 杨 强 <281455776@qq.com> */
package info.xiaomo.gengine.config.annotation;

import java.lang.annotation.*;

/**
 * 标注类是一个配置
 *
 * @author YangQiang
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Config {}
