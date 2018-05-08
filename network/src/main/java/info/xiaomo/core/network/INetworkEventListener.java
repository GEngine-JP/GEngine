package info.xiaomo.core.network;

import io.netty.channel.ChannelHandlerContext;

/**
 * 网络事件监听器
 *
 * @author 小莫
 */
public interface INetworkEventListener {

    /**
     * 连接建立
     *
     * @param ctx ctx
     */
    void onConnected(ChannelHandlerContext ctx);

    /**
     * 连接断开
     * * @param ctx
     */
    void onDisconnected(ChannelHandlerContext ctx);

    /**
     * 异常发生
     * * @param ctx
     * * @param cause
     */
    void onExceptionOccur(ChannelHandlerContext ctx, Throwable cause);

}
