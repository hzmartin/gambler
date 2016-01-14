package gambler.examples.scheduler.context;

import gambler.examples.scheduler.constant.AuthConstants;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

public class SystemContextListener implements ServletContextListener {

	private final Logger log = Logger.getLogger(getClass());

	public void contextInitialized(ServletContextEvent event) {
		log.info("server start!!! " + SystemContextListener.class.getName()
				+ " contextInitialized");
		log.info("Start loading configuration.");
		AuthConstants.reloadAllPermissions();
		AuthConstants.reloadAllRolePermissions();
		AuthConstants.reloadAllRoles();
		log.info("Loading configuration complete.");
	}

	public void contextDestroyed(ServletContextEvent event) {
		log.info("server stop!!! " + SystemContextListener.class.getName()
				+ " contextDestroyed");

	}

}
