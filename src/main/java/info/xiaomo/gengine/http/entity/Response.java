package info.xiaomo.gengine.http.entity;

import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * http返回，该消息只支持<tt>text/plain</tt>类型;
 *
 * @author 张力 2017/12/22 20:20
 */
public class Response {

    /** 文本了类型 */
    public static final String CONTENT_TYPE_TEXT = "text/plain";

    public static final HttpResponseStatus OK = HttpResponseStatus.OK;

    public static final HttpResponseStatus NOT_FOUND = HttpResponseStatus.NOT_FOUND;
    /** 状态 */
    public HttpResponseStatus status;
    /** 消息正文 */
    private String content;
    private String contentType;

    private String charset;

    private boolean keepAlive;

    public Response() {
        this.contentType = CONTENT_TYPE_TEXT;
        this.charset = "utf-8";
        this.status = OK;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public HttpResponseStatus getStatus() {
        return status;
    }

    public void setStatus(HttpResponseStatus status) {
        this.status = status;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }
}
