package info.xiaomo.gameCore.protocol;


import java.util.HashMap;
import java.util.Map;

public class MessagePool {
    private Map<Integer, Class<? extends AbstractHandler>> handlers = new HashMap<>();

    /**
     * 注册消息
     *
     * @param messageId messageId
     * @param handler   handler
     */
    public void register(int messageId, Class<? extends AbstractHandler> handler) {
        if (handler != null) {
            this.handlers.put(messageId, handler);
        }
    }


    public int getMessageId(Class clazz) {
        for (Integer messageId : handlers.keySet()) {
            Class<? extends AbstractHandler> aClass = handlers.get(messageId);
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
