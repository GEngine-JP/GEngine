package info.xiaomo.gengine.network;

import io.netty.channel.Channel;

/**
 * @author xiaomo
 */
public interface INetworkConsumer {

    /**
     * 执行具体的指令
     *
     * @param msg msg
     * @param channel channel
     */
    void consume(MsgPack msg, Channel channel);


}
