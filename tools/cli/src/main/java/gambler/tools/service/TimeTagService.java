/*
 * @(#) TimeTagService.java 2013-9-16
 * 
 */
package gambler.tools.service;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class TimeTagService
 * 
 * @author hzwangqh
 * @version 2013-9-16
 */
public class TimeTagService implements IService {

	public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

	public Date getDate(int timetag) {
		return new Date(timetag * 1000L);
	}

	public int getTimeTag(String date) throws ServiceException {
		return getTimeTag(date, DEFAULT_DATE_PATTERN);
	}

	/**
	 * 
	 * @param date
	 * @param pattern
	 *            - can be null, default pattern: 'yyyy-MM-dd'
	 * @return
	 * @throws ServiceException
	 */
	public int getTimeTag(String date, String pattern) throws ServiceException {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return (int) (sdf.parse(date).getTime() / 1000);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public int getNowTimeTag() {
		return (int) ((new java.util.Date()).getTime() / 1000);
	}

	public String getNowDateString() {
		return getSdf1().format(new java.util.Date());
	}

	private static SimpleDateFormat getSdf1() {
		ThreadLocal<SimpleDateFormat> formatter = new ThreadLocal<SimpleDateFormat>();
		SimpleDateFormat sdf = formatter.get();
		if (sdf == null) {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			formatter.set(sdf);
		}
		return sdf;
	}
}
