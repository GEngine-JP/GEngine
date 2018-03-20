package info.xiaomo.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import info.xiaomo.http.annotation.Controller;
import info.xiaomo.http.annotation.Json;
import info.xiaomo.http.annotation.Path;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * 请求分发器
 *
 * @author 张力
 * @date 2017/12/23 09:33
 */
public class Dispatcher {

    public static Logger LOGGER = LoggerFactory.getLogger(Dispatcher.class);

    private static Map<String, RequestHandler> REQ_MAPPING = new HashMap<>();


    private static Response NOT_FOUND = notFondResponse();

    public static void load() {
        String packageName = Dispatcher.class.getPackage().getName();
        load(packageName);
    }


    public static void load(String packageName) {

        //加载所有@Path的方法
        Set<Class<?>> controllerClasses = ClassUtil.findClassWithAnnotaion(packageName, Controller.class);

        for (Class<?> clazz : controllerClasses) {

            Method[] methods = clazz.getMethods();
            Object handlerInstance = null;
            for (Method method : methods) {
                Path annotation = method.getAnnotation(Path.class);
                if (annotation == null) {
                    continue;
                }

                Class<?>[] parameterTypes = method.getParameterTypes();

                if (parameterTypes.length != 1) {
                    LOGGER.error("[" + clazz.getName() + "." + method.getName() + "]方法存在Path注解，但是参数不止一个");
                    continue;
                }

                if (parameterTypes[0] != Request.class) {
                    LOGGER.error("[" + clazz.getName() + "." + method.getName() + "]方法存在Path注解，但是参数不是Request类型型");
                    continue;
                }

                String path = annotation.value();

                if (REQ_MAPPING.containsKey(path)) {
                    RequestHandler old = REQ_MAPPING.get(path);
                    LOGGER.error("[" + clazz.getName() + "." + method.getName() + "]路径重复," + path + "已经指向[" + old.handler.getClass().getName() + "." + old.method.getName() + "]");
                    continue;
                }


                if (handlerInstance == null) {
                    handlerInstance = createHandler(clazz);
                }

                RequestHandler handler = new RequestHandler(handlerInstance, method);
                if (method.getAnnotation(Json.class) != null) {
                    handler.responseJson = true;
                }
                REQ_MAPPING.put(path, handler);
                LOGGER.error("发现controller," + path + "->" + clazz.getName() + "." + method.getName());
            }

        }

    }


    public static Response dispatch(Request request) {
        String path = request.getPath();
        RequestHandler mapping = REQ_MAPPING.get(path);
        if (mapping == null) {
            return NOT_FOUND;
        }
        Object ret = mapping.doAction(request);

        if (ret == null) {
            return null;
        }

        if (ret instanceof Response) {
            //如果是Response那么直接返回
            return (Response) ret;
        }

        String resStr;
        if (mapping.responseJson) {
            //json编码
            resStr = JSON.toJSONString(ret, SerializerFeature.WriteNonStringKeyAsString);
        } else {
            resStr = ret.toString();
        }

        Response res = new Response();
        res.setStatus(HttpResponseStatus.OK);
        res.setContent(resStr);

        return res;
    }

    public static Object createHandler(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("创建handler失败,class:[" + clazz.getName() + "]", e);
        }
    }

    public static Response notFondResponse() {
        Response notFound = new Response();
        notFound.setStatus(Response.NOT_FOUND);
        notFound.setContent("Not found.");
        return notFound;
    }

    private static class RequestHandler {

        Method method;

        Object handler;

        boolean responseJson;

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

}
