package info.xiaomo.gengine.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * class加载工具类
 *
 * @author codingtony
 */
public final class ClassUtil {

    private ClassUtil() {}

    /**
     * 从包package中获取所有的Class 也可获取jar内的class
     *
     * @param packageStr
     * @return
     */
    public static Set<Class<?>> loadClasses(String packageStr) {

        // 第一个class类的集合
        Set<Class<?>> classes = new LinkedHashSet<>();
        // 是否循环迭代
        boolean recursive = true;
        // 获取包的名字 并进行替换
        String packageName = packageStr;
        String packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    // 以文件的方式扫描整个包下的文件 并添加到集合中
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
                } else if ("jar".equals(protocol)) {
                    // 如果是jar包文件
                    // 定义一个JarFile
                    //					System.err.println("jar类型的扫描");
                    JarFile jar;
                    try {
                        // 获取jar
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        // 从此jar包 得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        // 同样的进行循环迭代
                        while (entries.hasMoreElements()) {
                            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            // 如果是以/开头的
                            if (name.charAt(0) == '/') {
                                // 获取后面的字符串
                                name = name.substring(1);
                            }
                            // 如果前半部分和定义的包名相同
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                // 如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    // 获取包名 把"/"替换成"."
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                // 如果可以迭代下去 并且是一个包
                                // 如果是一个.class文件 而且不是目录
                                if (name.endsWith(".class") && !entry.isDirectory()) {
                                    // 去掉后面的".class" 获取真正的类名
                                    String className =
                                            name.substring(
                                                    packageName.length() + 1, name.length() - 6);
                                    try {
                                        // 添加到classes
                                        classes.add(Class.forName(packageName + '.' + className));
                                    } catch (ClassNotFoundException e) {
                                        // .error("添加用户自定义视图类错误 找不到此类的.class文件");
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        // log.error("在扫描用户定义视图时从jar包获取文件出错");
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classes;
    }

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName
     * @param packagePath
     * @param recursive
     * @param classes
     */
    public static void findAndAddClassesInPackageByFile(
            String packageName,
            String packagePath,
            final boolean recursive,
            Set<Class<?>> classes) {
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles =
                dir.listFiles(
                        new FileFilter() {
                            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
                            @Override
                            public boolean accept(File file) {
                                return (recursive && file.isDirectory())
                                        || (file.getName().endsWith(".class"));
                            }
                        });
        // 循环所有文件
        for (File file : Objects.requireNonNull(dirfiles)) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(
                        packageName + "." + file.getName(),
                        file.getAbsolutePath(),
                        recursive,
                        classes);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    // 添加到集合中去
                    // 经过回复同学的提醒，这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
                    classes.add(
                            Thread.currentThread()
                                    .getContextClassLoader()
                                    .loadClass(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    // log.error("添加用户自定义视图类错误 找不到此类的.class文件");
                    e.printStackTrace();
                }
            }
        }
    }

    public static void loadJar(String jarPath) throws Exception {
        // 系统类库路径
        File libPath = new File(jarPath);

        // 获取所有的.jar和.zip文件
        File[] jarFiles =
                libPath.listFiles(
                        new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String name) {
                                return name.endsWith(".jar") || name.endsWith(".zip");
                            }
                        });

        if (jarFiles != null) {
            // 从URLClassLoader类中获取类所在文件夹的方法
            // 对于jar文件，可以理解为一个存放class文件的文件夹
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            boolean accessible = method.isAccessible(); // 获取方法的访问权限
            try {
                if (!accessible) {
                    method.setAccessible(true); // 设置方法的访问权限
                }
                // 获取系统类加载器
                URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
                for (File file : jarFiles) {
                    URL url = file.toURI().toURL();
                    try {
                        method.invoke(classLoader, url);
                        System.out.println(file.getName());
                    } catch (Exception e) {
                        System.out.println(file.getName());
                    }
                }
            } finally {
                method.setAccessible(accessible);
            }
        }
    }

    /**
     * 获取同一路径下所有子类或接口实现类
     *
     * @param cls
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static List<Class<?>> getAllAssignedClass(Class<?> cls)
            throws IOException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        for (Class<?> c : getClasses(cls)) {
            if (cls.isAssignableFrom(c) && !cls.equals(c)) {
                classes.add(c);
            }
        }
        return classes;
    }

    /**
     * 取得当前类路径下的所有类
     *
     * @param cls
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static List<Class<?>> getClasses(Class<?> cls)
            throws IOException, ClassNotFoundException {
        String pk = cls.getPackage().getName();
        String path = pk.replace('.', '/');
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        URL url = classloader.getResource(path);
        return getClasses(new File(url.getFile()), pk);
    }

    /**
     * 迭代查找类
     *
     * @param dir
     * @param pk
     * @return
     * @throws ClassNotFoundException
     */
    private static List<Class<?>> getClasses(File dir, String pk) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        if (!dir.exists()) {
            return classes;
        }
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                classes.addAll(getClasses(f, pk + "." + f.getName()));
            }
            String name = f.getName();
            if (name.endsWith(".class")) {
                classes.add(Class.forName(pk + "." + name.substring(0, name.length() - 6)));
            }
        }
        return classes;
    }

    /**********************************************************************
     * 加载时判断指定包
     * @param filePath
     * @param specialName
     * @param childPackage
     * @return
     */
    public static List<Class<?>> getClassWithSpecialName(
            String filePath, String specialName, boolean childPackage) {
        List<Class<?>> classes = new ArrayList<>();
        List<String> classNames = getClassName(filePath, childPackage);
        for (String className : classNames) {
            if (!className.contains(specialName)) continue;

            try {
                classes.add(Class.forName(className));
            } catch (Exception ex) {
                ex.printStackTrace();
                ;
            }
        }
        return classes;
    }

    /**
     * 获取某路径下所有的class
     *
     * @param filePath
     * @param childPackage
     * @return
     */
    public static List<Class<?>> getClassListByFilePath(String filePath, boolean childPackage) {
        List<Class<?>> classes = new ArrayList<>();
        List<String> classNames = getClassName(filePath, childPackage);
        for (String className : classNames) {
            try {
                classes.add(Class.forName(className));
            } catch (Exception ex) {
                ex.printStackTrace();
                ;
            }
        }
        return classes;
    }

    /**
     * 从所有jar中搜索该包，并获取该包下所有类
     *
     * @param urls URL集合
     * @param packagePath 包路径
     * @param childPackage 是否遍历子包
     * @return 类的完整名称
     */
    private static List<String> getClassNameByJars(
            URL[] urls, String packagePath, boolean childPackage) {
        List<String> myClassName = new ArrayList<String>();
        if (urls != null) {
            for (int i = 0; i < urls.length; i++) {
                URL url = urls[i];
                String urlPath = url.getPath();
                // 不必搜索classes文件夹
                if (urlPath.endsWith("classes/")) {
                    continue;
                }
                String jarPath = urlPath + "!/" + packagePath;
                System.err.println("jarPath : " + jarPath);
                myClassName.addAll(getClassNameByJar(jarPath, childPackage));
            }
        }
        return myClassName;
    }

    /**
     * 获取某包下（包括该包的所有子包）所有类
     *
     * @param packageName 包名
     * @return 类的完整名称
     */
    public static List<String> getClassName(String packageName) {
        return getClassName(packageName, true);
    }

    /**
     * 获取某包下所有类
     *
     * @param packageName 包名
     * @param childPackage 是否遍历子包
     * @return 类的完整名称
     */
    public static List<String> getClassName(String packageName, boolean childPackage) {
        List<String> fileNames = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String packagePath = packageName.replace(".", "/");
        URL url = loader.getResource(packagePath);
        if (url != null) {
            String type = url.getProtocol();
            if (type.equals("file")) {
                fileNames = getClassNameByFile(url.getPath(), childPackage);
            } else if (type.equals("jar")) {
                fileNames = getClassNameByJar(url.getPath(), childPackage);
            }
        } else {
            fileNames =
                    getClassNameByJars(
                            ((URLClassLoader) loader).getURLs(), packagePath, childPackage);
        }
        return fileNames;
    }

    /**
     * 从项目文件获取某包下所有类
     *
     * @param filePath 文件路径
     * @param childPackage 是否遍历子包
     * @return 类的完整名称
     */
    private static List<String> getClassNameByFile(String filePath, boolean childPackage) {
        List<String> myClassName = new ArrayList<String>();
        File file = new File(filePath);
        File[] childFiles = file.listFiles();
        for (File childFile : childFiles) {
            if (childFile.isDirectory()) {
                if (childPackage) {
                    myClassName.addAll(getClassNameByFile(childFile.getPath(), childPackage));
                }
            } else {
                String childFilePath = childFile.toURI().toString(); // childFile.getPath();
                //                URI uri = childFile.toURI();
                if (childFilePath.endsWith(".class")) {
                    childFilePath =
                            childFilePath.substring(
                                    childFilePath.indexOf("/classes") + 9,
                                    childFilePath.lastIndexOf("."));
                    childFilePath = childFilePath.replace("/", ".");
                    myClassName.add(childFilePath);
                } else if (childFilePath.endsWith(".jar")) {
                    //                    childFilePath = childFilePath.substring(0,
                    // childFilePath.indexOf(".jar"));
                    //                    System.err.println("try to init jar :childFilePath" +
                    // childFilePath);
                    myClassName.addAll(getClassNameByJar(childFile.getPath(), "", childPackage));
                }
            }
        }

        return myClassName;
    }

    /**
     * 从jar获取某包下所有类
     *
     * @param jarPath jar文件路径
     * @param childPackage 是否遍历子包
     * @return 类的完整名称
     */
    public static List<String> getClassNameByJar(
            String jarPath, String packagePath, boolean childPackage) {
        List<String> myClassName = new ArrayList<String>();
        //        String[] jarInfo = jarPath.split("!");
        //        String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));
        //        String packagePath = jarInfo[1].substring(1);
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(jarPath);
            Enumeration<JarEntry> entrys = jarFile.entries();
            while (entrys.hasMoreElements()) {
                JarEntry jarEntry = entrys.nextElement();
                String entryName = jarEntry.getName();
                if (entryName.endsWith(".class")) {
                    if (childPackage) {
                        if (entryName.startsWith(packagePath)) {
                            entryName =
                                    entryName
                                            .replace("/", ".")
                                            .substring(0, entryName.lastIndexOf("."));
                            myClassName.add(entryName);
                        }
                    } else {
                        int index = entryName.lastIndexOf("/");
                        String myPackagePath;
                        if (index != -1) {
                            myPackagePath = entryName.substring(0, index);
                        } else {
                            myPackagePath = entryName;
                        }
                        if (myPackagePath.equals(packagePath)) {
                            entryName =
                                    entryName
                                            .replace("/", ".")
                                            .substring(0, entryName.lastIndexOf("."));
                            myClassName.add(entryName);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jarFile != null) {
                try {
                    jarFile.close();
                } catch (Exception e) {
                }
            }
        }
        return myClassName;
    }

    /**
     * 从jar获取某包下所有类
     *
     * @param jarPath jar文件路径
     * @param childPackage 是否遍历子包
     * @return 类的完整名称
     */
    public static List<String> getClassNameByJar(String jarPath, boolean childPackage) {
        List<String> myClassName = new ArrayList<String>();
        String[] jarInfo = jarPath.split("!");
        String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));
        String packagePath = jarInfo[1].substring(1);
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(jarFilePath);
            Enumeration<JarEntry> entrys = jarFile.entries();
            while (entrys.hasMoreElements()) {
                JarEntry jarEntry = entrys.nextElement();
                String entryName = jarEntry.getName();
                if (entryName.endsWith(".class")) {
                    if (childPackage) {
                        if (entryName.startsWith(packagePath)) {
                            entryName =
                                    entryName
                                            .replace("/", ".")
                                            .substring(0, entryName.lastIndexOf("."));
                            myClassName.add(entryName);
                        }
                    } else {
                        int index = entryName.lastIndexOf("/");
                        String myPackagePath;
                        if (index != -1) {
                            myPackagePath = entryName.substring(0, index);
                        } else {
                            myPackagePath = entryName;
                        }
                        if (myPackagePath.equals(packagePath)) {
                            entryName =
                                    entryName
                                            .replace("/", ".")
                                            .substring(0, entryName.lastIndexOf("."));
                            myClassName.add(entryName);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jarFile != null) {
                try {
                    jarFile.close();
                } catch (Exception e) {
                }
            }
        }
        return myClassName;
    }

    /**
     * 判断a是否为b的子类
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean isSubclass(Class<?> a, Class<?> b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        for (Class<?> x = a; x != null; x = x.getSuperclass()) {
            if (x == b) {
                return true;
            }
            if (b.isInterface()) {
                Class<?>[] interfaces = x.getInterfaces();
                for (Class<?> anInterface : interfaces) {
                    if (isSubclass(anInterface, b)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
