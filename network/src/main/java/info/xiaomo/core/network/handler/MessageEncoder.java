package info.xiaomo.core.network.handler;

import com.google.protobuf.AbstractMessage;
import info.xiaomo.core.network.IMessageAndHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xiaomo
 */
public class MessageEncoder extends MessageToByteEncoder<AbstractMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageEncoder.class);

    private IMessageAndHandler msgPool;

    public MessageEncoder(IMessageAndHandler msgPool) {
        this.msgPool = msgPool;
    }


    @Override
    protected void encode(ChannelHandlerContext ctx, AbstractMessage msg, ByteBuf out) {
        int messageId = msgPool.getMessageId(msg);
        if (messageId == 0) {
            LOGGER.error("编码到未知的消息{}", messageId);
        }
        byte[] bytes = msg.toByteArray();
        int length = Integer.BYTES + bytes.length;
        boolean writeAble = out.isWritable(length);
        if (!writeAble) {
            LOGGER.error("消息过大，编码失败 {} -> {}", messageId, length);
            return;
        }
        // int->4
        out.writeInt(messageId);
        // ->20(假设)
        out.writeBytes(bytes);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

}
