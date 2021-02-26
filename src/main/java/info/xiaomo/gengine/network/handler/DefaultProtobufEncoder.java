package info.xiaomo.gengine.network.handler;

import com.google.protobuf.Message;
import info.xiaomo.gengine.network.IMessagePool;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultProtobufEncoder extends MessageToByteEncoder<Message> {

    private IMessagePool pool;

    public DefaultProtobufEncoder(IMessagePool pool) {
        this.pool = pool;
    }

    @Override
    protected void encode(
            ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) {
        Integer msgId = pool.getMessageId(message);
        if (msgId == null) {
            log.error("消息未注册:{}", message.getClass().getSimpleName());
            return;
        }

        byte[] bytes = message.toByteArray();
        int length = Integer.BYTES + bytes.length;
        byteBuf.ensureWritable(length);
        boolean writable = byteBuf.isWritable(length);
        if (!writable) {
            log.error("消息过大，编码失败:{}:{}", msgId, length);
        }

        byteBuf.writeInt(msgId);
        byteBuf.writeBytes(bytes);
    }
}
