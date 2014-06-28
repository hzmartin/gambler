package gambler.examples.webapp2.util;

import java.util.regex.Pattern;

public class RegexUtil {

	private static final Pattern userIdPattern = Pattern
			.compile("[a-zA-Z][a-zA-Z0-9_]{5,19}");//以字母开头的字母+数字+下划线，6-20位

	public static boolean isValidUserId(String str) {
		return userIdPattern.matcher(str).matches();
	}
}
