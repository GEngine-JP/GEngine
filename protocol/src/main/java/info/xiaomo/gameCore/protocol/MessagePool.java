package info.xiaomo.gameCore.protocol;


import com.google.protobuf.AbstractMessage;

public interface MessagePool {

    AbstractMessage getMessage(int messageId);

    int getMessageId(AbstractMessage message);

    AbstractHandler getHandler(String handlerName);

    void register(int messageId, AbstractMessage messageClazz, Class<? extends AbstractHandler> handler);
}
