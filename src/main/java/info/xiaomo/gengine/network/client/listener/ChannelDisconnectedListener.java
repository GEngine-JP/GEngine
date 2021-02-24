package info.xiaomo.gengine.network.client.listener;

import java.util.concurrent.TimeUnit;
import info.xiaomo.gengine.network.client.Client;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Created by 张力 on 2017/6/30. */
public class ChannelDisconnectedListener implements ChannelFutureListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelDisconnectedListener.class);

    private final Client client;

    private final int index;

    public ChannelDisconnectedListener(Client client, int index) {
        this.client = client;
        this.index = index;
    }

    @Override
    public void operationComplete(ChannelFuture future) {
        if (client.isStopped()) {
            LOGGER.info(
                    "连接断开，客户端已关闭不进行重连，host->{},port->{}",
                    client.getBuilder().getHost(),
                    client.getBuilder().getPort());
        } else if (!client.isNeedReconnect()) {
            LOGGER.info(
                    "连接断开，重连功能关闭不重连，host->{},port->{}",
                    client.getBuilder().getHost(),
                    client.getBuilder().getPort());
        } else {
            int reconnectDelay = client.getReconnectDelay(index);
            client.countReconnectDelay(index);

            LOGGER.info(
                    "连接断开，{}秒后开始重连，host->{},port->{}",
                    reconnectDelay,
                    client.getBuilder().getHost(),
                    client.getBuilder().getPort());

            Client.executor.schedule(
                    () -> {
                        client.createChannel(index);
                    },
                    reconnectDelay,
                    TimeUnit.SECONDS);
        }
    }
}
