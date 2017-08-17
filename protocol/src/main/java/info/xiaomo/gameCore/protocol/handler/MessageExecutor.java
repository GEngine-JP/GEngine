package info.xiaomo.gameCore.protocol.handler;


import info.xiaomo.gameCore.protocol.AbstractHandler;
import info.xiaomo.gameCore.protocol.NetworkConsumer;
import info.xiaomo.gameCore.protocol.NetworkEventListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MessageExecutor extends ChannelInboundHandlerAdapter {

    private NetworkConsumer consumer;

    private NetworkEventListener listener;


    public MessageExecutor(NetworkConsumer consumer, NetworkEventListener listener) {
        super();
        this.consumer = consumer;
        this.listener = listener;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object handler) throws Exception {
        consumer.consume(ctx.channel(), (AbstractHandler) handler);
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

