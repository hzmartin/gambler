package gambler.examples.webapp2.util;

import java.text.ParseException;
import java.util.Date;
import org.apache.commons.lang.time.DateFormatUtils;

import org.apache.commons.lang.time.DateUtils;

public class TimeTagUtil {

	private static final String[] pattern = new String[] { "yyyy-MM", "yyyyMM",
			"yyyy/MM", "yyyyMMdd", "yyyy-MM-dd", "yyyy/MM/dd",
			"yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss" };

	public static Date parseDate(String str) throws ParseException {
		return DateUtils.parseDate(str, pattern);
	}

	public static String format_yyyyMMddHHmmss(Date date) {
		return DateFormatUtils.format(date, "yyyyMMddHHmmss");
	}
}
