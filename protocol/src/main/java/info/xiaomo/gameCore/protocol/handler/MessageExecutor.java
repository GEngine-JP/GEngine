package info.xiaomo.gameCore.protocol.handler;

import com.google.protobuf.AbstractMessage;
import info.xiaomo.gameCore.protocol.NetworkConsumer;
import info.xiaomo.gameCore.protocol.NetworkEventListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class MessageExecutor extends ChannelInboundHandlerAdapter {

    private NetworkConsumer consumer;

    private NetworkEventListener listener;


    public MessageExecutor(NetworkConsumer consumer, NetworkEventListener listener) {
        this.consumer = consumer;
        this.listener = listener;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        consumer.consume((AbstractMessage) msg, ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        listener.onExceptionOccur(ctx, cause);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.listener.onConnected(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.listener.onDisconnected(ctx);
    }
}
