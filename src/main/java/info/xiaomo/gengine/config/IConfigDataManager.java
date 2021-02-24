/** 创建日期: 2017年08月21日 17:34 创建作者: 杨 强 <281455776@qq.com> */
package info.xiaomo.gengine.config;

import java.util.List;

/**
 * 配置管理接口
 *
 * @author YangQiang
 */
public interface IConfigDataManager {
    String DEFAULT_XML_CONFIG_FILE = "data_config.xml";
    /**
     * 获取指定类的对应指定主键的配置
     *
     * @param clz
     * @param primaryKey
     * @param <T>
     * @return
     */
    <T> T getConfig(Class<T> clz, Object... primaryKey);

    /**
     * 获取指定类的所有配置列表
     *
     * @param clz
     * @param <T>
     * @return
     */
    <T> List<T> getConfigs(Class<T> clz);

    /**
     * 获取指定类的缓存配置
     *
     * @param clz
     * @param <T>
     * @return
     */
    <T> T getConfigCache(Class<T> clz);

    /**
     * 初始化数据
     *
     * @throws Exception
     */
    void init() throws Exception;

    /**
     * 重新载入数据
     *
     * @throws Exception
     */
    default void reload() throws Exception {
        init();
    }
}
