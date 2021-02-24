/** 创建日期: 2017年08月22日 10:24 创建作者: 杨 强 <281455776@qq.com> */
package info.xiaomo.gengine.config;

import java.util.List;

/**
 * 配置容器接口
 *
 * @author YangQiang
 */
public interface IConfigContainer {
    /**
     * 获取配置
     *
     * @param key key
     * @param <T> <T>
     * @return <T>
     */
    <T> T getConfig(Object key);

    /**
     * 获取list
     *
     * @param <T> <T>
     * @return <T>
     */
    <T> List<T> getList();
}
