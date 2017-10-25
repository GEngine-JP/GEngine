package info.xiaomo.core.network;


import com.google.protobuf.AbstractMessage;

/**
 * @author xiaomo
 */
public interface IMessageAndHandler {

    /**
     * 获取消息
     *
     * @param messageId
     * @return
     */
    AbstractMessage getMessage(int messageId);

    /**
     * 获取消息id
     * @param message
     * @return
     */
    int getMessageId(AbstractMessage message);

    /**
     * 获取handler
     * @param handlerName
     * @return
     */
    AbstractHandler getHandler(String handlerName);

    /**
     * 注册
     * @param messageId
     * @param messageClazz
     * @param handler
     */
    void register(int messageId, AbstractMessage messageClazz, Class<? extends AbstractHandler> handler);

    /**
     * 注册
     * @param messageId
     * @param messageClazz
     */
    void register(int messageId, AbstractMessage messageClazz);
}
