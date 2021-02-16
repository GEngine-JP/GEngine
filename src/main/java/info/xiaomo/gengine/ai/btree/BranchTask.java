package info.xiaomo.gengine.ai.btree;

import java.util.ArrayList;
import java.util.List;
import info.xiaomo.gengine.ai.btree.annotation.TaskConstraint;

/**
 * 分支任务节点<br>
 * A branch task defines a behavior tree branch, contains logic of starting or
 * running sub-branches and leaves
 *
 * @param <E> type of the blackboard object that tasks use to read or modify
 *            game state
 * @author implicit-invocation
 * @author davebaol
 */
@TaskConstraint(minChildren = 1)
public abstract class BranchTask<E> extends Task<E> {

	/**
	 * The children of this branch task.
	 */
	protected List<Task<E>> children;

	/**
	 * Create a branch task with no children
	 */
	public BranchTask() {
		this(new ArrayList<>());
	}

	/**
	 * Create a branch task with a list of children
	 *
	 * @param tasks list of this task's children, can be empty
	 */
	public BranchTask(List<Task<E>> tasks) {
		this.children = tasks;
	}

	@Override
	protected int addChildToTask(Task<E> child) {
		children.add(child);
		return children.size() - 1;
	}

	@Override
	public int getChildCount() {
		return children.size();
	}

	@Override
	public Task<E> getChild(int i) {
		return children.get(i);
	}

	@Override
	public void reset() {
		children.clear();
		super.reset();
	}

}
