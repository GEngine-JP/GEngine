package info.xiaomo.gengine.network.client;

import info.xiaomo.gengine.network.INetworkEventListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;

/**
 * 默认实现网络事件监听器
 *
 * @author 张力
 * @date 2017/12/25 14:19
 */
public class NetworkEventListenerAdapter implements INetworkEventListener {

    @Override
    public void onConnected(ChannelHandlerContext ctx) {

    }

    @Override
    public void onDisconnected(ChannelHandlerContext ctx) {

    }

    @Override
    public void onExceptionOccur(ChannelHandlerContext ctx, Throwable cause) {

    }
}
