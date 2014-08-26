package gambler.commons.util.time;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

public class TimeUtils {

	private static final String[] pattern = new String[] { "yyyy-MM", "yyyyMM",
			"yyyy/MM", "yyyyMMdd", "yyyy-MM-dd", "yyyy/MM/dd",
			"yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss" };

	public static final String DEFAULT_DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public static final int ONE_DAY = 24 * 60 * 60;

	public static final int ONE_HOUR = 60 * 60;

	public static final int ONE_MINUTE = 60;

	public static final long ONE_SECOND_IN_MILLIS = 1000L;

	public static final long ONE_MINUTE_IN_MILLIS = 60 * ONE_SECOND_IN_MILLIS;

	public static final long ONE_HOUR_IN_MILLIS = 60 * 60 * ONE_SECOND_IN_MILLIS;

	public static final long ONE_DAY_IN_MILLIS = 24 * 60 * 60 * ONE_SECOND_IN_MILLIS;

	/**
	 * parse date by format "yyyy-MM", "yyyyMM", "yyyy/MM", "yyyyMMdd",
	 * "yyyy-MM-dd", "yyyy/MM/dd", "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss",
	 * "yyyy/MM/dd HH:mm:ss"<br/>
	 * return null if ParseException
	 */
	public static Date parseDate(String str) {
		try {
			if (StringUtils.isBlank(str)) {
				return null;
			}
			return DateUtils.parseDate(str, pattern);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * get date by given seconds
	 */
	public static final Date getDate(int seconds) {
		return new Date(seconds * 1000L);
	}

	/**
	 * format date by the given pattern, return null if any exception
	 */
	public static String format(Date date, String pattern) {
		try {
			return DateFormatUtils.format(date, pattern);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @see TimeUtils#format(Date, String)
	 */
	public static String format(long millis, String pattern) {
		return format(getDate((int) (millis / 1000)), pattern);
	}

	/**
	 * @see TimeUtils#format(Date, String)
	 */
	public static String format(int seconds, String pattern) {
		return format(seconds * ONE_SECOND_IN_MILLIS, pattern);
	}

	/**
	 * format: yyyy-MM-dd HH:mm:ss
	 * @see TimeUtils#format(Date, String)
	 */
	public static String defaultFormat(Date date) {
		return format(date, DEFAULT_DATE_FORMAT_PATTERN);
	}

	/**
	 * format: yyyy-MM-dd HH:mm:ss
	 * @see TimeUtils#format(Date, String)
	 */
	public static String defaultFormat(int seconds) {
		return format(seconds, DEFAULT_DATE_FORMAT_PATTERN);
	}

	/**
	 * format: yyyy-MM-dd HH:mm:ss
	 * @see TimeUtils#format(Date, String)
	 */
	public static String defaultFormat(long millis) {
		return format(millis, DEFAULT_DATE_FORMAT_PATTERN);
	}

	/**
	 * @param hour
	 *            - if > 12, ampm will be ignored
	 * @param minutes
	 * @param seconds
	 * @param ampm
	 *            - Calendar.AM/PM
	 * @return seconds value
	 */
	public static final int getTimeTagOfToday(int hour, int minutes,
			int seconds, int ampm) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.AM_PM, ampm);
		cal.set(Calendar.HOUR, hour);
		cal.set(Calendar.MINUTE, minutes);
		cal.set(Calendar.SECOND, seconds);
		return (int) (cal.getTimeInMillis() / 1000);
	}

	/**
	 * @return TIME in seconds
	 */
	public static final int getNowTime() {
		return (int) ((new java.util.Date()).getTime() / 1000);
	}

	// 获得当前日期与本周日相差的天数
	private static int getMondayPlus() {
		Calendar cd = Calendar.getInstance();
		// 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
		if (dayOfWeek == 1) {
			return 0;
		} else {
			return 1 - dayOfWeek;
		}
	}

	// 获得下周星期一的日期
	public static int getNextMonday() {
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 7);
		currentDate.set(Calendar.AM_PM, Calendar.AM);
		currentDate.set(Calendar.HOUR, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		Date monday = currentDate.getTime();
		return (int) (monday.getTime() / 1000);
	}

	// 上月第一天
	public static int getNextMonthFirstDay(int seconds) {
		Calendar lastDate = Calendar.getInstance();
		lastDate.setTime(getDate(seconds));
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.add(Calendar.MONTH, +1);
		return (int) (lastDate.getTimeInMillis() / 1000);
	}

	// 获得上周一的日期
	public static int getPreviousMonday() {
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus - 7);
		currentDate.set(Calendar.AM_PM, Calendar.AM);
		currentDate.set(Calendar.HOUR, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		Date monday = currentDate.getTime();
		return (int) (monday.getTime() / 1000);
	}

	// 获得本周一的日期
	public static int getMondayOFWeek() {
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus);
		currentDate.set(Calendar.AM_PM, Calendar.AM);
		currentDate.set(Calendar.HOUR, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		Date monday = currentDate.getTime();
		return (int) (monday.getTime() / 1000);
	}

	// 计算当月最后一天,返回字符串
	public static int getDefaultDay() {
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
		lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天
		return (int) (lastDate.getTimeInMillis() / 1000);
	}

	// 上月第一天
	public static int getPreviousMonthFirst() {
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.add(Calendar.MONTH, -1);// 减一个月，变为下月的1号
		// lastDate.add(Calendar.DATE,-1);//减去一天，变为当月最后一天
		return (int) (lastDate.getTimeInMillis() / 1000);
	}

	// 获取下个月第一天
	public static int getNextMonthFirst() {

		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.add(Calendar.MONTH, 1);// 减一个月，变为下月的1号
		return (int) (lastDate.getTimeInMillis() / 1000);
	}

	// 获取当月第一天
	public static int getFirstDayOfMonth() {
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		return (int) (lastDate.getTimeInMillis() / 1000);
	}

	public static int getNowTimeField(int field) {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(field);
	}
}
