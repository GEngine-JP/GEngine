package info.xiaomo.core.network.handler;

import com.google.protobuf.AbstractMessage;
import info.xiaomo.core.network.INetworkConsumer;
import info.xiaomo.core.network.INetworkEventListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author xiaomo
 */
public class MessageExecutor extends ChannelHandlerAdapter {

    private INetworkConsumer consumer;

    private INetworkEventListener listener;


    public MessageExecutor(INetworkConsumer consumer, INetworkEventListener listener) {
        this.consumer = consumer;
        this.listener = listener;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        consumer.consume((AbstractMessage) msg, ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        listener.onExceptionOccur(ctx, cause);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.listener.onConnected(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        this.listener.onDisconnected(ctx);
    }
}
