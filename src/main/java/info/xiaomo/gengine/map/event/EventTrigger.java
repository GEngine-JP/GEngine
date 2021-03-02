package info.xiaomo.gengine.map.event;


import info.xiaomo.gengine.map.AbstractGameMap;
import info.xiaomo.gengine.map.obj.Performer;

/**
 * 场景事件的触发器接口
 *
 * @author 张力
 * @date 2014-12-11 下午4:07:37
 */
public abstract class EventTrigger {

	public static final int NONE = 0; // 无此类型
	public static final int DEFAULT = 1;//默认触发（无条件触发）


	protected int triggerType;

	public EventTrigger(int triggerType) {
		this.triggerType = triggerType;
	}

	/**
	 * 检查触发条件
	 *
	 * @param event
	 * @return
	 */
	public abstract boolean check(AbstractGameMap map, MapEvent event, Performer player);

	public int getTriggerType() {
		return triggerType;
	}

}
