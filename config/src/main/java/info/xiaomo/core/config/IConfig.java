/**
 * 创建日期:  2017年08月12日 15:32
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.core.config;

/**
 * 配置文件接口
 *
 * @author YangQiang
 */
public interface IConfig {
    default void afterLoad() {
    }
}
