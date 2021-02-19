package info.xiaomo.gengine.thread;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池配置
 * <p>
 * <p>
 * 2017-03-30
 */
@Root
@Data
public class ThreadPoolExecutorConfig {

    // 线程池名称
    @Element(required = false)
    private String name;

    // 核心线程池值
    @Element()
    private int corePoolSize = 20;

    // 线程池最大值
    @Element()
    private int maxPoolSize = 200;

    // 线程池保持活跃时间(秒)
    @Element()
    private long keepAliveTime = 30L;

    // 心跳间隔（大于0开启定时监测线程）
    @Element(required = false)
    private int heart;

    // 缓存命令数
    @Element(required = false)
    private int commandSize = 100000;

    public ThreadPoolExecutor newThreadPoolExecutor() throws RuntimeException {
        return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(commandSize), new IoThreadFactory());
    }

}
