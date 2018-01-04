package gambler.tools.cli.util;

import java.util.regex.Pattern;

public class RegexUtil {

	private static final Pattern fixedPhonePattern = Pattern
			.compile("^(\\+?0*86-?)?(0+)?([0-9]{2,3})?([2-8][0-9]{6,7})([0-9]{1,4})?$");

	private static final Pattern mobilePattern = Pattern.compile("^\\+\\d{1,4}\\-\\d+|1[3,4,5,7,8,9]\\d{9}");

	private static final Pattern domesticMobilePattern = Pattern.compile("^1[3,4,5,7,8,9]\\d{9}");

	private static final Pattern internationMobilePattern = Pattern.compile("^\\+\\d{1,4}\\-\\d+");

	private static final Pattern foreignerMobilePattern = Pattern.compile("^\\d{5,}$");

	private static final Pattern telecomMobilePattern = Pattern
			.compile("^((133|149|153|173|177|180|181|189|199)\\d{8})|((1700|1701|1702)\\d{7})");

	private static final Pattern unicomMobilePattern = Pattern
			.compile("^((130|131|132|145|155|156|171|175|176|185|186)\\d{8})|((1707|1708|1709)\\d{7})");

	private static final Pattern emailPattern = Pattern
			.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

	private static final Pattern mobileEmailPattern = Pattern
			.compile("^1[3,4,5,7,8]\\d{9}@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+");

	private static final String ipDigitPattern = "(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)";

	private static final Pattern ipPattern = Pattern
			.compile(String.format("%s(\\.%s){3}", ipDigitPattern, ipDigitPattern));

	private static final Pattern internalIpPattern = Pattern.compile(String.format(
			"(10(\\.%s){3})|(172\\.(1[6-9]|2\\d|3[01])(\\.%s){2})|(192\\.168(\\.%s){2})|(127\\.0\\.0\\.%s)",
			ipDigitPattern, ipDigitPattern, ipDigitPattern, ipDigitPattern));

	private static final Pattern numbersStringPattern = Pattern.compile("\\d+");

	private static final Pattern uuidPattern = Pattern
			.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

	public static boolean isUUID(String str) {
		return uuidPattern.matcher(str).matches();
	}

	public static boolean isNumbersString(String str) {
		return numbersStringPattern.matcher(str).matches();
	}

	public static boolean isFixedPhone(String str) {
		return fixedPhonePattern.matcher(str).matches();
	}

	public static boolean isMobile(String str) {
		return mobilePattern.matcher(str).matches();
	}

	public static boolean isDomesticMobile(String str) {
		return domesticMobilePattern.matcher(str).matches();
	}

	public static boolean isInternationMobile(String str) {
		return internationMobilePattern.matcher(str).matches();
	}

	public static boolean isForeignerMobile(String str) {
		return foreignerMobilePattern.matcher(str).matches();
	}

	public static boolean isTelecomMobile(String str) {
		return telecomMobilePattern.matcher(str).matches();
	}

	public static boolean isUnicomMobile(String str) {
		return unicomMobilePattern.matcher(str).matches();
	}

	public static boolean isEmail(String str) {
		return emailPattern.matcher(str).matches();
	}

	/**
	 * 是否手机号码邮箱
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isMobileEmail(String str) {
		return mobileEmailPattern.matcher(str).matches();
	}

	public static boolean isInternalIp(String str) {
		return internalIpPattern.matcher(str).matches();
	}

	public static boolean isIp(String str) {
		return ipPattern.matcher(str).matches();
	}

	// public static String getMatchedIp(String str) {
	// return ipPattern.matcher(str).group();
	// }

	/**
	 * 把国外手机号的国际区号后、正式号码前的前缀0去掉 e.g. +852-0012345678 -> +852-12345678
	 */
	public static String trimForeignMobileZeroPrefix(String mobile) {
//		if (!SysConfig.getBoolean("TrimForeignMobileZeroPrefixEnable", false))
//			return mobile;

		return mobile.replaceFirst("-0+", "-");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// System.out.println(RegexUtil.isMobileNumber("13812345678"));
		// System.out.println(RegexUtil.isMobileNumber("16812345678"));

	}

	public static Pattern iphoneOsPattern = Pattern.compile("OSVersion: (.+?) Device: (.+)");

}
