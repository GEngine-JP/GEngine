package info.xiaomo.core.concurrent.queue;

import java.util.ArrayDeque;
import java.util.Queue;


/**
 * 无锁的命令队列 线程非安全
 *
 * @param <V>
 */
public class UnlockedCommandQueue<V> extends CommandQueue<V> {
    public UnlockedCommandQueue() {
        super(new ArrayDeque<>());
    }

    public UnlockedCommandQueue(String name) {
        super(name, new ArrayDeque<>());
    }

    public UnlockedCommandQueue(int size) {
        super(new ArrayDeque<>(size));
    }

    public UnlockedCommandQueue(String name, int size) {
        super(name, new ArrayDeque<>(size));
    }
}
