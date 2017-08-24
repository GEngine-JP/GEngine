/**
 * 创建日期:  2017年08月21日 17:47
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.gameCore.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 抽象的配置数据管理器
 *
 * @author YangQiang
 */
public abstract class AbstractConfigDataManager implements IConfigDataManager {
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractConfigDataManager.class);
    protected Map<String, IConfigWrapper> configs = new HashMap<>();
    protected Map<String, Object> caches = new HashMap<>();

    @Override
    public <T> T getConfig(Class<T> clz, Object... primaryKey) {
        IConfigWrapper wrapper = configs.get(clz.getName());
        if (wrapper == null) {
            return null;
        }
        StringBuilder key = new StringBuilder();
        if (primaryKey != null) {
            for (int i = 0; i < primaryKey.length; i++) {
                Object tempKey = primaryKey[i];
                if (i == 0) {
                    key.append(tempKey.toString());
                } else {
                    key.append(wrapper.getKeyDelimiter()).append(tempKey.toString());
                }
            }
        }
        return (T) wrapper.getConfig(key.toString());
    }

    @Override
    public <T> List<T> getConfigs(Class<T> clz) {
        IConfigWrapper wrapper = configs.get(clz.getName());
        if (wrapper == null) {
            return null;
        }
        return wrapper.getList();
    }

    @Override
    public <T> T getConfigCache(Class<T> clz) {
        return (T) caches.get(clz);
    }
}
