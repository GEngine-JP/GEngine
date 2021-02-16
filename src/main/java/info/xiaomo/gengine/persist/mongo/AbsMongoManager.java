package info.xiaomo.gengine.persist.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import info.xiaomo.gengine.common.utils.FileUtil;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * mongodb 管理类
 *
 * 
 *  2017-04-14
 */
public abstract class AbsMongoManager {

	static Logger log = LoggerFactory.getLogger(AbsMongoManager.class);

	private MongoClient mongoClient;
	private Morphia morphia;
	private MongoClientConfig mongoConfig;

	/**
	 * 创建mongodb连接
	 *
	 * @param configPath
	 */
	public void createConnect(String configPath) {
		mongoConfig = FileUtil.getConfigXML(configPath, "mongoClientConfig.xml", MongoClientConfig.class);
		if (mongoConfig == null) {
			throw new RuntimeException(String.format("mongodb 配置文件 %s/MongoClientConfig.xml 未找到", configPath));
		}
		MongoClientURI uri = new MongoClientURI(mongoConfig.getUrl());
		mongoClient = new MongoClient(uri);
		morphia = new Morphia();
		morphia.mapPackage("");

		initDao();
	}

	public MongoClient getMongoClient() {
		return mongoClient;
	}

	public Morphia getMorphia() {
		return morphia;
	}

	public MongoClientConfig getMongoConfig() {
		return mongoConfig;
	}

	/**
	 * 初始化dao
	 */
	protected abstract void initDao();

}
