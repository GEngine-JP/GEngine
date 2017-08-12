package info.xiaomo.gameCore.protocol.handler;

import info.xiaomo.gameCore.protocol.Connection;
import info.xiaomo.gameCore.protocol.NettyConnection;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 * <p>
 * author: xiaomo
 * github: https://github.com/xiaomoinfo
 * email : xiaomo@xiaomo.info
 * QQ    : 83387856
 * Date  : 2017/8/12 15:55
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
public class NettyMessageExecutor extends ChannelInboundHandlerAdapter {
    private static final AttributeKey<Connection> CONNECTION = AttributeKey.valueOf("CONNECTION");
    private MessageExecutor messageExecutor;

    public NettyMessageExecutor(MessageExecutor messageExecutor) {
        this.messageExecutor = messageExecutor;
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();
        Connection connection = channel.attr(CONNECTION).get();
        this.messageExecutor.doCommand(connection, msg);

        super.channelRead(ctx, msg);
    }

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        Connection connection = new NettyConnection(channel);
        channel.attr(CONNECTION).set(connection);
        this.messageExecutor.connected(connection);

        super.channelActive(ctx);
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        Connection connection = channel.attr(CONNECTION).get();
        this.messageExecutor.disconnected(connection);

        super.channelInactive(ctx);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        Connection connection = channel.attr(CONNECTION).get();
        this.messageExecutor.exceptionCaught(connection, cause);

        super.exceptionCaught(ctx, cause);
    }
}
