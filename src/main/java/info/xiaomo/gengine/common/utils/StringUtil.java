package info.xiaomo.gengine.common.utils;

import java.util.regex.Pattern;
import org.slf4j.helpers.MessageFormatter;

/** @author 杨强 */
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

  private StringUtil() {}

  /**
   * isNullOrEmpty.
   *
   * @param str a {@link java.lang.String} object.
   * @return true 字符串为空
   */
  public static boolean isNullOrEmpty(String str) {
    return null == str || str.trim().isEmpty();
  }

  /**
   * 判断整数（int）
   *
   * @note + -判断也为真
   * @param str a {@link java.lang.String} object.
   * @return a boolean.
   */
  public static boolean isInteger(String str) {
    if (null == str || "".equals(str)) {
      return false;
    }
    Pattern pattern = Pattern.compile("^[-+]?[\\d]*$");
    return pattern.matcher(str).matches();
  }

  // 判断浮点数（double和float ）
  /**
   * isDouble.
   *
   * @param str a {@link java.lang.String} object.
   * @return a boolean.
   */
  public static boolean isDouble(String str) {
    if (null == str || "".equals(str)) {
      return false;
    }
    Pattern pattern = Pattern.compile("^[-+]?[.\\d]*$");
    return pattern.matcher(str).matches();
  }

  /**
   * 首字母大写
   *
   * @param Str a {@link java.lang.String} object.
   * @return a {@link java.lang.String} object.
   */
  public static String upFirstChar(String Str) {
    char[] cs = Str.toCharArray();
    if (Character.isLowerCase(cs[0])) {
      cs[0] -= 32;
      return String.valueOf(cs);
    }
    return Str;
  }

  /**
   * 首字母转小写
   *
   * @param s a {@link java.lang.String} object.
   * @return a {@link java.lang.String} object.
   */
  public static String lowerFirstChar(String s) {
    if (Character.isLowerCase(s.charAt(0))) {
      return s;
    }

    return (new StringBuilder())
        .append(Character.toLowerCase(s.charAt(0)))
        .append(s.substring(1))
        .toString();
  }
}
