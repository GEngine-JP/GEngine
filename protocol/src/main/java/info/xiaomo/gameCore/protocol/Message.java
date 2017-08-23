package info.xiaomo.gameCore.protocol;


import com.google.protobuf.InvalidProtocolBufferException;
import info.xiaomo.gameCore.base.concurrent.IQueueDriverCommand;
import io.netty.buffer.ByteBuf;

/**
 * 网络请求的消息，该消息继承了队列执行命令接口，可以直接放入QueueDriver中执行
 *
 * @author 张力
 */
public interface Message extends IQueueDriverCommand {

    void decode(byte[] bytes) throws InvalidProtocolBufferException;

    ByteBuf encode();

    byte[] getContent();

    int length();

    void setLength(int length);

    int getId();

    int getQueueId();

}
