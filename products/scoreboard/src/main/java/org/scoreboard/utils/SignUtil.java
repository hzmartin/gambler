package org.scoreboard.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

public class SignUtil {

	public static final String sha1OfNaturalOrder(String... params) {
		if (params == null) {
			throw new IllegalArgumentException("params required");
		}
		List<String> validParams = new ArrayList<String>();
		for (String param : params) {
			if (param != null) {
				validParams.add(param);
			}
		}
		String[] validParamArr = validParams.toArray(new String[0]);
		Arrays.sort(validParamArr);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < validParamArr.length; i++) {
			sb.append(validParamArr[i]);
		}
		return DigestUtils.sha1Hex(sb.toString());
	}
	
	public static void main(String[] args) {
		System.out.println(sha1OfNaturalOrder("xxx","33", null, "xxkk"));
	}
}
