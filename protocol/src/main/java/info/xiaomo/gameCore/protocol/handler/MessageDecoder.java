package info.xiaomo.gameCore.protocol.handler;

import info.xiaomo.gameCore.protocol.AbstractHandler;
import info.xiaomo.gameCore.protocol.MessagePool;
import info.xiaomo.gameCore.protocol.entity.BaseMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MessageDecoder extends LengthFieldBasedFrameDecoder {
    public static final Logger LOGGER = LoggerFactory.getLogger(MessageDecoder.class);
    private MessagePool msgPool;

    private MessageDecoder(MessagePool msgPool, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
                           int lengthAdjustment, int initialBytesToStrip) throws IOException {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
        this.msgPool = msgPool;
    }

    public MessageDecoder(MessagePool msgPool) throws IOException {
        this(msgPool, 1024 * 1024, 0, 4, -4, 0);
    }

    /**
     * 解码
     *
     * @param ctx ctx
     * @param in  in
     * @return Object
     * @throws Exception Exception
     */
    @SuppressWarnings("unchecked")
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf byteBuf = (ByteBuf) super.decode(ctx, in);
        if (byteBuf == null) {
            return null;
        }
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        AbstractHandler abstractHandler = null;
        try {
            BaseMsg.MsgPack msgPack = BaseMsg.MsgPack.parseFrom(bytes);
            int messageId = msgPack.getMsgID();
            abstractHandler = this.msgPool.getHandler(messageId);
            if (abstractHandler == null) {
                LOGGER.error("未知的消息消息id【{}】", messageId);
                return null;
            }
            Object msg = abstractHandler.decode(msgPack.getBody().toByteArray());
            abstractHandler.setMessage(msg);
        } catch (Exception e) {
            LOGGER.error("初始化消息错误", e);
        }
        return abstractHandler;
    }

}
