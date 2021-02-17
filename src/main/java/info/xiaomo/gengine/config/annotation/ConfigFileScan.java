/**
 * 创建日期:  2017年08月24日 16:18
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.gengine.config.annotation;

import java.lang.annotation.*;
import info.xiaomo.gengine.config.ConfigSuffix;

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
	 * 不配置的话默认读取 java/src/resource下的文件路径
	 * 如果配置的是相对路径则是在以java/src/resources为根路径
	 *
	 * @return
	 */
	String value();

	/**
	 * 配置文件后缀
	 * excel只支持xlsx表格
	 *
	 * @return
	 */
	ConfigSuffix suffix() default ConfigSuffix.excel;
}
