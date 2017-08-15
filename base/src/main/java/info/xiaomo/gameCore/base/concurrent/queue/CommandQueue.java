/**
 * 工 程 名:  game-core
 * 创建日期:  2017年07月17日 10:22
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.gameCore.base.concurrent.queue;

import java.util.Queue;

/**
 * 命令队列
 *
 * @author 杨 强
 */
public class CommandQueue<V> implements ICommandQueue<V> {
    protected String name;
    private final Queue<V> queue;
    private volatile boolean running = false;

    public CommandQueue(Queue<V> queue) {
        this.queue = queue;
    }

    public CommandQueue(String name, Queue<V> queue){
        this.name = name;
        this.queue = queue;
    }

    public V poll() {
        return this.queue.poll();
    }

    public boolean offer(V value) {
        return this.queue.offer(value);
    }

    public void clear() {
        this.queue.clear();
    }

    public int size() {
        return this.queue.size();
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
