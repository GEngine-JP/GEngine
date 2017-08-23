package info.xiaomo.gameCore.protocol;

import io.netty.channel.Channel;

public interface NetworkConsumer {

    /**
     * 执行具体的指令
     *
     * @param message message
     * @param channel channel
     * @return
     */
    void consume(Message message, Channel channel);


}
