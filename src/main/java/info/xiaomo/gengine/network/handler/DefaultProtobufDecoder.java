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
public class DefaultProtobufDecoder extends ByteToMessageDecoder {

    private IMessagePool pool;

    public DefaultProtobufDecoder(IMessagePool pool) {
        this.pool = pool;
    }

    @Override
    protected void decode(
            ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list)
            throws Exception {
        int msgId = byteBuf.readInt();
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);

        Message message = pool.getMessage(msgId);

        if (message == null) {
            log.error("解码到未注册的消息id:{}", msgId);
        }
        list.add(Objects.requireNonNull(message).getParserForType().parseFrom(bytes));
    }
}
