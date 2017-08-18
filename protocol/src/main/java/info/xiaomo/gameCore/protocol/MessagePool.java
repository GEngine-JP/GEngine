package info.xiaomo.gameCore.protocol;


import info.xiaomo.gameCore.base.AbstractHandler;
import info.xiaomo.gameCore.protocol.message.AbstractMessage;

public interface MessagePool {

    AbstractMessage getMessage(int messageId);

    void register(int messageId, Class<? extends AbstractMessage> messageClazz, Class<? extends AbstractHandler> handlerClazz);
}
