package jobs;

import gambler.commons.advmap.AdvancedKey;

import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class LoadSysConfigJob extends AbstractJob {

	private final Logger log = Logger.getLogger(getClass());

	@Override
	protected void executeInternal(JobExecutionContext jobexecutioncontext)
			throws JobExecutionException {
		JobDetail jobDetail = jobexecutioncontext.getJobDetail();
		log.info("execute job(" + jobDetail.getFullName() + ") start... ");
		sysconf.load();
		Set<Entry<AdvancedKey, String>> entrySet = sysconf.entrySet();
		log.info("sysconf loaded");
		for (Entry<AdvancedKey, String> entry : entrySet) {
			log.info(entry.getKey() + "=" + entry.getValue());
		}
		log.info("execute job(" + jobDetail.getFullName() + ") end!");
	}

}
