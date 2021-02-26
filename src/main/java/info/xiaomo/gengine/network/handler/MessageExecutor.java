package info.xiaomo.gengine.network.handler;

import com.google.protobuf.Message;
import info.xiaomo.gengine.network.IMessagePool;
import info.xiaomo.gengine.network.INetworkConsumer;
import info.xiaomo.gengine.network.INetworkEventListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/** @author xiaomo */
@Slf4j
public class MessageExecutor extends SimpleChannelInboundHandler<Message> {

    protected final INetworkEventListener listener;
    private final INetworkConsumer consumer;
    private final IMessagePool pool;

    public MessageExecutor(
            INetworkConsumer consumer, INetworkEventListener listener, IMessagePool pool) {
        this.consumer = consumer;
        this.listener = listener;
        this.pool = pool;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        listener.onExceptionOccur(ctx, cause);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message message) {
        int msgId = pool.getMessageId(message);

        if (msgId == 0) {
            log.error("请求消息未设置msgId");
            return;
        }
        consumer.consume(message, ctx.channel());
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
