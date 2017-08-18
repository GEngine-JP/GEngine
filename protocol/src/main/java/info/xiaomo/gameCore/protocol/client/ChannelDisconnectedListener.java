package info.xiaomo.gameCore.protocol.client;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by 张力 on 2017/6/30.
 */
public class ChannelDisconnectedListener implements ChannelFutureListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelDisconnectedListener.class);

    private Client client;

    private int index;


    public ChannelDisconnectedListener(Client client, int index) {
        this.client = client;
        this.index = index;
    }

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        if (client.isStopped()) {
            LOGGER.info("连接断开，客户端已关闭不进行重连，index->{},host->{},port->{}", client.getBuilder().getHost(), client.getBuilder().getPort());
        } else {
            LOGGER.info("连接断开，10秒后开始重连，index->{},host->{},port->{}", client.getBuilder().getHost(), client.getBuilder().getPort());
            Client.executor.schedule(() -> {
                client.createChannel(index);
            }, 10, TimeUnit.SECONDS);
        }


    }
}
