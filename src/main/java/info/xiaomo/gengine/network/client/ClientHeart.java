package info.xiaomo.gengine.network.client;

import com.google.protobuf.AbstractMessage;

/** 心跳，处理client的channel心跳，负责发送ping消息到服务器端 Created by 张力 on 2017/6/30. */
public class ClientHeart implements Runnable {

    private final PingMessageFactory pingMessageFactory;

    private final Client client;

    public ClientHeart(PingMessageFactory pingMessageFactory, Client client) {
        this.pingMessageFactory = pingMessageFactory;
        this.client = client;
    }

    @Override
    public void run() {
        if (!client.connected) {
            return;
        }
        AbstractMessage ping = pingMessageFactory.getPingMessage();
        client.ping(ping);
    }

    public interface PingMessageFactory {

        AbstractMessage getPingMessage();
    }
}
