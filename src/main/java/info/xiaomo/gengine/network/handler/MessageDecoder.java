package info.xiaomo.gengine.network.handler;

import java.util.List;
import info.xiaomo.gengine.network.MsgPack;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

/** @author xiaomo */
@Slf4j
public class MessageDecoder extends ByteToMessageDecoder {

    private final int upLimit;

    public MessageDecoder(int upLimit) {
        this.upLimit = upLimit;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
            throws Exception {
        if (in.readableBytes() < 7) {
            return;
        }

        in.markReaderIndex();
        byte head = in.readByte();
        short length = in.readShort();
        if ((length <= 0) || (length > this.upLimit)) throw new IllegalArgumentException();
        int msgId = in.readInt();
        if (in.readableBytes() < length - 4) {
            in.resetReaderIndex();
            return;
        }
        byte[] bytes = new byte[length - 4];
        in.readBytes(bytes);
        out.add(new MsgPack(head, msgId, bytes));
    }
}
