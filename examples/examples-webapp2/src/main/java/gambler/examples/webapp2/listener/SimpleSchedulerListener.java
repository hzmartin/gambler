package gambler.examples.webapp2.listener;

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
