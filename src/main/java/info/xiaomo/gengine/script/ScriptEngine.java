package info.xiaomo.gengine.script;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarFile;
import javax.script.ScriptException;
import info.xiaomo.gengine.script.annotation.Exclude;
import info.xiaomo.gengine.script.annotation.Script;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 脚本引擎.
 *
 * <p>
 *
 * <p>初始化脚本和重新加载脚本
 *
 * @author 张力
 * @date 2018/2/25 15:23
 */
public class ScriptEngine {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptEngine.class);

    private static Map<Class<?>, IScript> script_1_to_1 = new HashMap<>();

    private static Map<Class<?>, List<IScript>> script_1_to_n = new HashMap<>();

    private static boolean dev = false;

    private static String scriptJarFile = null;

    static {
        scriptJarFile = System.getProperty("game.script.file");
        String devBoolean = System.getProperty("game.script.dev", "false");
        dev = Boolean.parseBoolean(devBoolean);
        LOGGER.info("当前脚本模式：{},script path:{}", dev, scriptJarFile);
    }

    public static void setDev(boolean dev) {
        ScriptEngine.dev = dev;
    }

    public static void setScriptJarFile(String scriptJarFile) {
        ScriptEngine.scriptJarFile = scriptJarFile;
    }

    /**
     * 通过Script注解加载脚本
     *
     * @param packageName 所在包名
     * @return
     */
    public static boolean load(String packageName) throws ScriptException {

        URL url = null;
        JarFile jarFile = null;
        if (scriptJarFile != null) {
            File file = new File(scriptJarFile);
            if (file.exists()) {
                try {
                    url = file.toURI().toURL();
                    jarFile = new JarFile(new File(scriptJarFile));

                } catch (IOException e) {
                    LOGGER.error("脚本文件URL读取失败,file:" + file.getAbsolutePath(), e);
                }
            }
        }

        if (!dev && (url == null || jarFile == null)) {
            throw new ScriptException(
                    "非调试模式下必须通过-Dgame.script.file参数设置脚本jar包所在路径,设置调试模式请使用-Dgame.script.dev=true");
        }

        ScriptClassLoader classLoader = ScriptClassLoader.newInstance(url, packageName, dev);

        Set<Class<?>> classList;

        if (dev) {
            classList = ClassUtil.findClassWithAnnotation(classLoader, packageName, Script.class);
        } else {
            classList =
                    ClassUtil.findClassWithAnnotation(
                            classLoader, packageName, jarFile, Script.class);
        }
        return checkAndLoad(classList, true);
    }

    /**
     * 通过指定的引导事件来初始化脚本引擎
     *
     * @param packageName 脚本所在包名
     * @param bootstrapImpl 脚本的引导实现类
     */
    public static boolean load(String packageName, String bootstrapImpl) throws ScriptException {

        URL url = null;
        if (scriptJarFile != null) {
            File file = new File(scriptJarFile);
            if (file.exists()) {
                try {
                    url = file.toURI().toURL();
                } catch (MalformedURLException e) {
                    LOGGER.error("脚本文件URL读取失败,file:" + file.getAbsolutePath(), e);
                }
            }
        }

        if (!dev && url == null) {
            throw new ScriptException("非调试模式下必须通过-Dscript.file参数设置脚本jar包所在路径");
        }
        ScriptClassLoader classLoader = ScriptClassLoader.newInstance(url, packageName, dev);

        Class<?> clazz;
        try {
            clazz = classLoader.loadClass(bootstrapImpl);
        } catch (ClassNotFoundException e) {
            LOGGER.error("找不到指定的脚本启动实现类：" + bootstrapImpl, e);
            return false;
        }

        BootstrapScript bootstrapScript;
        try {
            bootstrapScript = (BootstrapScript) clazz.getConstructor().newInstance();
        } catch (Exception e) {
            LOGGER.error("实例化脚本启动实现类发生错误：" + bootstrapImpl, e);
            return false;
        }

        List<Class<? extends IScript>> scriptList = bootstrapScript.registerScript();

        // 去重
        List<Class<?>> uniqueScriptList = new ArrayList<>();
        for (Class<? extends IScript> script : scriptList) {
            if (uniqueScriptList.contains(script)) {
                LOGGER.warn("脚本[{}]重复注册", script.getName());
                continue;
            }
            uniqueScriptList.add(script);
        }

        return checkAndLoad(uniqueScriptList, false);
    }

    /**
     * 检查并加载脚本
     *
     * @param classList 列表
     * @param annotation 是否是通过注解方式注册的脚本
     * @return
     */
    @SuppressWarnings("unchecked")
    private static boolean checkAndLoad(Collection<Class<?>> classList, boolean annotation)
            throws ScriptException {

        Map<Class<?>, IScript> script1To1 = new HashMap<>(10);

        Map<Class<?>, List<IScript>> script1ToN = new HashMap<>(10);

        Map<IScript, Integer> orderMap = new HashMap<>();

        for (Class<?> scriptImpl : classList) {

            if (!IScript.class.isAssignableFrom(scriptImpl)) {
                LOGGER.error("注册脚本[{}]不是IScript的子类", scriptImpl.getName());
                continue;
            }

            if (annotation) {
                Exclude excludeAnnotation = scriptImpl.getAnnotation(Exclude.class);
                if (excludeAnnotation != null) {
                    continue;
                }
            }

            int modifiers = scriptImpl.getModifiers();
            if (Modifier.isAbstract(modifiers)) {
                throw new ScriptException("脚本[{" + scriptImpl.getName() + "}]是一个抽象类或者接口");
            }

            List<Class<?>> scriptIntfList = fetchInterface(scriptImpl);
            if (scriptIntfList.isEmpty()) {
                LOGGER.error("注册脚本[{}]没有实现任何脚本事件接口", scriptImpl.getName());
                continue;
            }

            try {
                IScript script = (IScript) scriptImpl.getConstructor().newInstance();

                if (annotation) {
                    Script scriptAnnotation = scriptImpl.getAnnotation(Script.class);
                    // 序号
                    int order = scriptAnnotation.order();
                    orderMap.put(script, order);
                }

                for (Class<?> intf : scriptIntfList) {
                    if (script1ToN.containsKey(intf)) {
                        // 已经是1对N的关系，直接加入列表
                        List<IScript> list = script1ToN.get(intf);
                        list.add(script);
                        script1ToN.put(intf, list);
                    } else if (script1To1.containsKey(intf)) {
                        // 目前为止是1对1的关系，修改为1对N的关系
                        IScript exist = script1To1.remove(intf);
                        List<IScript> list = new ArrayList<>();
                        list.add(exist);
                        list.add(script);
                        script1ToN.put(intf, list);
                    } else {
                        // 1对1
                        script1To1.put(intf, script);
                    }
                }

            } catch (Exception e) {
                LOGGER.error("检查接口注册失败,script:" + scriptImpl.getName(), e);
                return false;
            }
        }

        if (annotation) {
            // 排序
            script1ToN.forEach((k, v) -> v.sort(Comparator.comparingInt(orderMap::get)));
        }

        // 整体替换
        ScriptEngine.script_1_to_1 = script1To1;
        ScriptEngine.script_1_to_n = script1ToN;
        return true;
    }

    /**
     * 获取脚本实现的事件接口
     *
     * @param scriptImpl
     * @return
     */
    private static List<Class<?>> fetchInterface(Class<?> scriptImpl) {

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

    @SuppressWarnings("unchecked")
    public static <T extends IScript> T get1t1(Class<T> clazz) {
        T ret = (T) script_1_to_1.get(clazz);
        if (ret != null) {
            return ret;
        }
        List<IScript> list = script_1_to_n.get(clazz);
        if (list != null && !list.isEmpty()) {
            LOGGER.warn("1对N类型的脚本被当做1对1类型来使用，脚本接口：{}", clazz.getName());
            return (T) list.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T extends IScript> List<T> get1tn(Class<T> clazz) {
        List<IScript> ret = script_1_to_n.get(clazz);
        if (ret != null) {
            return (List<T>) ret;
        }
        IScript script = script_1_to_1.get(clazz);
        if (script != null) {
            LOGGER.warn("1对1类型的脚本被当做1对N类型来使用，脚本接口：{}", clazz.getName());
            ret = new ArrayList<>(1);
            ret.add(script);
            return (List<T>) ret;
        }
        return Collections.emptyList();
    }
}
