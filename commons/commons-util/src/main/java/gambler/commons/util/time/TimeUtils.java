package gambler.commons.util.time;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

public class TimeUtils {

	private static final String[] pattern = new String[] { "yyyy-MM", "yyyyMM", "yyyy/MM", "yyyyMMdd", "yyyy-MM-dd",
			"yyyy/MM/dd", "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss" };

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
	 * 
	 * @see TimeUtils#format(Date, String)
	 */
	public static String defaultFormat(Date date) {
		return format(date, DEFAULT_DATE_FORMAT_PATTERN);
	}

	/**
	 * format: yyyy-MM-dd HH:mm:ss
	 * 
	 * @see TimeUtils#format(Date, String)
	 */
	public static String defaultFormat(int seconds) {
		return format(seconds, DEFAULT_DATE_FORMAT_PATTERN);
	}

	/**
	 * format: yyyy-MM-dd HH:mm:ss
	 * 
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
	public static final int getTimeTagOfToday(int hour, int minutes, int seconds, int ampm) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.AM_PM, ampm);
		cal.set(Calendar.HOUR, hour);
		cal.set(Calendar.MINUTE, minutes);
		cal.set(Calendar.SECOND, seconds);
		return (int) (cal.getTimeInMillis() / 1000);
	}

	public static final int getBeginOfThisday(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.AM_PM, Calendar.AM);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return (int) (cal.getTimeInMillis() / 1000);
	}

	public static final int getBeginOfToday() {
		return getBeginOfThisday(new Date());
	}

	/**
	 * @return TIME in seconds
	 */
	public static final int getNowTime() {
		return (int) ((new java.util.Date()).getTime() / 1000);
	}

	// 获取当前时间的某一个部分
	public static int getNowTimeField(int field) {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(field);
	}

	// ---------- week-----------

	// 获得当前日期与本周一相差的天数
	private static int getMondayPlus(Date date) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
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
		return getNextMondayOfWeek(new Date());
	}

	// 获取某一周的下周一日期
	public static int getNextMondayOfWeek(Date date) {
		int mondayPlus = getMondayPlus(date);
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.setTime(date);
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 7);
		currentDate.set(Calendar.AM_PM, Calendar.AM);
		currentDate.set(Calendar.HOUR, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		Date monday = currentDate.getTime();
		return (int) (monday.getTime() / 1000);
	}

	// 获得上周一的日期
	public static int getPreviousMonday() {
		return getPreviousMondayOfThisWeek(new Date());
	}

	// 获取某一周的上周一日期
	public static int getPreviousMondayOfThisWeek(Date date) {
		int mondayPlus = getMondayPlus(date);
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.setTime(date);
		currentDate.add(GregorianCalendar.DATE, mondayPlus - 7);
		currentDate.set(Calendar.AM_PM, Calendar.AM);
		currentDate.set(Calendar.HOUR, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		Date monday = currentDate.getTime();
		return (int) (monday.getTime() / 1000);
	}

	// 获得本周一的日期
	public static int getThisMonday() {
		return getMondayOfThisWeek(new Date());
	}

	// 获取某一周周一的日期
	public static int getMondayOfThisWeek(Date date) {
		int mondayPlus = getMondayPlus(date);
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.setTime(date);
		currentDate.add(GregorianCalendar.DATE, mondayPlus);
		currentDate.set(Calendar.AM_PM, Calendar.AM);
		currentDate.set(Calendar.HOUR, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		Date monday = currentDate.getTime();
		return (int) (monday.getTime() / 1000);

	}

	// --------- month---------------

	// 获取当月第一天
	public static int getFirstDayOfThisMonth() {
		return getFirstDayOfMonth(new Date(), 0);
	}

	// 获取某月第一天
	public static int getFirstDayOfMonth(Date date, int monthoffset) {
		Calendar lastDate = Calendar.getInstance();
		lastDate.setTime(date);
		lastDate.add(Calendar.MONTH, monthoffset);// 加一个月，变为下月的1号
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.set(Calendar.AM_PM, Calendar.AM);
		lastDate.set(Calendar.HOUR, 0);
		lastDate.set(Calendar.MINUTE, 0);
		lastDate.set(Calendar.SECOND, 0);
		return (int) (lastDate.getTimeInMillis() / 1000);
	}

	// 获取当月第一天
	public static int getLastDayOfMonth() {
		return getLastDayOfMonth(new Date(), 0);
	}

	// 获取某月最后一天
	public static int getLastDayOfMonth(Date date, int monthoffset) {
		Calendar lastDate = Calendar.getInstance();
		lastDate.setTime(date);
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.add(Calendar.MONTH, monthoffset + 1);// 加一个月，变为下月的1号
		lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天
		lastDate.set(Calendar.AM_PM, Calendar.AM);
		lastDate.set(Calendar.HOUR, 0);
		lastDate.set(Calendar.MINUTE, 0);
		lastDate.set(Calendar.SECOND, 0);
		return (int) (lastDate.getTimeInMillis() / 1000);
	}

	public static void main(String[] args) {
		int seconds = (int)(TimeUtils.parseDate("2017-11-02").getTime()/1000);
		System.out.println(TimeUtils.format(seconds, DEFAULT_DATE_FORMAT_PATTERN));
		seconds = TimeUtils.getLastDayOfMonth(TimeUtils.getDate(seconds), 1);
		System.out.println(TimeUtils.format(seconds, DEFAULT_DATE_FORMAT_PATTERN));
	}
}
