package info.xiaomo.gameCore.protocol.handler;

import com.google.protobuf.MessageLite;
import info.xiaomo.gameCore.protocol.MessagePool;
import info.xiaomo.gameCore.protocol.entity.BaseMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageEncoder extends MessageToByteEncoder<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageEncoder.class);

    private MessagePool msgPool;

    public MessageEncoder(MessagePool msgPool) {
        this.msgPool = msgPool;
    }

    public byte[] encode(Object msg) {
        if (msg == null) {
            return null;
        }
        if (!MessageLite.class.isInstance(msg)) {
            LOGGER.error("非法的消息【{}】", msg.getClass().getName());
            return null;
        }
        try {
            MessageLite message = (MessageLite) msg;
            int messageId = this.msgPool.getMessageId(message.getClass());
            BaseMsg.MsgPack.Builder builder = BaseMsg.MsgPack.newBuilder();
            builder.setMsgID(messageId);
            builder.setBody(message.toByteString());

            BaseMsg.MsgPack msgPack = builder.build();
            return msgPack.toByteArray();
        } catch (Exception e) {
            LOGGER.error("编码错误错误", e);
        }
        return null;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {

    }
}
