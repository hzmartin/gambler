package gambler.commons.util.security;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

public class Base64Util {

    public static byte[] decode(byte[] b) {
        if (b != null) {
            byte[] decodeBytes = Base64.decodeBase64(b);
            return decodeBytes;
        }
        return null;

    }

    public static byte[] decode2byte(String str) {
        return (Base64.decodeBase64(str));

    }

    public static String decode(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        return new String(Base64.decodeBase64(str));

    }

    public static String encode(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        return encode(str.getBytes());
    }

    public static String encode(byte[] b) {
        String encodeString = Base64.encodeBase64String(b);
        encodeString = StringUtils.replace(encodeString, "\n", "");
        encodeString = StringUtils.replace(encodeString, "\r", "");
        return encodeString;
    }

}
