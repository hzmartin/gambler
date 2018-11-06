package gambler.quartz.utils;

import gambler.commons.advmap.XMLMap;
import gambler.quartz.service.MailService;

import java.net.InetAddress;

public class ConfigUtil {

	public static String getDeployUserAccount() {
		return getSysConf().getString("DEPLOY_USER_ACCOUNT", "appops");
	}

	public static String getHostname() {
		try {
			return InetAddress.getLocalHost().getCanonicalHostName();
		} catch (Exception e) {
			return "NA";
		}
	}

	public static XMLMap getSysConf() {
		return SpringContextHolder.getBean("sysconf");
	}

	public static MailService getMailService() {
		return SpringContextHolder.getBean("mailService");
	}

	public static boolean isMailServiceEnabled() {
		return getSysConf().getBoolean("MAIL_SERVICE_ENABLED", false);
	}
}
