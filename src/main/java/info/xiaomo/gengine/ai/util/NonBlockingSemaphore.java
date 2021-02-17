
package info.xiaomo.gengine.ai.util;

/**
 * 非阻塞信号量<br>
 * A counting semaphore that does not block the thread when the requested
 * resource is not available. No actual resource objects are used; the semaphore
 * just keeps a count of the number available and acts accordingly.
 *
 * @author davebaol
 */
public interface NonBlockingSemaphore {

	/**
	 * Acquires a resource if available.
	 * <p>
	 * An invocation of this method yields exactly the same result as
	 * {@code acquire(1)}
	 *
	 * @return {@code true} if the resource has been acquired; {@code false}
	 * otherwise.
	 */
	public boolean acquire();

	/**
	 * Acquires the specified number of resources if they all are available.
	 *
	 * @return {@code true} if all the requested resources have been acquired;
	 * {@code false} otherwise.
	 */
	public boolean acquire(int resources);

	/**
	 * Releases a resource returning it to this semaphore.
	 * <p>
	 * An invocation of this method yields exactly the same result as
	 * {@code release(1)}
	 *
	 * @return {@code true} if the resource has been released; {@code false}
	 * otherwise.
	 */
	public boolean release();

	/**
	 * Releases the specified number of resources returning it to this semaphore.
	 *
	 * @return {@code true} if all the requested resources have been released;
	 * {@code false} otherwise.
	 */
	public boolean release(int resources);

	/**
	 * Abstract factory for creating concrete instances of classes implementing
	 * {@link NonBlockingSemaphore}.
	 *
	 * @author davebaol
	 */
	public interface Factory {

		/**
		 * Creates a semaphore with the specified name and resources.
		 *
		 * @param name         the name of the semaphore
		 * @param maxResources the maximum number of resource
		 * @return the newly created semaphore.
		 */
		public NonBlockingSemaphore createSemaphore(String name, int maxResources);
	}

}
