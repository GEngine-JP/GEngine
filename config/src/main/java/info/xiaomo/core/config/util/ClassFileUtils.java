/**
 * 创建日期:  2017年08月21日 19:07
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.core.config.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * calss文件工具 查找指定包下的满足要求的类
 *
 * @author YangQiang
 */
public class ClassFileUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassFileUtils.class);


    /**
     * 获取指定包下所有满足要求的类
     *
     * @param packageName
     * @param predicate
     * @return
     */
    public static Set<Class> getClasses(String packageName, Predicate<Class> predicate) {
        return getClasses(packageName, true, predicate);
    }

    /**
     * 获取指定包下满足要求的类
     *
     * @param packageName 包名
     * @param recursive 是否递归查找
     * @param predicate 判定条件
     * @return
     */
    public static Set<Class> getClasses(String packageName, boolean recursive, Predicate<Class> predicate) {
        Set<Class> ret = new LinkedHashSet<>();
        String packageDirName = packageName.replace('.', '/');
        try {
            Enumeration<URL> dirs = ClassFileUtils.class.getClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    ret.addAll(findSubClassFromFile(packageName, filePath, recursive, predicate));
                } else if ("jar".equals(protocol)) {
                    JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                    ret.addAll(findSubClassFromJar(jar, packageName, recursive, predicate));
                }
            }
        } catch (Exception e) {
            LOGGER.error("读取配置Class文件出错", e);
        }

        return ret;
    }

    private static Set<Class> findSubClassFromJar(JarFile jar, String packageName, boolean recursive, Predicate<Class> predicate) {
        Set<Class> ret = new LinkedHashSet<>();
        Enumeration<JarEntry> entries = jar.entries();

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.isDirectory()) {
                continue;
            }
            String name = entry.getName();
            if (!name.endsWith(".class")) {
                continue;
            }
            if (name.charAt(0) == '/') {
                name = name.substring(1);
            }

            // 去掉后缀后的类完整名
            String className = name.replace('/', '.').substring(0, name.length() - 6);

            String newPackageName = null;
            int idx = className.lastIndexOf('.');
            if (idx != -1) {
                newPackageName = className.substring(0, idx);
            }
            if (newPackageName == null) {
                continue;
            }
            // 不是指定包的类并且不满足子包的类
            if ((!newPackageName.equals(packageName)) && !(recursive && newPackageName.startsWith(packageName))) {
                continue;
            }
            try {
                Class clazz = Class.forName(className, false, ClassFileUtils.class.getClassLoader());
                if (predicate.test(clazz)) {
                    ret.add(clazz);
                }
            } catch (Throwable e) {
                LOGGER.error("读取Jar中的Class文件出错", e);
            }
        }
        return ret;
    }

    private static Set<Class> findSubClassFromFile(String packageName, String packagePath, boolean recursive, Predicate<Class> predicate) {
        Set<Class> ret = new LinkedHashSet<>();
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return ret;
        }
        File[] files = dir.listFiles(file -> (file.getName().endsWith(".class")) || (recursive && file.isDirectory()));
        if (files == null) {
            return ret;
        }
        for (File file : files) {
            String fileName = file.getName();
            if (file.isDirectory()) {
                ret.addAll(findSubClassFromFile(packageName + "." + fileName, file.getAbsolutePath(), recursive, predicate));
                continue;
            }
            String className = packageName + '.' + fileName.substring(0, fileName.length() - 6);
            try {
                Class clazz = Class.forName(className, false, ClassFileUtils.class.getClassLoader());
                if (predicate.test(clazz)) {
                    ret.add(clazz);
                }
            } catch (Exception e) {
                LOGGER.error("读取文件夹中的Class文件出错", e);
            }
        }

        return ret;
    }
}
