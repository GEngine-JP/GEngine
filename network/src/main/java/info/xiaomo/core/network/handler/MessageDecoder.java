package info.xiaomo.core.network.handler;

import com.google.protobuf.AbstractMessage;
import info.xiaomo.core.network.IMessageAndHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MessageDecoder extends ByteToMessageDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageDecoder.class);

    private IMessageAndHandler msgPool;

    public MessageDecoder(IMessageAndHandler msgPool) {
        this.msgPool = msgPool;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int messageId = in.readInt(); // int占4个字节
        byte[] bytes = new byte[in.readableBytes()]; // 消息体的长度(假设长度为20)
        in.readBytes(bytes);
        // 4+20 =24
        AbstractMessage message = msgPool.getMessage(messageId);
        if (message == null) {
            LOGGER.error("未注册的消息id{}", messageId);
            return;
        }

        out.add(message.getParserForType().parseFrom(bytes));
    }
}
