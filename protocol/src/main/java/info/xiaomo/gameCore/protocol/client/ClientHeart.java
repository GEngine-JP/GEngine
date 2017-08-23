package info.xiaomo.gameCore.protocol.client;


import info.xiaomo.gameCore.protocol.Message;

/**
 * 心跳，处理client的channel心跳，负责发送ping消息到服务器端
 * Created by 张力 on 2017/6/30.
 */
public class ClientHeart implements Runnable {

    private PingMessageFactory pingMessageFactory;

    private Client client;

    public ClientHeart(PingMessageFactory pingMessageFactory, Client client) {
        this.pingMessageFactory = pingMessageFactory;
        this.client = client;
    }

    @Override
    public void run() {

        Message ping = pingMessageFactory.getPingMessage();
        client.ping(ping);

    }


    public interface PingMessageFactory {


        Message getPingMessage();


    }
}
