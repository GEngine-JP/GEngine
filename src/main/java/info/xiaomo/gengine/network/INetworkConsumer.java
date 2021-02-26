package info.xiaomo.gengine.network;

import com.google.protobuf.Message;
import io.netty.channel.Channel;
/** @author xiaomo */
public interface INetworkConsumer {

    /**
     * 执行具体的指令
     *
     * @param msg msg
     * @param channel channel
     */
    void consume(Message msg, Channel channel);
}
