package info.xiaomo.gameCore.protocol.handler;

import info.xiaomo.gameCore.protocol.AbstractHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

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
    private MessageExecutor messageExecutor;

    public NettyMessageExecutor(MessageExecutor messageExecutor) {
        this.messageExecutor = messageExecutor;
    }

    public void channelRead(ChannelHandlerContext ctx, Object handler) throws Exception {
        this.messageExecutor.doCommand(ctx.channel(), (AbstractHandler) handler);
        super.channelRead(ctx, handler);
    }

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.messageExecutor.connected(ctx);
        super.channelActive(ctx);
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.messageExecutor.disconnected(ctx);
        super.channelInactive(ctx);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        this.messageExecutor.exceptionCaught(ctx, cause);
        super.exceptionCaught(ctx, cause);
    }
}
