package gambler.examples.webapp2.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

/**
 * 
 * @author cctao
 * 
 */
public class SystemStaticLoadListener implements ServletContextListener {

	private final Logger log = Logger.getLogger(getClass());

	public void contextInitialized(ServletContextEvent event) {
		log.info("server start!!! " + SystemStaticLoadListener.class.getName()
				+ " contextInitialized");
	}

	public void contextDestroyed(ServletContextEvent event) {
		log.info("server stop!!! " + SystemStaticLoadListener.class.getName()
				+ " contextDestroyed");

	}

}
