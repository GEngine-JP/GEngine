package info.xiaomo.core.persist.redis.jedis;

import com.alibaba.fastjson.JSON;
import info.xiaomo.core.persist.redis.IPubSubScript;
import info.xiaomo.core.script.ScriptManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;

/**
 * Redis 监听事件
 *
 * @author JiangZhiYong
 * 2017年7月10日 下午2:00:34
 */
public class JedisPubListener extends JedisPubSub implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(JedisPubListener.class);

	private final String[] channels;

	public JedisPubListener(String... channels) {
		this.channels = channels;
	}

	@Override
	public void onMessage(String channel, String message) {
		try {
			JedisPubSubMessage jedisPubSubMessage = JSON.parseObject(message, JedisPubSubMessage.class);
			if (jedisPubSubMessage != null) {
				ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IPubSubScript.class,
						script -> script.onMessage(channel, jedisPubSubMessage));
			}

		} catch (Exception e) {
			LOGGER.error("JedisPubListener", e);
		}
	}

	@Override
	public void onSubscribe(String channel, int subscribedChannels) {
		super.onSubscribe(channel, subscribedChannels);
		LOGGER.info("onSubscribe：{},{}", channel, subscribedChannels);
	}

	public void start() {
		Thread thread = new Thread(this, "JedisPubSub");
		thread.start();
	}

	public void stop() {
		unsubscribe();
	}

	@Override
	public void run() {
		try {
			if (channels != null && channels.length > 0) {
				JedisManager.getJedisCluster().subscribe(this, channels);
			}
		} catch (Exception e) {
			LOGGER.error(null, e);
		}
	}

}
