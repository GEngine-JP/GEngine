package info.xiaomo.gameCore.base;

import info.xiaomo.gameCore.base.concurrent.AbstractCommand;
import info.xiaomo.gameCore.base.concurrent.HandlerFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 消息处理器
 * @author 张力
 * @date 2014-12-4 上午10:39:37
 *
 */
public abstract class AbstractHandler extends AbstractCommand {
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractHandler.class);

	protected Object message;
	
	/**
	 * 参数,这个参数一般来是用来存放该handler的玩家信息的
	 */
	protected Object param;
	
	private HandlerFilter filter;

	/**
	 * 具体业务逻辑
	 */
	protected abstract void execute();
	
	/**
	 * 执行消息的动作
	 */
	public void doAction(){
		if(this.filter != null){
			if(this.filter.before(this)){
				execute();
			}
			this.filter.after(this);
		} else {
			execute();
		}
		
	}
	
	private long startTime = 0;
	
	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}

	public Object getParam() {
		return param;
	}

	public void setParam(Object param) {
		this.param = param;
	}

	public void setFilter(HandlerFilter filter){
		this.filter = filter;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	
	
	
}
