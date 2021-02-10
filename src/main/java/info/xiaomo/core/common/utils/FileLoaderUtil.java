package info.xiaomo.core.common.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @author xiaomo
 */
public class FileLoaderUtil {

    /**
     * 通过文件名找URL
     * @param fileName 文件名
     * @return URL
     */
    public static URL findURLByFileName(String fileName) {
        ClassLoader cl = FileLoaderUtil.class.getClassLoader();
        return cl.getResource(fileName);
    }

    /**
     * 通过文件名找输入流
     * @param fileName 文件名
     * @return 输入流
     */
    public static InputStreamReader findInputStreamByFileName(String fileName) {
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            return new InputStreamReader(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

