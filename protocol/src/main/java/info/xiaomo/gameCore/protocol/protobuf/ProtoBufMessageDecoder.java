package info.xiaomo.gameCore.protocol.protobuf;

import info.xiaomo.gameCore.protocol.AbstractHandler;
import info.xiaomo.gameCore.protocol.MessagePool;
import info.xiaomo.gameCore.protocol.entity.BaseMsg;
import info.xiaomo.gameCore.protocol.handler.MessageDecoder;
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
 * Date  : 2017/8/12 15:21
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
public class ProtoBufMessageDecoder implements MessageDecoder {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProtoBufMessageDecoder.class);
    private MessagePool messagePool;

    public ProtoBufMessageDecoder(MessagePool messagePool) {
        this.messagePool = messagePool;
    }

    /**
     * 解码
     *
     * @param bytes bytes
     * @return AbstractHandler
     */
    @SuppressWarnings("unchecked")
    public AbstractHandler decode(byte[] bytes) {
        AbstractHandler abstractHandler = null;
        try {
            BaseMsg.MsgPack msgPack = BaseMsg.MsgPack.parseFrom(bytes);
            int messageId = msgPack.getMsgID();
            abstractHandler = this.messagePool.getHandler(messageId);
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
