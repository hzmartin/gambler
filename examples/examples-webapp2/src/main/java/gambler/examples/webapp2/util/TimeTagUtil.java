package gambler.examples.webapp2.util;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

public class TimeTagUtil {

	private static final String[] pattern = new String[] { "yyyy-MM", "yyyyMM",
			"yyyy/MM", "yyyyMMdd", "yyyy-MM-dd", "yyyy/MM/dd",
			"yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss" };

	public static final long ONE_DAY = 24 * 60 * 60 * 1000L;

    public static final int ONE_DAY_IN_SECONDS = 24 * 60 * 60;

    public static final long ONE_HOUR = 60 * 60 * 1000L;

    public static final long ONE_MINUTE = 60 * 1000L;

    public static final long ONE_SECOND = 1000L;

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

	public static String format_yyyyMMdd_HH_mm_ss(Date date) {
		if(date == null){
			return "";
		}
		return DateFormatUtils.format(date, "yyyyMMdd HH:mm:ss");
	}
}
