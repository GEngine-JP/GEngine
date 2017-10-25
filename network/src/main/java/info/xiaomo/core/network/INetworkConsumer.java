package info.xiaomo.core.network;

import com.google.protobuf.AbstractMessage;
import io.netty.channel.Channel;

/**
 * @author xiaomo
 */
public interface INetworkConsumer {

    /**
     * 执行具体的指令
     *
     * @param message message
     * @param channel channel
     */
    void consume(AbstractMessage message, Channel channel);


}
