package info.xiaomo.gengine.persist.redis;


import info.xiaomo.gengine.persist.redis.jedis.JedisPubSubMessage;
import info.xiaomo.gengine.script.IScript;

/**
 * 订阅消息处理器
 *
 * 
 * <p>
 * 2017年7月10日 上午10:29:29
 */
public interface IPubSubScript extends IScript {

	/**
	 * 消息处理
	 *
	 * @param channel
	 * @param message
	 */
	void onMessage(String channel, JedisPubSubMessage message);
}
