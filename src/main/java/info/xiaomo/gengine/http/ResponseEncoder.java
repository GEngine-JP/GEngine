package info.xiaomo.gengine.http;

import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpVersion;

/**
 * 将自定义的Response转化为Netty的HttpResponse
 * @author 张力
 *  2017/12/22 20:23
 */
public class ResponseEncoder extends MessageToMessageEncoder<Response> {



    @Override
    protected void encode(ChannelHandlerContext ctx, Response msg, List<Object> out) throws Exception {

        String content = msg.getContent();
        int byteBufLen = 0;
        if (content != null && content.length() > 0) {
            byteBufLen = content.length();
        }
        ByteBuf buf;
        if (byteBufLen > 0) {
            buf = ctx.alloc().buffer(byteBufLen);
            buf.writeBytes(content.getBytes());
        } else {
            buf = Unpooled.EMPTY_BUFFER;
        }

        DefaultFullHttpResponse httpResponse
                = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, msg.getStatus(), buf);
        httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, String.format("%s;charset=%s",msg.getContentType(),msg.getCharset()));
        httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, httpResponse.content().toString());
        if(msg.isKeepAlive()) {
            httpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        out.add(httpResponse);
    }
}
