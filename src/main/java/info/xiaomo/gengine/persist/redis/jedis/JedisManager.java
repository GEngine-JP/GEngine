package info.xiaomo.gengine.persist.redis.jedis;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import info.xiaomo.gengine.utils.FileUtil;
import info.xiaomo.gengine.utils.JsonUtil;
import info.xiaomo.gengine.utils.YamlUtil;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
 * redis集群管理类
 *
 * <p>
 *
 * <p>2017年8月18日 下午5:32:34
 */
public class JedisManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(JedisManager.class);
    private static JedisCluster jedisCluster;
    private static JedisManager redisManager;

    private Map<String, String> keysShaMap; // key:脚本名称

    /** @param configPath redis配置文件路径 */
    public JedisManager(String configPath) {
        this(loadJedisClusterConfig(configPath));
    }

    public JedisManager(JedisClusterConfig config) {
        HashSet<HostAndPort> jedisClusterNodes = new HashSet<>();
        config.getNodes()
                .forEach(
                        node -> {
                            if (node == null) {
                                return;
                            }
                            try {
                                if (node.getIp() != null && node.getIp().length() > 5) {
                                    jedisClusterNodes.add(
                                            new HostAndPort(node.getIp(), node.getPort()));
                                }
                            } catch (Exception e) {
                                LOGGER.error(node.toString(), e);
                            }
                        });
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(config.getPoolMaxTotal());
        poolConfig.setMaxIdle(config.getPoolMaxIdle());
        poolConfig.setMaxWaitMillis(config.getMaxWaitMillis());
        poolConfig.setTimeBetweenEvictionRunsMillis(config.getTimeBetweenEvictionRunsMillis());
        poolConfig.setMinEvictableIdleTimeMillis(config.getMinEvictableIdleTimeMillis());
        poolConfig.setSoftMinEvictableIdleTimeMillis(config.getSoftMinEvictableIdleTimeMillis());
        poolConfig.setTestOnBorrow(config.isTestOnBorrow());
        poolConfig.setTestWhileIdle(config.isTestWhileIdle());
        poolConfig.setTestOnReturn(config.isTestOnReturn());
        jedisCluster =
                new JedisCluster(
                        jedisClusterNodes,
                        config.getConnectionTimeout(),
                        config.getSoTimeout(),
                        config.getMaxRedirections(),
                        poolConfig);
    }

    private static JedisClusterConfig loadJedisClusterConfig(String configPath) {
        JedisClusterConfig jedisClusterConfig =
                YamlUtil.read(configPath + "jedisClusterConfig.yml", JedisClusterConfig.class);
        if (jedisClusterConfig == null) {
            LOGGER.error("redis配置{}未找到", configPath);
            System.exit(1);
        }
        return jedisClusterConfig;
    }

    public static JedisCluster getJedisCluster() {
        return jedisCluster;
    }

    public static JedisManager getInstance() {
        return redisManager;
    }

    public static void setRedisManager(JedisManager redisManager) {
        JedisManager.redisManager = redisManager;
    }

    /**
     * 初始化脚本
     *
     * <p>
     *
     * <p>2017年8月7日 下午6:18:37 脚本路径
     */
    public void initScript(String configPath) {
        try {
            String path = configPath + File.separator + "lua"; // lua脚本路径
            List<File> sources = new ArrayList<>();
            FileUtil.getFiles(path, sources, ".lua", null);
            if (sources.size() < 1) {
                LOGGER.warn("{}目录无任何lua脚本", configPath);
                return;
            }
            for (File file : sources) {
                String fileName = file.getName().substring(0, file.getName().indexOf("."));
                scriptFlush(fileName);
                loadScript(path, fileName);
            }

        } catch (Exception e) {
            LOGGER.error("redis 脚本", e);
        }
    }

    /** 清除脚本缓存 */
    public void scriptFlush(String fileName) {
        getJedisCluster().scriptFlush(fileName.getBytes());
    }

    /**
     * 初始化脚本
     *
     * @param path 脚本所在路径
     * @param fileName 脚本文件名称
     */
    public void loadScript(String path, String fileName) throws Exception {
        String script = FileUtil.readTxtFile(path + File.separator, fileName + ".lua");
        if (script == null || script.length() < 1) {
            throw new Exception(path + "/" + fileName + ".lua 加载出错");
        }
        String hash = getJedisCluster().scriptLoad(script, fileName);
        if (hash == null || hash.length() < 1) {
            throw new Exception(fileName + ".lua 脚本注入出错");
        }
        if (keysShaMap == null) {
            keysShaMap = new HashMap<>();
        }
        LOGGER.debug("Redis脚本：{}-->{}", fileName, hash);
        keysShaMap.put(fileName, hash);
    }

    /**
     * 获取脚本 sha
     *
     * @param fileName 脚本名称
     *     <p>2017年8月7日 下午6:05:24
     */
    private String getSha(String fileName) {
        if (keysShaMap.containsKey(fileName)) {
            return keysShaMap.get(fileName);
        }
        LOGGER.warn(String.format("脚本 %s没初始化", fileName));
        return null;
    }

    /**
     * 执行脚本
     *
     * @param scriptName 脚本文件名称
     * @param keys redis key列表
     * @param args 参数集合
     * @return 2017年8月7日 下午6:10:31
     */
    @SuppressWarnings("unchecked")
    public <T> T executeScript(String scriptName, List<String> keys, List<String> args) {
        String sha = getSha(scriptName);
        if (sha == null) {
            return null;
        }
        Object object = getJedisCluster().evalsha(sha, keys, args);
        if (object == null) {
            return null;
        }
        return (T) object;
    }

    /**
     * 获取所有map对象
     *
     * @param key
     * @param keyClass
     * @param valueClass
     * @return 2017年10月24日 上午10:05:43
     */
    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> hgetAll(
            final String key, final Class<K> keyClass, final Class<V> valueClass) {
        Map<String, String> hgetAll = getJedisCluster().hgetAll(key);
        if (hgetAll == null) {
            return null;
        }
        Map<K, V> map = new ConcurrentHashMap<>();
        hgetAll.forEach(
                (k, v) -> {
                    map.put((K) parseKey(k, keyClass), JsonUtil.parseObject(v, valueClass));
                });
        return map;
    }

    /**
     * 获取map指定属性对象
     *
     * @param key
     * @param field
     * @return 2017年10月24日 上午10:08:43
     */
    public <V> V hget(final String key, final Object field, Class<V> clazz) {
        String hget = getJedisCluster().hget(key, field.toString());
        if (hget == null) {
            return null;
        }
        return JsonUtil.parseObject(hget, clazz);
    }

    /**
     * 存储map对象
     *
     * @param key
     * @param field
     * @param value
     * @return
     *     <p>2017年10月24日 上午10:13:21
     */
    public Long hset(final String key, final Object field, final Object value) {
        return getJedisCluster()
                .hset(key, field.toString(), JsonUtil.toJSONStringWriteClassNameWithFiled(value));
    }

    /**
     * 解析key对象
     *
     * @param key
     * @param keyClass
     * @return 2017年10月24日 上午9:57:45
     */
    private Object parseKey(String key, Class<?> keyClass) {
        if (keyClass.isAssignableFrom(Long.class)) {
            return Long.parseLong(key);
        } else if (keyClass.isAssignableFrom(Integer.class)) {
            return Integer.parseInt(key);
        }
        return key;
    }
}
