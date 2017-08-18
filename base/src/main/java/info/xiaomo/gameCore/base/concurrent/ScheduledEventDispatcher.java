package info.xiaomo.gameCore.base.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;


/**
 * 场景定时事件派发器. 该分发器会按照设置好的周期和间隔将事件派发到场景驱动器中
 * 
 * @author 张力
 * @date 2015-3-11 上午5:49:11
 * 
 */
public class ScheduledEventDispatcher implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledEventDispatcher.class);
	
	/**
	 * 事件列表
	 */
	private final List<ScheduledEvent> events = new ArrayList<>();

	/**
	 * 场景id
	 */
	private long stageId;

	/**
	 * 该派发器所属场景的驱动器
	 */
	private Driver driver;
	

	/**
	 * 该派发器在线程池中的执行句柄
	 */
	private Future<?> future;
	
	public ScheduledEventDispatcher(Driver driver, long stageId) {
		this.stageId = stageId;
		this.driver = driver;
	}
	
	/**
	 * 添加定时事件
	 * 
	 * @param event
	 *            定时事件
	 */
	public void addTimerEvent(ScheduledEvent event) {
		synchronized (this.events) {
			this.events.add(event);
		}
		LOGGER.debug("ScheduledEvent事件:addScheduledEvent=" + this.stageId + "=event=" + event.getClass().getName());
	}

	/**
	 * 移除定时事件
	 * 
	 * @param event
	 *            定时事件
	 */
	public void removeTimerEvent(ScheduledEvent event) {
		synchronized (this.events) {
			this.events.remove(event);
			LOGGER.debug("ScheduledEvent事件:ScheduledEvent=remove");
		}
	}
	
	/**
	 * 清除定时事件
	 */
	public void clearTimerEvent(){
		synchronized (this.events) {
			this.events.clear();
			LOGGER.debug("ScheduledEvent事件:ScheduledEvent=clear");
		}
	}
	
	/**
	 * 停止
	 * @param mayInterruptIfRunning 是否终端正在执行的操作
	 */
	public void stop(boolean mayInterruptIfRunning){
		this.future.cancel(mayInterruptIfRunning);
	}
	
	@Override
	public void run() {
		synchronized (events) {
			Iterator<ScheduledEvent> it = this.events.iterator();
			while (it.hasNext()) {
				ScheduledEvent event = it.next();
				if (event.remain() <= 0L) {// 定时时间到
					if (event.getLoop() > 0) {
						// 设置下一个循环（同时更新了下一次的end时间）
						event.setLoop(event.getLoop() - 1);
					} else {
						// 如果循环次数为0，那么意味着无线循环
						event.setLoop(event.getLoop());
					}
					// 放入场景驱动
					this.driver.addCommand(event);
				}
				if (event.getLoop() == 0) {
					// 循环次数为0之后移除该事件
					it.remove();
					LOGGER.info(this.stageId + "移除定时事件：" + event.getClass().getName());
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
