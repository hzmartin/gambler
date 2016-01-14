package gambler.examples.scheduler.listener;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;

public class SimpleTriggerListener implements TriggerListener {
	private final Logger logger = Logger.getLogger(getClass());

	private String name;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void triggerFired(Trigger trigger, JobExecutionContext context) {

		String triggerName = trigger.getName();
		logger.info(triggerName + " was fired");
	}

	public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {

		String triggerName = trigger.getName();
		logger.info(triggerName + " was not vetoed");
		return false;
	}

	public void triggerMisfired(Trigger trigger) {
		String triggerName = trigger.getName();
		logger.info(triggerName + " misfired");
	}

	public void triggerComplete(Trigger trigger, JobExecutionContext context,
			int triggerInstructionCode) {

		String triggerName = trigger.getName();
		logger.info(triggerName + " is complete");
	}

}