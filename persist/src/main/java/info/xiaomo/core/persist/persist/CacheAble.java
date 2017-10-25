package info.xiaomo.core.persist.persist;

/**
 * @author xiaomo
 */
public interface CacheAble {

    /**
     * 获取id
     *
     * @return long
     */
    long getId();

    /**
     * 数据类型
     * @return int
     */
    int dataType();

    /**
     * 获取服务器id
     * @return int
     */
    int getServerId();
}
