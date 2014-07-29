package gambler.examples.webapp2.listener;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

public class SimpleJobListener implements JobListener {
	private final Logger logger = Logger.getLogger(getClass());

	private String name;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void jobToBeExecuted(JobExecutionContext context) {
		String jobName = context.getJobDetail().getFullName();
		logger.info(jobName + " is about to be executed");
	}

	public void jobExecutionVetoed(JobExecutionContext context) {
		String jobName = context.getJobDetail().getFullName();
		logger.info(jobName + " was vetoed and not executed()");
	}

	public void jobWasExecuted(JobExecutionContext context,
			JobExecutionException jobException) {

		String jobName = context.getJobDetail().getFullName();
		if (jobException == null) {
			logger.info(jobName
					+ " was executed successfully without exception");
		} else {
			logger.error(jobName + " was executed with exception("
					+ jobException.getMessage() + ")!");
		}
	}
}