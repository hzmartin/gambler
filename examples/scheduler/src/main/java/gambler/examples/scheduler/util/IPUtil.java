package gambler.examples.scheduler.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class IPUtil {
	public static final Logger logger = Logger.getLogger(IPUtil.class);

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
					if (!RegexValidateUtil.isInternalIp(ip)) {
						realIp = ip;
						break;
					}
				}
			}

			if (realIp == null) {
				realIp = request.getRemoteAddr();
			}
			logger.debug("X-Forwarded-For contains ip: " + xff
					+ ", real ip used: " + realIp);
			realIp = realIp.trim();
			return realIp;
		} catch (Exception ex) {
			logger.error("get request ip error!", ex);
			return null;
		}
	}
}
