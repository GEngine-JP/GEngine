package info.xiaomo.gengine.network.handler;

import info.xiaomo.gengine.network.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author xiaomo
 */
@Slf4j
public class MessageDecoder extends ByteToMessageDecoder {

    private final int upLimit;

    public MessageDecoder(int upLimit) {
        this.upLimit = upLimit;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 7) {
            return;
        }

        in.markReaderIndex();
        byte head = in.readByte();
        short length = in.readShort();
        if ((length <= 0) || (length > this.upLimit))
            throw new IllegalArgumentException();
        int cmd = in.readInt();
        if (in.readableBytes() < length - 4) {
            in.resetReaderIndex();
            return;
        }
        byte[] bytes = new byte[length - 4];
        in.readBytes(bytes);
        out.add(new Packet(head, cmd, bytes));
    }
}
