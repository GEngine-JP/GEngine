/**
 * 创建日期:  2017年08月21日 17:39
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.gameCore.config;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置容器
 *
 * @author YangQiang
 */
@Data
public class ConfigContainer<T> implements IConfigContainer {
    private Map<Object, T> configMap = new HashMap<>();
    private List<T> configList = new ArrayList<>();

    public T getConfig(Object key) {
        return configMap.get(key);
    }

    public List<T> getList() {
        return configList;
    }
}
