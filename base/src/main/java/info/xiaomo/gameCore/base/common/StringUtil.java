/**
 * 创建日期:  2017年08月25日 17:31
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.gameCore.base.common;


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
    public static final String format(String format, Object... params) {
        return MessageFormatter.arrayFormat(format, params).getMessage();
    }
}
