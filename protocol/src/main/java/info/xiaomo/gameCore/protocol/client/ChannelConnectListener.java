package info.xiaomo.gameCore.protocol.client;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


/**
 * Created by 张力 on 2017/6/30.
 */
public class ChannelConnectListener implements ChannelFutureListener {

    private Client client;

    private int index;

    public static final Logger LOGGER = LoggerFactory.getLogger(ChannelConnectListener.class);

    public ChannelConnectListener(Client client, int index) {
        this.client = client;
        this.index = index;
    }

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {

        ClientBuilder builder = client.getBuilder();

        if (future.isSuccess()) {

            client.registerChannel(index, future.channel());

            if (client.getBuilder().getListener() != null) {
                client.getBuilder().getListener().onConnected(null);
            }

            LOGGER.info("成功连接到服务器,index->{}, host->{}, port->{}", index, builder.getHost(), builder.getPort());
            return;
        }


        LOGGER.error("连接服务器失败,index->{}, host->{}, port->{}", index, builder.getHost(), builder.getPort());

        if (client.isStopped()) {
            LOGGER.error("客户端已经关闭不重连,index->{}, host->{}, port->{}", index, builder.getHost(), builder.getPort());
        } else {
            LOGGER.error("10秒后进行重连,index->{}, host->{}, port->{}", index, builder.getHost(), builder.getPort());
            Client.executor.schedule(() -> {
                client.createChannel(index);
            }, 10, TimeUnit.SECONDS);
        }


    }
}
