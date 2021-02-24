package info.xiaomo.gengine.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * 配置容器
 *
 * @author YangQiang
 */
@Data
public class ConfigContainer<T> implements IConfigContainer {
    private Map<Object, T> configMap = new HashMap<>(10);
    private List<T> configList = new ArrayList<>();

    @SuppressWarnings("unchecked")
    @Override
    public T getConfig(Object key) {
        return configMap.get(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> getList() {
        return configList;
    }
}
