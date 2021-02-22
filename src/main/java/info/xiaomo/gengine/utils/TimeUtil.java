package info.xiaomo.gengine.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 时间
 */
public final class TimeUtil {

	/**
	 * 一分钟的毫秒时长
	 */
	public static final long ONE_MINUTE_IN_MILLISECONDS = 60L * 1000;
	/**
	 * 一小时的毫秒时长
	 */
	public static final long ONE_HOUR_IN_MILLISECONDS = 60L * ONE_MINUTE_IN_MILLISECONDS;


	/**
	 * 1秒的时长
	 */
	public static final long ONE_MILLS = 1000L;

	public static final long ONE_DAY_IN_MILLISECONDS = 24L * ONE_HOUR_IN_MILLISECONDS;
	/**
	 * 一天的秒时长
	 */
	public static final long ONE_DAY_IN_SECONDS = 24L * 60 * 60;

	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static final DateTimeFormatter YYYYMMDDHHMMSS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withLocale(Locale.getDefault());
	/**
	 * yyyy-MM-dd HH:mm
	 */
	public static final DateTimeFormatter YYYYMMDDHHMM = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	/**
	 * yyyy-MM-dd HH
	 */
	public static final DateTimeFormatter YYYYMMDDHH = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
	/**
	 * yyyy-MM-dd
	 */
	public static final DateTimeFormatter YYYYMMDD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	public static final ZoneOffset ZONE_OFFSET = ZoneOffset.ofTotalSeconds(Calendar.getInstance().getTimeZone().getRawOffset() / 1000);
	private static final Logger LOGGER = LoggerFactory.getLogger(TimeUtil.class);
	/**
	 * 服务器时间偏移量
	 */
	private static long timeOffset;//86400000

	private TimeUtil() {
	}

	/**
	 * @param localDateTime
	 * @param formatter
	 * @return
	 * @ 返回按格式要求的文本，如yyyy-MM-dd HH:mm:ss
	 */
	public static String getDateTimeFormat(LocalDateTime localDateTime, DateTimeFormatter formatter) {
		return localDateTime.format(formatter);
	}

	/**
	 * @param time
	 * @param formatter
	 * @return
	 * @
	 */
	public static String getDateTimeFormat(long time, DateTimeFormatter formatter) {
		LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
		return ldt.format(formatter);
	}

