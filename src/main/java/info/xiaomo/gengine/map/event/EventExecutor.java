package info.xiaomo.gengine.map.event;


import info.xiaomo.gengine.map.AbstractGameMap;
import info.xiaomo.gengine.map.obj.Performer;

/**
 * 场景事件执行器接口
 *
 * @author 张力
 * @date 2014-12-11 下午4:07:25
 */
public abstract class EventExecutor {

	public static final int NONE = 0; // 无此类型
	public static final int TRANSMIT = 1; //传送


	protected int executorType;

	/**
	 * 执行事件
	 *
	 * @param map
	 * @param event
	 * @param player
	 */
	public abstract void execute(AbstractGameMap map, MapEvent event, Performer player);


	public EventExecutor(int executorType) {
		this.executorType = executorType;
	}

	public int getExecutorType() {
		return executorType;
	}
}
