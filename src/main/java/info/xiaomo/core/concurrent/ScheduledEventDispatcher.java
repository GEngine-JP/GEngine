package info.xiaomo.core.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 定时事件派发器. </br>
 * <p>
 * 该派发器会按照设置好的周期和间隔将事件添加到CommandQueueDriver中</br>
 */
public class ScheduledEventDispatcher implements Command {
    /**
     * 派发器名字
     */
    private String name;
    /**
     * 事件列表
     */
    private List<ScheduledEvent> events = new LinkedList<>();

    /**
     * 命令队列驱动器
     */
    private CommandQueueDriver commandQueueDriver;

    /**
     * 该派发器在线程池中的执行句柄
     */
    private Future future;

    public ScheduledEventDispatcher(String name, CommandQueueDriver commandQueueDriver) {
        this.name = name;
        this.commandQueueDriver = commandQueueDriver;
    }

    /**
     * 添加定时事件
     *
     * @param event 定时事件
     */
    public void addScheduledEvent(ScheduledEvent event) {
        synchronized (events) {
            events.add(event);
        }
        LOGGER.debug("【{}】定时事件派发器添加定时事件【{}】", name, event.getClass().getName());
    }

    /**
     * 移除定时事件
     *
     * @param event 定时事件
     */
    public void removeScheduledEvent(ScheduledEvent event) {
        synchronized (events) {
            events.remove(event);
        }
        LOGGER.debug("【{}】定时事件派发器移除定时事件【{}】", name, event.getClass().getName());
    }

    /**
     * 清除定时事件
     */
    public void clearScheduledEvent() {
        synchronized (events) {
            events.clear();
        }
        LOGGER.debug("【{}】定时任务派发器清空事件", name);
    }

    /**
     * 停止
     *
     * @param mayInterruptIfRunning 是否终端正在执行的操作
     */
    public void stop(boolean mayInterruptIfRunning) {
        if (future == null){
            return;
        }
        future.cancel(mayInterruptIfRunning);
    }

    @Override
    public void action() {
        synchronized (events) {
            ScheduledEvent[] eventArray = events.toArray(new ScheduledEvent[0]);
            if (eventArray == null) {
                return;
            }
            for (ScheduledEvent event : eventArray) {
                // 定时时间到
                if (event.remain() <= 0L) {
                    if (event.getLoop() > 0) {
                        // 更新任务循环次数并设置下次定时时间
                        event.setLoop(event.getLoop() - 1);
                    } else {
                        // 循环次数小于等于0都会无限循环执行下去
                        event.setLoop(event.getLoop());
                    }

                    // 提交到任务队列中
                    commandQueueDriver.submit(event);

                    if (event.getLoop() == 0) {
                        LOGGER.info("【{}】定时任务派发器移除已经完成的任务【{}】", name, event.getClass().getName());
                        removeScheduledEvent(event);
                    }
                }
            }
        }
    }

    public Future<?> getFuture() {
        return future;
    }

    public void setFuture(Future<?> future) {
        this.future = future;
    }
}
