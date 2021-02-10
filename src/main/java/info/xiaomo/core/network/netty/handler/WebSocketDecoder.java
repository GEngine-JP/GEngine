package info.xiaomo.core.network.netty.handler;

import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 * <p>
 *
 * @author : xiaomo
 * github: https://github.com/xiaomoinfo
 * email : xiaomo@xiaomo.info
 * QQ    : 83387856
 * Date  : 2017/8/21 17:43
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
public class WebSocketDecoder extends MessageToMessageDecoder<BinaryWebSocketFrame> {


    @Override
    protected void decode(ChannelHandlerContext ctx, BinaryWebSocketFrame msg, List<Object> out) {
        out.add(msg.content().retain());
    }
}
