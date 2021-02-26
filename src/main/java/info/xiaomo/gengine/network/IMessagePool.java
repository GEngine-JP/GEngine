package info.xiaomo.gengine.network;

import com.google.protobuf.Message;

/** @author xiaomo */
public interface IMessagePool {

    /**
     * 获取消息
     *
     * @param messageId messageId
     * @return AbstractMessage
     */
    Message getMessage(int messageId);

    /**
     * 获取消息id
     *
     * @param message message
     * @return int
     */
    int getMessageId(Message message);

    /**
     * 获取handler
     *
     * @return AbstractHandler
     */
    AbstractHandler getHandler(int messageId);

    /**
     * 注册
     *
     * @param messageId messageId
     * @param messageClazz messageClazz
     * @param handler handler
     */
    void register(int messageId, Message messageClazz, Class<? extends AbstractHandler> handler);

    /**
     * 注册
     *
     * @param messageId messageId
     * @param messageClazz messageClazz
     */
    void register(int messageId, Message messageClazz);
}
