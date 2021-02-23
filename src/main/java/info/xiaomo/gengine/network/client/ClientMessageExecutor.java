package info.xiaomo.gengine.network.client;


import com.google.protobuf.AbstractMessage;
import info.xiaomo.gengine.network.INetworkConsumer;
import info.xiaomo.gengine.network.INetworkEventListener;
import info.xiaomo.gengine.network.handler.MessageExecutor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ClientMessageExecutor extends MessageExecutor {

    public static final Logger LOGGER = LoggerFactory.getLogger(ClientMessageExecutor.class);

    protected Map<Short, ClientFuture<AbstractMessage>> futureMap;

    public ClientMessageExecutor(INetworkConsumer consumer, INetworkEventListener listener,
                                 Map<Short, ClientFuture<AbstractMessage>> futureMap, boolean idleCheck) {
        super(consumer, listener);
        this.futureMap = futureMap;
    }

//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//
//        AbstractMessage m = (AbstractMessage) msg;
//        ClientFuture<AbstractMessage> f = futureMap.get(m.getSequence());
//        if (f != null) {
//            if (!f.isCancelled()) {
//                f.result(m);
//            }
//        } else {
//            super.channelRead(ctx, msg);
//        }
//
//    }
//
//    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//
//        if(this.isIdleCheck() && evt instanceof IdleStateEvent) {
//            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
//            IdleState state = idleStateEvent.state();
//            if(state == IdleState.READER_IDLE) {
//                this.listener.idle(ctx, state);
//            }
//        } else {
//            super.userEventTriggered(ctx, evt);
//        }
//    }
}
