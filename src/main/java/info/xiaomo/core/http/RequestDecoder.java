package info.xiaomo.core.http;

import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * http请求解码器，该解码器会把FullHttpRequest转化成自定义Request
 * @author 张力
 * @date 2017/12/22 20:19
 */
public class RequestDecoder extends MessageToMessageDecoder<FullHttpRequest> {
    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpRequest msg, List<Object> out) throws Exception {
        out.add(new Request(ctx.channel(),msg.retain()));
    }
}
