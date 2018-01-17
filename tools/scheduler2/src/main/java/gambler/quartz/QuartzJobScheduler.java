/**
 * @(#)QuartzJobScheduler.java, 2014-3-21.
 *
 * Copyright 2014 Netease, Inc. All rights reserved. NETEASE
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package gambler.quartz;

import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import gambler.commons.advmap.XMLMap;

/**
 * 
 * Trigger the job to run on the next round minute Trigger trigger =
 * TriggerBuilder.newTrigger() .withSchedule(
 * SimpleScheduleBuilder.simpleSchedule() .withIntervalInSeconds(30)
 * .repeatForever()) .build();
 * 
 * // CronTrigger the job to run on the every 20 seconds CronTrigger cronTrigger
 * = TriggerBuilder.newTrigger()
 * .withIdentity("crontrigger","crontriggergroup1")
 * .withSchedule(CronScheduleBuilder.cronSchedule("10 * * * * ?")) .build();
 * 
 * @author hzwangqh
 */
public class QuartzJobScheduler {

	public static final XMLMap SYSCONFIG = new XMLMap("Gambler Scheduler Config", 0, "sysconf.xml");

	public static void main(String[] args) throws Exception {

		log.info("start creating scheduler ...");
		SchedulerFactory schFactory = new StdSchedulerFactory();
		Scheduler scheduler = schFactory.getScheduler();
		scheduler.start();

		int jobSize = SYSCONFIG.getInteger("jobs.size");
		for (int jobId = 1; jobId <= jobSize; jobId++) {
			scheduleJob(scheduler, jobId);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void scheduleJob(Scheduler scheduler, int jobId) throws Exception {
		String packname = SYSCONFIG.getString("jobs.package");
		String jobName = SYSCONFIG.getString("jobclazz." + jobId);
		log.info("start schedule job " + jobName + "(" + jobId + ")" + " ... ");
		Properties props = new Properties();
		try {
			String resourceUrl = "jobconf/" + jobName + ".properties";
			InputStream resource = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceUrl);
			if (resource != null) {
				props.load(resource);
			}

			String effectiveClazzName = packname + "." + jobName;
			Class theJobClass = Class.forName(effectiveClazzName);
			JobBuilder jobBuilder = JobBuilder.newJob(theJobClass).withIdentity(jobName, "gambler");
			Set<Entry<Object, Object>> entrySet = props.entrySet();
			for (Entry<Object, Object> entry : entrySet) {
				jobBuilder = jobBuilder.usingJobData(entry.getKey().toString(), entry.getValue().toString());
			}
			JobDetail theJob = jobBuilder.build();
			String theJobCronConfig = props.getProperty("cron");
			String theJobSimpleConfig = props.getProperty("simple");
			if (StringUtils.isNotBlank(theJobSimpleConfig)) {

				int interval = Integer.parseInt(theJobSimpleConfig);
				Trigger theTrigger = TriggerBuilder.newTrigger()
						.withSchedule(
								SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(interval).repeatForever())
						.build();
				scheduler.scheduleJob(theJob, theTrigger);

				log.info("scheduled job " + theJobClass.getCanonicalName() + "(" + theJobSimpleConfig
						+ "s interval) ... ");
			} else if (StringUtils.isNotBlank(theJobCronConfig)) {

				CronTrigger theTrigger = TriggerBuilder.newTrigger().withIdentity(jobName, "gambler")
						.withSchedule(CronScheduleBuilder.cronSchedule(theJobCronConfig)).build();
				scheduler.scheduleJob(theJob, theTrigger);
				log.info("scheduled job " + theJobClass.getCanonicalName() + "(" + theJobCronConfig + ") ... ");
			} else {
				log.info("skip scheduling job " + theJobClass);
			}
		} catch (Exception e) {
			log.error("schedule job " + jobId + " error!", e);
		}

	}

	private static final Logger log = Logger.getLogger(QuartzJobScheduler.class);

}
