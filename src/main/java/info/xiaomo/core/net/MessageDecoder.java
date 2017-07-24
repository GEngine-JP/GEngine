package info.xiaomo.core.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class MessageDecoder extends LengthFieldBasedFrameDecoder {

    private WebSocketServerHandshaker handShaker;

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageDecoder.class);

    private MessagePool msgPool;

    private int port;


    /**
     * @param maxFrameLength      最大长度
     * @param lengthFieldOffset   长度字段的偏移位置
     * @param lengthFieldLength   长度字段的长度
     * @param lengthAdjustment    长度调整，比如说 -2 那么实际上长度 就变成了 消息中的长度  - (-2)
     * @param initialBytesToStrip 解码返回的byte中，需要跳过的字节数，比如说可以设置跳过头部信息
     * @throws IOException
     */
    private MessageDecoder(MessagePool msgPool, int port, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
                           int lengthAdjustment, int initialBytesToStrip) throws IOException {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
        this.msgPool = msgPool;
        this.port = port;
    }

    public MessageDecoder(MessagePool msgPool, int port) throws IOException {
        this(msgPool, port, 1024 * 1024, 0, 4, -4, 0);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, ((FullHttpRequest) msg));
        } else if (msg instanceof WebSocketFrame) {
            ByteBuf byteBuf = handleWebSocketFrame(ctx, (WebSocketFrame) msg);
            msg = decode(ctx, byteBuf);
        }
        if (msg != null) {
            super.channelRead(ctx, msg);
        }
    }


    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if (in == null) {
            return null;
        }
        try {
            final int length = in.readInt();
            final int id = in.readInt(); // 消息
            final short sequence = in.readShort();

            Message message = msgPool.get(id);
            if (message == null) {
                LOGGER.error("未注册的消息,id:" + id);
                return null;
            }

            message.setLength(length);
            message.setSequence(sequence);

            message.decode(in);
            LOGGER.debug("解析消息:" + message);
            return message;
        } catch (Exception e) {
            LOGGER.error(ctx.channel() + "消息解码异常", e);
            return null;
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }


    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        if (!req.decoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
            return;
        }

        // 构造握手响应返回，本机测试
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "ws://localhost:" + port + "/websocket", null, false);
        handShaker = wsFactory.newHandshaker(req);
        if (handShaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handShaker.handshake(ctx.channel(), req);
        }
    }


    private ByteBuf handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {

        // 判断是否是关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            handShaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return null;
        }

        // 判断是否是Ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
        }
        return frame.content();
    }


    private static void sendHttpResponse(ChannelHandlerContext ctx,
                                         FullHttpRequest req, FullHttpResponse res) {
        // 返回应答给客户端
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            HttpUtil.setContentLength(res, res.content().readableBytes());
        }

        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpUtil.isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
