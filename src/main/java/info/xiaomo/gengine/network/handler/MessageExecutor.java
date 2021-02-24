package info.xiaomo.gengine.network.handler;

import com.google.protobuf.AbstractMessage;
import com.google.protobuf.Message;
import java.lang.reflect.Method;
import info.xiaomo.gengine.network.INetworkConsumer;
import info.xiaomo.gengine.network.INetworkEventListener;
import info.xiaomo.gengine.network.MsgPack;
import info.xiaomo.gengine.network.pool.MessageAndHandlerPool;
import info.xiaomo.gengine.utils.ClassUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/** @author xiaomo */
@Slf4j
public class MessageExecutor extends SimpleChannelInboundHandler<MsgPack> {

    protected final INetworkEventListener listener;
    private final INetworkConsumer consumer;

    public MessageExecutor(INetworkConsumer consumer, INetworkEventListener listener) {
        this.consumer = consumer;
        this.listener = listener;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        listener.onExceptionOccur(ctx, cause);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, MsgPack msgPack) throws Exception {
        AbstractMessage clazz = MessageAndHandlerPool.messages.get(msgPack.getMsgId());

        Method m = ClassUtil.findMethod(clazz.getClass(), "getDefaultInstance");
        if (m != null) {
            AbstractMessage message = (AbstractMessage) m.invoke(null);
            Message msg = message.newBuilderForType().mergeFrom(msgPack.getBytes()).build();
            msgPack.setMsg(msg);
            consumer.consume(msgPack, ctx.channel());
        } else {
            log.error("找有找到消息体:{}", msgPack.getMsgId());
        }
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
