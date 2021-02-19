package info.xiaomo.gengine.script.zl;

import java.net.URL;
import java.net.URLClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 和jvm使用同一个classpath的脚本类加载器
 *
 * @author 张力
 * @date 2018/3/3 14:08
 */
public class ScriptClassLoader extends URLClassLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptClassLoader.class);

    private String classPackage;

    private ClassLoader defaultClassLoader;

    /**
     * 开发模式，开发模式直接加载，不走自定义加载逻辑
     */
    private boolean dev;

    public static ScriptClassLoader newInstance(URL url, String classPackage, boolean dev){
        URL[] urls;
        if(dev) {
            //调试模式下用父加载器的URL列表
            urls = ((URLClassLoader) ScriptClassLoader.class.getClassLoader()).getURLs();
        } else {
            urls = new URL[]{url};
        }

        return new ScriptClassLoader(urls,classPackage, dev);

    }


    protected ScriptClassLoader(URL[] urls, String classPackage, boolean dev) {
        super(urls);
        this.classPackage = classPackage;
        this.dev = dev;
        this.defaultClassLoader = ScriptClassLoader.class.getClassLoader();
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {


        if (!this.dev && name.startsWith(classPackage)) {
            //系统默认实现是有锁的，此处没有，所以逻辑必须保证线程安全

            //此方法和URLClassLoDader不同的地方是指定包下面的类，不适用双亲委派模型
            //那么就算脚本也在虚拟机ClassPath下面，也会通过这里直接去加载类，是不会为委派给父类

            Class<?> c = findLoadedClass(name);

            if (c == null) {
                c = findClass(name);
                LOGGER.info("加载类[{}]完毕.", name);
            } else {
                LOGGER.info("类[{}]已加载，无需再次加载.", name);
            }

            if (resolve) {
                resolveClass(c);
            }
            return c;
        } else {
            //非指定包下的类，就用父类加载逻辑
            return super.loadClass(name, resolve);
        }

    }


}
