package info.xiaomo.gengine.network.handler;

import info.xiaomo.gengine.network.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiaomo
 * 我们的数据包（即一条游戏前后端通信的消息长度）可以定义如下：
 * 数据包 = 1字节标志位 + 2字节消息体长度 + 4字节协议号长度 + N消息体
 * 比如客户端请求登录的Protobuf协议如下：
 */
@Slf4j
public class MessageEncoder extends MessageToByteEncoder<Packet> {


    private final int downLimit;

    public MessageEncoder(int downLimit) {
        this.downLimit = downLimit;
    }


    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf buf) {
        if ((packet.getBytes().length > this.downLimit) && (log.isWarnEnabled()))
            log.warn("packet size[" + packet.getBytes().length + "] is over limit[" + this.downLimit + "]");

        buf.writeByte(packet.getHead());
        buf.writeShort(packet.getBytes().length + 4);
        buf.writeInt(packet.getCmd());
        buf.writeBytes(packet.getBytes());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

}
