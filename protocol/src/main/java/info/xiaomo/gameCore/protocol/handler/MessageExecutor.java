package info.xiaomo.gameCore.protocol.handler;


import info.xiaomo.gameCore.protocol.AbstractHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;


public interface MessageExecutor {
    void doCommand(Channel channel, AbstractHandler handler) throws Exception;

    void connected(ChannelHandlerContext ctx);

    void disconnected(ChannelHandlerContext ctx);

    void exceptionCaught(ChannelHandlerContext ctx, Throwable paramThrowable);
}

