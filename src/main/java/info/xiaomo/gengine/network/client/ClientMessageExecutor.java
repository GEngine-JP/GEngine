package info.xiaomo.gengine.network.client;

import com.google.protobuf.Message;
import java.util.Map;
import info.xiaomo.gengine.network.*;
import info.xiaomo.gengine.network.handler.MessageExecutor;
import info.xiaomo.gengine.utils.AttributeUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientMessageExecutor extends MessageExecutor {

    protected Map<Short, ClientFuture<Message>> futureMap;

    protected boolean idleCheck;

    public ClientMessageExecutor(
            INetworkConsumer consumer,
            INetworkEventListener listener,
            IMessagePool pool,
            Map<Short, ClientFuture<Message>> futureMap,
            boolean idleCheck) {
        super(consumer, listener, pool);
        this.futureMap = futureMap;
        this.idleCheck = idleCheck;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message msg) {
        Integer msgId = pool.getMessageId(msg);

        if (msgId == null) {
            log.error("消息未注册:{}", msg);
            return;
        }

        ISession session = AttributeUtil.get(ctx.channel(), SessionKey.SESSION);

        if (session == null) {
            return;
        }
        AbstractHandler handler = pool.getHandler(msgId);
        if (handler == null) {
            log.error("msgId:{} 未注册handler", msgId);
            return;
        }
        handler.setMessage(msg);
        handler.setParam(session);
        handler.setSession(session);
        handler.doAction();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            IdleState state = idleStateEvent.state();
            if (state == IdleState.READER_IDLE) {
                this.listener.idle(ctx, state);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
