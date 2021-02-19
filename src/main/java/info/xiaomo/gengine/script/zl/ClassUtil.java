package info.xiaomo.gengine.script.zl;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * class工具类，包含
 *
 * @author 张力
 * @date 2015-4-3 下午4:21:22
 */
public class ClassUtil {

	private static final String PROTOCOL_FILE = "file";

	private static final String PROTOCOL_JAR = "jar";

	private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);


	/**
	 * 获取指定包下的具有指定注解的类
	 *
	 * @param packageName     指定包名，注意，这里只能是包名，不能是具体class，否则找不到该具体class
	 * @param annotationClass 指定注解类型
	 * @return
	 */
	public static Set<Class<?>> findClassWithAnnotation(ClassLoader classLoader, String packageName, Class<? extends Annotation> annotationClass) {


		if (classLoader == null) {
			classLoader = ClassUtil.class.getClassLoader();
		}
		Set<Class<?>> allClazz = new LinkedHashSet<>();
		boolean recursive = true;
		String packageDir = packageName.replace('.', '/');
		try {
			Enumeration<URL> dirs = classLoader.getResources(packageDir);
			while (dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
				String protocol = url.getProtocol();
				if (PROTOCOL_FILE.equals(protocol)) {
					//文件夹路径
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					allClazz.addAll(findClassFromDir(classLoader, packageName, filePath, recursive));
				} else if (PROTOCOL_JAR.equals(protocol)) {
					JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
					allClazz.addAll(findClassFromJar(classLoader, jar, packageDir));
				}
			}
		} catch (Throwable e) {
			LOGGER.error("读取日志Class文件出错", e);
		}

		Set<Class<?>> ret = new LinkedHashSet<>();

		for (Class<?> clazz : allClazz) {
			if (clazz.getAnnotation(annotationClass) != null) {
				LOGGER.info("发现脚本类：" + clazz.getName());
				ret.add(clazz);
			}
		}

		return ret;
	}


	public static Set<Class<?>> findClassWithAnnotation(ClassLoader classLoader, String packageName, JarFile file, Class<? extends Annotation> annotationClass) {


		if (classLoader == null) {
			classLoader = ClassUtil.class.getClassLoader();
		}
		String packageDir = packageName.replace('.', '/');
		Set<Class<?>> allClazz = findClassFromJar(classLoader, file, packageDir);

		Set<Class<?>> ret = new LinkedHashSet<>();

		for (Class<?> clazz : allClazz) {
			if (clazz.getAnnotation(annotationClass) != null) {
				LOGGER.info("发现脚本类：" + clazz.getName());
				ret.add(clazz);
			}
		}
		return ret;
	}

	/**
	 * @param jar        jar归档文件
	 * @param packageDir package转换成文件目录格式的字符串
	 * @return
	 */
	private static Set<Class<?>> findClassFromJar(ClassLoader classLoader, JarFile jar, String packageDir) {

		Set<Class<?>> ret = new LinkedHashSet<>();

		Enumeration<JarEntry> entries = jar.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			if (entry.isDirectory()) {
				//jar中的entry是所有层级的文件都列出来的，所以文件夹实际上一点用都没有
				continue;
			}
			String name = entry.getName();
			//以packageDir开头并且是class文件
			if (!name.startsWith(packageDir) || !name.endsWith(".class")) {
				continue;
			}

			name = name.replaceAll("/", ".");
			name = name.substring(0, name.length() - 6);
			try {
				Class<?> clazz = Class.forName(name, false, classLoader);
				ret.add(clazz);
			} catch (Throwable e) {
				LOGGER.error("读取Jar中的Class文件出错:" + name, e);
			}
		}
		return ret;

	}

	/**
	 * 获取文件夹下所有的类
	 *
	 * @param packageName 包名,该目录下的class文件对应的包名，因为文件是绝对路径的，无法计算包名，所以从外部传入
	 * @param filePath    文件夹路径
	 * @param recursive   是否递归寻找子文件夹下的内容
	 * @return
	 */
	private static Set<Class<?>> findClassFromDir(ClassLoader classLoader, String packageName, String filePath, boolean recursive) {

		File dir = new File(filePath);
		if (!dir.exists() || !dir.isDirectory()) {
			return Collections.emptySet();
		}

		Set<Class<?>> ret = new LinkedHashSet<>();

		File[] files = dir.listFiles(file -> (recursive && file.isDirectory()) || file.getName().endsWith(".class"));

		for (File file : Objects.requireNonNull(files)) {

			if (file.isDirectory()) {
				ret.addAll(findClassFromDir(classLoader, packageName + "." + file.getName(), file.getAbsolutePath(), recursive));
			} else {
				String className = file.getName().substring(0, file.getName().length() - 6);
				try {
					Class<?> clazz = Class.forName(packageName + '.' + className, false,
							classLoader);
					ret.add(clazz);
				} catch (Throwable e) {
					LOGGER.error("读取文件夹中的Class文件出错", e);
				}
			}

		}
		return ret;
	}

}
