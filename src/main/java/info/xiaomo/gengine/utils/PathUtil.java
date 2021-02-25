package info.xiaomo.gengine.utils;

import java.io.File;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PathUtil {

    /** 获取项目目录下的game-conf文件夹目录 */
    public static String getConfigPath() {
        String path = System.getProperty("user.dir");
        return findPath(new File(path + "/game-conf")) + "/";
    }

    /**
     * 递归查找game-conf，如果找不到就找它的上一级，直到找到根目录
     *
     * @param file target
     * @return filePath
     */
    private static String findPath(File file) {
        if (file.exists()) {
            return file.getPath();
        } else {
            file = new File(file.getParent());
        }
        return findPath(new File(file.getParent() + "/game-conf/"));
    }
}
