package info.xiaomo.gengine.script;

import java.util.List;

import org.mongodb.morphia.annotations.Entity;

/**
 * 配置加载脚本
 * <p>
 * <p>
 * 2017年10月12日 下午2:34:03
 */
public interface IConfigScript extends IScript {

    /**
     * 加载配置
     * <p>
     * <p>
     * 2017年10月12日 下午2:35:19
     *
     * @param tableName 指定的配置表 null加载所有
     * @return String
     */
    default String reloadConfig(List<String> tableName) {
        return "未加载任何配置";
    }

    /**
     * 是否包含加载表
     * <p>
     * <p>
     * 2017年10月18日 下午4:15:17
     *
     * @param tables tables
     * @param clazz  clazz
     * @return boolean
     */
    default boolean containTable(List<String> tables, Class<?> clazz) {
        if (tables == null || tables.isEmpty()) {
            return true;
        }
        Entity entity = clazz.getAnnotation(Entity.class);
        return entity != null && tables.contains(entity.value());
    }
}
