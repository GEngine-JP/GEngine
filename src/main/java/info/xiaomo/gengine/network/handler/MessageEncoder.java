package info.xiaomo.gengine.network.handler;

import info.xiaomo.gengine.network.MsgPack;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiaomo 我们的数据包（即一条游戏前后端通信的消息长度）可以定义如下： 数据包 = 1字节标志位 + 2字节消息体长度 + 4字节协议号长度 + N消息体
 *     比如客户端请求登录的Protobuf协议如下：
 */
@Slf4j
public class MessageEncoder extends MessageToByteEncoder<MsgPack> {

    public MessageEncoder() {}

    @Override
    protected void encode(ChannelHandlerContext ctx, MsgPack msgPack, ByteBuf buf) {
        buf.writeByte(msgPack.getHead());
        buf.writeShort(msgPack.getBytes().length + 4);
        buf.writeInt(msgPack.getMsgId());
        buf.writeBytes(msgPack.getBytes());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
