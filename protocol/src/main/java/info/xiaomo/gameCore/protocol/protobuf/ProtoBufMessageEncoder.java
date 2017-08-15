package info.xiaomo.gameCore.protocol.protobuf;

import com.google.protobuf.MessageLite;
import info.xiaomo.gameCore.protocol.MessagePool;
import info.xiaomo.gameCore.protocol.entity.BaseMsg;
import info.xiaomo.gameCore.protocol.handler.MessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 * <p>
 * author: xiaomo
 * github: https://github.com/xiaomoinfo
 * email : xiaomo@xiaomo.info
 * QQ    : 83387856
 * Date  : 2017/8/12 15:35
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
public class ProtoBufMessageEncoder implements MessageEncoder {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProtoBufMessageEncoder.class);
    private MessagePool messagePool;

    public ProtoBufMessageEncoder(MessagePool messagePool) {
        this.messagePool = messagePool;
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
            int messageId = this.messagePool.getMessageId(message.getClass());
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
}
