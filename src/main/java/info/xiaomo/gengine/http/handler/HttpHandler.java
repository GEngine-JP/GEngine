package info.xiaomo.gengine.http.handler;

import info.xiaomo.gengine.http.entity.Response;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http请求处理器
 *
 * @author 张力 2017/12/22 15:33
 */
public class HttpHandler extends ChannelHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpHandler.class);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("请求发生错误", cause);
    }

    /**
     * 空返回
     *
     * @return
     */
    public Response emptyResponse() {
        Response res = new Response();
        res.setStatus(Response.OK);
        return res;
    }

    /**
     * 内部错误
     *
     * @param e
     * @return
     */
    public Response innerError(Exception e) {
        Response res = new Response();
        res.setStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
        res.setContent(toString(e));
        return res;
    }

    /**
     * 解析错误堆栈信息
     *
     * @param e
     * @return
     */
    public String toString(Throwable e) {
        StringBuilder builder = new StringBuilder();
        builder.append(e.getClass().getName()).append(": ").append(e.getMessage()).append("\n");
        StackTraceElement[] stackTrace = e.getStackTrace();
        for (StackTraceElement se : stackTrace) {
            builder.append("\tat ").append(se).append("\n");
        }
        Throwable cause = e.getCause();
        while (cause != null) {
            builder.append(causedBy(cause));
            cause = cause.getCause();
        }
        return builder.toString();
    }

    private String causedBy(Throwable e) {
        StringBuilder builder = new StringBuilder();
        builder.append("Caused by: ")
                .append(e.getClass().getName())
                .append(": ")
                .append(e.getMessage())
                .append("\n");
        StackTraceElement[] stackTrace = e.getStackTrace();
        int length = stackTrace.length;
        if (length > 0) {
            builder.append("\tat ").append(stackTrace[0]).append("\n");
        }
        if (length > 1) {
            builder.append("\t... ").append(length - 1).append(" utils frames omitted\n");
        }

        return builder.toString();
    }
}
