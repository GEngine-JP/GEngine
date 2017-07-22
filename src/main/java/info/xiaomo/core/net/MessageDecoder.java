package info.xiaomo.core.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class MessageDecoder extends SimpleChannelInboundHandler<Object> {

    private WebSocketServerHandshaker handShaker;

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageDecoder.class);

    private MessagePool msgPool;


    public MessageDecoder(MessagePool msgPool) {
        this.msgPool = msgPool;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // WebSocket接入
        BinaryWebSocketFrame frame = null;
        // 传统的HTTP接入
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
            return;
        } else if (msg instanceof WebSocketFrame) {
            frame = handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
        if (frame == null) {
            return;
        }

        ByteBuf buf = frame.content();
        try {

            final int length = buf.readInt();

            // 消息
            final int id = buf.readInt();

            final short sequence = buf.readShort();


            Message message = msgPool.get(id);
            if (message == null) {
                LOGGER.error("未注册的消息,id:" + id);
                return;
            }

            byte[] bytes = null;
            int remainLength = buf.readableBytes();
            if (remainLength > 0) {
                bytes = new byte[remainLength];
                buf.readBytes(bytes);
            }

            message.setLength(length);
            message.setSequence(sequence);
            if (bytes != null) {
                message.decode(bytes);
            }
            LOGGER.debug("解析消息:" + message);

        } catch (Exception e) {
            LOGGER.error(ctx.channel() + "消息解码异常", e);
        } finally {
            frame.release();
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }


    private void handleHttpRequest(ChannelHandlerContext ctx,
                                   FullHttpRequest req) throws Exception {

        if (!req.decoderResult().isSuccess()
                || (!"websocket".equals(req.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1,
                    BAD_REQUEST));
            return;
        }

        // 构造握手响应返回，本机测试
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "ws://localhost:8888/websocket", null, false);
        handShaker = wsFactory.newHandshaker(req);
        if (handShaker == null) {
            WebSocketServerHandshakerFactory
                    .sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handShaker.handshake(ctx.channel(), req);
        }
    }


    private BinaryWebSocketFrame handleWebSocketFrame(ChannelHandlerContext ctx,
                                                      WebSocketFrame frame) {

        // 判断是否是关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            handShaker.close(ctx.channel(),
                    (CloseWebSocketFrame) frame.retain());
            return null;
        }
        // 判断是否是Ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(
                    new PongWebSocketFrame(frame.content().retain()));
            return null;
        }

        return (BinaryWebSocketFrame) frame;

    }


    private static void sendHttpResponse(ChannelHandlerContext ctx,
                                         FullHttpRequest req, FullHttpResponse res) {
        // 返回应答给客户端
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(),
                    CharsetUtil.UTF_8);
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
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
