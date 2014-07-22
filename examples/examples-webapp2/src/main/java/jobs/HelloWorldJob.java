package jobs;

import gambler.examples.webapp2.domain.auth.User;
import gambler.examples.webapp2.service.AuthUserService;
import gambler.examples.webapp2.util.SpringContextHolder;

import java.sql.Timestamp;

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
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		log.info("job start... @" + timestamp);
		AuthUserService service = SpringContextHolder
				.getBean("authUserService");
		User user = service.findUserById("wangqihui");
		JobDetail jobDetail = jobexecutioncontext.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		int count = 3;
		while (!jobInterrupted) {
			count--;
			System.out.println("hello " + user + ", execute Job("
					+ jobDetail.getFullName() + ", "
					+ jobDetail.getDescription() + ")"
					+ jobDataMap.getString("msg") + " @" + timestamp);

			if (count <= 0) {
				break;
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(jobInterrupted){
			log.warn("Job " + jobDetail.getFullName() + " has been interrupted!");
		}
		log.info("job end! @" + timestamp);
	}

	@Override
	public void interrupt() throws UnableToInterruptJobException {
		jobInterrupted = true;

	}

}
