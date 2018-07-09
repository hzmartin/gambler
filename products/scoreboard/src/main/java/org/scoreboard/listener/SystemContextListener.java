package org.scoreboard.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

public class SystemContextListener implements ServletContextListener {
	private static final Logger logger = Logger
			.getLogger(SystemContextListener.class);

	public void contextInitialized(ServletContextEvent event) {
		logger.info("Start loading configuration.");

		try {
			logger.info("Load configuration complete.");
		} catch (Exception e) {
			logger.error("load sysconf.xml error!", e);
		}

	}

	public void contextDestroyed(ServletContextEvent event) {
		logger.info("web server stop.");
	}

}
