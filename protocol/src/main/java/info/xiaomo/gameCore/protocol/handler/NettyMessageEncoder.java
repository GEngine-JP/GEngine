package info.xiaomo.gameCore.protocol.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 * <p>
 * author: xiaomo
 * github: https://github.com/xiaomoinfo
 * email : xiaomo@xiaomo.info
 * QQ    : 83387856
 * Date  : 2017/8/12 15:31
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NettyMessageEncoder extends MessageToByteEncoder<Object> {
    public static final int DEFAULT_LENGTH_FIELD_LENGTH = 4;

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyMessageEncoder.class);

    private MessageEncoder encoder;

    private int lengthFieldLength;

    public NettyMessageEncoder(MessageEncoder encoder) {
        this(encoder, 4);
    }

    public NettyMessageEncoder(MessageEncoder encoder, int lengthFieldLength) {
        this.encoder = encoder;
        this.lengthFieldLength = lengthFieldLength;
    }

    /**
     * 编码
     *
     * @param ctx ctx
     * @param msg msg
     * @param out out
     * @throws Exception Exception
     */
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        byte[] bytes = this.encoder.encode(ctx);
        if ((bytes == null) || (bytes.length == 0)) {
            return;
        }
        boolean writable = out.isWritable(this.lengthFieldLength + bytes.length);
        if (!writable) {
            LOGGER.error("消息过大, 编码失败【{}】", bytes.length);
            return;
        }
        out.writeShort(bytes.length + this.lengthFieldLength).writeBytes(bytes);
    }
}
