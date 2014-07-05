package gambler.examples.webapp2.service;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("schedulerService")
public class SchedulerService {

    @Autowired
    @Qualifier("quartzScheduler")
    private Scheduler scheduler;

    private String avoidNullName(String name) {
        if (StringUtils.isBlank(name)) {
            return UUID.randomUUID().toString();
        } else {
            return name;
        }

    }

    public void pauseTrigger(String triggerName, String group) throws SchedulerException {
        scheduler.pauseTrigger(triggerName, group);

    }

    public void resumeTrigger(String triggerName, String group) throws SchedulerException {
        scheduler.resumeTrigger(triggerName, group);
    }

    @SuppressWarnings("rawtypes")
    public JobDetail addJob(String jobClassName, String jobName,
            String jobGroup, String jobDataMapJson, String description,
            boolean volatility, boolean durability, boolean shouldRecover,
            boolean replace) throws SchedulerException {
        Class jobClass = null;
        try {
            jobClass = Class.forName(jobClassName);
        } catch (ClassNotFoundException e) {
            throw new SchedulerException("add job " + jobClassName + " error!",
                    e);
        }
        JobDetail jobDetail = new JobDetail(avoidNullName(jobName),
                jobGroup, jobClass, volatility, durability, shouldRecover);
        jobDetail.setDescription(description);
        JSONObject fromObject = JSONObject.fromObject(jobDataMapJson);
        Set keySet = fromObject.keySet();
        for (Object paramname : keySet) {
            jobDetail.getJobDataMap().put(paramname,
                    fromObject.get(paramname));

        }
        scheduler.addJob(jobDetail, replace);
        return jobDetail;
    }

    public Date scheduleJob(String jobName, String jobGroup,
            String triggerName, String triggerGroup, Date startTime,
            Date endTime, Integer repeatCount, Long repeatInterval)
            throws SchedulerException {
        if (startTime == null) {
            startTime = new Date(System.currentTimeMillis());
        }
        if (repeatCount == null) {
            repeatCount = SimpleTrigger.REPEAT_INDEFINITELY;
        }
        SimpleTrigger trigger = new SimpleTrigger(avoidNullName(triggerName),
                triggerGroup, jobName, jobGroup, startTime, endTime,
                repeatCount, repeatInterval);
        return scheduler.scheduleJob(trigger);
    }

    public boolean unscheduleJob(String triggerName, String triggerGroup)
            throws SchedulerException {
        return scheduler.unscheduleJob(triggerName, triggerGroup);
    }

    public Date scheduleCronJob(String jobName, String jobGroup,
            String triggerName, String triggerGroup,
            CronExpression cronExpression) throws SchedulerException {
        CronTrigger cronTrigger = new CronTrigger(avoidNullName(triggerName),
                triggerGroup, jobName, jobGroup);
        cronTrigger.setCronExpression(cronExpression);
        return scheduler.scheduleJob(cronTrigger);
    }
}
