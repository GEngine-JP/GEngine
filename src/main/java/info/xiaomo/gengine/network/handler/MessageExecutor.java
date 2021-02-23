package info.xiaomo.gengine.network.handler;

import com.google.protobuf.AbstractMessage;
import info.xiaomo.gengine.network.INetworkConsumer;
import info.xiaomo.gengine.network.INetworkEventListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author xiaomo
 */
public class MessageExecutor extends ChannelInboundHandlerAdapter {

    private final INetworkConsumer consumer;

    protected final INetworkEventListener listener;


    public MessageExecutor(INetworkConsumer consumer, INetworkEventListener listener) {
        this.consumer = consumer;
        this.listener = listener;
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        listener.onExceptionOccur(ctx, cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        consumer.consume((AbstractMessage) msg, ctx.channel());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        if (this.listener != null) {
            this.listener.onConnected(ctx);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if (this.listener != null) {
            this.listener.onDisconnected(ctx);
        }
    }
}
