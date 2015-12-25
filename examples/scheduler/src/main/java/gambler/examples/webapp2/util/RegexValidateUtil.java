package gambler.examples.webapp2.util;

import java.util.regex.Pattern;

public class RegexValidateUtil {

	private static final Pattern userIdPattern = Pattern
			.compile("[a-zA-Z][a-zA-Z0-9_]{5,19}");//以字母开头的字母+数字+下划线，6-20位

	private static final String ipDigitPattern = "(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)";

//	private static final Pattern ipPattern = Pattern.compile(String.format(
//			"%s(\\.%s){3}", ipDigitPattern, ipDigitPattern));

	private static final Pattern internalIpPattern = Pattern
			.compile(String
					.format("(10(\\.%s){3})|(172\\.(1[6-9]|2\\d|3[01])(\\.%s){2})|(192\\.168(\\.%s){2})|(127\\.0\\.0\\.%s)",
							ipDigitPattern, ipDigitPattern, ipDigitPattern,
							ipDigitPattern));

	public static boolean isInternalIp(String str) {
		return internalIpPattern.matcher(str).matches();
	}

	
	public static boolean isValidUserId(String str) {
		return userIdPattern.matcher(str).matches();
	}
}
