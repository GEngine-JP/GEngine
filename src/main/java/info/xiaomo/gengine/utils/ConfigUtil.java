package info.xiaomo.gengine.utils;

public class ConfigUtil {

    /**
     * 获取项目目录下的game-conf文件夹目录
     *
     * @return
     */
    public static String getConfigPath() {
        return System.getProperty("user.dir") + "/game-conf";
    }
}
