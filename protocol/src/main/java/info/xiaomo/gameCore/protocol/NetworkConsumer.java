package info.xiaomo.gameCore.protocol;

import info.xiaomo.gameCore.protocol.message.AbstractMessage;
import io.netty.channel.Channel;

public interface NetworkConsumer {
	
	/**
	 * 执行具体的指令
	 * 
	 * @param message message
	 * @param channel channel
	 * @return
	 */
	void consume(AbstractMessage message, Channel channel);

	/**
	 * 客户端连接成功
	 * 
	 * @param channel channel
	 */
	void connected(Channel channel);

	/**
	 * 客户端断开连接
	 * 
	 * @param channel channel
	 */
	void disconnected(Channel channel);

	/**
	 * 发生异常
	 * 
	 * @param channel channel
	 * @param error error
	 */
	void exceptionOccurred(Channel channel, Throwable error);
	
	
}
