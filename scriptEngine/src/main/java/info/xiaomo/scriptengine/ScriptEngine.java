package info.xiaomo.scriptengine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 脚本引擎.
 *
 * <p>初始化脚本和重新加载脚本</p>
 *
 * @author 张力
 * @date 2018/2/25 15:23
 */
public class ScriptEngine {


    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptEngine.class);

    private static Map<Class<?>, IScript> script_1_to_1 = new HashMap<>();

    private static Map<Class<?>, List<IScript>> script_1_to_n = new HashMap<>();

    private static boolean dev = false;



    public static void setDev(boolean dev) {
        ScriptEngine.dev = dev;
    }

    /**
     * 通过指定的引导事件来初始化脚本引擎
     *
     * @param bootstrapImpl
     */
    public static boolean load(String packageName, String bootstrapImpl) {
        ScriptClassLoader classLoader = new ScriptClassLoader(packageName, dev);
        Class<?> clazz;
        try {
            clazz = classLoader.loadClass(bootstrapImpl);
        } catch (ClassNotFoundException e) {
            LOGGER.error("找不到指定的脚本启动实现类：" + bootstrapImpl, e);
            return false;
        }

        BootstrapScript bootstrapScript;
        try {
            bootstrapScript = (BootstrapScript) clazz.newInstance();
        } catch (Exception e) {
            LOGGER.error("实例化脚本启动实现类发生错误：" + bootstrapImpl, e);
            return false;
        }

        Map<Class<?>, IScript> script_1_to_1 = new HashMap<>();

        Map<Class<?>, List<IScript>> script_1_to_n = new HashMap<>();


        List<Class<? extends IScript>> scriptList = bootstrapScript.registerScript();

        //去重
        List<Class<? extends IScript>> uniqueScriptList = new ArrayList<>();
        for(Class<? extends IScript> script : scriptList) {
            if(uniqueScriptList.contains(script)) {
                LOGGER.warn("脚本[{}]重复注册", script.getName());
                continue;
            }
            uniqueScriptList.add(script);
        }


        for (Class<? extends IScript> scriptImpl : uniqueScriptList) {
            List<Class<?>> scriptIntfList = fetchInterface(scriptImpl);

            if (scriptIntfList.isEmpty()) {
                LOGGER.error("注册脚本[{}]没有实现任何脚本事件接口", scriptImpl.getName());
                continue;
            }

            try {
                IScript script = scriptImpl.newInstance();
                for (Class<?> intf : scriptIntfList) {
                    if (script_1_to_n.containsKey(intf)) {
                        //已经是1对N的关系，直接加入列表
                        List<IScript> list = script_1_to_n.get(intf);
                        list.add(script);
                        script_1_to_n.put(intf, list);
                    } else if (script_1_to_1.containsKey(intf)) {
                        //目前为止是1对1的关系，修改为1对N的关系
                        IScript exist = script_1_to_1.remove(intf);
                        List<IScript> list = new ArrayList<>();
                        list.add(exist);
                        list.add(script);
                        script_1_to_n.put(intf, list);
                    } else {
                        //1对1
                        script_1_to_1.put(intf, script);
                    }
                }

            } catch (Exception e) {
                LOGGER.error("检查接口注册失败,script:" + scriptImpl.getName(), e);
                return false;
            }

        }

        //整体替换
        ScriptEngine.script_1_to_1 = script_1_to_1;
        ScriptEngine.script_1_to_n = script_1_to_n;

        return true;

    }

    /**
     * 获取脚本实现的事件接口
     *
     * @param scriptImpl
     * @return
     */
    private static List<Class<?>> fetchInterface(Class<? extends IScript> scriptImpl) {

        Class<?>[] interfaces = scriptImpl.getInterfaces();

        List<Class<?>> ret = new ArrayList<>();
        for (Class<?> clazz : interfaces) {

            if (clazz.equals(IScript.class)) {
                continue;
            }

            if (!IScript.class.isAssignableFrom(clazz)) {
                continue;
            }
            ret.add(clazz);
        }

        return ret;
    }

    /*private static IScript[] addToArray(IScript[] oldArray, IScript script) {
        if(oldArray.length == 0) {
            return new IScript[]{script};
        }
        IScript[] newArray = new IScript[oldArray.length + 1];
        System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
        newArray[newArray.length - 1] = script;
        return newArray;
    }*/


    public static <T extends IScript> T get1t1(Class<T> clazz) {
        T ret = (T) script_1_to_1.get(clazz);
        if (ret != null) {
            return ret;
        }
        List<IScript> list = script_1_to_n.get(clazz);
        if (list != null && !list.isEmpty()) {
            LOGGER.warn("1对N类型的脚本被当做1对1类型来使用，脚本接口：", clazz.getName());
            return (T) list.get(0);
        }
        return null;
    }

    public static <T extends IScript> List<T> get1tn(Class<T> clazz) {
        List<IScript> ret = script_1_to_n.get(clazz);
        if (ret != null) {
            return (List<T>) ret;
        }
        IScript script = script_1_to_1.get(clazz);
        if (script != null) {
            LOGGER.warn("1对1类型的脚本被当做1对N类型来使用，脚本接口：", clazz.getName());
            ret = new ArrayList<>(1);
            ret.add(script);
            return (List<T>) ret;
        }
        return Collections.emptyList();
    }

}
