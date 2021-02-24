package info.xiaomo.gengine.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * 把今天最好的表现当作明天最新的起点．．～ いま 最高の表現 として 明日最新の始発．．～ Today the best performance as tomorrow newest
 * starter! Created by IntelliJ IDEA.
 *
 * <p>
 *
 * @author : xiaomo github: https://github.com/xiaomoinfo email : xiaomo@xiaomo.info QQ : 83387856
 *     Date : 2017/8/17 15:25 desc : Copyright(©) 2017 by xiaomo.
 */
public class SystemUtil {

    private static InetAddress localHost = null;

    private SystemUtil() {
        try {
            localHost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * 本地IP
     *
     * @return IP地址
     */
    public static String getIP() {
        return localHost.getHostAddress();
    }

    /**
     * 获取用户机器名称
     *
     * @return
     */
    public static String getHostName() {
        return localHost.getHostName();
    }

    /**
     * 获取Mac地址
     *
     * @return Mac地址，例如：F0-4D-A2-39-24-A6
     */
    public static String getMac() {
        NetworkInterface byInetAddress;
        try {
            byInetAddress = NetworkInterface.getByInetAddress(localHost);
            byte[] hardwareAddress = byInetAddress.getHardwareAddress();
            return getMacFromBytes(hardwareAddress);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前系统名称
     *
     * @return 当前系统名，例如： windows xp
     */
    public static String getSystemName() {
        Properties sysProperty = System.getProperties();
        // 系统名称
        return sysProperty.getProperty("os.name").toLowerCase();
    }

    private static String getMacFromBytes(byte[] bytes) {
        StringBuilder mac = new StringBuilder();
        byte currentByte;
        boolean first = false;
        for (byte b : bytes) {
            if (first) {
                mac.append("-");
            }
            currentByte = (byte) ((b & 240) >> 4);
            mac.append(Integer.toHexString(currentByte));
            currentByte = (byte) (b & 15);
            mac.append(Integer.toHexString(currentByte));
            first = true;
        }
        return mac.toString().toUpperCase();
    }

    public static boolean isWindows() {
        String systemName = getSystemName();
        return systemName.contains("windows");
    }

    public static boolean isLinux() {
        String systemName = getSystemName();
        return systemName.contains("linux");
    }

    public static boolean isMac() {
        String systemName = getSystemName();
        return systemName.contains("mac");
    }

    public static void main(String[] args) {
        System.out.println(isWindows());
        System.out.println(isMac());
        System.out.println(isLinux());
    }
}
