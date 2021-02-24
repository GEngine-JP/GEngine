package info.xiaomo.gengine.network.client.listener;

import java.util.concurrent.TimeUnit;
import info.xiaomo.gengine.network.client.Client;
import info.xiaomo.gengine.network.client.ClientBuilder;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Created by 张力 on 2017/6/30. */
public class ChannelConnectListener implements ChannelFutureListener {

    public static final Logger LOGGER = LoggerFactory.getLogger(ChannelConnectListener.class);
    private final Client client;
    private final int index;

    public ChannelConnectListener(Client client, int index) {
        this.client = client;
        this.index = index;
    }

    @Override
    public void operationComplete(ChannelFuture future) {

        ClientBuilder builder = client.getBuilder();

        if (future.isSuccess()) {

            client.registerChannel(index, future.channel());

            if (client.getBuilder().getClientListener() != null) {
                client.getBuilder().getClientListener().afterConnected(future.channel());
            }
            LOGGER.info(
                    "成功连接到服务器,index->{}, host->{}, port->{}",
                    index,
                    builder.getHost(),
                    builder.getPort());
            return;
        }

        LOGGER.error(
                "连接服务器失败,index->{}, host->{}, port->{}",
                index,
                builder.getHost(),
                builder.getPort());

        if (client.isStopped()) {
            LOGGER.error(
                    "客户端已经关闭不重连,index->{}, host->{}, port->{}",
                    index,
                    builder.getHost(),
                    builder.getPort());
        } else if (!client.isNeedReconnect()) {
            LOGGER.error(
                    "重连功能关闭不重连,index->{}, host->{}, port->{}",
                    index,
                    builder.getHost(),
                    builder.getPort());
        } else {

            int reconnectDelay = client.getReconnectDelay(index);
            client.countReconnectDelay(index);

            LOGGER.error(
                    "{}秒后进行重连,index->{}, host->{}, port->{}",
                    reconnectDelay,
                    index,
                    builder.getHost(),
                    builder.getPort());
            Client.executor.schedule(
                    () -> {
                        client.createChannel(index);
                    },
                    reconnectDelay,
                    TimeUnit.SECONDS);
        }
    }
}
