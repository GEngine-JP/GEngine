package info.xiaomo.gengine.network.handler;

import com.google.protobuf.AbstractMessage;
import info.xiaomo.gengine.network.INetworkConsumer;
import info.xiaomo.gengine.network.INetworkEventListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author xiaomo
 */
public class MessageExecutor extends SimpleChannelInboundHandler<Object> {

    private final INetworkConsumer consumer;

    private final INetworkEventListener listener;


    public MessageExecutor(INetworkConsumer consumer, INetworkEventListener listener) {
        this.consumer = consumer;
        this.listener = listener;
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        listener.onExceptionOccur(ctx, cause);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) {
        consumer.consume((AbstractMessage) msg, ctx.channel());
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
