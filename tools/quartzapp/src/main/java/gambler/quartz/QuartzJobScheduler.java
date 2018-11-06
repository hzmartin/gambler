/**
 * @(#)QuartzJobScheduler.java, 2014-3-21.
 *
 * Copyright 2014 Netease, Inc. All rights reserved. NETEASE
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package gambler.quartz;

import gambler.quartz.utils.ConfigUtil;
import gambler.quartz.utils.SpringContextHolder;

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
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


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

	private static final Logger log = Logger.getLogger(QuartzJobScheduler.class);

	private static Scheduler scheduler = null;

	static {
		try {
			SchedulerFactory schFactory = new StdSchedulerFactory();
			scheduler = schFactory.getScheduler();
		} catch (Exception e) {
			log.error("init scheduler error", e);
		}
	}

	public static void main(String[] args) throws Exception {

		log.info("start creating scheduler ...");
		scheduler.start();

		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:/application-context*.xml");
		SpringContextHolder holder = (SpringContextHolder) ac.getBean("springContextHolder");
		holder.setApplicationContext(ac);

		int jobsize = ConfigUtil.getSysConf().getInteger("job_list.size", 0);
		for (int i = 0; i < jobsize; i++) {
			String jobId = ConfigUtil.getSysConf().getString("job_list" + "." + i);
			if (StringUtils.isNotBlank(jobId)) {
				scheduleJob(jobId);
			}
		}
		String adminmaillist = ConfigUtil.getSysConf().getString("admin_maillist", "hzwangqh@corp.netease.com");
		ConfigUtil.getMailService().sendSimpleMail(adminmaillist.split("[,;]"),
				"quartzapp started @ " + ConfigUtil.getHostname(), "");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void scheduleJob(String jobId) throws Exception {
		log.info("start schedule job " + jobId + " ... ");
		Properties props = new Properties();
		try {
			String resourceUrl = "jobconf/" + jobId + ".properties";
			InputStream resource = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceUrl);
			if (resource != null) {
				props.load(resource);
			}
			String effectiveClazzName = props.getProperty("clazz");
			Class theJobClass = Class.forName(effectiveClazzName);
			String jobgroup = props.getProperty("group", "quartz");
			JobBuilder jobBuilder = JobBuilder.newJob(theJobClass).withIdentity(jobId, jobgroup);
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

				CronTrigger theTrigger = TriggerBuilder.newTrigger().withIdentity(jobId, jobgroup)
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

}
