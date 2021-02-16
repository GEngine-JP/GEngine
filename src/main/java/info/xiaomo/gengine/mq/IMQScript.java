package info.xiaomo.gengine.mq;


import info.xiaomo.gengine.script.IScript;

/**
 * MQ 消息处理脚本
 *
 * 
 * <p>
 * 2017年7月28日 上午10:39:14
 */
public interface IMQScript extends IScript {

	/**
	 * ＭＱ消息接收处理
	 *
	 * @param msg
	 * 
	 * <p>
	 * 2017年7月28日 上午10:39:59
	 */
	default void onMessage(String msg) {

	}
}
