package info.xiaomo.core.config.util;

import java.util.Calendar;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.CalendarConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.beanutils.converters.SqlTimeConverter;

/**
 * Bean 工具类
 *
 * @author YangQiang
 */
public class BeanUtils extends org.apache.commons.beanutils.BeanUtils {
	private static final CalendarConverter calendarConverter = new CalendarConverter(null);
	private static final DateConverter dateConverter = new DateConverter(null);
	private static final SqlDateConverter sqlDateConverter = new SqlDateConverter(null);
	private static final SqlTimeConverter sqlTimeConverter = new SqlTimeConverter(null);
	public static String[] defaultPattern = {
			"yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss", "yy-MM-dd HH:mm:ss", "yyyy-MM-dd", "MM/dd/yyyy", "HH:mm:ss"
	};

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
