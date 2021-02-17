package info.xiaomo.gengine.ai.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 非阻塞信号量仓库
 *
 * @author davebaol
 */
public class NonBlockingSemaphoreRepository {

	private static final Map<String, NonBlockingSemaphore> REPO = new HashMap<>();

	private static NonBlockingSemaphore.Factory FACTORY = new SimpleNonBlockingSemaphore.Factory();

	public static void setFactory(NonBlockingSemaphore.Factory factory) {
		FACTORY = factory;
	}

	/**
	 * 添加任务信号控制
	 *
	 * @param name         信号量名称
	 * @param maxResources 最大信号量数
	 * @return
	 */
	public static NonBlockingSemaphore addSemaphore(String name, int maxResources) {
		NonBlockingSemaphore sem = FACTORY.createSemaphore(name, maxResources);
		REPO.put(name, sem);
		return sem;
	}

	public static NonBlockingSemaphore getSemaphore(String name) {
		return REPO.get(name);
	}

	public static NonBlockingSemaphore removeSemaphore(String name) {
		return REPO.remove(name);
	}

	public static void clear() {
		REPO.clear();
	}

}
