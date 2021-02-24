package info.xiaomo.gengine.http.entity;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import lombok.Data;

/**
 * http请求
 *
 * @author 张力 2017/12/22 13:27
 */
@Data
public class Request {

    private final URI uri;
    private final HttpMethod method;
    private Map<String, String> parameters = new HashMap<>();
    private String path;

    private boolean parameterParsed = false;

    private FullHttpRequest msg;

    private Channel channel;

    private boolean keepAlive;

    public Request(Channel channel, FullHttpRequest msg) throws URISyntaxException {
        this.method = msg.method();
        this.uri = new URI(msg.uri());
        this.path = uri.getPath();
        this.msg = msg;
        this.channel = channel;
    }

    public void sendResponse(Response res) {
        //        if (HttpHeaderUtil.isKeepAlive(telegram)) {
        //            this.channel.writeAndFlush(res);
        //        } else {
        //            res.setKeepAlive(this.keepAlive);
        //            this.channel.writeAndFlush(res).addListener(ChannelFutureListener.CLOSE);
        //        }
    }

    public String getParameter(String name) {
        if (!parameterParsed) {
            parseParameter();
        }
        return parameters.get(name);
    }

    public Map<String, String> getParameters() {
        if (!parameterParsed) {
            parseParameter();
        }
        return parameters;
    }

    private void parseParameter() {

        this.parameterParsed = true;

        // 解析query参数，默认使用UTF8编码
        QueryStringDecoder queryDecoder = new QueryStringDecoder(this.uri);

        queryDecoder
                .parameters()
                .forEach(
                        (k, v) -> {
                            if (v != null && v.size() > 0) {
                                parameters.put(k, v.get(0));
                            }
                        });

        if (this.method == HttpMethod.GET) {
            return;
        }
        // 解析post参数
        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(this.msg);
        postDecoder
                .getBodyHttpDatas()
                .forEach(
                        v -> {
                            if (v.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                                Attribute attribute = (Attribute) v;
                                try {
                                    this.parameters.put(attribute.getName(), attribute.getValue());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
    }
}
