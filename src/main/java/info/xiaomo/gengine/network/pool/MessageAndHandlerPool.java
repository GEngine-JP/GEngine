package info.xiaomo.gengine.network.pool;

import com.google.protobuf.AbstractMessage;

import java.util.HashMap;
import java.util.Map;

import info.xiaomo.gengine.network.AbstractHandler;
import info.xiaomo.gengine.network.IMessageAndHandler;

/**

 * desc  : 消息池的父类
 * Copyright(©) 2017 by xiaomo.
 */
public class MessageAndHandlerPool implements IMessageAndHandler {

    /**
     * 消息类字典
     */
    public static final Map<Integer, AbstractMessage> messages = new HashMap<>(10);

    /**
     * 类和
     */
    private final Map<String, Integer> ids = new HashMap<>(10);

    private final Map<Integer, Class<? extends AbstractHandler>> handlers = new HashMap<>(10);

    @Override
    public AbstractMessage getMessage(int messageId) {
        return messages.get(messageId);
    }

    @Override
    public int getMessageId(AbstractMessage message) {
        return ids.get(message.getClass().getName());
    }


    @Override
    public AbstractHandler getHandler(int messageId) {
        Class<? extends AbstractHandler> clazz = handlers.get(messageId);
        if (clazz != null) {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public void register(int messageId, AbstractMessage messageClazz, Class<? extends AbstractHandler> handler) {
        messages.put(messageId, messageClazz);
        handlers.put(messageId, handler);
        ids.put(messageClazz.getClass().getName(), messageId);
    }

    @Override
    public void register(int messageId, AbstractMessage messageClazz) {
        messages.put(messageId, messageClazz);
        ids.put(messageClazz.getClass().getName(), messageId);
    }

}
