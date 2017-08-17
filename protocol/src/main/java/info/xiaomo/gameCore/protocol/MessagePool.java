package info.xiaomo.gameCore.protocol;


import com.google.protobuf.MessageOrBuilder;

import java.util.HashMap;
import java.util.Map;

public class MessagePool {
    private Map<Integer, Class<? extends AbstractHandler>> handlers = new HashMap<>();
    // 处理类字典
    private Map<Integer, Class<? extends MessageOrBuilder>> messages = new HashMap<>();

    /**
     * 注册消息
     *
     * @param messageId messageId
     * @param handler   handler
     */
    public void register(int messageId, Class<? extends MessageOrBuilder> message, Class<? extends AbstractHandler> handler) {
        if (handler != null && messages != null) {
            this.handlers.put(messageId, handler);
            this.messages.put(messageId, message);
        }
    }


    public int getMessageId(Class clazz) {
        for (Integer messageId : messages.keySet()) {
            Class<? extends MessageOrBuilder> aClass = messages.get(messageId);
            if (aClass.getName().equals(clazz.getName())) {
                return messageId;
            }
        }
        return 0;
    }

    /**
     * 通过消息id获取handler
     *
     * @param messageId messageId
     * @return AbstractHandler
     * @throws Exception Exception
     */
    public AbstractHandler getHandler(int messageId) throws Exception {
        Class handler = this.handlers.get(messageId);
        if (handler == null) {
            return null;
        }
        return (AbstractHandler) handler.newInstance();
    }
}
