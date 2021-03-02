package info.xiaomo.gengine.ai.fsm;

import info.xiaomo.gengine.map.AbstractGameMap;
import info.xiaomo.gengine.map.obj.Performer;
import lombok.extern.slf4j.Slf4j;

/**
 * 状态机中的状态
 *
 * @param <T>
 * @author 张力
 */
@Slf4j
public abstract class FSMState<T extends Performer> {


	/**
	 * 不显示在场景中，不活动
	 */
	public static final int Sleep = 1;

	/**
	 * 活动状态
	 */
	public static final int Active = 2;

	/**
	 * 战斗状态
	 */
	public static final int Fight = 3;

	/**
	 * 死亡状态
	 */
	public static final int Die = 4;

	/**
	 * 类型
	 */
	protected int type;

	/**
	 * 执行者
	 */
	protected T performer;

	public FSMState(int type, T performer) {
		this.type = type;
		this.performer = performer;
	}

	/**
	 * 进入状态
	 *
	 * @param map
	 */
	public void enter(AbstractGameMap map) {

	}

	/**
	 * 退出状态
	 *
	 * @param map
	 */
	public void exit(AbstractGameMap map) {

	}

	/**
	 * 更新状态
	 *
	 * @param map
	 * @param delta
	 */
	public void update(AbstractGameMap map, int delta) {
	}

	/**
	 * 检查状态转换
	 *
	 * @param map
	 * @return
	 */
	public int checkTransition(AbstractGameMap map) {
		return 0;
	}

	/**
	 * 拷贝原状态机休眠时间到当前状态机
	 *
	 * @param sleepTime
	 */
	public void copyTime(int sleepTime) {

	}

	/**
	 * 获取当前状态机休眠时间
	 *
	 * @return
	 */
	public int getTime() {
		return 0;
	}

}
