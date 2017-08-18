package info.xiaomo.gameCore.protocol.handler;

import info.xiaomo.gameCore.protocol.message.AbstractMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<AbstractMessage> {

    public MessageEncoder() {
        super();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, AbstractMessage msg, ByteBuf out) throws Exception {
        ByteBuf buffer = null;
        try {
            buffer = msg.encode();
            out.writeBytes(buffer);
        } finally {
            if (buffer != null) {
                buffer.release();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

}
