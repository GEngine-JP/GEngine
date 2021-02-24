package info.xiaomo.gengine.utils;

public class OSUtil {

    public static final boolean isWindows =
            System.getProperty("os.name").toLowerCase().startsWith("windows");
    public static final boolean isLinux =
            System.getProperty("os.name").toLowerCase().startsWith("linux");
    public static final boolean isMac =
            System.getProperty("os.name").toLowerCase().startsWith("mac");
}
