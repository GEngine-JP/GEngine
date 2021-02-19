package info.xiaomo.gengine.ai.btree.annotation;

import java.lang.annotation.*;

/**
 * This annotation specifies how many children the task can have. It is applied to the task class.
 *
 * @author davebaol
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface TaskConstraint {

	/**
	 * Returns the minimum number of allowed children, defaults to 0.
	 *
	 * @return the minimum number of allowed children.
	 */
	int minChildren() default 0;

	/**
	 * Returns the maximum number of allowed children, defaults to {@code Integer.MAX_VALUE}.
	 *
	 * @return the maximum number of allowed children.
	 */
	int maxChildren() default Integer.MAX_VALUE;
}
