package info.xiaomo.core.persist.persist;

/**
 * 
 * 数据持久化构造器
 * @author 张力
 *
 */
public interface PersistFactory {
	
	String tableName();
	
	int dataType();
	
	String createCreateSql();
	String createInsertSql();
	String createUpdateSql();
	String createDeleteSql();
	
	Object[] createInsertParameters(Persistable obj);
	Object[] createUpdateParameters(Persistable obj);
	Object[] createDeleteParameters(Persistable obj);
	
	long taskPeriod();
	
}
