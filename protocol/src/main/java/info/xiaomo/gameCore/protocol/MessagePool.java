package info.xiaomo.gameCore.protocol;


public interface MessagePool {

    Message getMessage(int messageId);

    void register(int messageId, Class<? extends Message> messageClazz);
}
