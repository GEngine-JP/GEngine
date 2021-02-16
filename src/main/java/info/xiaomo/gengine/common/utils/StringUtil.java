package info.xiaomo.gengine.common.utils;


import org.slf4j.helpers.MessageFormatter;

/**
 * @author 杨强
 */
public final class StringUtil {

    /**
     * 字符串是否是空白字符串
     *
     * @param text
     * @return null或者全是空白字符的是会返回true
     */
    public static boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }

    /**
     * 格式化字符串
     *
     * @param format
     * @param params
     * @return
     */
    public static String format(String format, Object... params) {
        return MessageFormatter.arrayFormat(format, params).getMessage();
    }
}
