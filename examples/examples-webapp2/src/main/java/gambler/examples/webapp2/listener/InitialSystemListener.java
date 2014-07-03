package gambler.examples.webapp2.listener;

import gambler.examples.webapp2.constant.AuthConstants;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

public class InitialSystemListener implements ServletContextListener {

	private final Logger log = Logger.getLogger(getClass());

	public void contextInitialized(ServletContextEvent event) {
		log.info("server start!!! " + InitialSystemListener.class.getName()
				+ " contextInitialized");
		log.info("Start loading configuration.");
		AuthConstants.reloadAllPermissions();
		AuthConstants.reloadAllRolePermissions();
		log.info("Loading configuration complete.");
	}

	public void contextDestroyed(ServletContextEvent event) {
		log.info("server stop!!! " + InitialSystemListener.class.getName()
				+ " contextDestroyed");

	}

}
