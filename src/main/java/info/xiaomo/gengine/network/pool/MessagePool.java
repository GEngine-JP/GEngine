package info.xiaomo.gengine.network.pool;

import com.google.protobuf.Message;
import java.util.HashMap;
import java.util.Map;
import info.xiaomo.gengine.network.AbstractHandler;
import info.xiaomo.gengine.network.IMessagePool;

/** desc : 消息池的父类 Copyright(©) 2017 by xiaomo. */
public class MessagePool implements IMessagePool {

  /** 消息类字典 */
  public static final Map<Integer, Message> messages = new HashMap<>(10);

  /** 类和 */
  private final Map<String, Integer> ids = new HashMap<>(10);

  private final Map<Integer, Class<? extends AbstractHandler>> handlers = new HashMap<>(10);

  @Override
  public Message getMessage(int messageId) {
    return messages.get(messageId);
  }

  @Override
  public int getMessageId(Message message) {
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
  public void register(
      int messageId, Message messageClazz, Class<? extends AbstractHandler> handler) {
    messages.put(messageId, messageClazz);
    handlers.put(messageId, handler);
    ids.put(messageClazz.getClass().getName(), messageId);
  }

  @Override
  public void register(int messageId, Message messageClazz) {
    messages.put(messageId, messageClazz);
    ids.put(messageClazz.getClass().getName(), messageId);
  }
}
