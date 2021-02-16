/**
 * 创建日期:  2017年08月22日 11:45
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.gengine.config;

/**
 * 配置构建器
 *
 * @author YangQiang
 */
@FunctionalInterface
public interface IConfigBuilder {
    /**
     * 构建
     *
     * @return IConfigBuilder
     */
    IConfigBuilder build();

    /**
     * delimiter
     * @return String
     */
    default String getKeyDelimiter() {
        return "_";
    }
}
