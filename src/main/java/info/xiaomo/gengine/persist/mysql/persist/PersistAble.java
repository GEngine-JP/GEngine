package info.xiaomo.gengine.persist.mysql.persist;

/**
 * 可持久化的数据接口
 *
 * @author 张力
 */
public interface PersistAble extends CacheAble {

	/**
	 * 是否是脏数据
	 *
	 * @return boolean
	 */
	boolean isDirty();

	/**
	 * 设置是否为脏数据
     *
     * @param dirty dirty
     */
    void setDirty(boolean dirty);

}
