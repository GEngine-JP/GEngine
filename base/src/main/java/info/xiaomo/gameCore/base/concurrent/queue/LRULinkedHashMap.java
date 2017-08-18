package info.xiaomo.gameCore.base.concurrent.queue;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LRULinkedHashMap<K, V> extends LinkedHashMap<K, V> {

	private static final long serialVersionUID = -3791412708654730531L;

	//最大数量
	private int max = 16;

	/**
	 * 初始容量
	 */
	private static final int START_NUMBER = 16;

	/**
	 * 容量因子，超过0.75倍之后，将调整Map的容量
	 */
	private static final float DEFAULT_LOAD_FACTOR = 0.75F;

	private Lock lock = new ReentrantLock();

	public LRULinkedHashMap(int max) {
		super(START_NUMBER, DEFAULT_LOAD_FACTOR, true);
		this.max = max;
	}

	/**
	 * 如果此映射移除其最旧的条目，则返回 true，此处如果Map的size大于最大数量了，就返回true
	 * 这个可以实现LURMap
	 */
	@Override
	public boolean removeEldestEntry(Map.Entry<K, V> eldest) {
		return size() > this.max;
	}

	@Override
	public V get(Object k) {
		try {
			this.lock.lock();
			return super.get(k);
		} finally {
			this.lock.unlock();
		}
	}

	@Override
	public V put(K key, V value) {
		try {
			this.lock.lock();
			return super.put(key, value);
		} finally {
			this.lock.unlock();
		}
	}

	@Override
	public V remove(Object key) {
		try {
			this.lock.lock();
			return super.remove(key);
		} finally {
			this.lock.unlock();
		}
	}
}
