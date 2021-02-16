package info.xiaomo.gengine.ai.btree.branch;

import java.util.Arrays;
import java.util.List;
import info.xiaomo.gengine.ai.btree.SingleRunningChildBranch;
import info.xiaomo.gengine.ai.btree.Task;

/**
 * 选择节点<br>
 * A {@code Selector} is a branch task that runs every children until one of
 * them succeeds. If a child task fails, the selector will start and run the
 * next child task.
 *
 * @param <E> type of the blackboard object that tasks use to read or modify
 *            game state
 * @author implicit-invocation
 */
public class Selector<E> extends SingleRunningChildBranch<E> {

	/**
	 * Creates a {@code Selector} branch with no children.
	 */
	public Selector() {
		super();
	}

	/**
	 * Creates a {@code Selector} branch with the given children.
	 *
	 * @param tasks the children of this task
	 */
	@SafeVarargs
	public Selector(Task<E>... tasks) {
		super(Arrays.asList(tasks));
	}

	/**
	 * Creates a {@code Selector} branch with the given children.
	 *
	 * @param tasks the children of this task
	 */
	public Selector(List<Task<E>> tasks) {
		super(tasks);
	}

	@Override
	public void childFail(Task<E> runningTask) {
		super.childFail(runningTask);
		if (++currentChildIndex < children.size()) {
			run(); // Run next child
		} else {
			fail(); // All children processed, return failure status
		}
	}

	@Override
	public void childSuccess(Task<E> runningTask) {
		super.childSuccess(runningTask);
		success(); // Return success status when a child says it succeeded
	}

}
