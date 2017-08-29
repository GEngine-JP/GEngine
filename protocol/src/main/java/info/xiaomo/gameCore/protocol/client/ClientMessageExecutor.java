package info.xiaomo.gameCore.protocol.client;


import com.google.protobuf.AbstractMessage;
import com.google.protobuf.MessageLite;
import info.xiaomo.gameCore.protocol.NetworkConsumer;
import info.xiaomo.gameCore.protocol.NetworkEventListener;
import info.xiaomo.gameCore.protocol.handler.MessageExecutor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ClientMessageExecutor extends MessageExecutor {

    public static final Logger LOGGER = LoggerFactory.getLogger(ClientMessageExecutor.class);

    protected Map<Short, ClientFuture<AbstractMessage>> futureMap;

    public ClientMessageExecutor(NetworkConsumer consumer,
                                 NetworkEventListener listener,
                                 Map<Short, ClientFuture<AbstractMessage>> futureMap) {
        super(consumer, listener);
        this.futureMap = futureMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        MessageLite m = (MessageLite) msg;
//        ClientFuture<AbstractMessage> f = futureMap.get(m.getValidateId());
//        if (f != null) {
//            if (!f.isCancelled()) {
//                f.result(m);
//            }
//        } else {
//            super.channelRead(ctx, msg);
//        }

    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;

            IdleState state = idleStateEvent.state();
            if (state == IdleState.READER_IDLE) {
                ctx.close();
                LOGGER.error("连接超时，主动断开连接");
            }

        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
