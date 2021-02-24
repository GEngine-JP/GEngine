package info.xiaomo.gengine.http.handler;

import java.lang.reflect.Method;
import info.xiaomo.gengine.http.entity.Request;

public class RequestHandler {

    public Method method;

    public Object handler;

    public boolean responseJson;

    public RequestHandler(Object handler, Method method) {
        this.method = method;
        this.handler = handler;
    }

    public Object doAction(Request request) {
        try {
            return this.method.invoke(this.handler, request);
        } catch (Exception e) {
            throw new RuntimeException("请求执行错误", e);
        }
    }
}
