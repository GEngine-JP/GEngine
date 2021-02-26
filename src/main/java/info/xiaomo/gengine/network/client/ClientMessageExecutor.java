package info.xiaomo.gengine.network.client;

import com.google.protobuf.Message;
import java.util.Map;
import info.xiaomo.gengine.network.IMessagePool;
import info.xiaomo.gengine.network.INetworkConsumer;
import info.xiaomo.gengine.network.INetworkEventListener;
import info.xiaomo.gengine.network.handler.MessageExecutor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientMessageExecutor extends MessageExecutor {

    protected Map<Short, ClientFuture<Message>> futureMap;

    protected IMessagePool pool;

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

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message msg) {

        //        ClientFuture<MsgPack> f = futureMap.get(m.getSequence());
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
                this.listener.idle(ctx, state);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
