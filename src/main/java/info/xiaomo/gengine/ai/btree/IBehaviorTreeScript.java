package info.xiaomo.gengine.ai.btree;

import info.xiaomo.gengine.bean.AbsPerson;
import info.xiaomo.gengine.script.IScript;

/**
 * 行为树脚本
 *
 *
 * <p>
 * 2017年11月
 */
public interface IBehaviorTreeScript extends IScript {

	/**
	 * 为对象添加行为树
	 *
	 * @param person
	 *
	 */
	default void addBehaviorTree(AbsPerson person) {

	}
}
