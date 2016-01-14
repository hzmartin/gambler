package jobs;

import gambler.commons.advmap.XMLMap;
import gambler.commons.util.time.TimeUtils;
import gambler.examples.scheduler.util.SpringContextHolder;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.InterruptableJob;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author Martin
 */
public abstract class AbstractJob extends QuartzJobBean implements
		InterruptableJob {

	protected final Logger log = Logger.getLogger(getClass());

	private boolean jobInterrupted = false;

	protected XMLMap sysconf = SpringContextHolder.getBean("sysconf");

	public boolean isTimeOut(long endTime) {
		return System.currentTimeMillis() - endTime > 0;
	}

	public boolean isTimeOut(int endTime) {
		return System.currentTimeMillis() - (1000L * endTime) > 0;
	}

	public boolean isTimeOut(Date endTime) {
		if (endTime == null) {
			return false;
		}
		return System.currentTimeMillis() - endTime.getTime() > 0;
	}

	public long getJobPendingInterval() {
		return TimeUtils.ONE_MINUTE
				* sysconf.getInteger("jobPendingInterval", 5);// mins
	}

	public int getNotifyInterval() {
		return sysconf.getInteger("notifyInterval", 5);// mills
	}

	public boolean isJobInterrupted() {
		return jobInterrupted;
	}

	@Override
	public void interrupt() throws UnableToInterruptJobException {
		jobInterrupted = true;
	}
	
	@Override
    protected void executeInternal(JobExecutionContext jobexecutioncontext)
        throws JobExecutionException {
        JobDetail jobDetail = jobexecutioncontext.getJobDetail();
        log.info("execute job(" + jobDetail.getFullName() + ") start... ");
        try {
            process(jobexecutioncontext);
        } catch (Exception e) {
            throw new JobExecutionException(e.getMessage(), e);
        } finally {
            log.info("execute job(" + jobDetail.getFullName() + ") end!");
        }
    }

    protected abstract void process(JobExecutionContext jobexecutioncontext) throws Exception;

}
