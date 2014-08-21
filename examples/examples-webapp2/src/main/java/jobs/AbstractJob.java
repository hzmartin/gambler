/**
 * @(#)AbstractQuartzJob.java, 2014-3-25.
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package jobs;

import gambler.commons.advmap.XMLMap;
import gambler.examples.webapp2.util.SpringContextHolder;
import gambler.examples.webapp2.util.TimeTagUtil;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.InterruptableJob;
import org.quartz.UnableToInterruptJobException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author Administrator
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
		return TimeTagUtil.ONE_MINUTE
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

}