	/**
	 * @param formatter
	 * @return
	 * @ 获取时间字符串
	 */
	public static String getDateTimeFormat(DateTimeFormatter formatter) {
		LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(currentTimeMillis()), ZoneId.systemDefault());
		return ldt.format(formatter);
	}

	/**
	 * 带时区的时间
	 *
	 * @param zonedDateTime
	 * @param formatter
	 * @return
	 */
	public static String getDateTimeFormat(ZonedDateTime zonedDateTime, DateTimeFormatter formatter) {
		return zonedDateTime.format(formatter);
	}

	/**
	 * @param text
	 * @param formatter
	 * @return
	 * @ 根据格式转换为LocalDateTime
	 */
	public static LocalDateTime getLocalDateTime(String text, DateTimeFormatter formatter) {
		try {
			return LocalDateTime.parse(text, formatter);
		} catch (Exception e) {
			LOGGER.error("getLocalDateTime", e);
		}
		return null;
	}

	/**
	 * @param text
	 * @param formatter
	 * @return
	 * @
	 */
	public static ZonedDateTime getZonedDateTime(String text, DateTimeFormatter formatter) {
		try {
			LocalDateTime m1 = LocalDateTime.parse(text, formatter);
			return m1.atZone(ZoneId.systemDefault());
		} catch (Exception e) {
			LOGGER.error("getLocalDateTime", e);
		}
		return null;
	}

	/**
	 * @param days
	 * @param formatter
	 * @return
	 * @ 获取与今日相差天数的日期格式，负为日期前，正为日期后。如yyyy-MM-dd HH
	 */
	public static String getOffToDay(int days, DateTimeFormatter formatter) {
		LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(currentTimeMillis()), ZoneId.systemDefault());
		if (days < 0) {
			ldt = ldt.minusDays(-days);
		} else {
			ldt = ldt.plusDays(days);
		}
		return ldt.format(formatter);
	}

	/**
	 * @param days
	 * @return
	 * @ 获取与今日相差天数的日期凌晨的毫秒数，负为日期前，正为日期后。如yyyy-MM-dd HH
	 */
	public static long getOffToDayZeroMil(int days) {
		try {
			ZonedDateTime ldt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(currentTimeMillis()), ZoneId.systemDefault());
			if (days < 0) {
				ldt = ldt.minusDays(-days);
			} else {
				ldt = ldt.plusDays(days);
			}
			ldt = ZonedDateTime.of(ldt.getYear(), ldt.getMonthValue(), ldt.getDayOfMonth(), 0, 0, 0, 0, ZoneId.systemDefault());
			return ldt.toInstant().toEpochMilli();
		} catch (Exception e) {
			LOGGER.error("", e);
		}
		return 0;
	}


	/**
	 * 判断指定的时间是否是今天
	 *
	 * @param time
	 * @return
	 */
	public static boolean isToday(long time) {
		return isSameDay(System.currentTimeMillis(), time);
	}

	/**
	 * 获取两个时间的逻辑间隔天数,以源时间为基准,目标时间小于源时间则返回大于或等于天数，反之返回小于等于天数
	 * <p>
	 * 举例：sourceTime=今天凌晨0点0分1秒,targetTime=昨天晚上11点59分59秒,则返回1
	 *
	 * @param sourceTime
	 * @param targetTime
	 * @return
	 */
	public static int getLogicIntervalDays(long sourceTime, long targetTime) {
		long source0ClockTime = getZeroClockTime(sourceTime);
		long target0ClockTime = getZeroClockTime(targetTime);

		return getRealIntervalDays(source0ClockTime, target0ClockTime);
	}

	/**
	 * 获取两个时间的实际间隔天数
	 *
	 * @param sourceTime
	 * @param targetTime
	 * @return
	 */
	public static int getRealIntervalDays(long sourceTime, long targetTime) {
		return (int) getIntervalTime(sourceTime, targetTime, ONE_DAY_IN_MILLISECONDS);
	}

	/**
	 * 根据指定的时间单位获取相差的单位时间，如时间单位为一天的毫秒数则该函数跟{@link#getRealIntervalDays} 则是相同的效果
	 *
	 * @param sourceTime
	 * @param targetTime
	 * @param timeUnit   时间单位(毫秒)
	 * @return
	 */
	public static long getIntervalTime(long sourceTime, long targetTime, long timeUnit) {
		return (sourceTime - targetTime) / timeUnit;
	}

	/**
	 * 获取在指定时间戳和指定小时，分钟，秒，毫秒数的时间
	 *
	 * @param time        时间戳
	 * @param hour        小时(24小时制)
	 * @param minute      分钟
	 * @param second      秒
	 * @param milliSecond 毫秒
	 * @return
	 */
	public static long getTimeInMillis(long time, int hour, int minute, int second, int milliSecond) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		calendar.set(Calendar.MILLISECOND, milliSecond);
		return calendar.getTimeInMillis();
	}

	/**
	 * 获取指定日期的时间戳
	 *
	 * @param year
	 * @param month       从1开始
	 * @param day
	 * @param hour
	 * @param minute
	 * @param second
	 * @param milliSecond
	 * @return
	 */
	public static long getTimeInMillis(int year, int month, int day, int hour, int minute, int second,
	                                   int milliSecond) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		calendar.set(Calendar.MILLISECOND, milliSecond);
		return calendar.getTimeInMillis();
	}

	/**
	 * 获取今日指定的时间
	 *
	 * @param hour        小时(24小时制)
	 * @param minute      分钟
	 * @param second      秒
	 * @param milliSecond 毫秒
	 * @return
	 */
	public static long getTodayTime(int hour, int minute, int second, int milliSecond) {
		return getTimeInMillis(System.currentTimeMillis(), hour, minute, second, milliSecond);
	}

	/**
	 * 获取指定时间的零点时间
	 *
	 * @param time
	 * @return
	 */
	public static long getZeroClockTime(long time) {
		return getTimeInMillis(time, 0, 0, 0, 0);
	}

	/**
	 * 获取开服天数,开服首日算作第一天
	 *
	 * @return
	 */
	public static int getOpenServerDay(long openServerZeroTime) {
		return getLogicIntervalDays(System.currentTimeMillis(), openServerZeroTime) + 1;
	}

	/**
	 * 获取合服天数，合服首日算作第一天
	 *
	 * @return
	 */
	public static int getCombineServerDay(long combineDayZeroTime) {
		return getLogicIntervalDays(System.currentTimeMillis(), combineDayZeroTime) + 1;
	}

	/**
	 * 返回指定时间和格式的时间字符串
	 *
	 * @param time
	 * @param format
	 * @return
	 */
	public static String getTimeString(long time, String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(new Date(time));
	}

	/**
	 * 从字符串中获取时间
	 *
	 * @param timeStr
	 * @param format
	 * @return
	 */
	public static long getTimeFromString(String timeStr, String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		try {
			return dateFormat.parse(timeStr).getTime();
		} catch (ParseException e) {
			LOGGER.error("", e);
		}
		return Long.MIN_VALUE;
	}

	/**
	 * 获取格式化的剩余时间
	 * <p>
	 * 例如:1天20小时5分0秒,20小时0分0秒,1秒
	 *
	 * @param leftTime
	 * @return
	 */
	public static String getLeftTimeString(long leftTime) {
		StringBuilder sb = new StringBuilder();
		// 获取剩余天数
		int day = (int) (leftTime / ONE_DAY_IN_MILLISECONDS);
		// 1天及以上的显示剩余天
		if (day > 0) {
			sb.append(day).append("天");
			leftTime -= (day * ONE_DAY_IN_MILLISECONDS);
		}
		int hour = (int) (leftTime / ONE_HOUR_IN_MILLISECONDS);
		// 1小时及以上或者前面显示了天数则后面需要小时
		if (hour > 0 || sb.length() > 0) {
			sb.append(hour).append("小时");
			leftTime -= (hour * ONE_HOUR_IN_MILLISECONDS);
		}
		int minute = (int) (leftTime / ONE_MINUTE_IN_MILLISECONDS);
		if (minute > 0 || sb.length() > 0) {
			sb.append(minute).append("分");
			leftTime -= (minute * ONE_MINUTE_IN_MILLISECONDS);
		}
		sb.append(leftTime / 1000).append("秒");
		return sb.toString();
	}

	/**
	 * 判断今天是否为同一个月
	 *
	 * @return boolean
	 */
	public static boolean isSameMouth(int oldMonth) {
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH) + 1;
		return month == oldMonth;
	}

	/**
	 * @param time
	 * @return
	 * @ 获取传入毫秒的月份中的日 1-12
	 */
	public static int getDayOfMonth(long time) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()).getDayOfMonth();
	}

	public static long getNowOfMills() {
		return System.currentTimeMillis();
	}

	public static int getNowOfSeconds() {
		return (int) (System.currentTimeMillis() / 1000);
	}

	public static int getNowOfMinutes() {
		return TimeUtil.getNowOfSeconds() / 60;
	}


	/**
	 * @return
	 * @ 获取系统当前月份中的日 1-12
	 */
	public static int getDayOfMonth() {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(currentTimeMillis()), ZoneId.systemDefault()).getDayOfMonth();
	}

	/**
	 * @return
	 * @ 获取一月最小的天数
	 */
	public static int getMinDaysOfMonth() {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(currentTimeMillis()), ZoneId.systemDefault()).getMonth().minLength();
	}

	/**
	 * 获取传入毫秒的月份 1-12
	 *
	 * @param time
	 * @return
	 */
	public static int getMonth(long time) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()).getMonthValue();
	}

	/**
	 * 获取系统当前月份 1-12
	 *
	 * @return
	 */
	public static int getMonth() {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(currentTimeMillis()), ZoneId.systemDefault()).getMonthValue();
	}

	/**
	 * 获取传入毫秒的当日小时 0 to 23
	 *
	 * @param time
	 * @return
	 */
	public static int getHour(long time) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()).getHour();
	}

	/**
	 * 获取系统当前小时 0 to 23
	 *
	 * @return
	 */
	public static int getHour() {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(currentTimeMillis()), ZoneId.systemDefault()).getHour();
	}

	/**
	 * 获取传入毫秒的秒 0-59
	 *
	 * @param time
	 * @return
	 */
	public static int getSecond(long time) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()).getSecond();
	}

	/**
	 * 获取系统当前秒 0-59
	 *
	 * @return
	 */
	public static int getSecond() {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(currentTimeMillis()), ZoneId.systemDefault()).getSecond();
	}

	/**
	 * 获取传入毫秒的当日分钟 0-59
	 *
	 * @param time
	 * @return
	 */
	public static int getMinute(long time) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()).getMinute();
	}

	/**
	 * 获取系统当前分钟 0-59
	 *
	 * @return
	 */
	public static int getMinute() {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(currentTimeMillis()), ZoneId.systemDefault()).getMinute();
	}

	/**
	 * 获取传入毫秒的星期 1-7
	 *
	 * @param time
	 * @return
	 */
	public static int getDayOfWeek(long time) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()).getDayOfWeek().getValue();
	}

	/**
	 * 获取年
	 *
	 * @param time
	 * @return
	 */
	public static int getYear(long time) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()).getYear();
	}

	public static int getYear() {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(currentTimeMillis()), ZoneId.systemDefault()).getYear();
	}

	/**
	 * 获取系统当前的星期 1-7
	 *
	 * @return
	 */
	public static int getDayOfWeek() {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(currentTimeMillis()), ZoneId.systemDefault()).getDayOfWeek().getValue();
	}

	/**
	 * 获取传入毫秒的天 1 to 365, or 366
	 *
	 * @param time
	 * @return
	 */
	public static int getDayOfYear(long time) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()).getDayOfYear();
	}

	/**
	 * 获取系统当前的天 1 to 365, or 366
	 *
	 * @return
	 */
	public static int getDayOfYear() {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(currentTimeMillis()), ZoneId.systemDefault()).getDayOfYear();
	}

	/**
	 * @param formatter
	 * @return
	 * @ 返回当周第一天格式
	 */
	public static String getNowWeekMondayFormat(DateTimeFormatter formatter) {
		LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(currentTimeMillis()), ZoneId.systemDefault());
		ldt = LocalDateTime.of(ldt.minusDays(ldt.getDayOfWeek().getValue() - 1).toLocalDate(), LocalTime.MIN);
		return ldt.format(formatter);
	}

	/**
	 * 返回当月第一天格式
	 *
	 * @param formatter
	 * @return
	 */
	public static String getNowMonthFirstDayFormat(DateTimeFormatter formatter) {
		LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(currentTimeMillis()), ZoneId.systemDefault());
		ldt = LocalDateTime.of(ldt.minusDays(ldt.getDayOfMonth() - 1).toLocalDate(), LocalTime.MIN);
		return ldt.format(formatter);
	}

	/**
	 * 返回当年第一天格式
	 *
	 * @param formatter
	 * @return
	 */
	public static String getNowYearFirstDayFormat(DateTimeFormatter formatter) {
		LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(currentTimeMillis()), ZoneId.systemDefault());
		ldt = LocalDateTime.of(ldt.minusDays(ldt.getDayOfYear() - 1).toLocalDate(), LocalTime.MIN);
		return ldt.format(formatter);
	}

	/**
	 * @param time1
	 * @param time2
	 * @return
	 * @ 判断两个时间是否在同一天
	 */
	public static boolean isSameDay(long time1, long time2) {
		return Duration.ofMillis(time1).toDays() - Duration.ofMillis(time2).toDays() == 0;
	}

	/**
	 * 判断两个时间是否在同一周(注意这里周日和周一判断是在一周里的)
	 *
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static boolean isSameWeek(long time1, long time2) {
		LocalDateTime ldt1 = LocalDateTime.ofInstant(Instant.ofEpochMilli(time1), ZoneId.systemDefault());
		LocalDateTime ldt2 = LocalDateTime.ofInstant(Instant.ofEpochMilli(time2), ZoneId.systemDefault());
		TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
		return ldt1.getYear() == ldt2.getYear() && ldt1.get(woy) == ldt2.get(woy);
	}

	/**
	 * @param time1
	 * @param time2
	 * @return
	 * @ 判断两个时间是否在同一月
	 */
	public static boolean isSameMonth(long time1, long time2) {
		LocalDateTime ldt1 = LocalDateTime.ofInstant(Instant.ofEpochMilli(time1), ZoneId.systemDefault());
		LocalDateTime ldt2 = LocalDateTime.ofInstant(Instant.ofEpochMilli(time2), ZoneId.systemDefault());
		return ldt1.getYear() == ldt2.getYear() && ldt1.getMonthValue() == ldt2.getMonthValue();
	}

	/**
	 * @param time1
	 * @param time2
	 * @return
	 * @ 判断两个时间是否在同一季度
	 */
	public static boolean isSameQuarter(long time1, long time2) {
		LocalDateTime ldt1 = LocalDateTime.ofInstant(Instant.ofEpochMilli(time1), ZoneId.systemDefault());
		LocalDateTime ldt2 = LocalDateTime.ofInstant(Instant.ofEpochMilli(time2), ZoneId.systemDefault());
		return ldt1.getYear() == ldt2.getYear() && ldt1.getMonthValue() / 4 == ldt2.getMonthValue() / 4;
	}

	/**
	 * @param time1
	 * @param time2
	 * @return
	 * @ 判断两个时间是否在同一年
	 */
	public static boolean isSameYear(long time1, long time2) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(time1), ZoneId.systemDefault()).getYear()
				== LocalDateTime.ofInstant(Instant.ofEpochMilli(time2), ZoneId.systemDefault()).getYear();
	}

	public static boolean isToDay(long time) {
		return isSameDay(time, currentTimeMillis());
	}

	/**
	 * @param time
	 * @return
	 * @ 传入时间与当前时间的day差值
	 */
	public static long dayOffsetNow(long time) {
		return Duration.ofMillis(currentTimeMillis()).toDays() - Duration.ofMillis(time).toDays();
	}

	/**
	 * @param time1
	 * @param time2
	 * @return
	 * @ 传入时间之间的day差值
	 */
	public static long dayOffset(long time1, long time2) {
		return Duration.ofMillis(time1).toDays() - Duration.ofMillis(time2).toDays();
	}

	/**
	 * @return 获取当前纪元毫秒 1970-01-01T00:00:00Z.
	 */
	public static long currentTimeMillis() {
		return Clock.systemDefaultZone().instant().toEpochMilli() + timeOffset;
	}

	public static long offsetCurrentTimeMillis(int offsetDays, int hour, int minute, int secord) {
		ZonedDateTime ldt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(currentTimeMillis()), ZoneId.systemDefault());
		if (offsetDays > 0) {
			ldt = ldt.plusDays(offsetDays);
		} else if (offsetDays < 0) {
			ldt = ldt.minusDays(offsetDays);
		}
		ldt = ldt.withHour(hour);
		ldt = ldt.withMinute(minute);
		ldt = ldt.withSecond(secord);
		return ldt.toEpochSecond() * 1000;
	}

	/**
	 * @return 获取当前纪元秒 1970-01-01T00:00:00Z.
	 */
	public static long epochSecond() {
		return Clock.systemDefaultZone().instant().getEpochSecond() + timeOffset / 1000;
	}

	/**
	 * 设置当前系统时间
	 *
	 * @param datetime
	 * @return
	 */
	public static boolean setCurrentDateTime(String datetime) {
		ZonedDateTime zdt = getZonedDateTime(datetime, YYYYMMDDHHMMSS);
		if (zdt == null) {
			zdt = getZonedDateTime(datetime, YYYYMMDDHHMM);
		}
		if (zdt == null) {
			zdt = getZonedDateTime(datetime, YYYYMMDDHH);
		}
		if (zdt == null) {
			zdt = getZonedDateTime(datetime, YYYYMMDD);
		}
		if (zdt == null) {
			return false;
		}
		timeOffset = zdt.toEpochSecond() * 1000 - Clock.systemDefaultZone().instant().toEpochMilli();
		return true;
	}

	public static ZonedDateTime getZonedDateTime(String datetime) {
		ZonedDateTime zdt = getZonedDateTime(datetime, YYYYMMDDHHMMSS);
		if (zdt == null) {
			zdt = getZonedDateTime(datetime, YYYYMMDDHHMM);
		}
		if (zdt == null) {
			zdt = getZonedDateTime(datetime, YYYYMMDDHH);
		}
		if (zdt == null) {
			zdt = getZonedDateTime(datetime, YYYYMMDD);
		}
		return zdt;
	}

	/**
	 * 获取一个带时间偏移量和时区的LocalDate
	 *
	 * @return
	 */
	public static LocalDate getLocalDate() {
		long currentTimeMillis = currentTimeMillis();
		LocalDateTime ldt = LocalDateTime.ofEpochSecond(currentTimeMillis / 1000, 0, ZONE_OFFSET);
		return ldt.toLocalDate();
	}

	/**
	 * 获取一个带时间偏移量和时区的LocalDateTime
	 *
	 * @return
	 */
	public static LocalDateTime getLocalDateTime() {
		long currentTimeMillis = currentTimeMillis();
		return LocalDateTime.ofEpochSecond(currentTimeMillis / 1000, 0, ZONE_OFFSET);
	}

	public static long getDayOfSecond() {
		LocalDateTime localDateTime = getLocalDateTime();
		int hour = localDateTime.getHour();
		int minute = localDateTime.getMinute();
		long second = localDateTime.getSecond();
		second = hour * 60 * 60 + minute * 60 + second;
		return second;
	}

	public static long getDayOfMinute() {
		LocalDateTime localDateTime = getLocalDateTime();
		int hour = localDateTime.getHour();
		int minute = localDateTime.getMinute();
		minute = hour * 60 + minute;
		return minute;
	}

	/**
	 * 根据传入的年月日时分秒 返回一个毫秒数
	 *
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minte
	 * @param second
	 * @return
	 */
	public static long getTimeByLocal(int year, int month, int day, int hour, int minte, int second) {
		return LocalDateTime.of(year, month, month, hour, minte, second).atZone(ZONE_OFFSET).toInstant().toEpochMilli();
	}

	/**
	 * 根据传入的描述获取该时刻所在日的秒数
	 *
	 * @param currentTimeMillis
	 * @return
	 */
	public static int getSecondByDay(long currentTimeMillis) {
		int hour = getHour(currentTimeMillis);
		int minute = getMinute(currentTimeMillis);
		int second = getSecond(currentTimeMillis);
		return hour * 3600 + minute * 60 + second;
	}

	public static int getSecondByDay() {
		int hour = getHour();
		int minute = getMinute();
		int second = getSecond();
		return hour * 3600 + minute * 60 + second;
	}

	/**
	 * @return The current value of the system timer, in nanoseconds.
	 */
	public static long nanoTime() {
		return System.nanoTime();
	}
}
