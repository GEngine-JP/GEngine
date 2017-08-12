/**
 * 工 程 名:  excelbean
 * 创建日期:  2017年02月13日 11:26
 * 创建作者:  杨 强  <281455776@qq.com>
 */
package info.xiaomo.gameCore.config.excel;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.IntrospectionContext;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.CalendarConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.beanutils.converters.SqlTimeConverter;

import java.beans.PropertyDescriptor;
import java.util.Calendar;

/**
 * Bean 工具类
 *
 * @author YangQiang
 */
public class BeanUtils {
    public static String[] defaultPattern = {"yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss", "yy-MM-dd HH:mm:ss",
        "yyyy-MM-dd", "MM/dd/yyyy", "HH:mm:ss"};
    private static CalendarConverter calendarConverter = new CalendarConverter(null);
    private static DateConverter dateConverter = new DateConverter(null);
    private static SqlDateConverter sqlDateConverter = new SqlDateConverter(null);
    private static SqlTimeConverter sqlTimeConverter = new SqlTimeConverter(null);

    static {
        setDateTimePattern(defaultPattern);
        ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
        convertUtilsBean.register(calendarConverter, Calendar.class);
        convertUtilsBean.register(dateConverter, java.util.Date.class);
        convertUtilsBean.register(sqlDateConverter, java.sql.Date.class);
        convertUtilsBean.register(sqlTimeConverter, java.sql.Time.class);

        BeanUtilsBean.setInstance(new BeanUtilsBean(convertUtilsBean));
    }

    public static void setDateTimePattern(String[] patterns) {
        calendarConverter.setPatterns(patterns);
        dateConverter.setPatterns(patterns);
        sqlDateConverter.setPatterns(patterns);
        sqlTimeConverter.setPatterns(patterns);
    }

    public static final IntrospectionContext getBeanDesc(Class clazz) {
        PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(clazz);
        BeanDesc context = new BeanDesc(clazz);
        context.addPropertyDescriptors(propertyDescriptors);
        return context;
    }
}
