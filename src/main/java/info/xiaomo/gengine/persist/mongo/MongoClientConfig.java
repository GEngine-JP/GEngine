package info.xiaomo.gengine.persist.mongo;

import lombok.Data;
import org.simpleframework.xml.Element;

/**
 * Mongo配置文件
 */
@Data
public class MongoClientConfig {

	/**
	 * 数据库名字
	 */
	@Element(required = false)
	private String dbName = "lztb_hall";
	/**
	 * 数据库连接地址
	 */
	@Element(required = true)
	private String url = "mongodb://127.0.0.1:27017/?replicaSet=rs_lztb";


}
