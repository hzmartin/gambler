package gambler.quartz.job;

import gambler.quartz.utils.ConfigUtil;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


/**
 * test mail
 * 
 * @author hzwangqh
 */
public class TestMailJob extends AbstractQuartzJob {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("test mail...");
		final JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		String to = jobDataMap.getString("to");
		ConfigUtil.getMailService().sendSimpleMail(to, "[" + ConfigUtil.getHostname() + "]TEST NOTIFICATION",
				"TEST SUCCESS!");
	}
}
