package jobs;

import gambler.examples.webapp2.domain.auth.User;
import gambler.examples.webapp2.service.AuthUserService;
import gambler.examples.webapp2.util.SpringContextHolder;

import org.apache.log4j.Logger;
import org.quartz.InterruptableJob;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class HelloWorldJob extends QuartzJobBean implements InterruptableJob {

	private final Logger log = Logger.getLogger(getClass());

	private boolean jobInterrupted = false;

	@Override
	protected void executeInternal(JobExecutionContext jobexecutioncontext)
			throws JobExecutionException {
		JobDetail jobDetail = jobexecutioncontext.getJobDetail();
		log.info("execute job(" + jobDetail.getFullName() + ") start... ");
		AuthUserService service = SpringContextHolder
				.getBean("authUserService");
		User user = service.findUserById("wangqihui");
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		int count = 3;
		while (!jobInterrupted) {
			count--;
			log.info("hello " + user + ", Job(" + jobDetail.getFullName()
					+ ", " + jobDetail.getDescription() + ") echoes "
					+ jobDataMap.getString("msg"));

			if (count <= 0) {
				break;
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (jobInterrupted) {
			log.warn("Job " + jobDetail.getFullName()
					+ " has been interrupted!");
		}
		log.info("execute job(" + jobDetail.getFullName() + ") end!");
	}

	@Override
	public void interrupt() throws UnableToInterruptJobException {
		jobInterrupted = true;

	}

}
