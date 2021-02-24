package info.xiaomo.gengine.network.handler;

import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

/**
 * @Date : 2017/8/21 17:38
 *
 * @desc : Copyright(©) 2017 by xiaomo.
 */
public class WebSocketEncoder extends MessageToMessageEncoder<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
        out.add(new BinaryWebSocketFrame(msg).retain());
    }
}
