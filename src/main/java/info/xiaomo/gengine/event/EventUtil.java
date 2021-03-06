package info.xiaomo.gengine.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 由于事件都是预先注册好的，所以这里不考虑多线程问题，不允许在游戏运行过成中动态添加观察者
 *
 * @author 张力 2014-12-6 上午11:19:00
 */
public class EventUtil {

    /** 游戏启动就初始化的监听者列表 */
    private static final Map<Integer, List<IEventListener>> PREPARED_LISTENERS = new HashMap<>(10);

    /** 游戏运行中动态添加的监听者列表 */
    private static final Logger LOGGER = LoggerFactory.getLogger(EventUtil.class);

    public static void addListener(IEventListener listener, Integer type) {
        List<IEventListener> listenerList =
                PREPARED_LISTENERS.computeIfAbsent(type, k -> new ArrayList<>());
        listenerList.add(listener);
    }

    public static void fireEvent(Integer type) {
        fireEvent(type, null);
    }

    public static void fireEvent(Integer type, Object obj) {

        List<IEventListener> listenerList = PREPARED_LISTENERS.get(type);
        if (listenerList != null) {
            for (IEventListener listener : listenerList) {
                try {
                    listener.update(type, obj);
                } catch (Exception e) {
                    LOGGER.error("事件执行错误", e);
                }
            }
        }
    }
}
