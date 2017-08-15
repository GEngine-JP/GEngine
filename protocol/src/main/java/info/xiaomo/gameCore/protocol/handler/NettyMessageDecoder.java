package info.xiaomo.gameCore.protocol.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.nio.ByteOrder;

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
 * Date  : 2017/8/12 15:34
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {
    public static final int DEFAULT_LENGTH_FIELD_LENGTH = 4;
    public static final int DEFAULT_MAX_FRAME_LENGTH = 1048576;
    private MessageDecoder decoder;

    public NettyMessageDecoder(MessageDecoder decoder) {
        this(decoder, 1048576);
    }

    public NettyMessageDecoder(MessageDecoder decoder, int maxFrameLength) {
        this(maxFrameLength, 0, 4, 0, 4);
        this.decoder = decoder;
    }

    public NettyMessageDecoder(MessageDecoder decoder, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(1048576, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
        this.decoder = decoder;
    }

    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }

    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
    }

    public NettyMessageDecoder(ByteOrder byteOrder, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
        super(byteOrder, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
    }

    public NettyMessageDecoder(MessageDecoder decoder, ByteOrder byteOrder, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
        super(byteOrder, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
        this.decoder = decoder;
    }


    /**
     * 解码
     *
     * @param ctx ctx
     * @param in  in
     * @return Object
     * @throws Exception Exception
     */
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in)
            throws Exception {
        ByteBuf byteBuf = (ByteBuf) super.decode(ctx, in);
        if (byteBuf == null) {
            return null;
        }
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        return this.decoder.decode(bytes);
    }
}