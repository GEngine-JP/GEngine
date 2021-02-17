package info.xiaomo.gengine.persist.redis.jedis;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.HashSet;

/**
 * redis集群配置文件
 */
@Data
public class JedisClusterConfig {

    @ElementList(required = false)
    private HashSet<JedisClusterNodesConfig> nodes = new HashSet<>();
    @Element()
    private int poolMaxTotal = 500;
    @Element()
    private int poolMaxIdle = 5;
    @Element
    private int connectionTimeout = 2000;
    @Element
    private int soTimeout = 2000;
    @Element
    private int maxRedirections = 6;
    @Element
    private int timeBetweenEvictionRunsMillis = 30000;
    @Element
    private int minEvictableIdleTimeMillis = 1800000;
    @Element
    private int softMinEvictableIdleTimeMillis = 1800000;
    @Element
    private int maxWaitMillis = 60000;
    @Element
    private boolean testOnBorrow = true;
    @Element
    private boolean testWhileIdle;
    @Element
    private boolean testOnReturn;

    @Data
    public static class JedisClusterNodesConfig {

        @Element()
        private String ip;
        @Element()
        private int port;


    }
}
