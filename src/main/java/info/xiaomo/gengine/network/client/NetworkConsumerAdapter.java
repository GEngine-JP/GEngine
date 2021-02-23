package info.xiaomo.gengine.network.client;

import com.google.protobuf.AbstractMessage;
import info.xiaomo.gengine.network.INetworkConsumer;
import io.netty.channel.Channel;

/**
 * 默认的网络消费者，实际什么都不干
 *
 * @author 张力
 * @date 2017/12/25 14:27
 */
public class NetworkConsumerAdapter implements INetworkConsumer {

    @Override
    public void consume(AbstractMessage msg, Channel channel) {
        // Nothing to do
    }
}
