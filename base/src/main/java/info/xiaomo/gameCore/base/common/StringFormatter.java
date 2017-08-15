/**
 * 创建日期:  2017年08月11日 18:56
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.gameCore.base.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串格式化器<br>
 *
 * @author YangQiang
 */
public class StringFormatter {
    public static String format(String str, Object... params) {
        if (params == null || params.length == 0) {
            return str;
        }
        if (str == null || str.trim().isEmpty()) {
            return str;
        }
        int index = 0;
        Pattern p = Pattern.compile("\\{}");
        Matcher m = p.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String param = m.group();
            if (params.length > index) {
                Object obj = params[index];
                if (obj != null) {
                    param = obj.toString();
                }
            }
            m.appendReplacement(sb, param);
            index++;
        }
        m.appendTail(sb);
        return sb.toString();
    }
}
