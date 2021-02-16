package info.xiaomo.gengine.ai.btree;

import info.xiaomo.gengine.script.IScript;
import info.xiaomo.gengine.struct.Person;

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
