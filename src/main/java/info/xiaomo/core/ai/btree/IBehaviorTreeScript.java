package info.xiaomo.core.ai.btree;

import info.xiaomo.core.script.IScript;
import info.xiaomo.core.struct.Person;

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
	default void addBehaviorTree(Person person) {

	}
}
