package info.xiaomo.gengine.persist.mysql;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import info.xiaomo.gengine.persist.mysql.jdbc.JdbcTemplate;
import info.xiaomo.gengine.persist.mysql.persist.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据管理类，主要是指玩家游戏数据，保存方式为二进制
 *
 * @author 张力
 */
public class MysqlDataProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(MysqlDataProvider.class);

	/**
	 * 线程池线程个数
	 */
	private static final int EXECUTOR_CORE_POOL_SIZE = 8;

	/**
	 * 持久化任务map
	 */
	private final Map<Integer, PersistTask> persistTaskMap = new HashMap<>(10);

	private final Map<Integer, PersistAbleCache> cacheMap = new HashMap<>(10);

	/**
	 * JDBC模板类
	 */
	private JdbcTemplate template;

	/**
	 * 持久化线程池
	 */
	private ScheduledThreadPoolExecutor executor;


	public void init(JdbcTemplate template) {

		this.template = template;
		executor = new ScheduledThreadPoolExecutor(EXECUTOR_CORE_POOL_SIZE, new ThreadFactory() {
			final AtomicInteger count = new AtomicInteger(0);

			@Override
			public Thread newThread(Runnable r) {
				int curCount = count.incrementAndGet();
				return new Thread(r, "数据库持久化线程（线程池）-" + curCount);
			}
		});
	}

	/**
	 * 注册一个持久化任务
	 *
	 * @param persistFactory persistFactory
	 */
	public void registerPersistTask(PersistFactory persistFactory) {
		PersistAbleCache cache = new PersistAbleCache(this.template, 10000);
		cacheMap.put(persistFactory.dataType(), cache);
		PersistTask task = new PersistTask(template, persistFactory, cache);
		persistTaskMap.put(persistFactory.dataType(), task);
		executor.scheduleAtFixedRate(task, 10 * 1000, persistFactory.taskPeriod(), TimeUnit.MILLISECONDS);
	}

	/**
	 * 持久化一条数据
	 *
	 * @param data data
	 * @param type type
	 */
	public void persist(PersistAble data, PersistType type) {
		PersistTask task = persistTaskMap.get(data.dataType());
		task.add(data, type);
	}

	/**
	 * 当前数据数量
	 *
	 * @return int
	 */
	public int size(int dataType) {
		return cacheMap.get(dataType).size();
	}

	/**
	 * 结束持久化任何，并且存储尚未更新的数据
	 */
	public void store() {

		executor.shutdown();
		int t = 120;
		try {
			while (t > 0 && !executor.awaitTermination(1, TimeUnit.SECONDS)) {
				t--;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		try {
			for (PersistTask task : persistTaskMap.values()) {
				task.start();
			}
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

	/**
	 * 从缓存中获取一条数据
	 *
	 * @param id
	 * @return T
	 */
	public <T> T get(long id, int dataType) {
		return cacheMap.get(dataType).get(id);
	}

	/**
	 * 放入缓存数据
	 *
	 * @param obj obj
	 */
	public void put(PersistAble obj) {
		cacheMap.get(obj.dataType()).put(obj);
	}

	/**
	 * 保存一条数据
	 *
	 * @param dataId dataId
	 */
	public void update(long dataId, int dataType, boolean immediately) {
		PersistAble obj = cacheMap.get(dataType).get(dataId);
		if (obj == null) {
			return;
		}
		if (immediately) {
			PersistFactory factory = getFactory(obj.dataType());
			if (factory == null) {
				return;
			}
			try {
				template.update(factory.createUpdateSql(), factory.createUpdateParameters(obj));
			} catch (Exception e) {
				LOGGER.error("立即更新数据库失败,id:" + obj.getId(), e);
			}
		} else {
			persist(obj, PersistType.UPDATE);
		}

	}

	/**
	 * 插入一条数据.<br/>
	 * 该数据会添加进缓存，并且存入数据库.<br/>
	 * 注意：该方法是异步写入数据库.<br/>
	 */
	public void insert(PersistAble obj, boolean immediately) {
		if (obj == null) {
			return;
		}

		PersistAbleCache cache = cacheMap.get(obj.dataType());
		if (immediately) {
			PersistFactory factory = getFactory(obj.dataType());
			if (factory == null) {
				return;
			}
			// 实时写入数据库
			try {
				template.update(factory.createInsertSql(), factory.createInsertParameters(obj));
			} catch (Exception e) {
				LOGGER.error("立即写入数据库失败,id:" + obj.getId(), e);
			}
			cache.put(obj);
		} else {
			cache.put(obj);
			// 添加到写队列中
			persist(obj, PersistType.INSERT);
		}
	}

	/**
	 * 从缓存中移除数据
	 *
	 * @param id id
	 * @return PersistAble
	 */
	public PersistAble removeFromCache(long id, int dataType) {
		return cacheMap.get(dataType).remove(id);
	}

	/**
	 * 从缓存中移除数据，并且延时从数据库中删除
	 *
	 * @param id id
	 * @return PersistAble
	 */
	public PersistAble removeFromDisk(long id, int dataType, boolean immediately) {

		PersistAble obj = cacheMap.get(dataType).remove(id);
		if (obj == null) {
			return null;
		}
		if (immediately) {
			PersistFactory factory = getFactory(obj.dataType());
			if (factory == null) {
				throw new RuntimeException("找不到持久化工厂类:" + obj.dataType());
			}
			try {
				template.update(factory.createDeleteSql(), factory.createDeleteParameters(obj));
			} catch (Exception e) {
				LOGGER.error("立即删除数据库失败,id:" + obj.getId(), e);
			}
		} else {
			persist(obj, PersistType.DELETE);
		}

		return obj;
	}

	private PersistFactory getFactory(int dataType) {
		PersistTask task = persistTaskMap.get(dataType);
		if (task == null) {
			return null;
		}
		return task.getPersistFactory();
	}

}
