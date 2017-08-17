package info.xiaomo.gameCore.protocol;

import io.netty.channel.Channel;

/**
 * 网络消费者，消费网络请求
 *
 * @author 张力
 */
public interface NetworkConsumer {

    void consume(Channel channel, AbstractHandler handler);

}
