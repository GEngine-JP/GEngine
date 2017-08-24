package info.xiaomo.gameCore.protocol.websocket;

import info.xiaomo.gameCore.protocol.Message;
import info.xiaomo.gameCore.protocol.handler.MessageEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.EncoderException;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.util.ReferenceCountUtil;

public class WebSocketMessageEncoder extends MessageEncoder {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

        ByteBuf buf = null;
        try {
            if (acceptOutboundMessage(msg)) {
                @SuppressWarnings("unchecked")
                Message cast = (Message) msg;
                buf = allocateBuffer(ctx, cast, isPreferDirect());
                try {
                    encode(ctx, cast, buf);
                } finally {
                    ReferenceCountUtil.release(cast);
                }

                if (buf.isReadable()) {
                    ctx.write(new BinaryWebSocketFrame(buf), promise);
                } else {
                    buf.release();
                    ctx.write(Unpooled.EMPTY_BUFFER, promise);
                }
                buf = null;
            } else {
                ctx.write(msg, promise);
            }
        } catch (EncoderException e) {
            throw e;
        } catch (Throwable e) {
            throw new EncoderException(e);
        } finally {
            if (buf != null) {
                buf.release();
            }
        }
    }
}
