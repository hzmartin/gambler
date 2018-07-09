package org.scoreboard.utils;

import java.util.regex.Pattern;

public class RegexUtil {

    private static final Pattern fixedPhonePattern = Pattern
        .compile("^(\\+?0*86-?)?(0+)?([0-9]{2,3})?([2-8][0-9]{6,7})([0-9]{1,4})?$");

    private static final Pattern mobilePattern = Pattern
        .compile("^\\+\\d{1,4}\\-\\d+|1[3,4,5,6,7,8,9]\\d{9}");

    private static final Pattern domesticMobilePattern = Pattern
        .compile("^1[3,4,5,6,7,8,9]\\d{9}");

    private static final Pattern internationMobilePattern = Pattern
        .compile("^\\+\\d{1,4}\\-\\d+");

    private static final Pattern foreignerMobilePattern = Pattern
        .compile("^\\d{5,}$");

    private static final Pattern telecomMobilePattern = Pattern
        .compile(SysConfig.getString("TelecomMobileRegex",
            "^((133|149|153|173|177|180|181|189|199)\\d{8})|((1700|1701|1702)\\d{7})"));

    private static final Pattern unicomMobilePattern = Pattern
        .compile(SysConfig.getString("UnicomMobileRegex",
            "^((130|131|132|145|155|156|171|175|176|185|186|166)\\d{8})|((1707|1708|1709)\\d{7})"));

    //    private static final Pattern emailPattern = Pattern
    //        .compile("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+");
    private static final Pattern emailPattern = Pattern
        .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    private static final Pattern mobileEmailPattern = Pattern
        .compile("^1[3,4,5,6,7,8,9]\\d{9}@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+");

    private static final Pattern yidPattern = Pattern
        .compile("[a-zA-Z][a-zA-Z0-9_]{5,19}"); // 易信号：以字母开头的字母+数字+下划线，6-20位

    private static final Pattern yidPatternForFindUser = Pattern
        .compile("[a-zA-Z][a-zA-Z0-9_]{4,19}"); // 易信号：以字母开头的字母+数字+下划线，5-20位

    private static final Pattern ecpidPattern = Pattern
        .compile(".+@ecplive\\.com");

    private static final String ipDigitPattern = "(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)";

    private static final Pattern ipPattern = Pattern.compile(String.format(
        "%s(\\.%s){3}", ipDigitPattern, ipDigitPattern));

    private static final Pattern internalIpPattern = Pattern
        .compile(String
            .format(
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

    public static boolean isYid(String str) {
        return yidPattern.matcher(str).matches();
    }

    public static boolean isYidForFindUser(String str) {
        return yidPatternForFindUser.matcher(str).matches();
    }

    public static boolean isEcpid(String str) {
        return ecpidPattern.matcher(str).matches();
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
        if (!SysConfig.getBoolean("TrimForeignMobileZeroPrefixEnable", false))
            return mobile;

        return mobile.replaceFirst("-0+", "-");
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        // System.out.println(RegexUtil.isMobileNumber("13812345678"));
        // System.out.println(RegexUtil.isMobileNumber("16812345678"));

        // System.out.println(RegexUtil.isEmail("aaa@163.com"));
        // System.out.println(RegexUtil.isEmail("aaa@163."));

        // System.out.println(RegexUtil.isYid("a"));
        // System.out.println(RegexUtil.isYid("1a"));
        // System.out.println(RegexUtil.isYid("Aa1_aaa"));
        // System.out.println(RegexUtil.isYid("Aa1aaa123456"));
        // System.out.println(RegexUtil.isYid("Aa1aaa1234567"));

        // System.out.println(RegexUtil.isEcpid("aaa@ecplive.com"));

        // System.out.println(RegexUtil.isInternalIp("127.0.0.1"));
        // System.out.println(RegexUtil.isInternalIp("192.168.164.95"));
        // System.out.println(RegexUtil.isInternalIp("172.18.1.1"));
        // System.out.println(RegexUtil.isInternalIp("10.120.104.1"));
        // System.out.println(RegexUtil.isInternalIp("114.113.197.1"));

        String[] emailArray = { "mkyong@yahoo.com", "mkyong-100@yahoo.com",
            "mkyong.100@yahoo.com", "mkyong111@mkyong.com",
            "mkyong-100@mkyong.net", "mkyong.100@mkyong.com.au",
            "mkyong@1.com", "mkyong@gmail.com.com", "mkyong+100@gmail.com",
            "mkyong-100@yahoo-test.com", "mkyong", "mkyong@.com.my",
            "mkyong123@gmail.a", "mkyong123@.com", "mkyong123@.com.com",
            ".mkyong@mkyong.com", "mkyong()*@gmail.com", "mkyong@%*.com",
            "mkyong..2002@gmail.com", "mkyong.@gmail.com",
            "mkyong@mkyong@gmail.com", "mkyong@gmail.com.1a" };
        for (String email: emailArray) {
            System.out.println(email + "    " + RegexUtil.isEmail(email));
        }

        // System.out.println(isNumbersString("0098"));
        // System.out.println(isUUID("2512B8E4-2519-462B-AA8E-36FB2FADF16D"));
        // System.out.println(RegexUtil.getMatchedIp("114.113.197.1"));
    }

    public static Pattern iphoneOsPattern = Pattern
        .compile("OSVersion: (.+?) Device: (.+)");

}
