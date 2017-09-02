package info.xiaomo.gameCore.network;


import com.google.protobuf.AbstractMessage;

public interface IMessageAndHandler {

    AbstractMessage getMessage(int messageId);

    int getMessageId(AbstractMessage message);

    AbstractHandler getHandler(String handlerName);

    void register(int messageId, AbstractMessage messageClazz, Class<? extends AbstractHandler> handler);

    void register(int messageId, AbstractMessage messageClazz);
}
