/**
 * 创建日期:  2017年08月22日 11:45
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.gameCore.config;

/**
 * 配置构建器
 *
 * @author YangQiang
 */
@FunctionalInterface
public interface IConfigBuilder {
    IConfigBuilder build();

    default String getKeyDelimiter() {
        return "_";
    }
}
