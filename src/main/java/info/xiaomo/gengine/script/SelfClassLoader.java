package info.xiaomo.gengine.script;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 自定义类加载器，加载classpath之外的脚本
 *
 * @author 张力
 * @date 2018/3/3 14:08
 */
public class SelfClassLoader extends ClassLoader {

    private String classPath;

    public SelfClassLoader(String classPath) {
        this.classPath = classPath;
    }


    @Override
    protected Class<?> findClass(String name) {
        byte[] data = readClassFile(name);
        if (data == null) {
            return null;
        }
        return defineClass(name, data, 0, data.length, null);
    }


    private byte[] readClassFile(String name) {
        FileInputStream fis;
        byte[] data = null;
        try {
            File file = new File(classPath + classNameToFilePath(name));
            System.out.println(file.getAbsolutePath());
            fis = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int ch;
            while ((ch = fis.read()) != -1) {
                out.write(ch);
            }
            data = out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("readClassFile-IOException");
        }
        return data;
    }

    private String classNameToFilePath(String className) {
        String[] array = className.split(".");
        StringBuilder builder = new StringBuilder();
        for (String str : array) {
            builder.append("/").append(str);
        }
        builder.append(".class");
        return builder.toString();
    }

}
