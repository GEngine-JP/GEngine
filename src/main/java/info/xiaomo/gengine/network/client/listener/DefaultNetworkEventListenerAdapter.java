package info.xiaomo.gengine.network.client.listener;

import info.xiaomo.gengine.network.INetworkEventListener;
import io.netty.channel.ChannelHandlerContext;

/**
 * 默认实现网络事件监听器
 *
 * @author 张力
 * @date 2017/12/25 14:19
 */
public class DefaultNetworkEventListenerAdapter implements INetworkEventListener {

    @Override
    public void onConnected(ChannelHandlerContext ctx) {}

    @Override
    public void onDisconnected(ChannelHandlerContext ctx) {}

    @Override
    public void onExceptionOccur(ChannelHandlerContext ctx, Throwable cause) {}

    @Override
    public void idle(ChannelHandlerContext ctx, Object evt) {}
}
