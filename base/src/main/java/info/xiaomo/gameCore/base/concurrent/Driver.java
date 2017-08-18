package info.xiaomo.gameCore.base.concurrent;

import info.xiaomo.gameCore.base.concurrent.executor.AutoSubmitExecutor;
import info.xiaomo.gameCore.base.concurrent.executor.DumpStackThread;
import info.xiaomo.gameCore.base.concurrent.executor.QueueMonitor;
import info.xiaomo.gameCore.base.concurrent.queue.ICommandQueue;
import info.xiaomo.gameCore.base.concurrent.queue.UnlockedCommandQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 游戏世界驱动器,用于驱动游戏世界中的心跳,AI等
 * @author 张力
 * @date 2015-3-10 上午4:28:26
 *
 */
public class Driver {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Driver.class);
	
	/**
	 * 队列最大数量
	 */
	private int maxQueueSize;
	
	/**
	 * 驱动名称
	 */
	private String name;
	
	/**
	 * 场景ID
	 */
	private long id;
	
	/**
	 * 任务队列
	 */
	private ICommandQueue<AbstractCommand> queue = new UnlockedCommandQueue<AbstractCommand>();
	
	/**
	 * 任务执行器
	 */
	private AutoSubmitExecutor executor;


	public Driver(AutoSubmitExecutor executor, String name, long id, int maxQueueSize, boolean monitor) {
		this.executor = executor;
		this.name = name;
		this.maxQueueSize = maxQueueSize;
		this.id = id;

		if(monitor) {
			QueueMonitor queueMonitor = QueueMonitor.newInstance(name);
			queue.setMonitor(queueMonitor);
		}

	}

	/**
	 * 添加一个命令到队列中
	 * @param command
	 */
	public boolean addCommand(AbstractCommand command){
		
		
        if(command.getQueueId() > 0 && command.getQueueId() != this.id){
        	LOGGER.error("场景驱动[" + this.name + "]-" + id + "执行到不属于本场景的命令,命令所在场景：" + command.getQueueId());
        	return false;
        }
        
        boolean result;
        synchronized (queue) {

        	int queueSize= queue.size();
            //队列中的元素已经超过允许的最大个数时，就将改队列清空，丢弃多有的指令
            if (this.maxQueueSize > 0 && queueSize > this.maxQueueSize) {
            	LOGGER.error("场景驱动[" + this.name + "]-" + id + "抛弃指令!" + Thread.currentThread().getName());
                queue.clear();
                Thread thread = new DumpStackThread();
                thread.setDaemon(true);
				thread.start();
            }

            if (queue.size() > 200) {
            	LOGGER.error("场景驱动[" + this.name + "]-" + id + "队列长度超过200<" + queue.size());
            }
            if(queueSize == 500) {
				Thread thread = new DumpStackThread();
				thread.setDaemon(true);
				thread.start();
			} else if(queueSize == 2000 ) {
				//dump堆栈
				Thread thread = new DumpStackThread();
				thread.setDaemon(true);
				thread.start();
			}

            result = queue.offer(command);
            if (result) {
                //设置command的queue属性
            	command.setCommandQueue(queue);
                if (queue.isProcessingCompleted()) {
                    //如果该队列中的所有command都已经执行完毕，那么重新启动该队列的执行
                    queue.setProcessingCompleted(false);
                    executor.execute(queue.poll());
                }
            } else {
            	LOGGER.error("队列添加任务失败");
            }
        }
        return result;
	}
	
	
	/**
	 * 获取任务队列个数
	 * @return
	 */
	public int getQueueSize(){
		return queue.size();
	}

	/**
	 * 获取最大任务个数
	 * @return
	 */
	public int getMaxQueueSize() {
		return maxQueueSize;
	}
	
	
}
