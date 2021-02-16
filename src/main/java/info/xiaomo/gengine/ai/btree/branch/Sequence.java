package info.xiaomo.gengine.ai.btree.branch;

import java.util.Arrays;
import java.util.List;
import info.xiaomo.gengine.ai.btree.SingleRunningChildBranch;
import info.xiaomo.gengine.ai.btree.Task;

/**
 * 顺序执行任务节点<br>
 * A {@code Sequence} is a branch task that runs every children until one of them fails. If a child task succeeds, the selector
 * will start and run the next child task.
 *
 * @param <E> type of the blackboard object that tasks use to read or modify game state
 * @author implicit-invocation
 */
public class Sequence<E> extends SingleRunningChildBranch<E> {

	/**
	 * Creates a {@code Sequence} branch with no children.
	 */
	public Sequence() {
		super();
	}

	/**
	 * Creates a {@code Sequence} branch with the given children.
	 *
	 * @param tasks the children of this task
	 */
	public Sequence(List<Task<E>> tasks) {
		super(tasks);
	}

	/**
	 * Creates a {@code Sequence} branch with the given children.
	 *
	 * @param tasks the children of this task
	 */
	public Sequence(Task<E>... tasks) {
		super(Arrays.asList(tasks));
	}

	@Override
	public void childSuccess(Task<E> runningTask) {
		super.childSuccess(runningTask);
		if (++currentChildIndex < children.size()) {
			run(); // Run next child
		} else {
			success(); // All children processed, return success status
		}
	}

	@Override
	public void childFail(Task<E> runningTask) {
		super.childFail(runningTask);
		fail(); // Return failure status when a child says it failed
	}

}
