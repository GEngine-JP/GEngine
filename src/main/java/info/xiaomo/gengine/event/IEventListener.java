package info.xiaomo.gengine.event;

/**
 * Copyright(©) 2017 by xiaomo.
 */
public interface IEventListener {

	/**
	 * 更新
	 *
	 * @param type  type
	 * @param param param
	 */
	void update(Integer type, Object param);

}
