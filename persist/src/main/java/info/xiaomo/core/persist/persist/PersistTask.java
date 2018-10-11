package info.xiaomo.core.persist.persist;

import info.xiaomo.core.persist.jdbc.JdbcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 持久化数据的线程任务
 *
 * @author 小莫
 */
public class PersistTask extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersistTask.class);

    /**
     * 一次批量写入最多的次数
     */
    private static final int BATCH_EXECUTE_MAX_SIZE = 300;

    /**
     * 队列锁
     */
    private final Object LOCK = new Object();

    /**
     * 需要持久化的id列表
     */
    private Map<Long, PersistType> persistMap = new HashMap<>(10);


    /**
     * 需要插入的数据的参数（每次run方法调用的时候使用，使用完毕clear）
     */
    private LinkedList<Object[]> insertParams = new LinkedList<>();

    /**
     * 需要更新的数据的参数（每次run方法调用的时候使用，使用完毕clear）
     */
    private LinkedList<Object[]> updateParams = new LinkedList<>();

    /**
     * 需要删除的数据的参数（每次run方法调用的时候使用，使用完毕clear）
     */
    private LinkedList<Object[]> deleteParams = new LinkedList<>();


    /**
     * 上一次插入失败的参数
     */
    private List<Object[]> insertErrorParams = new LinkedList<>();

    /**
     * 上一次更新失败的参数
     */
    private List<Object[]> updateErrorParams = new LinkedList<>();

    /**
     * 上一次删除失败的参数
     */
    private List<Object[]> deleteErrorParams = new LinkedList<>();


    /**
     * 插入语句
     */
    private String insertSql;

    /**
     * 更新语句
     */
    private String updateSql;

    /**
     * 删除语句
     */
    private String deleteSql;

    private JdbcTemplate template;

    private PersistableCache cache;

    private PersistFactory persistFactory;

    public PersistTask(JdbcTemplate template, PersistFactory persistFactory, PersistableCache cache) {
        this.template = template;
        this.cache = cache;
        this.persistFactory = persistFactory;
        insertSql = this.persistFactory.createInsertSql();
        updateSql = this.persistFactory.createUpdateSql();
        deleteSql = this.persistFactory.createDeleteSql();
    }

    public void add(Persistable obj, PersistType persistType) {
        if (obj == null) {
            return;
        }
        synchronized (LOCK) {
            if (persistType == PersistType.INSERT) {
                persistMap.put(obj.getId(), persistType);
            } else if (persistType == PersistType.UPDATE) {
                // 1、该数据已经有更新标志的话不增加更新标志，2、该数据已经删除或者还未入库，不增加更新标志
                persistMap.putIfAbsent(obj.getId(), persistType);
            } else if (persistType == PersistType.DELETE) {
                PersistType type = persistMap.get(obj.getId());
                // 如果当前数据还未入库，那么不必入库了，直接从缓存中删除
                if (type != null && type == PersistType.INSERT) {
                    persistMap.remove(obj.getId());
                } else {
                    // 否则设置删除标志
                    persistMap.put(obj.getId(), PersistType.DELETE);
                }
            } else {
                throw new RuntimeException("不支持的数据持久化类型dataid=" + obj.getId());
            }
            obj.setDirty(true);
        }
    }

    @Override
    public void run() {
        try {
            //弥补上次保存错误的数据
            rescueErrorUpdate();

            //插入本次数据
            Map<Long, PersistType> cloneMap = new HashMap<>(10);
            synchronized (LOCK) {
                cloneMap.putAll(persistMap);
                persistMap.clear();
            }

            cloneMap.entrySet().stream().filter(longPersistTypeEntry -> {
                //检查是否已经达到300条，然后执行批量更新
                checkAndUpdate();
                if (longPersistTypeEntry == null) {
                    return false;
                }
                Long id = longPersistTypeEntry.getKey();
                if (id == null) {
                    return false;
                }
                PersistType type = longPersistTypeEntry.getValue();
                if (type == null) {
                    return false;
                }
                Persistable data = cache.get(id);
                return data != null;
            }).forEach(longPersistTypeEntry -> {
                Long id = longPersistTypeEntry.getKey();
                PersistType type = longPersistTypeEntry.getValue();
                Persistable data = cache.get(id);
                if (type == PersistType.UPDATE) {
                    updateParams.add(persistFactory.createUpdateParameters(data));
                    data.setDirty(false);
                } else if (type == PersistType.INSERT) {
                    insertParams.add(persistFactory.createInsertParameters(data));
                    data.setDirty(false);
                } else if (type == PersistType.DELETE) {
                    deleteParams.add(persistFactory.createDeleteParameters(data));
                }
            });
            //执行剩下的数据更新操作
            finallyUpdate();
        } catch (Throwable e) {
            LOGGER.error("持久化任务执行失败", e);
        }
    }

    private void rescueErrorUpdate() {
        //弥补上次失败的数据
        if (!insertErrorParams.isEmpty()) {
            batchDbData(insertSql, insertErrorParams);
        }

        if (!updateErrorParams.isEmpty()) {
            batchDbData(updateSql, updateErrorParams);
        }

        if (!deleteErrorParams.isEmpty()) {
            batchDbData(deleteSql, deleteErrorParams);
        }
    }

    private void checkAndUpdate() {
        //批量更新每次只更新300条
        if (insertParams.size() > BATCH_EXECUTE_MAX_SIZE) {
            batchDbData(insertSql, insertParams);
        }

        if (updateParams.size() > BATCH_EXECUTE_MAX_SIZE) {
            batchDbData(updateSql, updateParams);
        }

        if (deleteParams.size() > BATCH_EXECUTE_MAX_SIZE) {
            batchDbData(deleteSql, deleteParams);
        }
    }

    private void finallyUpdate() {
        if (!insertParams.isEmpty()) {
            batchDbData(insertSql, insertParams);
            insertParams.clear();
        }

        if (!updateParams.isEmpty()) {
            batchDbData(updateSql, updateParams);
        }

        if (!deleteParams.isEmpty()) {
            batchDbData(deleteSql, deleteParams);
        }
    }

    private void batchDbData(String sql, List<Object[]> params) {
        try {
            template.batchUpdate(sql, params);
        } catch (Exception e) {
            LOGGER.error("#################数据库操作失败，请立马重启###################");
            LOGGER.error(sql, e);
            updateErrorParams.addAll(params);
        }
        params.clear();
    }

    public PersistFactory getPersistFactory() {
        return persistFactory;
    }

}
