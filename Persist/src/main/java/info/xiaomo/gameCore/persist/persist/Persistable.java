package info.xiaomo.gameCore.persist.persist;

/**
 * 可持久化的数据接口
 * @author 张力
 *
 */
public interface Persistable extends CacheAble {
	
	boolean isDirty();
	
	void setDirty(boolean dirty);
	
}
