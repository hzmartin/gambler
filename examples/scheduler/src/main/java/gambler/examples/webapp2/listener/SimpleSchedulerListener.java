package gambler.examples.webapp2.listener;

import gambler.commons.advmap.XMLMap;
import gambler.examples.webapp2.service.MailService;
import gambler.examples.webapp2.util.SpringContextHolder;

import org.apache.log4j.Logger;
import org.quartz.SchedulerException;
import org.quartz.listeners.SchedulerListenerSupport;

public class SimpleSchedulerListener extends SchedulerListenerSupport {

	private final Logger logger = Logger.getLogger(getClass());

	@Override
	public void schedulerError(String msg, SchedulerException cause) {
		logger.error(msg, cause);
	}

	@Override
	public void schedulerStarted() {
		logger.info("scheduler started!");

		try {
			XMLMap sysconf = SpringContextHolder.getBean("sysconf");
			if (sysconf.getBoolean("switch.mailService", false)) {
				MailService service = SpringContextHolder
						.getBean("mailService");
				service.sendHtmlMail(
						new String[] { "wqihui@yeah.net" }, null,
						"hello", "UTF-8", "THE SCHEDULER HAS STARTED!");
			}
		} catch (Exception e) {
			logger.error("mail notify error!", e);
		}
	}

	@Override
	public void schedulerInStandbyMode() {
		logger.warn("scheduler in standby mode!");
	}

	@Override
	public void schedulerShutdown() {
		logger.warn("scheduler has been shutdown!");
	}

}
