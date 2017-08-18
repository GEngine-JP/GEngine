package info.xiaomo.gameCore.protocol.handler;

import info.xiaomo.gameCore.protocol.NetworkConsumer;
import info.xiaomo.gameCore.protocol.message.AbstractMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class MessageExecutor extends ChannelInboundHandlerAdapter {

    private NetworkConsumer consumer;


    public MessageExecutor(NetworkConsumer consumer) {
        this.consumer = consumer;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        consumer.consume((AbstractMessage) msg, ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        this.consumer.exceptionOccurred(ctx.channel(), cause);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.consumer.connected(ctx.channel());
        super.channelActive(ctx);

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.consumer.disconnected(ctx.channel());
        super.channelInactive(ctx);
    }


}
