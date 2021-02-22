package info.xiaomo.gengine.network;


import com.google.protobuf.AbstractMessage;

/**
 * @author xiaomo
 */
public interface IMessageAndHandler {

    /**
     * 获取消息
     *
     * @param messageId messageId
     * @return AbstractMessage
     */
    AbstractMessage getMessage(int messageId);

    /**
     * 获取消息id
     * @param message message
     * @return int
     */
    int getMessageId(AbstractMessage message);

    /**
     * 获取handler
     * @param  handlerName handlerName
     * @return AbstractHandler
     */
    AbstractHandler getHandler(String handlerName);

    /**
     * 注册
     * @param  messageId messageId
     * @param messageClazz messageClazz
     * @param handler handler
     */
    void register(int messageId, AbstractMessage messageClazz, Class<? extends AbstractHandler> handler);

    /**
     * 注册
     * @param messageId messageId
     * @param messageClazz messageClazz
     */
    void register(int messageId, AbstractMessage messageClazz);
}
