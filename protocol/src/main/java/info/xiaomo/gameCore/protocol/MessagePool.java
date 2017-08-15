package info.xiaomo.gameCore.protocol;


import info.xiaomo.gameCore.protocol.handler.MessageHandler;

import java.util.HashMap;
import java.util.Map;

public class MessagePool {
    private Map<Integer, Class<? extends MessageHandler>> handlers = new HashMap<>();
    private Map<String, Integer> msgIds = new HashMap<>();


    /**
     * 注册消息
     *
     * @param msgId      msgId
     * @param msgCls     msgCls
     * @param handlerCls handlerCls
     */
    public void register(int msgId, Class msgCls, Class<? extends MessageHandler> handlerCls) {
        if (handlerCls != null) {
            this.handlers.put(msgId, handlerCls);
        }
        if (msgCls != null) {
            this.msgIds.put(msgCls.getName(), msgId);
        }
    }

    /**
     * 注册消息
     *
     * @param msgId  msgId
     * @param msgCls msgCls
     */
    public void register(int msgId, Class msgCls) {
        register(msgId, msgCls, null);
    }

    public int getMessageId(Class msgCls) {
        Integer msgId = this.msgIds.get(msgCls.getName());
        if (msgId == null) {
            return 0;
        }
        return msgId;
    }

    /**
     * 通过消息id获取handler
     *
     * @param msgId msgId
     * @return MessageHandler
     * @throws Exception Exception
     */
    public MessageHandler getHandler(int msgId) throws Exception {
        Class handlerClass = this.handlers.get(msgId);
        if (handlerClass == null) {
            return null;
        }
        return (MessageHandler) handlerClass.newInstance();
    }
}
