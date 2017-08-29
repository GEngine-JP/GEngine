package info.xiaomo.gameCore.protocol;

import com.google.protobuf.AbstractMessage;
import io.netty.channel.Channel;

public interface NetworkConsumer {

    /**
     * 执行具体的指令
     *
     * @param message message
     * @param channel channel
     */
    void consume(AbstractMessage message, Channel channel);


}
