package info.xiaomo.gengine.network.handler;

import com.google.protobuf.Message;

import java.util.List;
import java.util.Objects;

import info.xiaomo.gengine.network.IMessagePool;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageDecoder extends ByteToMessageDecoder {

    private final IMessagePool pool;

    public MessageDecoder(IMessagePool pool) {
        this.pool = pool;
    }

    @Override
    protected void decode(
            ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> out)
            throws Exception {
        int msgId = byteBuf.readInt();
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);

        Message message = pool.getMessage(msgId);

        if (message == null) {
            log.error("解码到未注册的消息id:{}", msgId);
            return;
        }
        out.add(message.getParserForType().parseFrom(bytes));
    }
}
