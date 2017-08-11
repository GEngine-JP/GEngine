package info.xiaomo.gameCore.base.net;


import info.xiaomo.gameCore.base.concurrent.IQueueCommand;

/**
 * 网络请求的消息，该消息继承了队列执行命令接口，可以直接放入QueueDriver中执行
 *
 * @author xiaomo
 */
public interface Message extends IQueueCommand {

    void decode(byte[] buf);

    byte[] encode();

    int length();

    void setLength(int length);

    int getId();

    int getQueueId();

    void setSequence(short sequence);

    short getSequence();


}
