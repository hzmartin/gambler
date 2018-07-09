package org.scoreboard.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class IPUtil {
	public static final Logger logger = Logger.getLogger(IPUtil.class);

	private static final String ALL_ACCESS_SYMBOL = "*";

	/**
	 * 获取web客户端真实ip
	 */
	public static String getRequestIp(HttpServletRequest request) {
		try {
			String realIp = null;

			// 优先拿前端nginx放在forward包头里的ip
			String xff = request.getHeader("X-Forwarded-For");
			if (xff != null && !xff.isEmpty()) {
				String[] ips = xff.split(",");
				for (String ip : ips) {
					// 2g/3g网关可能在该包头里放一个内网ip，需要过滤
					if (!RegexUtil.isInternalIp(ip)) {
						realIp = ip;
						break;
					}
				}
			}

			if (realIp == null) {
				// logger.warn("X-Forwarded-For contains no external ip: " +
				// xff);
				realIp = request.getRemoteAddr();
			}

			realIp = realIp.trim();
			return realIp;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return null;
		}
	}

	/**
	 * @return true - allowed, false = forbidden
	 */
	public static boolean checkIpAccessRule(String rule, String requestIp) {

		if (StringUtils.isBlank(rule)) {
			return false;
		}

		if (StringUtils.isBlank(requestIp)) {
			return false;
		}

		String[] iprs = rule.split(";");
		for (String r : iprs) {
			if (ALL_ACCESS_SYMBOL.equals(r)) {
				// 开放给所有访问ip
				return true;
			}
			if (requestIp.equals(r)) {
				// 允许访问
				return true;
			}
			// 10.1.1.* 检查*匹配规则
			int index = r.indexOf(ALL_ACCESS_SYMBOL);
			if (index != -1 && requestIp.startsWith(r.substring(0, index))) {
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		String requestIp = "10.160.247.34";
		String rule = "10.160.*";
		System.out.println(checkIpAccessRule(rule, requestIp));
	}
}
