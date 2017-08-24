/**
 * 工 程 名:  excelbean
 * 创建日期:  2017年02月13日 11:26
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.gameCore.config.util;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.CalendarConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.beanutils.converters.SqlTimeConverter;

import java.util.Calendar;

/**
 * Bean 工具类
 *
 * @author YangQiang
 */
public class BeanUtils extends org.apache.commons.beanutils.BeanUtils {
    public static String[] defaultPattern = {
        "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss", "yy-MM-dd HH:mm:ss", "yyyy-MM-dd", "MM/dd/yyyy", "HH:mm:ss"
    };
    private static CalendarConverter calendarConverter = new CalendarConverter(null);
    private static DateConverter dateConverter = new DateConverter(null);
    private static SqlDateConverter sqlDateConverter = new SqlDateConverter(null);
    private static SqlTimeConverter sqlTimeConverter = new SqlTimeConverter(null);

    // 注册默认的时间转换器
    static {
        setDateTimePattern(defaultPattern);
        ConvertUtils.register(calendarConverter, Calendar.class);
        ConvertUtils.register(dateConverter, java.util.Date.class);
        ConvertUtils.register(sqlDateConverter, java.sql.Date.class);
        ConvertUtils.register(sqlTimeConverter, java.sql.Time.class);

    }

    public static void setDateTimePattern(String[] patterns) {
        calendarConverter.setPatterns(patterns);
        dateConverter.setPatterns(patterns);
        sqlDateConverter.setPatterns(patterns);
        sqlTimeConverter.setPatterns(patterns);
    }
}
