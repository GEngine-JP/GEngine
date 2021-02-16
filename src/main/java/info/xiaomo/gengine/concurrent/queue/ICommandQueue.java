package info.xiaomo.gengine.concurrent.queue;

/**
 * 任务队列接口</br>
 * 所有实现该接口的队列都应该自己保证其线程安全
 *
 * @param <V>
 * @author Administrator
 */
public interface ICommandQueue<V> {

    /**
     * 下一执行命令
     *
     * @return V
     */
    V poll();

    /**
     * 增加执行指令
     *
     * @param value value
     * @return boolean
     */
    boolean offer(V value);

    /**
     * 清理
     */
    void clear();

    /**
     * 获取指令数量
     *
     * @return int
     */
    int size();

    /**
     * 是否在运行
     *
     * @return boolean
     */
    boolean isRunning();

    /**
     * 设置运行状态
     *
     * @param running running
     */
    void setRunning(boolean running);

    /**
     * 设置名字
     *
     * @param name name
     */
    void setName(String name);

    /**
     * 获取名字
     *
     * @return String
     */
    String getName();
}
