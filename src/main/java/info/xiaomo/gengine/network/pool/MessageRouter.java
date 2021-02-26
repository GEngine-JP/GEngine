package info.xiaomo.gengine.network.pool;

import com.google.protobuf.Message;
import java.util.HashMap;
import java.util.Map;
import info.xiaomo.gengine.network.*;
import info.xiaomo.gengine.utils.AttributeUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

/** @author xiaomo */
@Slf4j
public class MessageRouter implements INetworkConsumer {

    protected final Map<Integer, IProcessor> processors = new HashMap<>(10);

    protected final IMessagePool msgPool;

    public MessageRouter(IMessagePool msgPool) {
        this.msgPool = msgPool;
    }

    public void registerProcessor(int queueId, IProcessor consumer) {
        processors.put(queueId, consumer);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void consume(Message message, Channel channel) {

        // 将消息分发到指定的队列中，该队列有可能在同一个进程，也有可能不在同一个进程

        //        IProcessor processor = processors.get(msgPack.getSequence());
        //        if (processor == null) {
        //            log.error("找不到可用的消息处理器[{}]", msgPack.getSequence());
        //            return;
        //        }

        ISession session = AttributeUtil.get(channel, SessionKey.SESSION);

        if (session == null) {
            return;
        }

        Integer msgId = msgPool.getMessageId(message);

        if (msgId == null) {
            log.error("消息未注册:{}", message);
            return;
        }

        log.debug("收到消息:" + msgId);

        @Nullable AbstractHandler handler = msgPool.getHandler(msgId);
        if (handler == null) {
            log.error("msgId:{} 未注册handler", msgId);
            return;
        }

        handler.setMessage(message);
        handler.setParam(session);
        handler.setSession(session);
        handler.doAction();
        //        processor.process(handler);
    }

    public IProcessor getProcessor(int queueId) {
        return processors.get(queueId);
    }
}
