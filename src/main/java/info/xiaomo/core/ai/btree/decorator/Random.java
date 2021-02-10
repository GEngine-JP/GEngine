/*******************************************************************************
 * Copyright 2014 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package info.xiaomo.core.ai.btree.decorator;

import info.xiaomo.core.ai.btree.Decorator;
import info.xiaomo.core.ai.btree.Task;
import info.xiaomo.core.ai.btree.annotation.TaskAttribute;
import info.xiaomo.core.ai.btree.annotation.TaskConstraint;
import info.xiaomo.core.common.math.MathUtil;

/**
 * 结果随机<br>
 * The {@code Random} decorator succeeds with the specified probability,
 * regardless of whether the wrapped task fails or succeeds. Also, the wrapped
 * task is optional, meaning that this decorator can act like a leaf task.
 * <p>
 * Notice that if success probability is 1 this task is equivalent to the
 * success probability is 0 this task is equivalent to the decorator
 *
 * @param <E> type of the blackboard object that tasks use to read or modify
 *            game state
 * @author davebaol
 * @fix JiangZhiYong
 */
@TaskConstraint(minChildren = 0, maxChildren = 1)
public class Random<E> extends Decorator<E> {
	private static final long serialVersionUID = 1L;

	private static final float INIT_PRO = 0.5f;

	/**
	 * Optional task attribute specifying the random distribution that determines
	 * the success probability. It defaults to
	 */
	@TaskAttribute
	public float success;

	private float p;

	/**
	 * Creates a {@code Random} decorator with no child that succeeds or fails with
	 * equal probability.
	 */
	public Random() {
		this(INIT_PRO);
	}

	/**
	 * Creates a {@code Random} decorator with the given child that succeeds or
	 * fails with equal probability.
	 *
	 * @param task the child task to wrap
	 */
	public Random(Task<E> task) {
		this(INIT_PRO, task);
	}

	/**
	 * Creates a {@code Random} decorator with no child that succeeds with the
	 * specified probability.
	 *
	 * @param success the random distribution that determines success probability
	 */
	public Random(float success) {
		super();
		this.success = success;
	}

	/**
	 * Creates a {@code Random} decorator with the given child that succeeds with
	 * the specified probability.
	 *
	 * @param success the random distribution that determines success probability
	 * @param task    the child task to wrap
	 */
	public Random(float success, Task<E> task) {
		super(task);
		this.success = success;
	}

	/**
	 * Draws a value from the distribution that determines the success probability.
	 * <p>
	 * This method is called when the task is entered.
	 */
	@Override
	public void start() {
		p = success;
	}

	@Override
	public void run() {
		if (child != null)
			super.run();
		else
			decide();
	}

	@Override
	public void childFail(Task<E> runningTask) {
		decide();
	}

	@Override
	public void childSuccess(Task<E> runningTask) {
		decide();
	}

	private void decide() {
		if (MathUtil.random() <= p) {
			success();
		} else {
			fail();
		}
	}


	@Override
	public void reset() {
		this.p = 0;
		this.success = INIT_PRO;
		super.reset();
	}
}
