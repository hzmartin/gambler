package org.scoreboard.utils;

public class NotifyRespUtil {

	public static final String createText(String content, String yidofpa, String openid) {

		StringBuffer responseStr = new StringBuffer();

		responseStr
				.append("<xml>")
				.append("<ToUserName><![CDATA[" + openid + "]]></ToUserName>")
				.append("<FromUserName><![CDATA[" + yidofpa
						+ "]]></FromUserName>")
				.append("<CreateTime>" + System.currentTimeMillis()
						+ "</CreateTime>")
				.append("<MsgType><![CDATA[text]]></MsgType>")
				.append("<Content><![CDATA[" + content + "]]></Content>")
				.append("</xml>");
		return responseStr.toString();
	}

}
