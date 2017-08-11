/**
 * 工 程 名:  game-core
 * 创建日期:  2017年07月17日 17:48
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.core.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 简单的线程工厂
 *
 * @author 杨 强
 */
public class SimpleThreadFactory implements ThreadFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleThreadFactory.class);
    private final AtomicInteger count = new AtomicInteger(0);
    private String name;

    public SimpleThreadFactory(String name) {
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r) {
        int curCount = count.incrementAndGet();
        String threadName = name + "-" + curCount;
        LOGGER.info("创建线程:" + threadName);
        return new Thread(r, threadName);
    }
}
